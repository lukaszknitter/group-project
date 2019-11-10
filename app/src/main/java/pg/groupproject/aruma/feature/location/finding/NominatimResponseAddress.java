package pg.groupproject.aruma.feature.location.finding;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NominatimResponseAddress {
    private String city;
    private String country;
}
