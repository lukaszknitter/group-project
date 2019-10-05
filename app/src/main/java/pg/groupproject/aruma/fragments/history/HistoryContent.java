package pg.groupproject.aruma.fragments.history;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import pg.groupproject.aruma.R;
import pg.groupproject.aruma.feature.route.Route;
import pg.groupproject.aruma.utils.Utils;

public class HistoryContent {

	private static HistoryViewModel createHistory(Route route, Resources resources) {
		return new HistoryViewModel(route.getId(), route.getName(), makeDetails(route, resources));
	}

	private static String makeDetails(Route route, Resources resources) {
		return resources.getString(R.string.route_details) + ": " +
				"\n" + resources.getString(R.string.route_details_total_distance) + ": " + route.getDistance() +
				"\n" + resources.getString(R.string.route_details_total_time) + ": " +
				Utils.formatTimeFromSeconds((long) route.getTotalSeconds());
	}

	static List<HistoryViewModel> createContent(@NonNull final List<Route> routes, @NonNull final Resources resources) {
		final List<HistoryViewModel> content = new ArrayList<>();
		routes.forEach(route -> content.add(createHistory(route, resources)));
		return content;
	}

	@Getter
	public static class HistoryViewModel {
		private final int id;
		private final String title;
		private final String description;

		HistoryViewModel(int id, String title, String description) {
			this.id = id;
			this.title = title;
			this.description = description;
		}

		@Override
		public String toString() {
			return title;
		}
	}
}
