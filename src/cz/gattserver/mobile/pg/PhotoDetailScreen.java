package cz.gattserver.mobile.pg;

import java.util.List;

import com.codename1.components.ImageViewer;
import com.codename1.ui.layouts.BorderLayout;

import cz.gattserver.mobile.common.SwitchableForm;

public class PhotoDetailScreen extends SwitchableForm {

	private long galleryId;
	private String photo;
	private List<String> photoList;
	private ImageListModel imageListModel;

	public PhotoDetailScreen(long galleryId, String photo, List<String> photoList, SwitchableForm prevForm) {
		super(photo, prevForm);
		this.galleryId = galleryId;
		this.photo = photo;
		this.photoList = photoList;
	}

	@Override
	public SwitchableForm init() {
		setLayout(new BorderLayout());

		this.imageListModel = new ImageListModel(this, galleryId, photo, photoList);
		ImageViewer photoViewer = new ImageViewer(imageListModel.getCurrentItem());
		photoViewer.setImageList(imageListModel);
		add(BorderLayout.CENTER, photoViewer);

		revalidate();

		return this;
	}

}
