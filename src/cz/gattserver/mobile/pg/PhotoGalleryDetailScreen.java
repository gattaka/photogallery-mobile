package cz.gattserver.mobile.pg;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Image;
import com.codename1.ui.URLImage;
import com.codename1.ui.layouts.FlowLayout;

public class PhotoGalleryDetailScreen extends AbstractScreen {

	private int galleryId;

	public PhotoGalleryDetailScreen(int galleryId, String galleryNazev, SwitchableForm mainForm,
			AbstractScreen prevScreen) {
		super(galleryNazev, mainForm, prevScreen);
		this.galleryId = galleryId;

		setLayout(new FlowLayout(Component.CENTER));
		setScrollableY(true);

		init();
	}

	private void init() {
		InfiniteProgress prog = new InfiniteProgress();
		ConnectionRequest galleryRequest = new ConnectionRequest() {
			protected void readResponse(java.io.InputStream input) throws IOException {

				JSONParser p = new JSONParser();
				Map<String, Object> result = p.parseJSON(new InputStreamReader(input, "UTF-8"));

				if (result != null) {

					// galleryCont.addComponent(new SpanLabel((String)
					// result.get("author")));

					@SuppressWarnings("unchecked")
					ArrayList<String> photos = (ArrayList<String>) result.get("files");
					for (String photo : photos) {
						InfiniteProgress prog2 = new InfiniteProgress();
						ConnectionRequest photoMiniRequest = new ConnectionRequest() {
							protected void readResponse(java.io.InputStream photoMiniInput) throws IOException {

								Image photoMiniImage = URLImage.createImage(photoMiniInput);
								Button photoMiniButton = new Button(photoMiniImage);
								add(photoMiniButton);

								photoMiniButton.addActionListener(e -> {
									mainForm.switchComponent(
											new PhotoDetailScreen(galleryId, photo, mainForm, PhotoGalleryDetailScreen.this));
								});

							}
						};
						photoMiniRequest.setUrl("http://gattserver.cz/ws/pg/mini");
						photoMiniRequest.setPost(false);
						photoMiniRequest.addArgument("id", String.valueOf(galleryId));
						photoMiniRequest.addArgument("fileName", photo);
						photoMiniRequest.setDisposeOnCompletion(prog2.showInifiniteBlocking());
						NetworkManager.getInstance().addToQueue(photoMiniRequest);
					}
				}
			};
		};
		galleryRequest.setUrl("http://gattserver.cz/ws/pg/gallery");
		galleryRequest.setPost(false);
		galleryRequest.addArgument("id", String.valueOf(galleryId));
		galleryRequest.setDisposeOnCompletion(prog.showInifiniteBlocking());
		NetworkManager.getInstance().addToQueueAndWait(galleryRequest);
	}

	@Override
	protected void refresh() {
		removeAll();
		init();
	}

}
