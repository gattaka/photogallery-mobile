package cz.gattserver.mobile.common;

import com.codename1.ui.Container;

public abstract class SwitchableContainer extends Container implements SwitchableComponent, RefreshableComponent {

	protected SwitchableForm mainForm;
	protected SwitchableComponent prevComponent;
	protected String title;

	/**
	 * Vol� se p�i p�id�n� do p�edka (replace, add)
	 */
	public void attach() {
		mainForm.setTitle(title);
	}

	public SwitchableContainer(String title, SwitchableForm mainForm, SwitchableComponent prevComponent) {
		this.mainForm = mainForm;
		this.title = title;
		this.prevComponent = prevComponent;
	}

	public SwitchableComponent getPrevComponent() {
		return prevComponent;
	}
}
