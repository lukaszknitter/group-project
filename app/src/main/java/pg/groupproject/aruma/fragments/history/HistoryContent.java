package pg.groupproject.aruma.fragments.history;

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
public class HistoryContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<HistoryViewModel> ITEMS = new ArrayList<HistoryViewModel>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, HistoryViewModel> ITEM_MAP = new HashMap<String, HistoryViewModel>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createHistory(i));
        }
    }

    private static void addItem(HistoryViewModel item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static HistoryViewModel createHistory(int position) {
        return new HistoryViewModel(String.valueOf(position), "Item " + position, makeDetails(position));
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
    public static class HistoryViewModel {
        public final String id;
        public final String title;
        public final String description;

        public HistoryViewModel(String id, String title, String description) {
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
