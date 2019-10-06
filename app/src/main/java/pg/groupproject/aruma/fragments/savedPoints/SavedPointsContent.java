package pg.groupproject.aruma.fragments.savedPoints;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import pg.groupproject.aruma.R;
import pg.groupproject.aruma.feature.place.Place;

/**
 * Helper class for providing sample title for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class SavedPointsContent {


	private static SavedPointViewModel createSavedPoint(Place place, Resources resources) {
		return new SavedPointViewModel(place.getId(), place.getName(), makeDetails(place, resources));
	}

	private static String makeDetails(Place place, Resources resources) {
		return resources.getString(R.string.saved_place_creation_date) + ": " + place.getTimestamp();
	}

	static List<SavedPointsContent.SavedPointViewModel> createContent(@NonNull final List<Place> places, @NonNull final Resources resources) {
		final List<SavedPointsContent.SavedPointViewModel> content = new ArrayList<>();
		places.forEach(route -> content.add(createSavedPoint(route, resources)));
		return content;
	}


	@Getter
	public static class SavedPointViewModel {
		private final int id;
		private final String title;
		private final String description;

		SavedPointViewModel(int id, String title, String description) {
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
