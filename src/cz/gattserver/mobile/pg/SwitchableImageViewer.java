package cz.gattserver.mobile.pg;

import com.codename1.components.ImageViewer;

public class SwitchableImageViewer extends ImageViewer implements SwitchableComponent {

	protected SwitchableComponent prevComponent;

	public SwitchableImageViewer(SwitchableComponent prevComponent) {
		this.prevComponent = prevComponent;
	}

	@Override
	public SwitchableComponent getPrevComponent() {
		return prevComponent;
	}

	@Override
	public void attach() {
	}

}
