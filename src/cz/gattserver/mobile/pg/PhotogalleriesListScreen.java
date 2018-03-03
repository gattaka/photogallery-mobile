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

	private static final int PAGE_SIZE = 10;

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
			galleryRequest.addArgument("pageSize", String.valueOf(PAGE_SIZE));
			galleryRequest.setDisposeOnCompletion(prog.showInifiniteBlocking());
			NetworkManager.getInstance().addToQueueAndWait(galleryRequest);

			if (galleryRequest.getResponseCode() != 200)
				return null;

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

	private Integer fetchCount() {
		try {
			InfiniteProgress prog = new InfiniteProgress();
			ConnectionRequest galleryRequest = new ConnectionRequest() {

				@Override
				protected void handleErrorResponseCode(int code, String message) {
					if (code != 200)
						ErrorHandler.showError(ErrorType.SERVER, PhotogalleriesListScreen.this);
				}

				protected void handleException(Exception err) {
					if (Display.isInitialized() && !Display.getInstance().isMinimized())
						ErrorHandler.showError(ErrorType.CONNECTION, PhotogalleriesListScreen.this);
				}
			};

			galleryRequest.setUrl(Config.GALLERY_COUNT_RESOURCE);
			galleryRequest.setPost(false);
			galleryRequest.setDisposeOnCompletion(prog.showInifiniteBlocking());
			NetworkManager.getInstance().addToQueueAndWait(galleryRequest);

			if (galleryRequest.getResponseCode() != 200)
				return null;

			Integer count = Integer.parseInt(new String(galleryRequest.getResponseData(), "UTF-8"));
			return (int) Math.ceil((double) count / PAGE_SIZE);
		} catch (Exception err) {
			Log.e(err);
			return null;
		}
	}

	private void init() {
		pageNumber = 0;

		InfiniteScrollAdapter.createInfiniteScroll(PhotogalleriesListScreen.this, () -> {
			Integer pageCount = fetchCount();
			if (pageCount == null)
				return;
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
			InfiniteScrollAdapter.addMoreComponents(PhotogalleriesListScreen.this, cmps, pageNumber < pageCount);
		}, true);
	}

	@Override
	public void refresh() {
		removeAll();
		init();
	}

}
