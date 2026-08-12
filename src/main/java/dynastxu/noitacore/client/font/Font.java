package dynastxu.noitacore.client.font;

public enum Font {
    TYPE('\uE721'),
    MANA_DRAIN('\uE722'),
    CAST_DELAY('\uE723'),
    SPREAD('\uE724'),
    RECHARGE('\uE725'),
    RADIUS('\uE726'),
    SPEED('\uE727'),
    CRIT('\uE728'),
    PROJECTILE('\uE729'),
    USES('\uE730'),
    NONE('\uE731');

    private final char c;
    Font(char c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return String.valueOf(c);
    }
}
