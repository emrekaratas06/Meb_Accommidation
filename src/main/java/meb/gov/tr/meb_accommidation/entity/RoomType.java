package meb.gov.tr.meb_accommidation.entity;

public enum RoomType {
    SINGLE("Single Room"),
    DOUBLE("Double Room"),
    TRIPLE("Triple Room"),
    TWINS("TwinA_TwinB"),
    SUİT("Suit Room"),
    OTHER("Other Room");

    private final String displayName;

    RoomType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
