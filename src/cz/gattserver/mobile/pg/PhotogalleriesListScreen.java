package cz.gattserver.mobile.pg;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import com.codename1.components.InfiniteProgress;
import com.codename1.components.InfiniteScrollAdapter;
import com.codename1.components.MultiButton;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Component;
import com.codename1.ui.Display;
import com.codename1.ui.layouts.BoxLayout;

import cz.gattserver.mobile.common.SwitchableContainer;
import cz.gattserver.mobile.common.SwitchableForm;

public class PhotogalleriesListScreen extends SwitchableContainer {

	private int pageNumber = 0;

	public PhotogalleriesListScreen(SwitchableForm mainForm, SwitchableContainer prevScreen) {
		super("Pøehled fotogalerií", mainForm, prevScreen);
		setLayout(BoxLayout.y());
		setScrollableY(true);
		init();
	}

	private List<Map<String, String>> fetchPropertyData() {
		try {
			InfiniteProgress prog = new InfiniteProgress();
			ConnectionRequest galleryRequest = new ConnectionRequest() {

				@Override
				protected void handleErrorResponseCode(int code, String message) {
					switch (code) {
					case 404:
						ErrorHandler.showError(ErrorType.RECORD, PhotogalleriesListScreen.this);
						break;
					default:
						ErrorHandler.showError(ErrorType.SERVER, PhotogalleriesListScreen.this);
						super.handleErrorResponseCode(code, message);
					}
				}

				protected void handleException(Exception err) {
					if (Display.isInitialized() && !Display.getInstance().isMinimized())
						ErrorHandler.showError(ErrorType.CONNECTION, PhotogalleriesListScreen.this);
				}
			};

			galleryRequest.setUrl(Config.GALLERY_LIST_RESOURCE);
			galleryRequest.setPost(false);
			galleryRequest.addArgument("page", String.valueOf(pageNumber++));
			galleryRequest.addArgument("pageSize", "10");
			galleryRequest.setDisposeOnCompletion(prog.showInifiniteBlocking());
			NetworkManager.getInstance().addToQueueAndWait(galleryRequest);

			Map<String, Object> result = new JSONParser().parseJSON(
					new InputStreamReader(new ByteArrayInputStream(galleryRequest.getResponseData()), "UTF-8"));

			@SuppressWarnings("unchecked")
			List<Map<String, String>> list = (List<Map<String, String>>) result.get("root");
			return list;
		} catch (Exception err) {
			Log.e(err);
			return null;
		}
	}

	private void init() {
		InfiniteScrollAdapter.createInfiniteScroll(PhotogalleriesListScreen.this, () -> {
			List<Map<String, String>> list = fetchPropertyData();
			MultiButton[] cmps = new MultiButton[list.size()];
			for (int iter = 0; iter < cmps.length; iter++) {
				Map<String, String> gallery = list.get(iter);
				if (gallery == null) {
					InfiniteScrollAdapter.addMoreComponents(PhotogalleriesListScreen.this, new Component[0], false);
					return;
				}

				String galleryNazev = gallery.get("name");
				int galleryId = (int) Double.parseDouble(String.valueOf(gallery.get("id")));

				MultiButton btn = new MultiButton(galleryNazev);
				btn.addActionListener(e -> {
					mainForm.switchComponent(new PhotogalleryDetailScreen(galleryId, galleryNazev, mainForm,
							PhotogalleriesListScreen.this));
				});
				cmps[iter] = btn;
			}
			InfiniteScrollAdapter.addMoreComponents(PhotogalleriesListScreen.this, cmps, true);
		}, true);
	}

	@Override
	public void refresh() {
		removeAll();
		init();
	}

}
