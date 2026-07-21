package com.lockedIn.companion;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.lockedIn.companion.model.CompanionState;
import com.lockedIn.companion.service.*;
import com.lockedIn.companion.ui.PopUpService;

public class LockedInDesktopCompanion {
    public static void main(String[] args) throws NativeHookException {
        CompanionState state = new CompanionState();

        BackendClient backendClient = new BackendClient();
        CompanionHttpServer companionHttpServer = new CompanionHttpServer(backendClient);
        try {
            companionHttpServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    try {
                        backendClient.endSession();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
        );

        PopUpService popUpService = new PopUpService();

        ActivityTrackerService activityTrackerService = new ActivityTrackerService(state,backendClient);

        IdleMonitorService idleMonitorService =
                new IdleMonitorService(
                        popUpService,
                        backendClient,
                        state
                );

//        ResumeMonitorService resumeMonitorService =
//                new ResumeMonitorService(
//                        backendClient,
//                        popUpService,
//                        state
//                );

        ApplicationMonitorService applicationMonitorService =
                new ApplicationMonitorService(
                        backendClient,
                        popUpService
                );

        // Start keyboard + mouse tracking
        activityTrackerService.startTracking();

        // Idle monitoring thread
        new Thread(idleMonitorService::startMonitoring).start();

        // Resume monitoring thread
//        new Thread(resumeMonitorService::startMonitoring).start();

        // Application monitoring thread
        new Thread(applicationMonitorService::startMonitoring).start();

        System.out.println("LockedIn Companion Started...");
    }
}
