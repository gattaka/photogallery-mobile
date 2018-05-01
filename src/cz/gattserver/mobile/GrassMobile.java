package cz.gattserver.mobile;

import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.plaf.UIManager;
import cz.gattserver.mobile.common.SwitchableForm;

public class GrassMobile {

	private Form current;

	// spouští se pouze při novém startu, jednou za program
	public void init(Object context) {
		UIManager.initFirstTheme("/theme");

		// Enable Toolbar on all Forms by default
		// Toolbar.setGlobalToolbar(true);
	}

	// při nastartování nebo de-minimalizaci
	public void start() {
		if (current != null) {
			current.show();
			return;
		}
		SwitchableForm listForm = new SwitchableForm();
		listForm.switchScreen(new MenuScreen(listForm, null));
		listForm.show();
	}

	// při minimalizaci
	public void stop() {
		current = Display.getInstance().getCurrent();
		if (current instanceof Dialog) {
			((Dialog) current).dispose();
			current = Display.getInstance().getCurrent();
		}
	}

	// při ukončení aplikace
	public void destroy() {
	}
}
