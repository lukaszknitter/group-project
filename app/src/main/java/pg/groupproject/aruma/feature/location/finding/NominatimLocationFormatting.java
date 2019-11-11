package pg.groupproject.aruma.feature.location.finding;

import com.google.android.gms.common.util.Strings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class NominatimLocationFormatting {

    public static final String EMPTY_VALUE = "";

    static String formatAddress(NominatimLocationAddress address) {
        final StringBuilder value = new StringBuilder();
        if (!Strings.isEmptyOrWhitespace(address.getCity())) {
            value.append(address.getCity());
        }
        if (!Strings.isEmptyOrWhitespace(address.getRoad())) {
            if (!Strings.isEmptyOrWhitespace(address.getHouseNumber())) {
                addSeparator(value);
                value.append(address.getRoad() + " " + address.getHouseNumber());
            } else {
                addSeparator(value);
                value.append(address.getRoad());
            }
        }
        if (!Strings.isEmptyOrWhitespace(address.getCityDistrict())) {
            addSeparator(value);
            value.append(address.getCityDistrict());
        }
        if (!Strings.isEmptyOrWhitespace(address.getState())) {
            addSeparator(value);
            value.append(address.getState());
        }
        return value.toString();
    }

    static String formatFeatureName(String name) {
        if (Strings.isEmptyOrWhitespace(name)) {
            return EMPTY_VALUE;
        }
        if (!name.contains(",")) {
            return name;
        }
        return name.substring(0, name.indexOf(','));
    }

    private static void addSeparator(StringBuilder value) {
        if (value.length() != 0) {
            value.append(", ");
        }
    }
}
