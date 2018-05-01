package cz.gattserver.mobile.pg;

import com.codename1.ui.Button;
import com.codename1.ui.FontImage;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;

import cz.gattserver.mobile.common.SwitchableForm;

public class PhotogalleryMenuScreen extends SwitchableForm {

	public PhotogalleryMenuScreen(SwitchableForm prevForm) {
		super("Fotogalerie", prevForm);
	}

	@Override
	public SwitchableForm init() {
		setLayout(BoxLayout.y());
		setScrollableY(true);

		Style s = UIManager.getInstance().getComponentStyle("Button");

		FontImage p = FontImage.createMaterial(FontImage.MATERIAL_PHOTO_ALBUM, s);
		Button galleryBtn = new Button("Galerie", p);
		galleryBtn.addActionListener(e -> new PhotogalleriesListScreen(PhotogalleryMenuScreen.this).init().show());
		add(galleryBtn);

		p = FontImage.createMaterial(FontImage.MATERIAL_CLOUD_UPLOAD, s);
		Button uploadBtn = new Button("Upload fotek", p);
		uploadBtn.addActionListener(e -> new LocalPhotosScreen(PhotogalleryMenuScreen.this).init().show());
		add(uploadBtn);

		revalidate();

		return this;
	}

}
