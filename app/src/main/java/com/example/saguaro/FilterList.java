package com.example.saguaro;

public enum FilterList {
    BLUE, CYAN, GRAY, GREEN, LTGRAY,
    MAGENTA, RED, TRANSPARENT, YELLOW;

    private static FilterList[] vals = values();

    public FilterList next() {
        return vals[(this.ordinal() + 1) % vals.length];
    }

    public FilterList previous() {
        return this.ordinal() - 1 < 0 ? vals[vals.length - 1] : vals[this.ordinal() - 1];
    }
}