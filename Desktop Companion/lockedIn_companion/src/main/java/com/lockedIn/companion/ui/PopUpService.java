package com.lockedIn.companion.ui;

import com.lockedIn.companion.config.CompanionConfig;
import com.lockedIn.companion.service.BackendClient;
import com.lockedIn.companion.util.NativeUtils;

import javax.swing.*;
import java.awt.*;

public class PopUpService {
    public String showStudyPopUp() {
        System.out.println("STUDY POPUP OPENING");

        final String[] answer = {"NO_RESPONSE"};

        JDialog dialog = new JDialog();
        dialog.setTitle("LockedIn");
        dialog.setModal(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Are you still studying?");
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JLabel timerLabel = new JLabel("30");
        timerLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JButton stillStudying = new JButton("Still Studying");
        JButton distracted = new JButton("Distracted");

        stillStudying.setAlignmentX(JButton.CENTER_ALIGNMENT);
        distracted.setAlignmentX(JButton.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(20));
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(timerLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(stillStudying);
        panel.add(Box.createVerticalStrut(10));
        panel.add(distracted);

        dialog.add(panel);

        final int[] seconds = {CompanionConfig.POPUP_TIMEOUT_SECONDS};

        Timer timer = new Timer(1000, e -> {

            seconds[0]--;

            timerLabel.setText(String.valueOf(seconds[0]));

            if (seconds[0] <= 0) {
                ((Timer)e.getSource()).stop();
                answer[0] = "NO_RESPONSE";
                dialog.dispose();
            }
        });

        stillStudying.addActionListener(e -> {
            timer.stop();
            answer[0] = "STILL_STUDYING";
            dialog.dispose();
        });

        distracted.addActionListener(e -> {
            timer.stop();
            answer[0] = "DISTRACTED";
            dialog.dispose();
        });

        timer.start();

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setAlwaysOnTop(true);
        dialog.toFront();
        dialog.requestFocus();
        dialog.setVisible(true);

        System.out.println("STUDY POPUP CLOSED");
        System.out.println("ANSWER = " + answer[0]);
        return answer[0];
    }

    public void showRoastPopUp(){
        String[] roasts = {
                "2 minutes gone. Your competitors thank you.",
                "Focus. Nobody is coming to save your career.",
                "Future you is watching this disaster.",
                "If procrastination were an Olympic sport, you’d have a gold medal but still not a career."
        };

        JDialog dialog = new JDialog();
        dialog.setTitle("LockedIn");
        dialog.setModal(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Roast");
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JLabel roast = new JLabel("<html><center>" + roasts[(int) (Math.random() * roasts.length)] + "</center></html>");
        roast.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        JButton backToWork = new JButton("Back To Work");
        backToWork.setAlignmentX(JButton.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(20));
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(roast);
        panel.add(Box.createVerticalStrut(20));
        panel.add(backToWork);

        backToWork.addActionListener(e -> {
            dialog.dispose();
        });

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    public boolean showBuzzer(){
        JDialog dialog = new JDialog();
        dialog.setTitle("LockedIn");
        dialog.setModal(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Buzzer");
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JLabel timerLabel = new JLabel("20");
        timerLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JButton here = new JButton("HERE");
        here.setAlignmentX(JButton.CENTER_ALIGNMENT);


        panel.add(Box.createVerticalStrut(20));
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(timerLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(here);


        dialog.add(panel);

        final int[] seconds = {CompanionConfig.BUZZER_DURATION_SECONDS};
        final boolean[] answered = {false};

        Timer timer = new Timer(1000, e -> {

            seconds[0]--;
            Toolkit.getDefaultToolkit().beep();
            timerLabel.setText(String.valueOf(seconds[0]));

            if (seconds[0] <= 0) {
                ((Timer)e.getSource()).stop();
                answered[0] = false;
                dialog.dispose();
            }
        });

        here.addActionListener(e -> {timer.stop();answered[0] = true;dialog.dispose();});

        timer.start();

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setAlwaysOnTop(true);
        dialog.toFront();
        dialog.requestFocus();
        dialog.setVisible(true);

        return answered[0];
    }

//    public String showResumeEndPopUp() {
//
//        final String[] answer = {"END"};
//
//        JDialog dialog = new JDialog();
//        dialog.setTitle("LockedIn");
//        dialog.setModal(true);
//
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//
//        JLabel title = new JLabel("Welcome back. Resume session or end it?");
//        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
//
//        JButton resume = new JButton("RESUME");
//        JButton end = new JButton("END");
//
//        resume.setAlignmentX(JButton.CENTER_ALIGNMENT);
//        end.setAlignmentX(JButton.CENTER_ALIGNMENT);
//
//        panel.add(Box.createVerticalStrut(20));
//        panel.add(title);
//        panel.add(Box.createVerticalStrut(20));
//        panel.add(resume);
//        panel.add(Box.createVerticalStrut(20));
//        panel.add(end);
//
//        dialog.add(panel);
//
//        resume.addActionListener(e -> {
//            answer[0] = "RESUME";
//            dialog.dispose();
//        });
//
//        end.addActionListener(e -> {
//            answer[0] = "END";
//            dialog.dispose();
//        });
//
//        dialog.pack();
//        dialog.setLocationRelativeTo(null);
//        dialog.setVisible(true);
//
//        return answer[0];
//    }

    public void showRoastPopUp(String roastText){

        JDialog dialog = new JDialog();
        dialog.setTitle("LockedIn");
        dialog.setModal(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Roast");
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JLabel roast = new JLabel("<html><center>" + roastText + "</center></html>");
        roast.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        JButton backToWork = new JButton("Back To Work");
        backToWork.setAlignmentX(JButton.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(20));
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(roast);
        panel.add(Box.createVerticalStrut(20));
        panel.add(backToWork);

        backToWork.addActionListener(e -> {
            dialog.dispose();
        });

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
