package pg.groupproject.aruma.feature.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pg.groupproject.aruma.feature.route.Route;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {
	public static final String TABLE_NAME = "location";

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGITUDE = "longitude";
	public static final String COLUMN_SPEED = "speed";
	public static final String COLUMN_MASL = "meters_above_sea_level";
	public static final String COLUMN_TIMESTAMP = "timestamp";
	public static final String COLUMN_ROUTE_ID = "route_id";
	public static final String CREATE_TABLE =
			"CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
					+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ COLUMN_LATITUDE + " DECIMAL(8,6), "
					+ COLUMN_LONGITUDE + " DECIMAL(9,6), "
					+ COLUMN_SPEED + " DECIMAL(6,3), "
					+ COLUMN_MASL + " DECIMAL(30,2), "
					+ COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
					+ COLUMN_ROUTE_ID + " INTEGER, "
					+ "FOREIGN KEY (" + COLUMN_ROUTE_ID + ") "
					+ "REFERENCES " + Route.TABLE_NAME
					+ "(" + Route.COLUMN_ID + "));";
	private int id;
	private double latitude;
	private double longitude;
	private float speed;
	private double metersAboveSeaLevel;
	private String timestamp;
	private int routeId;
}