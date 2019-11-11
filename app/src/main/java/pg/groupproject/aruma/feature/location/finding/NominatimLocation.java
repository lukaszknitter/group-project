package pg.groupproject.aruma.feature.location.finding;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.android.gms.common.util.Strings;

import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Getter
class NominatimLocation implements Comparable<NominatimLocation> {

    private String lat;
    private String lon;
    @JsonProperty("display_name")
    private String displayName;
    private String type;
    private Double importance;
    private NominatimLocationAddress address;

    public String getDisplayName(int width) {
        return NominatimLocationFormatting.formatDisplayName(displayName, width);
    }

    @Override
    public int compareTo(NominatimLocation other) {
        if (other == null) {
            return -1;
        }

        final boolean thisLatOrLonEmpty = Strings.isEmptyOrWhitespace(this.lat) || Strings.isEmptyOrWhitespace(this.lon);
        final boolean otherLatOrLonEmpty = Strings.isEmptyOrWhitespace(other.lat) || Strings.isEmptyOrWhitespace(other.lon);
        if (!thisLatOrLonEmpty && otherLatOrLonEmpty) {
            return -1;
        } else if (thisLatOrLonEmpty && !otherLatOrLonEmpty) {
            return 1;
        }

        final boolean thisNameEmpty = Strings.isEmptyOrWhitespace(this.getDisplayName());
        final boolean otherNameEmpty = Strings.isEmptyOrWhitespace(other.getDisplayName());
        if (!thisNameEmpty && otherNameEmpty) {
            return -1;
        } else if (thisNameEmpty && !otherNameEmpty) {
            return 1;
        }

        if (this.address != null && other.address == null) {
            return -1;
        }
        final int addressCompareResult = this.address.compareTo(other.address);
        if (addressCompareResult != 0) {
            return addressCompareResult;
        }

        final double thisImportance = this.importance == null ? -1 : this.importance;
        final double otherImportance = other.importance == null ? -1 : other.importance;
        if (thisImportance != otherImportance) {
            return thisImportance > otherImportance ? -1 : 1;
        }
        return 0;
    }
}
