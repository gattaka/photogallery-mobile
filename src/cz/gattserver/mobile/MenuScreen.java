package cz.gattserver.mobile;

import com.codename1.ui.Button;
import com.codename1.ui.FontImage;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;

import cz.gattserver.mobile.common.SwitchableForm;
import cz.gattserver.mobile.pg.PhotogalleryMenuScreen;
import cz.gattserver.mobile.recipes.RecipesListScreen;

public class MenuScreen extends SwitchableForm {

	public MenuScreen() {
		super("GRASS Mobile", null);
	}

	@Override
	public SwitchableForm init() {
		setLayout(BoxLayout.y());
		setScrollableY(true);

		Style s = UIManager.getInstance().getComponentStyle("Button");

		FontImage p = FontImage.createMaterial(FontImage.MATERIAL_PHOTO_ALBUM, s);
		Button galleryBtn = new Button("Fotogalerie", p);
		galleryBtn.addActionListener(e -> new PhotogalleryMenuScreen(MenuScreen.this).init().show());
		add(galleryBtn);

		p = FontImage.createMaterial(FontImage.MATERIAL_CAKE, s);
		Button uploadBtn = new Button("Recepty", p);
		uploadBtn.addActionListener(e -> new RecipesListScreen(MenuScreen.this).init().show());
		add(uploadBtn);

		revalidate();

		return this;
	}

}
