package cz.gattserver.mobile.pg;

import java.io.IOException;

import com.codename1.components.ImageViewer;
import com.codename1.components.InfiniteProgress;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Image;
import com.codename1.ui.URLImage;
import com.codename1.ui.layouts.BorderLayout;

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
