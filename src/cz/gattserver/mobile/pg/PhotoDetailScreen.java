package cz.gattserver.mobile.pg;

import java.io.IOException;

import com.codename1.components.ImageViewer;
import com.codename1.components.InfiniteProgress;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Display;
import com.codename1.ui.Image;
import com.codename1.ui.URLImage;
import com.codename1.ui.layouts.BorderLayout;

import cz.gattserver.mobile.common.SwitchableContainer;
import cz.gattserver.mobile.common.SwitchableForm;

public class PhotoDetailScreen extends SwitchableContainer {

	private int galleryId;
	private String photo;

	public PhotoDetailScreen(int galleryId, String photo, SwitchableForm mainForm, SwitchableContainer prevScreen) {
		super(photo, mainForm, prevScreen);
		this.galleryId = galleryId;
		this.photo = photo;

		setLayout(new BorderLayout());

		init();
	}

	private void init() {
		InfiniteProgress prog = new InfiniteProgress();
		ConnectionRequest photoDetailRequest = new ConnectionRequest() {

			@Override
			protected void handleErrorResponseCode(int code, String message) {
				switch (code) {
				case 404:
					ErrorHandler.showError(ErrorType.RECORD, PhotoDetailScreen.this);
					break;
				default:
					ErrorHandler.showError(ErrorType.SERVER, PhotoDetailScreen.this);
					super.handleErrorResponseCode(code, message);
				}
			}

			protected void handleException(Exception err) {
				if (Display.isInitialized() && !Display.getInstance().isMinimized())
					ErrorHandler.showError(ErrorType.CONNECTION, PhotoDetailScreen.this);
			}

			protected void readResponse(java.io.InputStream input) throws IOException {
				Image rawImage = URLImage.createImage(input);
				ImageViewer photoViewer = new ImageViewer(rawImage);
				add(BorderLayout.CENTER, photoViewer);
			}
		};
		photoDetailRequest.setUrl(Config.PHOTO_DETAIL_RESOURCE);
		photoDetailRequest.setPost(false);
		photoDetailRequest.addArgument("id", String.valueOf(galleryId));
		photoDetailRequest.addArgument("fileName", photo);
		photoDetailRequest.setDisposeOnCompletion(prog.showInifiniteBlocking());
		NetworkManager.getInstance().addToQueueAndWait(photoDetailRequest);
	}

	@Override
	public void refresh() {
		removeAll();
		init();
	}

}
