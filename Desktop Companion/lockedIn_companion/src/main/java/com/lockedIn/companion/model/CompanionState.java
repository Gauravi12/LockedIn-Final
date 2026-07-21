package com.lockedIn.companion.model;

import java.time.LocalDateTime;

public class CompanionState {

    public CompanionState() {
        this.lastActivityTime = LocalDateTime.now();
        this.popUpShown = false;
    }

    private boolean activityAfterPause;
    private LocalDateTime lastActivityTime;
    private boolean waitingForRealActivity;

    public boolean isActivityAfterPause() {
        return activityAfterPause;
    }

    public void setActivityAfterPause(boolean activityAfterPause) {
        this.activityAfterPause = activityAfterPause;
    }

    public boolean isWaitingForRealActivity() {
        return waitingForRealActivity;
    }

    public void setWaitingForRealActivity(boolean waitingForRealActivity) {
        this.waitingForRealActivity = waitingForRealActivity;
    }

    public LocalDateTime getLastActivityTime() {
        return lastActivityTime;
    }

    public void setLastActivityTime(LocalDateTime lastActivityTime) {
        this.lastActivityTime = lastActivityTime;
    }

    public boolean isPopUpShown() {
        return popUpShown;
    }

    public void setPopUpShown(boolean popUpShown) {
        this.popUpShown = popUpShown;
    }

    private boolean popUpShown;
}
