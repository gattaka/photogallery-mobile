package cz.gattserver.mobile.pg;

import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;

public class ErrorHandler {

	private ErrorHandler() {
	}

	public static void showError(ErrorType errorType, Container cnt) {
		Style s = UIManager.getInstance().getComponentStyle("Label");
		s.setFgColor(0xff0000);
		s.setBgTransparency(0);
		String msg = null;
		Image image = null;
		switch (errorType) {
		case RECORD:
			image = FontImage.createMaterial(FontImage.MATERIAL_WARNING, s).toImage();
			msg = "Porušený záznam";
			break;
		case CONNECTION:
			image = FontImage.createMaterial(FontImage.MATERIAL_ERROR, s).toImage();
			msg = "Chyba spojení";
			break;
		case SERVER:
		default:
			image = FontImage.createMaterial(FontImage.MATERIAL_ERROR, s).toImage();
			msg = "Chyba serveru";
		}
		Label lbl = new Label(msg, image);
		if (cnt.getLayout() instanceof BorderLayout)
			cnt.add(BorderLayout.CENTER, lbl);
		else
			cnt.add(lbl);
	}

}
