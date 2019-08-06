package pg.groupproject.aruma.feature.place;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Place {
	public static final String TABLE_NAME = "route";

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
}