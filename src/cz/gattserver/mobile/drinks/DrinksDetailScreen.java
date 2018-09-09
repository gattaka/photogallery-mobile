package cz.gattserver.mobile.drinks;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Display;
import com.codename1.ui.layouts.BoxLayout;

import cz.gattserver.mobile.Config;
import cz.gattserver.mobile.common.ErrorHandler;
import cz.gattserver.mobile.common.ErrorType;
import cz.gattserver.mobile.common.SwitchableForm;

public abstract class DrinksDetailScreen extends SwitchableForm {

	private int id;

	public DrinksDetailScreen(int id, String name, SwitchableForm prevForm) {
		super(name, prevForm);
		this.id = id;
	}

	protected abstract void displayDetail(Map<String, Object> result);
	
	protected abstract String getDetailURLPrefix();

	@Override
	public SwitchableForm init() {
		setLayout(BoxLayout.y());
		setScrollableY(true);

		InfiniteProgress prog = new InfiniteProgress();

		ConnectionRequest request = new ConnectionRequest() {
			@Override
			protected void handleErrorResponseCode(int code, String message) {
				switch (code) {
				case 404:
					ErrorHandler.showError(ErrorType.RECORD, getContentPane());
					break;
				default:
					ErrorHandler.showError(ErrorType.SERVER, getContentPane());
					super.handleErrorResponseCode(code, message);
				}
			}

			protected void handleException(Exception err) {
				if (Display.isInitialized() && !Display.getInstance().isMinimized())
					ErrorHandler.showError(ErrorType.CONNECTION, getContentPane());
			}

			protected void readResponse(java.io.InputStream input) throws IOException {
				JSONParser p = new JSONParser();
				Map<String, Object> result = p.parseJSON(new InputStreamReader(input, "UTF-8"));

				if (result != null)
					displayDetail(result);
			};
		};
		request.setUrl(getDetailURLPrefix());
		request.setPost(false);
		request.addArgument("id", String.valueOf(id));
		request.setDisposeOnCompletion(prog.showInifiniteBlocking());
		NetworkManager.getInstance().addToQueueAndWait(request);

		return this;
	}

}
