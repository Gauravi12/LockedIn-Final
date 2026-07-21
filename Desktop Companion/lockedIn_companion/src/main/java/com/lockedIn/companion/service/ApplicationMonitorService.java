package com.lockedIn.companion.service;

import com.lockedIn.companion.dto.ContentResponse;
import com.lockedIn.companion.ui.PopUpService;
import com.lockedIn.companion.util.NativeUtils;

public class ApplicationMonitorService {

    private final BackendClient backendClient;
    private final PopUpService popUpService;
    private String lastProcess;

    public ApplicationMonitorService(
            BackendClient backendClient,
            PopUpService popUpService) {

        this.backendClient = backendClient;
        this.popUpService = popUpService;
    }

    public void startMonitoring() {
        while (true) {
            try {
                String status = backendClient.getSessionStatus();
                System.out.println("Status = " + status);

                if (!"ACTIVE".equals(status)) {
                    Thread.sleep(5000);
                    continue;
                }

                String process = NativeUtils.getActiveProcess();
                System.out.println(process);

                if(process == null){
                    Thread.sleep(5000);
                    continue;
                }

                if(process.equals(lastProcess)){
                    Thread.sleep(5000);
                    continue;
                }

                lastProcess = process;

                ContentResponse response = backendClient.checkContent(process);
                if (response != null && !response.isAllowed()) {
                    System.out.println("APP BLOCK DISTRACTION");
                    backendClient.addDistraction(0);
                    NativeUtils.killProcess(process);
                    popUpService.showRoastPopUp(response.getRoast());
                }
                Thread.sleep(5000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}