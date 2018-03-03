package cz.gattserver.mobile.pg;

import java.util.List;

import com.codename1.components.ImageViewer;
import com.codename1.ui.layouts.BorderLayout;

import cz.gattserver.mobile.common.SwitchableContainer;
import cz.gattserver.mobile.common.SwitchableForm;

public class PhotoDetailScreen extends SwitchableContainer {

	private ImageListModel imageListModel;

	public PhotoDetailScreen(long galleryId, String photo, List<String> photoList, SwitchableForm mainForm,
			SwitchableContainer prevScreen) {
		super(photo, mainForm, prevScreen);
		this.imageListModel = new ImageListModel(galleryId, photo, photoList);
		setLayout(new BorderLayout());

		init();
	}

	private void init() {
		ImageViewer photoViewer = new ImageViewer(imageListModel.getCurrentItem());
		photoViewer.setImageList(imageListModel);
		add(BorderLayout.CENTER, photoViewer);
	}

	@Override
	public void refresh() {
		removeAll();
		init();
	}

}
