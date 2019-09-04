package pg.groupproject.aruma.fragments.viewModels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample title for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class SavedPointsContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<SavedPointViewModel> ITEMS = new ArrayList<SavedPointViewModel>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, SavedPointViewModel> ITEM_MAP = new HashMap<String, SavedPointViewModel>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createSavedPoint(i));
        }
    }

    private static void addItem(SavedPointViewModel item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static SavedPointViewModel createSavedPoint(int position) {
        return new SavedPointViewModel(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore description information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of title.
     */
    public static class SavedPointViewModel {
        public final String id;
        public final String title;
        public final String description;

        public SavedPointViewModel(String id, String title, String description) {
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
