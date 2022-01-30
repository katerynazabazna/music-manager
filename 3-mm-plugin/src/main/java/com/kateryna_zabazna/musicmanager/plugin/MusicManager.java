package com.kateryna_zabazna.musicmanager.plugin;

import com.kateryna_zabazna.musicmanager.application.observer.LoginObserver;
import com.kateryna_zabazna.musicmanager.domain.user.User;
import com.kateryna_zabazna.musicmanager.plugin.ui.LoginUI;
import com.kateryna_zabazna.musicmanager.plugin.ui.MusicManagerUI;

import javax.swing.*;

public class MusicManager implements LoginObserver {

    MusicManager() {
        showLoginFrame();
    }

    @Override
    public void onLogin(User user) {
        showMainFrame(user);
    }

    @Override
    public void onLogout() {
        showLoginFrame();
    }

    private void showLoginFrame() {
        try {
            JFrame loginFrame = new LoginUI(this);
            loginFrame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("MusicManager: Could not open login window");
        }
    }

    private void showMainFrame(User user) {
        try {
            JFrame mainFrame = new MusicManagerUI(this, user);
            mainFrame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("MusicManager: Could not open main window");
        }
    }
}
