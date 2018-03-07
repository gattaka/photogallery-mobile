package cz.gattserver.mobile.pg;

import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.plaf.UIManager;
import cz.gattserver.mobile.common.SwitchableForm;

public class PhotogalleryMobile {

	private Form current;

	// spou�t� se pouze p�i nov�m startu, jednou za program
	public void init(Object context) {
		UIManager.initFirstTheme("/theme");

		// Enable Toolbar on all Forms by default
		// Toolbar.setGlobalToolbar(true);
	}

	// p�i nastartov�n� nebo de-minimalizaci
	public void start() {
		if (current != null) {
			current.show();
			return;
		}
		SwitchableForm listForm = new SwitchableForm();
		PhotogalleriesListScreen screen = new PhotogalleriesListScreen(listForm, null);
		listForm.switchScreen(screen);
		listForm.show();
	}

	// p�i minimalizaci
	public void stop() {
		current = Display.getInstance().getCurrent();
		if (current instanceof Dialog) {
			((Dialog) current).dispose();
			current = Display.getInstance().getCurrent();
		}
	}

	// p�i ukon�en� aplikace
	public void destroy() {
	}
}
