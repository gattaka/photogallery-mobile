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
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.URLImage;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;

import cz.gattserver.mobile.common.SwitchableContainer;
import cz.gattserver.mobile.common.SwitchableForm;

public class PhotogalleryDetailScreen extends SwitchableContainer {

	private int galleryId;

	public PhotogalleryDetailScreen(int galleryId, String galleryNazev, SwitchableForm mainForm,
			SwitchableContainer prevScreen) {
		super(galleryNazev, mainForm, prevScreen);
		this.galleryId = galleryId;
		setLayout(BoxLayout.y());
		setScrollableY(true);
		init();
	}

	private void init() {
		InfiniteProgress prog = new InfiniteProgress();
		ConnectionRequest galleryRequest = new ConnectionRequest() {

			@Override
			protected void handleErrorResponseCode(int code, String message) {
				switch (code) {
				case 404:
					ErrorHandler.showError(ErrorType.RECORD, PhotogalleryDetailScreen.this);
					break;
				default:
					ErrorHandler.showError(ErrorType.SERVER, PhotogalleryDetailScreen.this);
					super.handleErrorResponseCode(code, message);
				}
			}

			protected void handleException(Exception err) {
				if (Display.isInitialized() && !Display.getInstance().isMinimized())
					ErrorHandler.showError(ErrorType.CONNECTION, PhotogalleryDetailScreen.this);
			}

			protected void readResponse(java.io.InputStream input) throws IOException {

				JSONParser p = new JSONParser();
				Map<String, Object> result = p.parseJSON(new InputStreamReader(input, "UTF-8"));

				if (result != null) {

					// galleryCont.addComponent(new SpanLabel((String)
					// result.get("author")));

					if (result.containsKey("files")) {
						@SuppressWarnings("unchecked")
						ArrayList<String> photos = (ArrayList<String>) result.get("files");
						if (photos != null) {
							for (String photo : photos) {
								InfiniteProgress prog2 = new InfiniteProgress();
								ConnectionRequest photoMiniRequest = new ConnectionRequest() {
									@Override
									protected void handleErrorResponseCode(int code, String message) {
										if (code == 404) {
											Style s = UIManager.getInstance().getComponentStyle("Label");
											s.setFgColor(0xff0000);
											s.setBgTransparency(0);
											Image warningImage = FontImage.createMaterial(FontImage.MATERIAL_WARNING, s)
													.toImage();
											add(new Label("Err. loading: " + photo, warningImage));
										} else
											super.handleErrorResponseCode(code, message);
									}

									protected void readResponse(java.io.InputStream photoMiniInput) throws IOException {
										Image photoMiniImage = URLImage.createImage(photoMiniInput);
										Button photoMiniButton = new Button(photoMiniImage);
										add(photoMiniButton);

										photoMiniButton.addActionListener(e -> {
											mainForm.switchComponent(new PhotoDetailScreen(galleryId, photo, mainForm,
													PhotogalleryDetailScreen.this));
										});

									}
								};
								photoMiniRequest.setUrl(Config.PHOTO_MINIATURE_RESOURCE);
								photoMiniRequest.setPost(false);
								photoMiniRequest.addArgument("id", String.valueOf(galleryId));
								photoMiniRequest.addArgument("fileName", photo);
								photoMiniRequest.setDisposeOnCompletion(prog2.showInifiniteBlocking());
								NetworkManager.getInstance().addToQueue(photoMiniRequest);
							}
						}
					}

				}
			};
		};
		galleryRequest.setUrl(Config.GALLERY_DETAIL_RESOURCE);
		galleryRequest.setPost(false);
		galleryRequest.addArgument("id", String.valueOf(galleryId));
		galleryRequest.setDisposeOnCompletion(prog.showInifiniteBlocking());
		NetworkManager.getInstance().addToQueueAndWait(galleryRequest);
	}

	@Override
	public void refresh() {
		removeAll();
		init();
	}

}
