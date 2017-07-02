package cz.gattserver.mobile.pg;

import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BorderLayout;

public class SwitchableForm extends Form {

	// Object protože nemùžu mít <T extends Component & SwitchableComponent>
	private Object currentComponent;

	private void back() {
		SwitchableComponent sc = (SwitchableComponent) currentComponent;
		if (sc.getPrevComponent() != null) {
			innerSwitchComponent(sc.getPrevComponent());
		}
	}

	public SwitchableForm() {
		super(new BorderLayout());
		if (getToolbar() != null) {
			setBackCommand(new Command("back") {
				@Override
				public void actionPerformed(ActionEvent evt) {
					back();
				}
			});
			getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, (e) -> {
				back();
			});
			getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_REFRESH, (e) -> {
				if (currentComponent instanceof RefreshableComponent)
					((RefreshableComponent) currentComponent).refresh();
			});
		}
	}

	private void innerSwitchComponent(Object newComponent) {
		Component c = (Component) newComponent;
		SwitchableComponent s = (SwitchableComponent) newComponent;
		if (currentComponent != null) {
			removeComponent((Component) currentComponent);
		}
		add(BorderLayout.CENTER, c);
		repaint();
		currentComponent = s;
		s.attach();
	}

	public <T extends Component & SwitchableComponent> void switchComponent(T newScreen) {
		innerSwitchComponent(newScreen);
	}

}
