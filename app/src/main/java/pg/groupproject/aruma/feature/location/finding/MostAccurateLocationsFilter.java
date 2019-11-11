package pg.groupproject.aruma.feature.location.finding;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class MostAccurateLocationsFilter {

    private static final int ACCURATE_LOCATIONS_COUNT = 5;

    static List<NominatimLocation> getMostAccurateLocations(List<NominatimLocation> locations) {
        final List<NominatimLocation> locationsWithCoordinates = locations.stream()
                .filter(l -> !noCoordinates(l))
                .sorted(NominatimLocation::compareTo)
                .collect(Collectors.toList());

        return locationsWithCoordinates.subList(0, getCountHowManyLocationsToReturn(locationsWithCoordinates));
    }

    private static int getCountHowManyLocationsToReturn(List<NominatimLocation> locations) {
        if (locations.size() > ACCURATE_LOCATIONS_COUNT) {
            return ACCURATE_LOCATIONS_COUNT;
        }
        return locations.size();
    }

    private static boolean noCoordinates(NominatimLocation loc) {
        return loc.getLat() == null && loc.getLon() == null;
    }
}
