package com.kateryna_zabazna.musicmanager.application.observer;

import com.kateryna_zabazna.musicmanager.domain.user.User;

public interface LoginObserver {
    void onLogin(User user);
    void onLogout();
}
