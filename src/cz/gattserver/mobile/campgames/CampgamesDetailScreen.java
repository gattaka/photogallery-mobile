package cz.gattserver.mobile.campgames;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import com.codename1.components.InfiniteProgress;
import com.codename1.components.SpanLabel;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Display;
import com.codename1.ui.layouts.BoxLayout;

import cz.gattserver.mobile.Config;
import cz.gattserver.mobile.common.ErrorHandler;
import cz.gattserver.mobile.common.ErrorType;
import cz.gattserver.mobile.common.SwitchableForm;

public class CampgamesDetailScreen extends SwitchableForm {

	private int campgameId;

	public CampgamesDetailScreen(int id, String songName, SwitchableForm prevForm) {
		super(songName, prevForm);
		this.campgameId = id;
	}

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

				if (result != null) {
					SpanLabel sl = new SpanLabel((String) result.get("description"));
					add(sl);
				}
			};
		};
		request.setUrl(Config.CAMPGAMES_DETAIL_RESOURCE);
		request.setPost(false);
		request.addArgument("id", String.valueOf(campgameId));
		request.setDisposeOnCompletion(prog.showInfiniteBlocking());
		NetworkManager.getInstance().addToQueueAndWait(request);

		return this;
	}

}
