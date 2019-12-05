package pg.groupproject.aruma.feature.location.finding;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SimpleLocation {
    private static final float EXTENDED_SHIFT = 5.0f;
    private static final float SHIFT = 0.20f;

    private Float lon;
    private Float lat;
    private float shift = SHIFT;

    public SimpleLocation(Float lon, Float lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public boolean isEmpty() {
        return lon == null || lat == null;
    }

    public float getLonMin() {
        return lon - shift;
    }

    public float getLonMax() {
        return lon + shift;
    }

    public float getLatMin() {
        return lat - shift;
    }

    public float getLatMax() {
        return lat + shift;
    }

    public void setExtendedShift() {
        this.shift = EXTENDED_SHIFT;
    }

}
