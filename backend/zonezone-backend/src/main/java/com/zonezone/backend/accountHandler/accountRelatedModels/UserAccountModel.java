package com.zonezone.backend.accountHandler.accountRelatedModels;

/// @Author: Joseph Sheets
/// @Updated: 03/14/2025

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.time.LocalDate;
import java.time.Period;

@Setter
@Getter
@Entity
@Table(name = "user_accounts") // Custom table name
public class UserAccountModel {

    // Constants
    @Transient
    private static final double XP_SCALING_FACTOR = 1.25;

    @Transient
    private static final String profilePicturePathDirectory = "/home/joseph/zonezone-server/profilePictures/";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ✅ Auto-generate ID
    private Long userAccountID;

    // Account Type
    private String accountType;

    // Login Details
    private String accountUsername;
    private String accountPassword;

    // Personal Information
    private String accountEmail;
    private String inCountry;
    private String userTimeZone;
    private String selectedLanguage;
    private String userGender;
    private String userBirthday;
    private Integer userAge;

    // Online Status
    private Boolean isOnline;
    LocalDateTime lastOnlineTime;
    private Boolean isPlaying;
    private Boolean isInQueue;

    // Banning Values
    private String banReason;
    private Boolean isBanned;
    private LocalDateTime banTimeStamp;


    // Account Statistics
    private LocalDateTime accountCreationDate;
    private Integer numKills;
    private Integer numDeaths;
    private Double killDeathRatio;
    private Integer numGamesPlayed;
    private Integer numGamesWon;
    private Integer numGamesLost;
    private Integer numGamesTied;
    private Double winPercentage;
    private Integer userLevel;
    private Long currentLevelXP;
    private Long requiredLevelUpXP;
    private Long gemBalance;

    // Display Information
    private String profilePicturePath;

