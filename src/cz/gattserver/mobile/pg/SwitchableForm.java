package cz.gattserver.mobile.pg;

import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.animations.MorphTransition;

public class SwitchableForm extends Form {

	private AbstractScreen currentScreen;

	public SwitchableForm() {
		if (getToolbar() != null) {
			getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, (e) -> {
				if (currentScreen.getPrevScreen() != null) {
					switchComponent(currentScreen.getPrevScreen());
				}
			});
			getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_REFRESH, (e) -> {
				currentScreen.refresh();
			});
		}
	}

	public void switchComponent(AbstractScreen newScreen) {
		if (currentScreen != null) {
			replace(currentScreen, newScreen, MorphTransition.create(100));
		} else {
			add(newScreen);
		}
		currentScreen = newScreen;
		newScreen.attach();
	}

}
