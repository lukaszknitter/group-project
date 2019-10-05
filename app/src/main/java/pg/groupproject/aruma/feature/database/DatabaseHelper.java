package pg.groupproject.aruma.feature.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import pg.groupproject.aruma.feature.location.Location;
import pg.groupproject.aruma.feature.place.Place;
import pg.groupproject.aruma.feature.route.Route;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 5;
	private static DatabaseHelper mInstance = null;
	private static final String DATABASE_NAME = "aruma_db";
	private final Context context;

	private DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	public static DatabaseHelper getInstance(Context ctx) {
		if (mInstance == null) {
			mInstance = new DatabaseHelper(ctx.getApplicationContext());
		}
		return mInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		Log.w(DatabaseHelper.class.getName(),
				"Creating database " + DATABASE_NAME + ": v: " + DATABASE_VERSION);

		database.execSQL(Route.CREATE_TABLE);
		database.execSQL(Location.CREATE_TABLE);
		database.execSQL(Place.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(DatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + Place.TABLE_NAME);
		database.execSQL("DROP TABLE IF EXISTS " + Location.TABLE_NAME);
		database.execSQL("DROP TABLE IF EXISTS " + Route.TABLE_NAME);
		onCreate(database);
	}
}
