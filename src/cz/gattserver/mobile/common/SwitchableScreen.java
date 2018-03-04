package cz.gattserver.mobile.common;

public abstract class SwitchableScreen {

	protected SwitchableScreen prev;
	protected SwitchableForm mainForm;
	protected String title;

	protected abstract void init();

	public SwitchableScreen(String title, SwitchableForm mainForm, SwitchableScreen prevScreen) {
		this.mainForm = mainForm;
		this.title = title;
		this.prev = prevScreen;
	}

	public void switchScreen() {
		mainForm.setTitle(title);
		mainForm.getContentPane().removeAll();
		init();
	}

	public void refresh() {
		mainForm.getContentPane().removeAll();
		init();
	}

	public SwitchableScreen getPrev() {
		return prev;
	}

}
