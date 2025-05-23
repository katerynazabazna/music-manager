package com.kateryna_zabazna.musicmanager.domain.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAllUsers();

    Optional<User> findUserByUsername(String username);

    void save(User user);
}