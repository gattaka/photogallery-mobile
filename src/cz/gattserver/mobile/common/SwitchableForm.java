package cz.gattserver.mobile.common;

import com.codename1.ui.Command;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BorderLayout;

public class SwitchableForm extends Form {

	private SwitchableScreen currentScreen;

	private void back() {
		if (currentScreen.getPrev() != null)
			switchScreen(currentScreen.getPrev());
	}

	public void switchScreen(SwitchableScreen screen) {
		currentScreen = screen;
		screen.switchScreen();
	}

	public SwitchableForm() {
		super(new BorderLayout());

		// aby nebyly dva scrollbary
		setScrollVisible(false);

		Toolbar toolbar = new Toolbar();
		setToolbar(toolbar);

		// v kombinaci s donahráváním InfiniteScrollAdapter problikává
		// toolbar.setScrollOffUponContentPane(true);

		setBackCommand(new Command("back") {
			@Override
			public void actionPerformed(ActionEvent evt) {
				back();
			}
		});
		toolbar.addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, (e) -> back());
		toolbar.addMaterialCommandToRightBar("", FontImage.MATERIAL_REFRESH, (e) -> currentScreen.refresh());
	}

}
