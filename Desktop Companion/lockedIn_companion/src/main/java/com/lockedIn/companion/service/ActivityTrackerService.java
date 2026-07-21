package com.lockedIn.companion.service;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseMotionListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelListener;
import com.lockedIn.companion.model.CompanionState;

import java.time.LocalDateTime;

public class ActivityTrackerService implements NativeKeyListener,
        NativeMouseListener,
        NativeMouseMotionListener,
        NativeMouseWheelListener {

    private final CompanionState state;
    private final BackendClient backendClient;

    public ActivityTrackerService(CompanionState state , BackendClient backendClient) {
        this.backendClient = backendClient;
        this.state = state;
    }

    private void updateActivity() {
//        System.out.println("ACTIVITY DETECTED");
        state.setLastActivityTime(LocalDateTime.now());
//        if(backendClient.getSessionStatus().equals("PAUSED")) {
//            state.setActivityAfterPause(true);
//        }
        state.setWaitingForRealActivity(false);
    }

    public void startTracking() {

        try {

            GlobalScreen.registerNativeHook();

            GlobalScreen.addNativeKeyListener(this);

            GlobalScreen.addNativeMouseListener(this);

            GlobalScreen.addNativeMouseMotionListener(this);

            GlobalScreen.addNativeMouseWheelListener(this);

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
        updateActivity();
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
        updateActivity();
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent nativeEvent) {
        updateActivity();
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nativeEvent) {
        updateActivity();
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent nativeEvent) {
        updateActivity();
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent nativeEvent) {
        updateActivity();
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent nativeEvent) {
        updateActivity();
    }

    @Override
    public void nativeMouseWheelMoved(NativeMouseWheelEvent nativeEvent) {
        updateActivity();
    }
}