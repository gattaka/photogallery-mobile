package cz.gattserver.mobile.recipes;

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
import com.codename1.util.StringUtil;

import cz.gattserver.mobile.Config;
import cz.gattserver.mobile.common.ErrorHandler;
import cz.gattserver.mobile.common.ErrorType;
import cz.gattserver.mobile.common.SwitchableForm;

public class RecipeDetailScreen extends SwitchableForm {

	private int recipeId;

	public RecipeDetailScreen(int recipeId, String recipeName, SwitchableForm prevForm) {
		super(recipeName, prevForm);
		this.recipeId = recipeId;
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
					String out = StringUtil.replaceAll((String) result.get("description"), "<br/>", "\n");
					out = StringUtil.replaceAll(out, "<br>", "\n");
					SpanLabel sl = new SpanLabel(out);
					sl.setUIID("spanlabel");
					add(sl);
				}
			};
		};
		request.setUrl(Config.RECIPE_DETAIL_RESOURCE);
		request.setPost(false);
		request.addArgument("id", String.valueOf(recipeId));
		request.setDisposeOnCompletion(prog.showInifiniteBlocking());
		NetworkManager.getInstance().addToQueueAndWait(request);

		return this;
	}

}
