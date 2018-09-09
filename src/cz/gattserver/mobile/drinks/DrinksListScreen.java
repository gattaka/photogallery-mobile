package cz.gattserver.mobile.drinks;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import com.codename1.components.InfiniteScrollAdapter;
import com.codename1.components.MultiButton;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Component;
import com.codename1.ui.Display;
import com.codename1.ui.layouts.BoxLayout;

import cz.gattserver.mobile.common.ErrorHandler;
import cz.gattserver.mobile.common.ErrorType;
import cz.gattserver.mobile.common.SwitchableForm;

public abstract class DrinksListScreen extends SwitchableForm {

	private static final int PAGE_SIZE = 10;

	private int pageNumber = 0;

	public DrinksListScreen(SwitchableForm prevForm, String caption) {
		super(caption, prevForm);
	}

	protected abstract String getListURL();

	protected abstract String getCountURL();

	protected abstract DrinksDetailScreen onDetail(int id, String nazev, DrinksListScreen drinksListScreen);

	protected String constructDrinkLabel(Map<String, Object> drink) {
		return ((Double) drink.get("rating")).intValue() + "/5 " + (String) drink.get("name");
	}

	private List<Map<String, Object>> fetchPropertyData() {
		try {
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
			};

			request.setUrl(getListURL());
			request.setPost(false);
			request.addArgument("page", String.valueOf(pageNumber++));
			request.addArgument("pageSize", String.valueOf(PAGE_SIZE));
			NetworkManager.getInstance().addToQueueAndWait(request);

			if (request.getResponseCode() != 200)
				return null;

			Map<String, Object> result = new JSONParser()
					.parseJSON(new InputStreamReader(new ByteArrayInputStream(request.getResponseData()), "UTF-8"));

			@SuppressWarnings("unchecked")
			List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("root");
			return list;
		} catch (Exception err) {
			Log.e(err);
			return null;
		}
	}

	private Integer fetchCount() {
		try {
			ConnectionRequest request = new ConnectionRequest() {

				@Override
				protected void handleErrorResponseCode(int code, String message) {
					if (code != 200)
						ErrorHandler.showError(ErrorType.SERVER, getContentPane());
				}

				protected void handleException(Exception err) {
					if (Display.isInitialized() && !Display.getInstance().isMinimized())
						ErrorHandler.showError(ErrorType.CONNECTION, getContentPane());
				}
			};

			request.setUrl(getCountURL());
			request.setPost(false);
			NetworkManager.getInstance().addToQueueAndWait(request);

			if (request.getResponseCode() != 200)
				return null;

			Integer count = Integer.parseInt(new String(request.getResponseData(), "UTF-8"));
			return (int) Math.ceil((double) count / PAGE_SIZE);
		} catch (Exception err) {
			Log.e(err);
			return null;
		}
	}

	@Override
	public SwitchableForm init() {
		setLayout(BoxLayout.y());
		setScrollableY(true);

		pageNumber = 0;

		InfiniteScrollAdapter.createInfiniteScroll(getContentPane(), () -> {
			Integer pageCount = fetchCount();
			if (pageCount == null)
				return;
			List<Map<String, Object>> list = fetchPropertyData();
			MultiButton[] cmps = new MultiButton[list.size()];
			for (int iter = 0; iter < cmps.length; iter++) {
				Map<String, Object> drink = list.get(iter);
				if (drink == null) {
					InfiniteScrollAdapter.addMoreComponents(getContentPane(), new Component[0], false);
					return;
				}

				String nazev = constructDrinkLabel(drink);
				int id = (int) Double.parseDouble(String.valueOf(drink.get("id")));

				MultiButton btn = new MultiButton(nazev);
				btn.addActionListener(e -> onDetail(id, nazev, DrinksListScreen.this).init().show());
				cmps[iter] = btn;
			}
			InfiniteScrollAdapter.addMoreComponents(getContentPane(), cmps, pageNumber < pageCount);
		}, true);

		revalidate();

		return this;
	}

}
