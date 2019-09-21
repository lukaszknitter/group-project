package pg.groupproject.aruma.feature.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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
	public static final String COLUMN_ALTITUDE = "altitude";
	public static final String COLUMN_TIMESTAMP = "timestamp";
	public static final String COLUMN_ROUTE_ID = "route_id";
	public static final String CREATE_TABLE =
			"CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
					+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ COLUMN_LATITUDE + " DECIMAL(8,6), "
					+ COLUMN_LONGITUDE + " DECIMAL(9,6), "
					+ COLUMN_SPEED + " DECIMAL(6,3), "
					+ COLUMN_ALTITUDE + " DECIMAL(30,2), "
					+ COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
					+ COLUMN_ROUTE_ID + " INTEGER, "
					+ "FOREIGN KEY (" + COLUMN_ROUTE_ID + ") "
					+ "REFERENCES " + Route.TABLE_NAME
					+ "(" + Route.COLUMN_ID + "));";
	private int id;
	private double latitude;
	private double longitude;
	private float speed;
	private double altitude;
	private String timestamp;
	private int routeId;

	public static Location fromCSVRow(@NonNull final String[] values) {
		return Location.builder()
				.id(Integer.parseInt(values[0]))
				.latitude(Double.parseDouble(values[1]))
				.longitude(Double.parseDouble(values[2]))
				.speed(Float.parseFloat(values[3]))
				.altitude(Double.parseDouble(values[4]))
				.timestamp(values[5])
				.routeId(Integer.parseInt(values[6]))
				.build();
	}

	public static String csvHeader(@NonNull final String csvSeparator) {
		return COLUMN_ID + csvSeparator +
				COLUMN_LATITUDE + csvSeparator +
				COLUMN_LONGITUDE + csvSeparator +
				COLUMN_SPEED + csvSeparator +
				COLUMN_ALTITUDE + csvSeparator +
				COLUMN_TIMESTAMP + csvSeparator +
				COLUMN_ROUTE_ID;
	}

	public String toCSVRow(@NonNull final String csvSeparator) {
		return id + csvSeparator +
				latitude + csvSeparator +
				longitude + csvSeparator +
				speed + csvSeparator +
				altitude + csvSeparator +
				timestamp + csvSeparator +
				routeId;
	}
}