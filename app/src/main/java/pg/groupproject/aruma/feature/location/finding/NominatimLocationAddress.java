package pg.groupproject.aruma.feature.location.finding;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.android.gms.common.util.Strings;

import java.util.Locale;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
class NominatimLocationAddress implements Comparable<NominatimLocationAddress> {
    private String city;
    @JsonProperty("state_district")
    private String stateDistrict;
    private String country;
    @JsonProperty("country_code")
    private String countryCode;

    @Override
    public int compareTo(NominatimLocationAddress other) {
        if (other == null) {
            return -1;
        }

        final boolean thisEmptyCountryCode = Strings.isEmptyOrWhitespace(this.countryCode);
        final boolean otherEmptyCountryCode = Strings.isEmptyOrWhitespace(other.countryCode);
        if (!thisEmptyCountryCode && otherEmptyCountryCode) {
            return 1;
        } else if (thisEmptyCountryCode && !otherEmptyCountryCode) {
            return -1;
        }
        final String localeCountryCode = Locale.getDefault().getCountry();
        final boolean thisSameCountryCode = localeCountryCode.equalsIgnoreCase(this.countryCode);
        final boolean otherSameCountryCode = localeCountryCode.equalsIgnoreCase(other.countryCode);
        if (thisSameCountryCode && !otherSameCountryCode) {
            return -1;
        } else if (!thisSameCountryCode && otherSameCountryCode) {
            return 1;
        }

        // TODO tutaj porównanie LAT/LON z lokalizacji

        final boolean thisEmptyCountry = Strings.isEmptyOrWhitespace(this.country);
        final boolean otherEmptyCountry = Strings.isEmptyOrWhitespace(other.country);
        if (!thisEmptyCountry && otherEmptyCountry) {
            return -1;
        } else if (thisEmptyCountry && !otherEmptyCountry) {
            return 1;
        }

        final boolean thisEmptyCity = Strings.isEmptyOrWhitespace(this.city);
        final boolean otherEmptyCity = Strings.isEmptyOrWhitespace(other.city);
        if (!thisEmptyCity && otherEmptyCity) {
            return -1;
        } else if (thisEmptyCity && !otherEmptyCity) {
            return 1;
        }
        return 0;
    }
}
