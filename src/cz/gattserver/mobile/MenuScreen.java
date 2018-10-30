package cz.gattserver.mobile;

import com.codename1.ui.Button;
import com.codename1.ui.FontImage;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;

import cz.gattserver.mobile.campgames.CampgamesListScreen;
import cz.gattserver.mobile.common.SwitchableForm;
import cz.gattserver.mobile.drinks.DrinksMenuScreen;
import cz.gattserver.mobile.pg.PhotogalleryMenuScreen;
import cz.gattserver.mobile.recipes.RecipesListScreen;
import cz.gattserver.mobile.songs.SongsListScreen;

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
		Button recipesBtn = new Button("Recepty", p);
		recipesBtn.addActionListener(e -> new RecipesListScreen(MenuScreen.this).init().show());
		add(recipesBtn);

		p = FontImage.createMaterial(FontImage.MATERIAL_AUDIOTRACK, s);
		Button songsBtn = new Button("Zpěvník", p);
		songsBtn.addActionListener(e -> new SongsListScreen(MenuScreen.this).init().show());
		add(songsBtn);

		p = FontImage.createMaterial(FontImage.MATERIAL_LOCAL_DRINK, s);
		Button drinksBtn = new Button("Nápoje", p);
		drinksBtn.addActionListener(e -> new DrinksMenuScreen(MenuScreen.this).init().show());
		add(drinksBtn);
		
		p = FontImage.createMaterial(FontImage.MATERIAL_CASINO, s);
		Button campGamesBtn = new Button("Táborové hry", p);
		campGamesBtn.addActionListener(e -> new CampgamesListScreen(MenuScreen.this).init().show());
		add(campGamesBtn);

		revalidate();

		return this;
	}

}
