package com.lockedIn.companion.util;

import com.sun.jna.platform.win32.*;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Psapi;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.IntByReference;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class NativeUtils {

    public static String getActiveProcess() {

        try {
            HWND hwnd = User32.INSTANCE.GetForegroundWindow();

            IntByReference pid = new IntByReference();
            User32.INSTANCE.GetWindowThreadProcessId(hwnd, pid);

            System.out.println("PID = " + pid.getValue());

            Process process = Runtime.getRuntime().exec("tasklist /FI \"PID eq " + pid.getValue() + "\" /FO CSV /NH");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();

            if (line == null || line.isBlank()) {
                return null;
            }

            String processName = line.split(",")[0].replace("\"", "");

            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void killProcess(String processName){
        try{
            Runtime.getRuntime().exec("taskkill /F /IM " + processName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
