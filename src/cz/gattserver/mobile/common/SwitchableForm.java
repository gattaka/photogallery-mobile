package cz.gattserver.mobile.common;

import com.codename1.ui.Command;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BorderLayout;

public abstract class SwitchableForm extends Form {

	private SwitchableForm prevForm;
	private String title;

	protected void back() {
		if (prevForm != null)
			prevForm.showBack();
		onHide();
	}

	public SwitchableForm(String title, SwitchableForm prevForm) {
		super(new BorderLayout());
		this.title = title;
		this.prevForm = prevForm;

		// aby nebyly dva scrollbary
		setScrollVisible(false);

		setBackCommand(new Command("back") {
			@Override
			public void actionPerformed(ActionEvent evt) {
				back();
			}
		});

		Toolbar toolbar = new GrassToolbar();
		setToolbar(toolbar);
		// v kombinaci s donahráváním InfiniteScrollAdapter problikává
		// toolbar.setScrollOffUponContentPane(true);
		toolbar.addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, (e) -> back());
		toolbar.addMaterialCommandToRightBar("", FontImage.MATERIAL_REFRESH, (e) -> refresh());
	}

	@Override
	public void show() {
		super.show();
		setTitle(title);
		if (prevForm != null)
			prevForm.onHide();
	}

	private void refresh() {
		removeAll();
		init();
	}

	protected void onHide() {
	};

	public abstract SwitchableForm init();

}
