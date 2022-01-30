package com.kateryna_zabazna.musicmanager.application.service;

import com.kateryna_zabazna.musicmanager.application.exception.RegistrationFailedException;
import com.kateryna_zabazna.musicmanager.application.exception.UserAlreadyExistsException;
import com.kateryna_zabazna.musicmanager.application.exception.UserNotFoundException;
import com.kateryna_zabazna.musicmanager.domain.user.User;

public interface UserService {

    User login(String username, String password) throws UserNotFoundException;

    User register(String username, String password) throws UserAlreadyExistsException, RegistrationFailedException;

}
