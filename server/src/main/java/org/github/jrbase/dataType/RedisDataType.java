package org.github.jrbase.dataType;

public enum RedisDataType {
    STRINGS("strings", "s"),
    HASHES("Hashes", "h"),
    LISTS("Lists", "l"),
    SETS("Sets", "c"),
    SORTED_SETS("Sorted Sets", "z");

    private final String name;
    private final String abbreviation;

    RedisDataType(String name, String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}
