package pg.groupproject.aruma.feature.location.finding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimpleLocation {
    private static final float SHIFT = 0.20f;

    private Float lon;
    private Float lat;

    public boolean isEmpty() {
        return lon == null || lat == null;
    }

    public float getLonMin() {
        return lon - SHIFT;
    }

    public float getLonMax() {
        return lon + SHIFT;
    }

    public float getLatMin() {
        return lat - SHIFT;
    }

    public float getLatMax() {
        return lat + SHIFT;
    }

}
