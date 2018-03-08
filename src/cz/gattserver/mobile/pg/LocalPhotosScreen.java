package cz.gattserver.mobile.pg;

import java.io.IOException;

import com.codename1.components.MultiButton;
import com.codename1.io.Log;
import com.codename1.ui.Button;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;

import cz.gattserver.mobile.common.SwitchableForm;
import cz.gattserver.mobile.common.SwitchableScreen;

public class LocalPhotosScreen extends SwitchableScreen {

	public LocalPhotosScreen(SwitchableForm mainForm, SwitchableScreen prevScreen) {
		super("Lokální fotky", mainForm, prevScreen);
	}

	@Override
	protected void init() {
		mainForm.setLayout(BoxLayout.y());
		mainForm.setScrollableY(true);

		Style s = UIManager.getInstance().getComponentStyle("Button");
		FontImage icon = FontImage.createMaterial(FontImage.MATERIAL_VIDEO_LIBRARY, s);
		Button chooseBtn = new Button("Výbìr fotek", icon);
		chooseBtn.addActionListener(ev -> {
			Display.getInstance().openGallery((e) -> {
				if (e != null && e.getSource() != null) {
					String file = (String) e.getSource();
					try {
						MultiButton btn = new MultiButton(file);
						btn.setIcon(Image.createImage(file).scaledWidth(100));
						mainForm.add(btn);
						mainForm.revalidate();
					} catch (IOException err) {
						Log.e(err);
					}
				}
			}, Display.GALLERY_IMAGE);
		});
		mainForm.add(chooseBtn);

		mainForm.revalidate();
	}

}
