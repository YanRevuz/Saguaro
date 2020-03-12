package com.example.saguaro.Enum;

public enum FilterList {
    BLUE, GRAY, GREEN, RED, TRANSPARENT;

    private static FilterList[] vals = values();

    public FilterList next() {
        return vals[(this.ordinal() + 1) % vals.length];
    }

    public FilterList previous() {
        return this.ordinal() - 1 < 0 ? vals[vals.length - 1] : vals[this.ordinal() - 1];
    }
}