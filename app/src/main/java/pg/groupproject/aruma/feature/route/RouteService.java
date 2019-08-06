package pg.groupproject.aruma.feature.route;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import pg.groupproject.aruma.feature.database.DatabaseHelper;

import static pg.groupproject.aruma.feature.route.Route.COLUMN_DISTANCE;
import static pg.groupproject.aruma.feature.route.Route.COLUMN_ID;
import static pg.groupproject.aruma.feature.route.Route.COLUMN_TIMESTAMP;
import static pg.groupproject.aruma.feature.route.Route.COLUMN_TOTAL_TIME_S;
import static pg.groupproject.aruma.feature.route.Route.TABLE_NAME;


public class RouteService {

	private DatabaseHelper dbHelper;

	public RouteService(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	public long create() {
		Route route = Route.builder()
				.distance(0)
				.totalSeconds(0)
				.build();

		return insert(route);
	}

	public long insert(@NonNull final Route route) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		// `id` and `timestamp` will be inserted automatically.
		final ContentValues values = new ContentValues();
		values.put(COLUMN_DISTANCE, route.getDistance());
		values.put(COLUMN_TOTAL_TIME_S, route.getTotalSeconds());

		long id = db.insert(TABLE_NAME, null, values);

		db.close();

		return id;
	}

	public int update(@NonNull final Route route) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		final ContentValues values = new ContentValues();
		values.put(COLUMN_DISTANCE, route.getDistance());
		values.put(COLUMN_TOTAL_TIME_S, route.getTotalSeconds());

		return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
				new String[]{String.valueOf(route.getId())});
	}

	public void delete(long id) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(TABLE_NAME, COLUMN_ID + " = ?",
				new String[]{String.valueOf(id)});
		db.close();
	}

	public Route get(long id) {
		final SQLiteDatabase db = dbHelper.getReadableDatabase();

		final Cursor cursor = db.query(TABLE_NAME,
				null,
				COLUMN_ID + "=?",
				new String[]{String.valueOf(id)},
				null,
				null,
				null,
				null);

		if (cursor != null) {
			cursor.moveToFirst();
			final Route route = buildFromCursor(cursor);
			cursor.close();
			return route;
		} else {
			// TODO replace with exception
			Log.w(RouteService.class.getName(), "Route with id: " + id + " not found!");
			return null;
		}
	}

	public List<Route> getAll() {
		final SQLiteDatabase db = dbHelper.getReadableDatabase();

		final String selectQuery = "SELECT  * FROM " + TABLE_NAME
				+ " ORDER BY " + COLUMN_TIMESTAMP + " DESC";

		final Cursor cursor = db.rawQuery(selectQuery, null);

		final List<Route> routes = new ArrayList<>();
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				final Route route = buildFromCursor(cursor);

				routes.add(route);
				cursor.moveToNext();
			}
		}

		cursor.close();

		return routes;
	}

	private Route buildFromCursor(Cursor cursor) {
		return Route.builder()
				.id(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)))
				.distance(cursor.getDouble(cursor.getColumnIndex(COLUMN_DISTANCE)))
				.totalSeconds(cursor.getDouble(cursor.getColumnIndex(COLUMN_TOTAL_TIME_S)))
				.timestamp(cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP)))
				.build();
	}
}