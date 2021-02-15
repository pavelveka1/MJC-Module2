package com.epam.esm.dao.impl;

import com.epam.esm.configuration.ApplicationConfigDevProfile;
import com.epam.esm.dao.UserDAO;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = ApplicationConfigDevProfile.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = {UserDAOTest.class})
public class UserDAOTest {

    private static User user = new User(1, "Adeline", "Donal");

    @Autowired
    private UserDAO userDAO;

    @DisplayName("read user by id ")
    @Transactional
    @Test
    public void readUserById() {
        assertEquals(user, userDAO.getUser(1));
    }

    @DisplayName("result not null")
    @Transactional
    @Test
    public void readUserByIdResultNotNull() {
        assertNotNull(userDAO.getUser(1));
    }

    @DisplayName("result is null")
    @Transactional
    @Test
    public void readUserByNotExistId() {
        assertNull(userDAO.getUser(Integer.MAX_VALUE));
    }

    @DisplayName("read all users. Result: list of users with size=2 ")
    @Transactional
    @Test
    public void readUsers() {
        assertEquals(2, userDAO.getUsers(1, 100).size());
    }

    @DisplayName("should be thrown IllegalArgumentException if page = zero")
    @Transactional
    @Test
    public void readAllUsersZeroPage() {
        assertThrows(IllegalArgumentException.class, () -> {
            userDAO.getUsers(0, 100);
        });
    }

}
