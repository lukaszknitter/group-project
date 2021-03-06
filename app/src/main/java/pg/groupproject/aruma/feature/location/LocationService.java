package pg.groupproject.aruma.feature.location;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import pg.groupproject.aruma.feature.database.DatabaseHelper;

import static pg.groupproject.aruma.feature.location.Location.COLUMN_ALTITUDE;
import static pg.groupproject.aruma.feature.location.Location.COLUMN_ID;
import static pg.groupproject.aruma.feature.location.Location.COLUMN_LATITUDE;
import static pg.groupproject.aruma.feature.location.Location.COLUMN_LONGITUDE;
import static pg.groupproject.aruma.feature.location.Location.COLUMN_ROUTE_ID;
import static pg.groupproject.aruma.feature.location.Location.COLUMN_SPEED;
import static pg.groupproject.aruma.feature.location.Location.COLUMN_TIMESTAMP;
import static pg.groupproject.aruma.feature.location.Location.TABLE_NAME;

public class LocationService {

	private DatabaseHelper dbHelper;

	public LocationService(Context context) {
		dbHelper = DatabaseHelper.getInstance(context);
	}

	public long insert(@NonNull final android.location.Location location, long routeId) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		final ContentValues values = new ContentValues();
		values.put(COLUMN_LATITUDE, location.getLatitude());
		values.put(COLUMN_LONGITUDE, location.getLongitude());
		values.put(COLUMN_ALTITUDE, location.getAltitude());
		values.put(COLUMN_SPEED, location.getSpeed());
		values.put(COLUMN_ROUTE_ID, routeId);

		long id = db.insert(TABLE_NAME, null, values);

		db.close();

		return id;
	}

	public long insert(@NonNull final Location location, long routeId) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		final ContentValues values = new ContentValues();
		values.put(COLUMN_LATITUDE, location.getLatitude());
		values.put(COLUMN_LONGITUDE, location.getLongitude());
		values.put(COLUMN_ALTITUDE, location.getAltitude());
		values.put(COLUMN_SPEED, location.getSpeed());
		values.put(COLUMN_ROUTE_ID, routeId);

		if (location.getId() != 0)
			values.put(COLUMN_ID, location.getId());
		if (location.getTimestamp() != null)
			values.put(COLUMN_TIMESTAMP, location.getTimestamp());

		long id = db.insert(TABLE_NAME, null, values);

		db.close();

		return id;
	}

	public List<Location> getAllByRouteId(long routeId) {
		final SQLiteDatabase db = dbHelper.getReadableDatabase();

		final String selectQuery = "SELECT  * FROM " + TABLE_NAME
				+ " WHERE " + COLUMN_ROUTE_ID + " = " + routeId
				+ " ORDER BY " + COLUMN_TIMESTAMP + " ASC";

		final Cursor cursor = db.rawQuery(selectQuery, null);

		final List<Location> locations = new ArrayList<>();
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				final Location location = buildFromCursor(cursor);

				locations.add(location);
				cursor.moveToNext();
			}
		}

		cursor.close();
		db.close();

		return locations;
	}

	public List<Location> getAll() {
		final SQLiteDatabase db = dbHelper.getReadableDatabase();

		final String selectQuery = "SELECT  * FROM " + TABLE_NAME
				+ " ORDER BY " + COLUMN_TIMESTAMP + " DESC";

		final Cursor cursor = db.rawQuery(selectQuery, null);

		final List<Location> locations = new ArrayList<>();
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				final Location location = buildFromCursor(cursor);

				locations.add(location);
				cursor.moveToNext();
			}
		}

		cursor.close();
		db.close();

		return locations;
	}

	public void deleteAll() {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_NAME);
		db.close();
	}

	private Location buildFromCursor(Cursor cursor) {
		return Location.builder()
				.id(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)))
				.latitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE)))
				.longitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE)))
				.altitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_ALTITUDE)))
				.speed(cursor.getFloat(cursor.getColumnIndex(COLUMN_SPEED)))
				.timestamp(cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP)))
				.routeId(cursor.getInt(cursor.getColumnIndex(COLUMN_ROUTE_ID)))
				.build();
	}
}