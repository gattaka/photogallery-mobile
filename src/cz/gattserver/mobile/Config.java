package cz.gattserver.mobile;

public class Config {

	public static final String SERVER_ROOT = "http://gattserver.cz";
	// public static final String SERVER_ROOT = "http://localhost:8180/web";

	public static final String PG_ROOT = SERVER_ROOT + "/ws/pg";
	public static final String PG_CREATE = PG_ROOT + "/create";
	public static final String PG_LIST_RESOURCE = PG_ROOT + "/list";
	public static final String PG_COUNT_RESOURCE = PG_ROOT + "/count";
	public static final String PG_DETAIL_RESOURCE = PG_ROOT + "/gallery";
	public static final String PHOTO_MINIATURE_RESOURCE = PG_ROOT + "/mini";
	public static final String PHOTO_DETAIL_RESOURCE = PG_ROOT + "/photo";

	public static final String RECIPES_ROOT = SERVER_ROOT + "/ws/recipes";
	public static final String RECIPES_LIST_RESOURCE = RECIPES_ROOT + "/list";
	public static final String RECIPES_COUNT_RESOURCE = RECIPES_ROOT + "/count";
	public static final String RECIPE_DETAIL_RESOURCE = RECIPES_ROOT + "/recipe";

	public static final String SONGS_ROOT = SERVER_ROOT + "/ws/songs";
	public static final String SONGS_LIST_RESOURCE = SONGS_ROOT + "/list";
	public static final String SONGS_COUNT_RESOURCE = SONGS_ROOT + "/count";
	public static final String SONG_DETAIL_RESOURCE = SONGS_ROOT + "/song";

	public static final String DRINKS_ROOT = SERVER_ROOT + "/ws/drinks";
	public static final String DRINKS_BEER_LIST_RESOURCE = DRINKS_ROOT + "/beer-list";
	public static final String DRINKS_BEER_COUNT_RESOURCE = DRINKS_ROOT + "/beer-count";
	public static final String DRINKS_BEER_DETAIL_RESOURCE = DRINKS_ROOT + "/beer";
	public static final String DRINKS_RUM_LIST_RESOURCE = DRINKS_ROOT + "/rum-list";
	public static final String DRINKS_RUM_COUNT_RESOURCE = DRINKS_ROOT + "/rum-count";
	public static final String DRINKS_RUM_DETAIL_RESOURCE = DRINKS_ROOT + "/rum";
	public static final String DRINKS_WHISKEY_LIST_RESOURCE = DRINKS_ROOT + "/whiskey-list";
	public static final String DRINKS_WHISKEY_COUNT_RESOURCE = DRINKS_ROOT + "/whiskey-count";
	public static final String DRINKS_WHISKEY_DETAIL_RESOURCE = DRINKS_ROOT + "/whiskey";
	public static final String DRINKS_WINE_LIST_RESOURCE = DRINKS_ROOT + "/wine-list";
	public static final String DRINKS_WINE_COUNT_RESOURCE = DRINKS_ROOT + "/wine-count";
	public static final String DRINKS_WINE_DETAIL_RESOURCE = DRINKS_ROOT + "/wine";

}
