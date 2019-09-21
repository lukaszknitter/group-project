package pg.groupproject.aruma.feature.route;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Route {
	public static final String TABLE_NAME = "route";

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_DISTANCE = "distance";
	public static final String COLUMN_TOTAL_TIME_S = "total_time_s";
	public static final String COLUMN_TIMESTAMP = "timestamp";
	public static final String CREATE_TABLE =
			"CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
					+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ COLUMN_DISTANCE + " DECIMAL(30,2), "
					+ COLUMN_TOTAL_TIME_S + " DECIMAL(30,2), "
					+ COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP);";
	private int id;
	private double distance;
	private double totalSeconds;
	private String timestamp;

	public static Route fromCSVRow(@NonNull final String[] values) {
		return Route.builder()
				.id(Integer.parseInt(values[0]))
				.distance(Double.parseDouble(values[1]))
				.totalSeconds(Double.parseDouble(values[2]))
				.timestamp(values[3])
				.build();
	}

	public static String csvHeader(@NonNull final String csvSeparator) {
		return COLUMN_ID + csvSeparator +
				COLUMN_DISTANCE + csvSeparator +
				COLUMN_TOTAL_TIME_S + csvSeparator +
				COLUMN_TIMESTAMP;
	}

	public String toCSVRow(@NonNull final String csvSeparator) {
		return id + csvSeparator +
				distance + csvSeparator +
				totalSeconds + csvSeparator +
				timestamp;
	}
}