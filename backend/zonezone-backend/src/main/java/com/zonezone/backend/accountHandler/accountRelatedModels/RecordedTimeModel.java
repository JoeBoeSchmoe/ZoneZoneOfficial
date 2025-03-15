package com.zonezone.backend.accountHandler.accountRelatedModels;

import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Embeddable // ✅ Allows embedding inside AccountModel
public class RecordedTimeModel {

    // Time Data
    private Long daysPlayed;
    private Long hoursPlayed;
    private Long minutesPlayed;
    private Long secondsPlayed;
    private LocalDateTime lastUpdated; // ✅ New field

    @PrePersist
    public void onCreate() {
        if (daysPlayed == null) {
            daysPlayed = 0L;
        }
        if (hoursPlayed == null) hoursPlayed = 0L;
        if (minutesPlayed == null) minutesPlayed = 0L;
        if (secondsPlayed == null) secondsPlayed = 0L;
    }

    // Normalizes Number Of Data Of Time
    public void normalizeTime() {
        secondsPlayed = Math.max(secondsPlayed, 0L);
        minutesPlayed = Math.max(minutesPlayed, 0L);
        hoursPlayed = Math.max(hoursPlayed, 0L);
        daysPlayed = Math.max(daysPlayed, 0L);

        if (secondsPlayed >= 60) {
            minutesPlayed += secondsPlayed / 60;
            secondsPlayed %= 60;
        }
        if (minutesPlayed >= 60) {
            hoursPlayed += minutesPlayed / 60;
            minutesPlayed %= 60;
        }
        if (hoursPlayed >= 24) {
            daysPlayed += hoursPlayed / 24;
            hoursPlayed %= 24;
        }
    }

    // Adds Time To Pre-Existing Data
    public void addTime(long days, long hours, long minutes, long seconds) {
        this.daysPlayed += Math.max(days, 0);
        this.hoursPlayed += Math.max(hours, 0);
        this.minutesPlayed += Math.max(minutes, 0);
        this.secondsPlayed += Math.max(seconds, 0);
        normalizeTime();
        lastUpdated = LocalDateTime.now();
    }

    // Returns A Normal Display Of Recorded Time
    public String getFormattedTime() {
        return String.format("%d days, %02d hours, %02d minutes, %02d seconds", daysPlayed, hoursPlayed, minutesPlayed, secondsPlayed);
    }

    // Resets The Time Back To Default Values
    public void resetTime() {
        this.daysPlayed = 0L;
        this.hoursPlayed = 0L;
        this.minutesPlayed = 0L;
        this.secondsPlayed = 0L;
        lastUpdated = LocalDateTime.now();
    }
}
