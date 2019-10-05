package pg.groupproject.aruma.fragments.history;

import com.robinhood.spark.SparkAdapter;

import java.util.Random;

public class HistoryDetailsAltitudeChartAdapter extends SparkAdapter {
	private double[] yData;

	public HistoryDetailsAltitudeChartAdapter(double[] yData) {
		this.yData = yData;
	}

	public HistoryDetailsAltitudeChartAdapter() {
		Random random = new Random();
		yData = new double[50];
		for (int i = 0, count = yData.length; i < count; i++) {
			yData[i] = random.nextFloat();
		}
	}

	@Override
	public int getCount() {
		return yData.length;
	}

	@Override
	public Object getItem(int index) {
		return yData[index];
	}

	@Override
	public float getY(int index) {
		return (float) yData[index];
	}
}