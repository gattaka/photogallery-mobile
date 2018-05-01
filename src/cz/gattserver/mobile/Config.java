package cz.gattserver.mobile;

public class Config {

	public static final String SERVER_ROOT = "http://gattserver.cz";
	// public static final String SERVER_ROOT = "http://localhost:8180/web";

	public static final String PG_ROOT = SERVER_ROOT + "/ws/pg";

	public static final String GALLERY_CREATE = PG_ROOT + "/create";
	public static final String GALLERY_LIST_RESOURCE = PG_ROOT + "/list";
	public static final String GALLERY_COUNT_RESOURCE = PG_ROOT + "/count";
	public static final String GALLERY_DETAIL_RESOURCE = PG_ROOT + "/gallery";
	public static final String PHOTO_MINIATURE_RESOURCE = PG_ROOT + "/mini";
	public static final String PHOTO_DETAIL_RESOURCE = PG_ROOT + "/photo";

	public static final String RECIPES_ROOT = SERVER_ROOT + "/ws/recipes";

	public static final String RECIPES_LIST_RESOURCE = RECIPES_ROOT + "/list";
	public static final String RECIPES_COUNT_RESOURCE = RECIPES_ROOT + "/count";
	public static final String RECIPE_DETAIL_RESOURCE = RECIPES_ROOT + "/recipe";

}
