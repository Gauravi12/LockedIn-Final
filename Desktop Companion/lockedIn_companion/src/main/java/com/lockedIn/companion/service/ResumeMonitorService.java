//package com.lockedIn.companion.service;
//
//import com.lockedIn.companion.model.CompanionState;
//import com.lockedIn.companion.ui.PopUpService;
//
//public class ResumeMonitorService {
//
//    private final BackendClient backendClient;
//    private final PopUpService popUpService;
//    private final CompanionState state;
//
//    public ResumeMonitorService(BackendClient backendClient , PopUpService popUpService , CompanionState state){
//        this.backendClient = backendClient;
//        this.popUpService = popUpService;
//        this.state = state;
//    }
//
//    public void startMonitoring(){
//        System.out.println("Resume Monitor Started");
//        while(true){
//            try {
//                String status = backendClient.getSessionStatus();
//                if("PAUSED".equals(status) && state.isActivityAfterPause()){
//                    String ans = popUpService.showResumeEndPopUp();
//                    if(ans.equals("RESUME")){
//                        backendClient.resumeSession();
//                    }else{
//                        backendClient.endSession();
//                        state.setPopUpShown(false);
//                        state.setWaitingForRealActivity(false);
//                    }
//                }
//
//                Thread.sleep(5000);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    }
//}
