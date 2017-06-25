package cz.gattserver.mobile.pg;

import java.io.IOException;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Image;
import com.codename1.ui.URLImage;
import com.codename1.ui.layouts.BoxLayout;

public class PhotoDetailScreen extends AbstractScreen {

	private int galleryId;
	private String photo;

	public PhotoDetailScreen(int galleryId, String photo, SwitchableForm mainForm, AbstractScreen prevScreen) {
		super(photo, mainForm, prevScreen);
		this.galleryId = galleryId;
		this.photo = photo;

		setLayout(BoxLayout.y());
		setScrollableY(true);
		setScrollableX(true);

		init();
	}

	private void init() {
		InfiniteProgress prog = new InfiniteProgress();
		ConnectionRequest photoDetailRequest = new ConnectionRequest() {
			protected void readResponse(java.io.InputStream input) throws IOException {

				Image rawImage = URLImage.createImage(input);
				// TODO 10 by se mìlo brát dle paddingu
				Image photoImage = rawImage.scaledSmallerRatio(mainForm.getWidth() - 10, mainForm.getHeight() - 10);

				// Zasekne celý prùbìh, pokud je photoImage "moc" velké
				// ImageViewer photoViewer = new ImageViewer(photoImage);
				// photoCont.add(photoViewer);

				add(photoImage);
			}
		};
		photoDetailRequest.setUrl("http://gattserver.cz/ws/pg/photo");
		photoDetailRequest.setPost(false);
		photoDetailRequest.addArgument("id", String.valueOf(galleryId));
		photoDetailRequest.addArgument("fileName", photo);
		photoDetailRequest.setDisposeOnCompletion(prog.showInifiniteBlocking());
		NetworkManager.getInstance().addToQueue(photoDetailRequest);
	}

	@Override
	protected void refresh() {
		removeAll();
		init();
	}

}
