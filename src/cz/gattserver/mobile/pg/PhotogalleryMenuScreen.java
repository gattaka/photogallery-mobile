package cz.gattserver.mobile.pg;

import com.codename1.ui.Button;
import com.codename1.ui.FontImage;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;

import cz.gattserver.mobile.common.SwitchableForm;
import cz.gattserver.mobile.common.SwitchableScreen;

public class PhotogalleryMenuScreen extends SwitchableScreen {

	private SwitchableForm mainForm;

	public PhotogalleryMenuScreen(SwitchableForm mainForm, SwitchableScreen prevScreen) {
		super("Fotogalerie", mainForm, prevScreen);
		this.mainForm = mainForm;
	}

	protected void init() {
		mainForm.setLayout(BoxLayout.y());
		mainForm.setScrollableY(true);

		Style s = UIManager.getInstance().getComponentStyle("Button");

		FontImage p = FontImage.createMaterial(FontImage.MATERIAL_PHOTO_ALBUM, s);
		Button galleryBtn = new Button("Galerie", p);
		galleryBtn
				.addActionListener(e -> mainForm.switchScreen(new PhotogalleriesListScreen(mainForm, PhotogalleryMenuScreen.this)));
		mainForm.add(galleryBtn);

		p = FontImage.createMaterial(FontImage.MATERIAL_CLOUD_UPLOAD, s);
		Button uploadBtn = new Button("Upload fotek", p);
		uploadBtn.addActionListener(e -> mainForm.switchScreen(new LocalPhotosScreen(mainForm, PhotogalleryMenuScreen.this)));
		mainForm.add(uploadBtn);

		mainForm.revalidate();
	}

}
