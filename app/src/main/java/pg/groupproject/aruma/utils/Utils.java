package pg.groupproject.aruma.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class Utils {

	public static String formatTimeFromSeconds(long seconds) {
		long hours = seconds / 3600;
		long minutes = (seconds % 3600) / 60;
		long secondsLeft = seconds % 60;

		return String.format("%02d:%02d:%02d", hours, minutes, secondsLeft);
	}

	public static String formatDistanceFromMeters(long meters) {
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

		symbols.setGroupingSeparator(' ');
		formatter.setDecimalFormatSymbols(symbols);
		return formatter.format(meters) + " m";
	}

	public static String formatAverageSpeedFromMetersAndSeconds(long meters, long seconds) {
		double avgSpeed = (double) meters / seconds;
		return avgSpeed + " m/s";
	}
}
