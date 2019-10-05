package pg.groupproject.aruma.feature.place;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import pg.groupproject.aruma.feature.database.DatabaseHelper;

import static pg.groupproject.aruma.feature.place.Place.COLUMN_ID;
import static pg.groupproject.aruma.feature.place.Place.COLUMN_LATITUDE;
import static pg.groupproject.aruma.feature.place.Place.COLUMN_LONGITUDE;
import static pg.groupproject.aruma.feature.place.Place.COLUMN_NAME;
import static pg.groupproject.aruma.feature.place.Place.COLUMN_TIMESTAMP;
import static pg.groupproject.aruma.feature.place.Place.TABLE_NAME;

public class PlaceService {

	private DatabaseHelper dbHelper;

	public PlaceService(Context context) {
		dbHelper = DatabaseHelper.getInstance(context);
	}

	public long insert(@NonNull final Place place) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		final ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, place.getName());
		values.put(COLUMN_LATITUDE, place.getLatitude());
		values.put(COLUMN_LONGITUDE, place.getLongitude());

		if (place.getId() != 0)
			values.put(COLUMN_ID, place.getId());
		if (place.getTimestamp() != null)
			values.put(COLUMN_TIMESTAMP, place.getTimestamp());

		long id = db.insert(TABLE_NAME, null, values);

		db.close();

		return id;
	}

	public Place get(long id) {
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
			final Place place = buildFromCursor(cursor);
			cursor.close();
			db.close();
			return place;
		} else {
			Log.w(PlaceService.class.getName(), "Place with id: " + id + " not found!");
			return null;
		}
	}

	public List<Place> getAll() {
		final SQLiteDatabase db = dbHelper.getReadableDatabase();

		final String selectQuery = "SELECT  * FROM " + Place.TABLE_NAME
				+ " ORDER BY " + COLUMN_TIMESTAMP + " DESC";

		final Cursor cursor = db.rawQuery(selectQuery, null);

		final List<Place> places = new ArrayList<>();
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				final Place place = buildFromCursor(cursor);

				places.add(place);
				cursor.moveToNext();
			}
		}

		cursor.close();
		db.close();

		return places;
	}

	public void deleteAll() {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_NAME);
		db.close();
	}

	private Place buildFromCursor(Cursor cursor) {
		return Place.builder()
				.id(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)))
				.name(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)))
				.latitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE)))
				.longitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE)))
				.timestamp(cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP)))
				.build();
	}
}