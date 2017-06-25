package cz.gattserver.mobile.pg;

import com.codename1.ui.Container;

public abstract class AbstractScreen extends Container {

	protected SwitchableForm mainForm;
	protected AbstractScreen prevScreen;
	protected String title;

	/**
	 * Volá se, když je zavolána toolbar operace refresh
	 */
	protected abstract void refresh();

	/**
	 * Volá se pøi pøidání do pøedka (replace, add)
	 */
	protected void attach() {
		mainForm.setTitle(title);
	}

	public AbstractScreen(String title, SwitchableForm mainForm, AbstractScreen prevScreen) {
		this.mainForm = mainForm;
		this.title = title;
		this.prevScreen = prevScreen;
	}

	public AbstractScreen getPrevScreen() {
		return prevScreen;
	}
}
