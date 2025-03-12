package com.internship.persistence.repo;

import com.internship.config.JpaTestConfig;
import com.internship.persistence.entity.User;
import com.internship.service.exceptoin.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JpaTestConfig.class})
class UserRepositoryTest {
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void tearDown() {
        repository.deleteAll();
        repository.flush();
    }

    @Test
    void findByUsername_shouldFindUser_whenExists() throws NotFoundException {
        final User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);

        repository.save(user);
        final User actual = repository.findByUsername(USERNAME)
                .orElseThrow(NotFoundException::new);

        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void findByUsername_shouldFindNothing_whenDoesNotExist() {
        Optional<User> actual = repository.findByUsername(USERNAME);

        assertNotNull(actual);
        assertEquals(Optional.empty(), actual);
    }
}