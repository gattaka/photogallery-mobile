package cz.gattserver.mobile.pg;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Util;

/**
 * Úprava pùvodního MultipartRequest, který neumožòoval pod jedním "name" poslat
 * více souborù, protože mìl vazbu 1:1. Pokud se posílá více souborù a je
 * potøeba více souborù poslat pod jedním arg-name, musí MultipartRequest mít
 * vazbu 1:N
 * 
 * @author Hynek
 *
 */
public class FilesMultipartRequest extends ConnectionRequest {

	private static final String FILES_FORM_DATA_NAME = "files";
	private static final String CRLF = "\r\n";

	private static boolean canFlushStream = true;

	/**
	 * Special flag to keep input stream files open after they are read
	 */
	private static boolean leaveInputStreamsOpen;

	/**
	 * Special flag to keep input stream files open after they are read
	 * 
	 * @return the leaveInputStreamsOpen
	 */
	public static boolean isLeaveInputStreamsOpen() {
		return leaveInputStreamsOpen;
	}

	/**
	 * Special flag to keep input stream files open after they are read
	 * 
	 * @param aLeaveInputStreamsOpen
	 *            the leaveInputStreamsOpen to set
	 */
	public static void setLeaveInputStreamsOpen(boolean aLeaveInputStreamsOpen) {
		leaveInputStreamsOpen = aLeaveInputStreamsOpen;
	}

	private String boundary;
	private LinkedHashMap<String, InputStream> files = new LinkedHashMap<>();
	private Hashtable<String, String> filesizes = new Hashtable<>();
	private Hashtable<String, String> mimeTypes = new Hashtable<>();
	private long contentLength = -1L;
	private boolean manualRedirect = true;

	/**
	 * Initialize variables
	 */
	public FilesMultipartRequest() {
		setPost(true);
		setWriteRequest(true);

		// Just generate some unique random value.
		boundary = Long.toString(System.currentTimeMillis(), 16);

		// Line separator required by multipart/form-data.
		setContentType("multipart/form-data; boundary=" + boundary);
	}

	/**
	 * Returns the boundary string which is normally generated based on system
	 * time
	 * 
	 * @return the multipart boundary string
	 */
	public String getBoundary() {
		return boundary;
	}

	/**
	 * Sets the boundary string, normally you don't need this method. Its useful
	 * to workaround server issues only. Notice that this method must be invoked
	 * before adding any elements.
	 * 
	 * @param boundary
	 *            the boundary string
	 */
	public void setBoundary(String boundary) {
		this.boundary = boundary;
		setContentType("multipart/form-data; boundary=" + boundary);
	}

	protected void initConnection(Object connection) {
		contentLength = calculateContentLength();
		addRequestHeader("Content-Length", Long.toString(contentLength));
		super.initConnection(connection);
	}

	/**
	 * Adds a binary argument to the arguments
	 * 
	 * @param name
	 *            the name of the file data
	 * @param filePath
	 *            the path of the file to upload
	 * @param mimeType
	 *            the mime type for the content
	 * @throws IOException
	 *             if the file cannot be opened
	 */
	public void addData(String name, String filePath, String mimeType) throws IOException {
		// String filename = "";
		// for (int i = filePath.length() - 1; i >= 0; i--) {
		// if (filePath.charAt(i) == '\\' || filePath.charAt(i) == '/')
		// filename = filePath.substring(i + 1, filePath.length());
		// }
		files.put(name, FileSystemStorage.getInstance().openInputStream(filePath));
		filesizes.put(name, String.valueOf(FileSystemStorage.getInstance().getLength(filePath)));
		mimeTypes.put(name, mimeType);
	}

	protected long calculateContentLength() {
		long length = 0L;
		Iterator<String> e = files.keySet().iterator();

		// 2 = CRLF
		long dLength = ("Content-Disposition: form-data; name=\"" + FILES_FORM_DATA_NAME + "\"; filename=\"\"").length()
				+ 2;
		// 2 = CRLF
		long ctLength = "Content-Type: ".length() + 2;
		// 4 = 2 * CRLF
		long cteLength = "Content-Transfer-Encoding: binary".length() + 4;
		// -- + boundary + CRLF
		long bLength = boundary.length() + 4;
		// 2 = CRLF at end of part
		long baseBinaryLength = dLength + ctLength + cteLength + bLength + 2;

		while (e.hasNext()) {
			String filename = e.next();
			length += baseBinaryLength;
			length += filename.length();
			try {
				length += filename.getBytes("UTF-8").length;
			} catch (UnsupportedEncodingException ex) {
				length += filename.getBytes().length;
			}
			length += ((String) mimeTypes.get(filename)).length();
			length += Long.parseLong((String) filesizes.get(filename));
		}
		length += bLength + 2; // same as part boundaries, suffixed with: --
		return length;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void buildRequestBody(OutputStream os) throws IOException {
		Writer writer = null;
		writer = new OutputStreamWriter(os, "UTF-8");
		Iterator<String> e = files.keySet().iterator();
		while (e.hasNext()) {
			if (shouldStop())
				break;
			String filename = e.next();
			InputStream value = files.get(filename);

			writer.write("--");
			writer.write(boundary);
			writer.write(CRLF);
			writer.write("Content-Disposition: form-data; name=\"" + FILES_FORM_DATA_NAME + "\"; filename=\"" + filename
					+ "\"");
			writer.write(CRLF);
			writer.write("Content-Type: ");
			writer.write((String) mimeTypes.get(filename));
			writer.write(CRLF);
			writer.write("Content-Transfer-Encoding: binary");
			writer.write(CRLF);
			writer.write(CRLF);
			if (canFlushStream)
				writer.flush();
			byte[] buffer = new byte[8192];
			int s = value.read(buffer);
			while (s > -1) {
				if (shouldStop()) {
					break;
				}
				os.write(buffer, 0, s);
				if (canFlushStream) {
					writer.flush();
				}
				s = value.read(buffer);
			}
			if (!leaveInputStreamsOpen)
				Util.cleanup(value);
			value = null;
			if (canFlushStream)
				writer.flush();
			writer.write(CRLF);
			if (canFlushStream)
				writer.flush();
		}

		writer.write("--" + boundary + "--");
		writer.write(CRLF);
		writer.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codename1.io.ConnectionRequest#getContentLength()
	 */
	public int getContentLength() {
		return (int) contentLength;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codename1.io.ConnectionRequest#onRedirect(java.lang.String)
	 */
	public boolean onRedirect(String url) {
		return manualRedirect;
	}

	/**
	 * By default redirect responses (302 etc.) are handled manually in
	 * multipart requests
	 * 
	 * @return the autoRedirect
	 */
	public boolean isManualRedirect() {
		return manualRedirect;
	}

	/**
	 * By default redirect responses (302 etc.) are handled manually in
	 * multipart requests, set this to false to handle the redirect. Notice that
	 * a redirect converts a post to a get.
	 * 
	 * @param autoRedirect
	 *            the autoRedirect to set
	 */
	public void setManualRedirect(boolean autoRedirect) {
		this.manualRedirect = autoRedirect;
	}

	/**
	 * Sending large files requires flushing the writer once in a while to
	 * prevent Out Of Memory Errors, Some J2ME implementation are not able to
	 * flush the streams causing the upload to fail. This method can indicate to
	 * the upload to not use the flushing mechanism.
	 */
	public static void setCanFlushStream(boolean flush) {
		canFlushStream = flush;
	}
}