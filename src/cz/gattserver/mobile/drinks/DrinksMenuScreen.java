package cz.gattserver.mobile.drinks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Image;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.util.Base64;

import cz.gattserver.mobile.Config;
import cz.gattserver.mobile.common.SwitchableForm;

public class DrinksMenuScreen extends SwitchableForm {

	public DrinksMenuScreen(SwitchableForm prevForm) {
		super("Nápoje", prevForm);
	}

	private String orNothing(Object obj) {
		return obj == null ? "" : String.valueOf(obj);
	}

	@Override
	public SwitchableForm init() {
		setLayout(BoxLayout.y());
		setScrollableY(true);

		Button beersBtn = new Button("Piva");
		beersBtn.addActionListener(e -> new DrinksListScreen(DrinksMenuScreen.this, "Piva") {

			@Override
			protected String getListURL() {
				return Config.DRINKS_BEER_LIST_RESOURCE;
			}

			@Override
			protected String getCountURL() {
				return Config.DRINKS_BEER_COUNT_RESOURCE;
			}

			protected String constructDrinkLabel(Map<String, Object> drink) {
				return ((Double) drink.get("rating")).intValue() + "/5 " + drink.get("brewery") + " - "
						+ (String) drink.get("name");
			};

			@Override
			protected DrinksDetailScreen onDetail(int id, String nazev, DrinksListScreen drinksListScreen) {
				return new DrinksDetailScreen(id, nazev, drinksListScreen) {

					@Override
					protected void displayDetail(Map<String, Object> result) {
						String maltType = null;
						if ("BARLEY".equals(result.get("maltType")))
							maltType = "Ječmen";
						if ("WHEAT".equals(result.get("maltType")))
							maltType = "Pšenice";
						SpanLabel sl = new SpanLabel("Rating: " + ((Double) result.get("rating")).intValue() + "/5"
								+ "\nAlkohol (%): " + orNothing(result.get("alcohol")) + "\nIBU: "
								+ orNothing(result.get("ibu")) + "\nEPM (%): " + orNothing(result.get("degrees"))
								+ "\nTyp: " + orNothing(result.get("category")) + "\nSlad: " + maltType);
						sl.setUIID("spanlabel");
						add(sl);

						try {
							Image image;
							image = EncodedImage.createImage(
									new ByteArrayInputStream(Base64.decode(((String) result.get("image")).getBytes())));
							add(image);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					@Override
					protected String getDetailURLPrefix() {
						return Config.DRINKS_BEER_DETAIL_RESOURCE;
					}
				};
			}

		}.init().show());
		add(beersBtn);

		Button rumsBtn = new Button("Rumy");
		rumsBtn.addActionListener(e -> new DrinksListScreen(DrinksMenuScreen.this, "Rumy") {

			@Override
			protected String getListURL() {
				return Config.DRINKS_RUM_LIST_RESOURCE;
			}

			@Override
			protected String getCountURL() {
				return Config.DRINKS_RUM_COUNT_RESOURCE;
			}

			@Override
			protected DrinksDetailScreen onDetail(int id, String nazev, DrinksListScreen drinksListScreen) {
				return new DrinksDetailScreen(id, nazev, drinksListScreen) {

					@Override
					protected void displayDetail(Map<String, Object> result) {
						SpanLabel sl = new SpanLabel("Rating: " + (Double) result.get("rating"));
						sl.setUIID("spanlabel");
						add(sl);

						try {
							Image image;
							image = EncodedImage.createImage(
									new ByteArrayInputStream(Base64.decode(((String) result.get("image")).getBytes())));
							add(image);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					@Override
					protected String getDetailURLPrefix() {
						return Config.DRINKS_RUM_DETAIL_RESOURCE;
					}
				};
			}

		}.init().show());
		add(rumsBtn);

		Button whiskeyBtn = new Button("Whiskey");
		whiskeyBtn.addActionListener(e -> new DrinksListScreen(DrinksMenuScreen.this, "Whiskey") {

			@Override
			protected String getListURL() {
				return Config.DRINKS_WHISKEY_LIST_RESOURCE;
			}

			@Override
			protected String getCountURL() {
				return Config.DRINKS_WHISKEY_COUNT_RESOURCE;
			}

			@Override
			protected DrinksDetailScreen onDetail(int id, String nazev, DrinksListScreen drinksListScreen) {
				return new DrinksDetailScreen(id, nazev, drinksListScreen) {

					@Override
					protected void displayDetail(Map<String, Object> result) {
						SpanLabel sl = new SpanLabel("Rating: " + (Double) result.get("rating"));
						sl.setUIID("spanlabel");
						add(sl);

						try {
							Image image;
							image = EncodedImage.createImage(
									new ByteArrayInputStream(Base64.decode(((String) result.get("image")).getBytes())));
							add(image);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					@Override
					protected String getDetailURLPrefix() {
						return Config.DRINKS_WHISKEY_DETAIL_RESOURCE;
					}
				};
			}

		}.init().show());
		add(whiskeyBtn);

		Button wineBtn = new Button("Vína");
		wineBtn.addActionListener(e -> new DrinksListScreen(DrinksMenuScreen.this, "Vína") {

			@Override
			protected String getListURL() {
				return Config.DRINKS_WINE_LIST_RESOURCE;
			}

			@Override
			protected String getCountURL() {
				return Config.DRINKS_WINE_COUNT_RESOURCE;
			}

			@Override
			protected DrinksDetailScreen onDetail(int id, String nazev, DrinksListScreen drinksListScreen) {
				return new DrinksDetailScreen(id, nazev, drinksListScreen) {

					@Override
					protected void displayDetail(Map<String, Object> result) {
						SpanLabel sl = new SpanLabel("Rating: " + (Double) result.get("rating"));
						sl.setUIID("spanlabel");
						add(sl);

						try {
							Image image;
							image = EncodedImage.createImage(
									new ByteArrayInputStream(Base64.decode(((String) result.get("image")).getBytes())));
							add(image);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					@Override
					protected String getDetailURLPrefix() {
						return Config.DRINKS_WINE_DETAIL_RESOURCE;
					}
				};
			}

		}.init().show());
		add(wineBtn);

		revalidate();

		return this;
	}

}
