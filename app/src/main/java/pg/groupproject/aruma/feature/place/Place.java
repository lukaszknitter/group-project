package pg.groupproject.aruma.feature.place;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Place {
	public static final String TABLE_NAME = "place";

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGITUDE = "longitude";
	public static final String COLUMN_TIMESTAMP = "timestamp";
	public static final String CREATE_TABLE =
			"CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
					+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ COLUMN_NAME + " TEXT, "
					+ COLUMN_LATITUDE + " DECIMAL(8,6), "
					+ COLUMN_LONGITUDE + " DECIMAL(9,6), "
					+ COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP);";
	private int id;
	private String name;
	private double latitude;
	private double longitude;
	private String timestamp;

	public static Place fromCSVRow(@NonNull final String[] values) {
		return Place.builder()
				.id(Integer.parseInt(values[0]))
				.name(values[1])
				.latitude(Double.parseDouble(values[2]))
				.longitude(Double.parseDouble(values[3]))
				.timestamp(values[4])
				.build();
	}

	public static String csvHeader(@NonNull final String csvSeparator) {
		return COLUMN_ID + csvSeparator +
				COLUMN_NAME + csvSeparator +
				COLUMN_LATITUDE + csvSeparator +
				COLUMN_LONGITUDE + csvSeparator +
				COLUMN_TIMESTAMP;
	}

	public String toCSVRow(@NonNull final String csvSeparator) {
		return id + csvSeparator +
				name + csvSeparator +
				latitude + csvSeparator +
				longitude + csvSeparator +
				timestamp;
	}
}