package com.lockedIn.companion.service;

import com.lockedIn.companion.config.CompanionConfig;
import com.lockedIn.companion.model.CompanionState;
import com.lockedIn.companion.ui.PopUpService;

import java.security.cert.Extension;
import java.time.Duration;
import java.time.LocalDateTime;

public class IdleMonitorService {

    private final PopUpService popUpService;
    private final BackendClient backendClient;
    private final CompanionState state;

    public IdleMonitorService(PopUpService popUpService , BackendClient backendClient , CompanionState state){
        this.popUpService = popUpService;
        this.backendClient = backendClient;
        this.state = state;
    }

    public void startMonitoring(){

        System.out.println("Idle Monitor Started");
        while (true){
            System.out.println("waitingForRealActivity = " + state.isWaitingForRealActivity());
            System.out.println("Idle Monitor Running");
            try {
                String status = backendClient.getSessionStatus();
                if(status == null || !status.equals("ACTIVE")){
                    Thread.sleep(CompanionConfig.CHECK_INTERVAL_SECONDS * 1000L);
                    continue;
                }
                if (state.isWaitingForRealActivity()) {
                    Thread.sleep(CompanionConfig.CHECK_INTERVAL_SECONDS * 1000L);
                    continue;
                }

                long idleSeconds = Duration.between(state.getLastActivityTime(), LocalDateTime.now()).getSeconds();

                long idleLimit = CompanionConfig.IDLE_MINUTES * 60L;
                if(idleSeconds >= idleLimit && !state.isPopUpShown()) {
                    state.setPopUpShown(true);
                    LocalDateTime popUpTime = LocalDateTime.now();
                    String answer = popUpService.showStudyPopUp();
                    if (answer.equals("STILL_STUDYING")) {
                        state.setWaitingForRealActivity(true);
                        System.out.println("WAITING = TRUE");
                    } else if (answer.equals("DISTRACTED")) {
                        System.out.println("user distracted");
                        backendClient.addDistraction(idleSeconds + Duration.between(popUpTime , LocalDateTime.now()).getSeconds());
                        popUpService.showRoastPopUp();
                        state.setLastActivityTime(LocalDateTime.now());
                    } else {
                        boolean cameBack = popUpService.showBuzzer();
                        if(cameBack) {
                            System.out.println("after giving buzzer ans");
                            backendClient.addDistraction(idleSeconds + Duration.between(popUpTime , LocalDateTime.now()).getSeconds());
                            popUpService.showRoastPopUp();
                            state.setLastActivityTime(LocalDateTime.now());
                        }else {
                            System.out.println("no responce to the buzzer");
                            backendClient.addDistraction(idleSeconds);
                            backendClient.pauseSession();
                            state.setActivityAfterPause(false);
                        }
                    }
                    state.setPopUpShown(false);
                }

                Thread.sleep(CompanionConfig.CHECK_INTERVAL_SECONDS * 1000L);

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

}


