package pg.groupproject.aruma.feature.route;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lombok.NonNull;
import pg.groupproject.aruma.feature.database.DatabaseHelper;

import static pg.groupproject.aruma.feature.route.Route.COLUMN_DISTANCE;
import static pg.groupproject.aruma.feature.route.Route.COLUMN_FINISHED;
import static pg.groupproject.aruma.feature.route.Route.COLUMN_ID;
import static pg.groupproject.aruma.feature.route.Route.COLUMN_NAME;
import static pg.groupproject.aruma.feature.route.Route.COLUMN_TIMESTAMP;
import static pg.groupproject.aruma.feature.route.Route.COLUMN_TOTAL_TIME_S;
import static pg.groupproject.aruma.feature.route.Route.TABLE_NAME;


public class RouteService {

	private DatabaseHelper dbHelper;

	public RouteService(Context context) {
		dbHelper = DatabaseHelper.getInstance(context);
	}

	public long create() {
		Route route = Route.builder()
				.name(Calendar.getInstance().getTime().toString())
				.distance(0)
				.totalSeconds(0)
				.finished(false)
				.build();

		return insert(route);
	}

	public long insert(@NonNull final Route route) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		// `id` and `timestamp` will be inserted automatically.
		final ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, route.getName());
		values.put(COLUMN_DISTANCE, route.getDistance());
		values.put(COLUMN_TOTAL_TIME_S, route.getTotalSeconds());
		values.put(COLUMN_FINISHED, route.isFinished());

		if (route.getId() != 0)
			values.put(COLUMN_ID, route.getId());
		if (route.getTimestamp() != null)
			values.put(COLUMN_TIMESTAMP, route.getTimestamp());

		long id = db.insert(TABLE_NAME, null, values);

		db.close();

		return id;
	}

	public int update(@NonNull final Route route) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		final ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, route.getName());
		values.put(COLUMN_DISTANCE, route.getDistance());
		values.put(COLUMN_TOTAL_TIME_S, route.getTotalSeconds());
		values.put(COLUMN_FINISHED, route.isFinished());

		final int update = db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
				new String[]{String.valueOf(route.getId())});
		db.close();

		return update;
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

		try {
			if (cursor != null) {
				cursor.moveToFirst();
				final Route route = buildFromCursor(cursor);
				cursor.close();
				db.close();
				return route;
			} else throw new IllegalStateException("Cursor was null");
		} catch (Exception e) {
			Log.w(RouteService.class.getName(), "Route with id: " + id + " not found!");
			return null;
		}
	}

	public Route getLastFinishedRoute() {
		final SQLiteDatabase db = dbHelper.getReadableDatabase();

		final Cursor cursor = db.query(TABLE_NAME,
				null,
				COLUMN_FINISHED + "=?",
				new String[]{String.valueOf(1)},
				null,
				null,
				COLUMN_TIMESTAMP + " DESC",
				"1");

		if (cursor != null) {
			cursor.moveToFirst();
			final Route route = buildFromCursor(cursor);
			cursor.close();
			db.close();
			return route;
		} else {
			// TODO replace with exception
			Log.w(RouteService.class.getName(), "There is no finished session");
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
		db.close();

		return routes;
	}


	public void deleteAll() {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_NAME);
		db.close();
	}

	private Route buildFromCursor(Cursor cursor) {
		return Route.builder()
				.id(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)))
				.name(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)))
				.distance(cursor.getDouble(cursor.getColumnIndex(COLUMN_DISTANCE)))
				.totalSeconds(cursor.getDouble(cursor.getColumnIndex(COLUMN_TOTAL_TIME_S)))
				.finished(cursor.getInt(cursor.getColumnIndex(COLUMN_FINISHED)) != 0)
				.timestamp(cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP)))
				.build();
	}
}