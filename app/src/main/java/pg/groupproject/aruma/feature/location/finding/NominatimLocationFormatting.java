package pg.groupproject.aruma.feature.location.finding;

import com.google.android.gms.common.util.Strings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class NominatimLocationFormatting {

    static String formatDisplayName(String value, int width) {
        if (Strings.isEmptyOrWhitespace(value)) {
            return "";
        }
        if (value.length() < width) {
            return value;
        }
        while (value.length() > width) {
            int lastIndex = value.lastIndexOf(',');
            if (lastIndex == -1) {
                return value.substring(0, width - 3) + "...";
            }
            value = value.substring(0, lastIndex).trim();
        }
        return value;
    }
}