    // Social Details
    @ElementCollection
    @CollectionTable(name = "friended_users_table", joinColumns = @JoinColumn(name = "userAccountID"))
    @Column(name = "friended_user")
    private Set<Long> friendedUsersID = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "blocked_users_table", joinColumns = @JoinColumn(name = "userAccountID"))
    @Column(name = "blocked_user")
    private Set<Long> blockedUsersID = new HashSet<>();

    // Connected Objects For Play Time
    @Embedded
    private RecordedTimeModel totalUserPlayTime;

    @Transient
    private RecordedTimeModel userSessionPlayTime = new RecordedTimeModel();

    // Assigns Data To Variables Upon Creation
    @PrePersist
    public void onCreate() {
        // Errors Mean Variables Are Required For Account Creation
        // The Rest Are Just Given Default Values
        boolean developerMode = true; // Used To Bypass Required Parameters For Fast And Easy Testing

        // Required Or Optional Parameters
        if (accountType == null || accountType.isEmpty()) {
            accountType = "Standard";
        }
        if(accountUsername == null || accountUsername.isEmpty()) {
            if(developerMode) {
                accountUsername = "defaultUsername";
            }
            else {
                accountUsername = "ERROR: Username Not Found";
            }
        }
        if(accountPassword == null || accountPassword.isEmpty()) {
            if(developerMode) {
                accountPassword = "defaultPassword";
            }
            else {
                accountPassword = "ERROR: Password Not Found";
            }
        }
        if(accountEmail == null || accountEmail.isEmpty()) {
            if(developerMode) {
                accountEmail = "defaultEmail@gmail.com";
            }
            else {
                accountEmail = "ERROR: Email Address Not Found";
            }
        }
        if(inCountry == null || inCountry.isEmpty()) {
            if(developerMode) {
                inCountry = "United States";
            }
            else{
                inCountry = "ERROR: Country Not Found";
            }
        }
        if(userTimeZone == null || userTimeZone.isEmpty()) { // TODO
            userTimeZone = getSystemTimeZone();
        }
        if(selectedLanguage == null || selectedLanguage.isEmpty()) {
            if(developerMode) {
                selectedLanguage = "English";
            }
            else {
                selectedLanguage = "ERROR: Language Not Found";
            }
        }
        if(userGender == null || userGender.isEmpty()) {
            if(developerMode) {
                userGender = "Male";
            }
            else {
                userGender = "ERROR: Gender Not Found";
            }
        }
        if(userBirthday == null || userBirthday.isEmpty()) {
            if(developerMode) {
                userBirthday = "2004-01-30";
            }
            else {
                userBirthday = "ERROR: Birth Date Not Found";
            }
        }

        ensureUserBirthdayFormat(userBirthday);
        calculateAgeByBirthday();

        // Always Defaulted Values Upon Creation
        isBanned = false;
        isOnline = true;
        isPlaying = false;
        isInQueue = false;

        accountCreationDate = LocalDateTime.now();
        lastOnlineTime = null;
        banTimeStamp = null;
        banReason = null;

        numKills = 0;
        numDeaths = 0;
        killDeathRatio = 0.00;
        numGamesPlayed = 0;
        numGamesWon = 0;
        numGamesLost = 0;
        numGamesTied = 0;
        winPercentage = 0.00;
        userLevel = 1;
        currentLevelXP = 0L;
        requiredLevelUpXP = 100L;
        gemBalance = 0L;

        profilePicturePath = profilePicturePathDirectory + "default";

        totalUserPlayTime = new RecordedTimeModel();
        userSessionPlayTime = new RecordedTimeModel();

        totalUserPlayTime.resetTime();
        userSessionPlayTime.resetTime();
    }

    // Makes A Player Go Online
    public void goOnline() {
        isOnline = true;
    }

    // Makes Player Go Offline And Ensure No Inconsistencies In Data
    public void goOffline() {
        isOnline = false;
        lastOnlineTime = LocalDateTime.now();
        if(isPlaying || isInQueue) {
            isPlaying = false;
            isInQueue = false;
        }
        if(userSessionPlayTime != null) {
            userSessionPlayTime.resetTime();
        }
        calculateKillDeathRatio();
        calculateWinPercentage();
    }

    // Makes User Get Banned From ZoneZone
    public void banPunishment(String givenReason) {
        if (!accountType.equals("Admin")) {
            isBanned = true;
            banTimeStamp = LocalDateTime.now();
            banReason = givenReason;
            goOffline();
        }
    }

    // Sets Age Based Off Birthday
    public void calculateAgeByBirthday() {
        if (userBirthday != null) {
            this.userAge = Period.between(LocalDate.parse(this.userBirthday), LocalDate.now()).getYears();
        }
        if (this.userAge < 18 && !accountType.equals("Admin")) {
            this.accountType = "Limited"; // ✅ Auto-set "Limited" if under 18
        }
    }

    // Sets KD Based Off Wins And Losses
    public void calculateWinPercentage() {
        if(numGamesPlayed == 0) {
            winPercentage = 0.00;
        }
        else {
            winPercentage = (numGamesWon.doubleValue() / numGamesPlayed.doubleValue()) * 100.00;
        }
    }

    // Sets KD Based Off Kills And Deaths
    public void calculateKillDeathRatio() {
        if(numDeaths == 0) {
            killDeathRatio = numKills.doubleValue();
        }
        else {
            killDeathRatio = numKills.doubleValue() / numDeaths.doubleValue();
        }
    }

    // Increments A User's Play Time BOTH Total And The Current Session
    public void addPlayTime(int numDays, int numHours, int numMinutes, int numSeconds) {
        if(isOnline) {
            this.totalUserPlayTime.addTime(numDays, numHours, numMinutes, numSeconds);
            this.userSessionPlayTime.addTime(numDays, numHours, numMinutes, numSeconds);
        }
    }

    // Allows Flexibility In JSON "userBirthday" Variable, (XX-XX-XXXX or XXXX-XX-XXXX) Allowed
    public void ensureUserBirthdayFormat(String userBirthday) {
        if (userBirthday.matches("\\d{4}-\\d{2}-\\d{2}")) {
            this.userBirthday = userBirthday;
        }
        else {
            if (userBirthday.matches("\\d{2}-\\d{2}-\\d{4}")) {
                String[] parts = userBirthday.split("-");
                this.userBirthday = parts[2] + "-" + parts[0] + "-" + parts[1];
            }
            else {
                this.userBirthday = "ERROR: Invalid Date Format";
            }
        }
    }

    // Enter Queue Function
    public void enterQueue() {
        if(isOnline) {
            this.isInQueue = true;
            if(isPlaying) {
                isPlaying = false;
            }
        }
    }

    // Exit Queue Function
    public void exitQueue() {
        this.isInQueue = false;
    }

    // Enter Game Function
    public void enterGame() {
        if(isOnline) {
            this.isPlaying = true;
            exitQueue();
        }
    }

    // Exiting Game Function
    public void exitGame() {
        this.isPlaying = false;
    }

    // Record Game Results
    public void recordGameResult(boolean wonGame, boolean lostGame, boolean tiedGame, int hoursPlayed, int minutesPlayed, int secondsPlayed, int killsInGame, int deathsInGame, int gemsEarned, long experienceEarned) {
        // Ensure at least one game state is true
        int gameOutcomes = (wonGame ? 1 : 0) + (lostGame ? 1 : 0) + (tiedGame ? 1 : 0);
        if (gameOutcomes != 1) {
            System.err.println("Invalid game result: Exactly one of wonGame, lostGame, or tiedGame must be true.");
            return; // Avoids crashing if there's a mistake
        }

        // Ensure valid kill and death counts
        killsInGame = Math.max(0, killsInGame);
        deathsInGame = Math.max(0, deathsInGame);

        // Update Player Statistics
        numGamesPlayed++;
        if (wonGame) numGamesWon++;
        if (lostGame) numGamesLost++;
        if (tiedGame) numGamesTied++;

        // Update kill/death stats
        numKills += killsInGame;
        numDeaths += deathsInGame;

        // Reward User With Earned Values
        addGemsToBalance(gemsEarned);
        addExperienceToAccount(experienceEarned);

        // Update Kill Death Ratio
        calculateKillDeathRatio();

        // Update Win Percentage
        calculateWinPercentage();

        // Add Game Time To Player's Data
        this.totalUserPlayTime.addTime(0, hoursPlayed, minutesPlayed, secondsPlayed); // WILL NEVER PLAY A FULL DAY IN GAME

    }

    // Adds Long ID To FriendedUsersID
    public void addFriend(Long newFriendedUserID) {
        if (!friendedUsersID.contains(newFriendedUserID) && !newFriendedUserID.equals(userAccountID)) {
            friendedUsersID.add(newFriendedUserID);
        }
    }

    // Removes Long ID From FriendedUsersID
    public void removeFriend(Long exFriendedUserID) {
        friendedUsersID.removeIf(id -> id.equals(exFriendedUserID));
    }

    // Adding Long ID To BlockedUsersID
    public void addBlockUser(Long newBlockedUserID) {
        if (!blockedUsersID.contains(newBlockedUserID) && !newBlockedUserID.equals(userAccountID)) {
            blockedUsersID.add(newBlockedUserID);
        }
    }

    // Removing Long ID From BlockedUsersID
    public void removeBlockedUser(Long exBlockedUserID) {
        blockedUsersID.removeIf(id -> id.equals(exBlockedUserID));
    }

    // Adds Gems To User Balance
    public void addGemsToBalance(int gemsEarned) {
        gemBalance = gemBalance + gemsEarned;
    }

    // Removes Gems From User Balance
    public void removeGemsToBalance(int gemsTaken) {
        gemBalance = Math.max(0, gemBalance - gemsTaken); // Prevents negative gems
    }

    // Adds Player Experience To An Account
    public void addExperienceToAccount(long experienceEarned) {
        currentLevelXP = currentLevelXP + experienceEarned;
        checkLevelUpThreshold();
    }

    // Checks If Level Up Threshold Is Reached Based    On Current Level XP Count
    public void checkLevelUpThreshold() {
        while(currentLevelXP >= requiredLevelUpXP) {
            currentLevelXP = currentLevelXP - requiredLevelUpXP;
            requiredLevelUpXP = Math.round(requiredLevelUpXP *  XP_SCALING_FACTOR);
            userLevel++;
        }

    }

    public static boolean isValidTimeZone(String timeZone) {
        return ZoneId.getAvailableZoneIds().contains(timeZone);
    }

    // Fallback: Get the system's default timezone
    public static String getSystemTimeZone() {
        return ZoneId.systemDefault().toString();
    }

    // Sets The Player's Timezone
    public void setPlayerTimeZone(String givenTimeZone) {
        if(!isValidTimeZone(givenTimeZone)) {
            return;
        }
        else {
            userTimeZone = givenTimeZone;
        }
    }
}
