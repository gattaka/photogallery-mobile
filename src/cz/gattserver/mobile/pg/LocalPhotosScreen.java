package cz.gattserver.mobile.pg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.codename1.components.MultiButton;
import com.codename1.io.Log;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;

import cz.gattserver.mobile.Config;
import cz.gattserver.mobile.common.SwitchableForm;

public class LocalPhotosScreen extends SwitchableForm {

	private List<String> images = new ArrayList<>();
	private TextField newPgName;
	private Button saveBtn;

	public LocalPhotosScreen(SwitchableForm prevForm) {
		super("Lokální fotky", prevForm);
	}

	@Override
	public SwitchableForm init() {
		setLayout(BoxLayout.y());
		setScrollableY(true);

		Style s = UIManager.getInstance().getComponentStyle("Button");

		Container container = new Container(BoxLayout.y());
		container.setScrollableY(false);
		add(container);

		FontImage icon = FontImage.createMaterial(FontImage.MATERIAL_VIDEO_LIBRARY, s);
		Button chooseBtn = new Button("Výbìr fotek", icon);
		chooseBtn.addActionListener(ev -> {
			Display.getInstance().openGallery((e) -> {
				if (e != null && e.getSource() != null) {
					String file = (String) e.getSource();
					try {
						MultiButton btn = new MultiButton(file);
						Image img = Image.createImage(file);
						images.add(file);
						btn.setIcon(img.scaledWidth(50));
						container.add(btn);
						revalidate();
						scrollComponentToVisible(saveBtn);
						checkSaveButton();
					} catch (IOException err) {
						Log.e(err);
					}
				}
			}, Display.GALLERY_IMAGE);
		});
		add(chooseBtn);

		newPgName = new TextField("", "Název galerie", 20, TextArea.ANY);
		newPgName.addActionListener(e -> checkSaveButton());
		add("Název galerie").add(newPgName);

		icon = FontImage.createMaterial(FontImage.MATERIAL_CLOUD_UPLOAD, s);
		saveBtn = new Button("Uložit galerii", icon);
		saveBtn.addActionListener(ev -> {
			FilesMultipartRequest request = new FilesMultipartRequest();
			request.setUrl(Config.PG_CREATE + "?galleryName=" + newPgName.getText());
			try {
				int counter = 0;
				for (String img : images) {
					counter++;
					String name = "uknown.file";
					String mime = "text/plain";
					if (img.toLowerCase().endsWith(".png")) {
						name = "File" + counter + ".png";
						mime = "image/png";
					} else if (img.toLowerCase().endsWith(".jpg") || img.toLowerCase().endsWith(".jpeg")) {
						name = "File" + counter + ".jpg";
						mime = "image/jpeg";
					} else if (img.toLowerCase().endsWith(".gif")) {
						name = "File" + counter + ".gif";
						mime = "image/gif";
					}
					request.addData(name, img, mime);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			NetworkManager.getInstance().addToQueue(request);
		});
		saveBtn.setEnabled(false);
		add(saveBtn);

		revalidate();

		return this;
	}

	private void checkSaveButton() {
		saveBtn.setEnabled(!images.isEmpty() && !"".equals(newPgName.getText()));
	}

}
