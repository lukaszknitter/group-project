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
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DISTANCE = "distance";
	public static final String COLUMN_TOTAL_TIME_S = "total_time_s";
	public static final String COLUMN_FINISHED = "finished";
	public static final String COLUMN_TIMESTAMP = "timestamp";
	public static final String CREATE_TABLE =
			"CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
					+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ COLUMN_NAME + " TEXT, "
					+ COLUMN_DISTANCE + " DECIMAL(30,2), "
					+ COLUMN_TOTAL_TIME_S + " DECIMAL(30,2), "
					+ COLUMN_FINISHED + " BOOLEAN, "
					+ COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP);";
	private int id;
	private String name;
	private double distance;
	private double totalSeconds;
	private boolean finished;
	private String timestamp;

	public static Route fromCSVRow(@NonNull final String[] values) {
		return Route.builder()
				.id(Integer.parseInt(values[0]))
				.name(values[1])
				.distance(Double.parseDouble(values[2]))
				.totalSeconds(Double.parseDouble(values[3]))
				.finished(Boolean.parseBoolean(values[4]))
				.timestamp(values[5])
				.build();
	}

	public static String csvHeader(@NonNull final String csvSeparator) {
		return COLUMN_ID + csvSeparator +
				COLUMN_NAME + csvSeparator +
				COLUMN_DISTANCE + csvSeparator +
				COLUMN_TOTAL_TIME_S + csvSeparator +
				COLUMN_FINISHED + csvSeparator +
				COLUMN_TIMESTAMP;
	}

	public String toCSVRow(@NonNull final String csvSeparator) {
		return id + csvSeparator +
				name + csvSeparator +
				distance + csvSeparator +
				totalSeconds + csvSeparator +
				finished + csvSeparator +
				timestamp;
	}
}