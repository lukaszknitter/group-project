package pg.groupproject.aruma.feature.location.finding;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Setter
@Getter
public class NominatimResponse {

    private String lat;
    private String lon;
    @JsonProperty("display_name")
    private String displayName;
    private String city;
    private NominatimResponseAddress address;
}
