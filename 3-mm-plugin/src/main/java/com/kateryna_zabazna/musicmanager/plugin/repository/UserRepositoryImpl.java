package com.kateryna_zabazna.musicmanager.plugin.repository;

import com.kateryna_zabazna.musicmanager.domain.AbstractRepository;
import com.kateryna_zabazna.musicmanager.domain.user.User;
import com.kateryna_zabazna.musicmanager.domain.user.UserRepository;
import com.kateryna_zabazna.musicmanager.utils.CSVHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserRepositoryImpl extends AbstractRepository implements UserRepository {

    private static final String FILE_PATH = "./data/users.csv";
    private final CSVHelper csvHelper;
    private final List<User> users = new ArrayList<>();

    public UserRepositoryImpl() {
        this(new CSVHelper(FILE_PATH, ";"));
    }

    public UserRepositoryImpl(CSVHelper csvHelper) {
        this.csvHelper = csvHelper;
        // Pre-fetch all users at once
        reload();
    }

    @Override
    public List<User> findAllUsers() {
        return users;
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public void save(User user) {
        // Load users once again to reflect any potential changes
        reload();

        // Consider auto-increment ids
        if (user.getId() == AUTO_INC) {
            user.setId(users.size());
        }

        // Save
        users.add(user);

        // Save users list
        writeOut();
    }

    @Override
    protected void writeOut() {
        List<String[]> serializedUsers = users.stream()
                .map(User::getFieldContents)
                .collect(Collectors.toList());
        csvHelper.write(User.getCSVHeader(), serializedUsers);
    }

    @Override
    protected void reload() {
        users.clear();

        // Load all users
        Optional<List<String[]>> serializedUsers = csvHelper.read();
        serializedUsers.ifPresent(strings -> strings.forEach(serializedUser -> {
            // Get basic fields
            long userId = Long.parseLong(serializedUser[0]);
            String username = serializedUser[1];
            String password = serializedUser[2];

            // Create user object
            users.add(new User(userId, username, password));
        }));
    }
}
