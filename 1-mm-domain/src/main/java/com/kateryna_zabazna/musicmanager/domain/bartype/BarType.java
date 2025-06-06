package com.kateryna_zabazna.musicmanager.domain.bartype;

import java.util.Objects;

public class BarType {

    private long id;
    private final int beatCount; // e.g. for 6/8: 6
    private final int beatValue; // e.g. for 6/8: 8

    public BarType(long id, int beatCount, int beatValue) {
        this.id = id;
        this.beatCount = beatCount;
        this.beatValue = beatValue;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public int getBeatCount() {
        return beatCount;
    }

    public int getBeatValue() {
        return beatValue;
    }

    public String[] getFieldContents() {
        return new String[] {String.valueOf(id), String.valueOf(beatCount), String.valueOf(beatValue)};
    }

    public static String[] getCSVHeader() {
        return new String[] {"Id", "BeatCount", "BeatValue"};
    }

    @Override
    public String toString() {
        return beatCount + "/" + beatValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        return ((BarType) obj).getId() == this.id;
    }
}