package com.easytrip.backend.enums;

import lombok.Getter;

@Getter
public enum TravelCompanion {
    SOLO("Solo"),
    COUPLE("Couple"),
    FAMILY("Family"),
    FRIENDS("Friends");

    private final String label;

    TravelCompanion(String label) {
        this.label = label;
    }
}
