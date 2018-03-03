package cz.gattserver.mobile.pg;

import java.util.List;

import com.codename1.components.ImageViewer;
import com.codename1.ui.layouts.BorderLayout;

import cz.gattserver.mobile.common.SwitchableForm;
import cz.gattserver.mobile.common.SwitchableScreen;

public class PhotoDetailScreen extends SwitchableScreen {

	private ImageListModel imageListModel;

	public PhotoDetailScreen(long galleryId, String photo, List<String> photoList, SwitchableForm mainForm,
			SwitchableScreen prevScreen) {
		super(photo, mainForm, prevScreen);
		this.imageListModel = new ImageListModel(galleryId, photo, photoList);
	}

	protected void init() {
		mainForm.setLayout(new BorderLayout());

		ImageViewer photoViewer = new ImageViewer(imageListModel.getCurrentItem());
		photoViewer.setImageList(imageListModel);
		mainForm.getContentPane().add(BorderLayout.CENTER, photoViewer);
		
		mainForm.revalidate();
	}

}
