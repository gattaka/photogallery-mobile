package cz.gattserver.mobile;

import com.codename1.ui.Button;
import com.codename1.ui.FontImage;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;

import cz.gattserver.mobile.common.SwitchableForm;
import cz.gattserver.mobile.common.SwitchableScreen;
import cz.gattserver.mobile.pg.PhotogalleryMenuScreen;
import cz.gattserver.mobile.recipes.RecipesListScreen;

public class MenuScreen extends SwitchableScreen {

	private SwitchableForm mainForm;

	public MenuScreen(SwitchableForm mainForm, SwitchableScreen prevScreen) {
		super("GRASS Mobile", mainForm, prevScreen);
		this.mainForm = mainForm;
	}

	protected void init() {
		mainForm.setLayout(BoxLayout.y());
		mainForm.setScrollableY(true);

		Style s = UIManager.getInstance().getComponentStyle("Button");

		FontImage p = FontImage.createMaterial(FontImage.MATERIAL_PHOTO_ALBUM, s);
		Button galleryBtn = new Button("Fotogalerie", p);
		galleryBtn
				.addActionListener(e -> mainForm.switchScreen(new PhotogalleryMenuScreen(mainForm, MenuScreen.this)));
		mainForm.add(galleryBtn);

		p = FontImage.createMaterial(FontImage.MATERIAL_CAKE, s);
		Button uploadBtn = new Button("Recepty", p);
		uploadBtn.addActionListener(e -> mainForm.switchScreen(new RecipesListScreen(mainForm, MenuScreen.this)));
		mainForm.add(uploadBtn);

		mainForm.revalidate();
	}

}
