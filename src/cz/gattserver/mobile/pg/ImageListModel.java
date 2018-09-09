package cz.gattserver.mobile.pg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.codename1.io.Storage;
import com.codename1.io.Util;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.events.DataChangedListener;
import com.codename1.ui.events.SelectionListener;
import com.codename1.ui.list.ListModel;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.EventDispatcher;

import cz.gattserver.mobile.Config;

public class ImageListModel implements ListModel<Image> {

	private Form mainForm;
	private EventDispatcher listeners = new EventDispatcher();
	private List<String> imageURLs = new ArrayList<>();
	private List<String> photoList;
	private EncodedImage placeholder;
	private int selection;
	private Image[] images;

	private void updateTitle(int index) {
		mainForm.setTitle((index + 1) + "/" + photoList.size() + " " + photoList.get(index));
	}

	public ImageListModel(Form form, long galleryId, String currentPhoto, List<String> photoList) {
		this.mainForm = form;
		this.photoList = photoList;
		for (int i = 0; i < photoList.size(); i++) {
			String p = photoList.get(i);
			if (p.equals(currentPhoto))
				setSelectedIndex(i);
			String url = Config.PHOTO_DETAIL_RESOURCE + "?id=" + String.valueOf(galleryId) + "&fileName=" + p;
			imageURLs.add(url);
		}
		images = new EncodedImage[imageURLs.size()];

		// zástupný obrázek během nahrávání (refresh ikona)
		Style s = UIManager.getInstance().getComponentStyle("MultiLine1");
		placeholder = EncodedImage.createFromImage(FontImage.createMaterial(FontImage.MATERIAL_SYNC, s), true);
	}

	@Override
	public Image getItemAt(int index) {
		if (images[index] == null) {
			images[index] = placeholder;
			Util.downloadUrlToStorageInBackground(imageURLs.get(index), "list" + index, (e) -> {
				try {
					images[index] = EncodedImage.create(Storage.getInstance().createInputStream("list" + index));
					listeners.fireDataChangeEvent(index, DataChangedListener.CHANGED);
				} catch (IOException err) {
					err.printStackTrace();
				}
			});
		}
		return images[index];
	}

	public Image getCurrentItem() {
		return getItemAt(selection);
	}

	@Override
	public int getSize() {
		return imageURLs.size();
	}

	@Override
	public int getSelectedIndex() {
		return selection;
	}

	@Override
	public void setSelectedIndex(int index) {
		selection = index;
		updateTitle(index);
	}

	@Override
	public void addDataChangedListener(DataChangedListener l) {
		listeners.addListener(l);
	}

	@Override
	public void removeDataChangedListener(DataChangedListener l) {
		listeners.removeListener(l);
	}

	@Override
	public void addSelectionListener(SelectionListener l) {
	}

	@Override
	public void removeSelectionListener(SelectionListener l) {
	}

	@Override
	public void addItem(Image item) {
	}

	@Override
	public void removeItem(int index) {
	}

}
