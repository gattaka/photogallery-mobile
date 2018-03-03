package cz.gattserver.mobile.pg;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import com.codename1.components.InfiniteScrollAdapter;
import com.codename1.components.MultiButton;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Component;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.URLImage;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;

import cz.gattserver.mobile.common.SwitchableContainer;
import cz.gattserver.mobile.common.SwitchableForm;

public class PhotogalleryDetailScreen extends SwitchableContainer {

	private static final int PAGE_SIZE = 10;

	private int galleryId;
	private int pageNumber = 0;

	public PhotogalleryDetailScreen(int galleryId, String galleryNazev, SwitchableForm mainForm,
			SwitchableContainer prevScreen) {
		super(galleryNazev, mainForm, prevScreen);
		this.galleryId = galleryId;
		setLayout(BoxLayout.y());
		setScrollableY(true);
		init();
	}

	private List<String> getGalleryItems() {
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
		};
		galleryRequest.setUrl(Config.GALLERY_DETAIL_RESOURCE);
		galleryRequest.setPost(false);
		galleryRequest.addArgument("id", String.valueOf(galleryId));
		NetworkManager.getInstance().addToQueueAndWait(galleryRequest);

		try {
			Map<String, Object> result = new JSONParser().parseJSON(
					new InputStreamReader(new ByteArrayInputStream(galleryRequest.getResponseData()), "UTF-8"));

			@SuppressWarnings("unchecked")
			List<String> arrayList = (List<String>) result.get("files");
			return arrayList;
		} catch (Exception err) {
			Log.e(err);
			return null;
		}
	}

	private void init() {
		pageNumber = 0;

		Style s = UIManager.getInstance().getComponentStyle("MultiLine1");
		FontImage p = FontImage.createMaterial(FontImage.MATERIAL_PORTRAIT, s);
		EncodedImage placeholder = EncodedImage.createFromImage(p.scaled(p.getWidth() * 3, p.getHeight() * 3), false);

		List<String> list = getGalleryItems();
		InfiniteScrollAdapter.createInfiniteScroll(PhotogalleryDetailScreen.this, () -> {
			int start = pageNumber * PAGE_SIZE;
			int end = pageNumber * PAGE_SIZE + PAGE_SIZE;
			pageNumber++;
			boolean isMore = true;
			if (end >= list.size()) {
				end = list.size();
				isMore = false;
			}
			List<String> photos = list.subList(start, end);
			MultiButton[] cmps = new MultiButton[photos.size()];
			for (int iter = 0; iter < cmps.length; iter++) {
				String photo = photos.get(iter);
				if (photo == null) {
					InfiniteScrollAdapter.addMoreComponents(PhotogalleryDetailScreen.this, new Component[0], false);
					return;
				}
				String galID = String.valueOf(galleryId);
				String guid = "pg_" + galID + "_photo_" + photo;
				String url = Config.PHOTO_MINIATURE_RESOURCE + "?id=" + galID + "&fileName=" + photo;
				MultiButton btn = new MultiButton(photo);
				btn.addActionListener(e -> {
					mainForm.switchComponent(
							new PhotoDetailScreen(galleryId, photo, list, mainForm, PhotogalleryDetailScreen.this));
				});
				btn.setIcon(URLImage.createToStorage(placeholder, guid, url));
				cmps[iter] = btn;
			}
			InfiniteScrollAdapter.addMoreComponents(PhotogalleryDetailScreen.this, cmps, isMore);
		}, true);
	}

	@Override
	public void refresh() {
		removeAll();
		init();
	}

}
