package cz.gattserver.mobile.pg;

import java.util.List;

import com.codename1.components.ImageViewer;
import com.codename1.ui.layouts.BorderLayout;

import cz.gattserver.mobile.common.SwitchableForm;
import cz.gattserver.mobile.common.SwitchableScreen;

public class PhotoDetailScreen extends SwitchableScreen {

	private long galleryId;
	private String photo;
	private List<String> photoList;
	private ImageListModel imageListModel;

	public PhotoDetailScreen(long galleryId, String photo, List<String> photoList, SwitchableForm mainForm,
			SwitchableScreen prevScreen) {
		super(photo, mainForm, prevScreen);
		this.galleryId = galleryId;
		this.photo = photo;
		this.photoList = photoList;
	}

	protected void init() {
		mainForm.setLayout(new BorderLayout());

		this.imageListModel = new ImageListModel(mainForm, galleryId, photo, photoList);
		ImageViewer photoViewer = new ImageViewer(imageListModel.getCurrentItem());
		photoViewer.setUIID("photoViewer");
		photoViewer.setImageList(imageListModel);
		mainForm.getContentPane().add(BorderLayout.CENTER, photoViewer);

		mainForm.revalidate();
	}

}
