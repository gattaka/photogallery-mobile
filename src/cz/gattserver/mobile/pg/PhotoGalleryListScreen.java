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
import com.codename1.ui.layouts.BoxLayout;

public class PhotoGalleryListScreen extends SwitchableContainer {

	public PhotoGalleryListScreen(SwitchableForm mainForm, SwitchableContainer prevScreen) {
		super("Pøehled fotogalerií", mainForm, prevScreen);
		setLayout(BoxLayout.y());
		setScrollableY(true);
		init();
	}

	private void init() {
		InfiniteProgress prog = new InfiniteProgress();
		ConnectionRequest galleryListRequest = new ConnectionRequest() {
			protected void readResponse(java.io.InputStream input) throws IOException {
				JSONParser p = new JSONParser();
				Map<String, Object> result = p.parseJSON(new InputStreamReader(input, "UTF-8"));

				if (result != null && result.containsKey("root")) {
					@SuppressWarnings("unchecked")
					ArrayList<Map<String, String>> list = (ArrayList<Map<String, String>>) result.get("root");
					for (Map<String, String> gallery : list) {
						String galleryNazev = gallery.get("name");
						int galleryId = (int) Double.parseDouble(String.valueOf(gallery.get("id")));
						Button button = new Button(galleryNazev);
						button.addActionListener(e -> {
							mainForm.switchComponent(new PhotoGalleryDetailScreen(galleryId, galleryNazev, mainForm,
									PhotoGalleryListScreen.this));
						});
						add(button);
					}
				}
			}
		};
		galleryListRequest.setUrl(Config.GALLERY_LIST_RESOURCE);
		galleryListRequest.setPost(false);
		galleryListRequest.setDisposeOnCompletion(prog.showInifiniteBlocking());
		NetworkManager.getInstance().addToQueueAndWait(galleryListRequest);
	}

	@Override
	public void refresh() {
		removeAll();
		init();
	}

}
