package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.UserDAO;
import com.epam.esm.entity.User;
import com.epam.esm.service.exception.IdNotExistServiceException;
import com.epam.esm.service.exception.PaginationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class UserServiceImplTest {

    private static User user1 = new User(1, "login1", "password", "firstName1", "lastName1");
    private static User user2 = new User(2, "login2", "password", "firstName2", "lastName2");
    private static List<User> users = new ArrayList<>();

    @BeforeAll
    public static void init() {
        users.add(user1);
        users.add(user2);
    }


    @Mock
    private UserDAO userDAOImpl;

    @Mock
    private GiftCertificateDAO giftCertificateDAO;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService = new UserServiceImpl();

    @DisplayName("should be returned User")
    @Test
    public void testGetUser() throws IdNotExistServiceException {
        when(userDAOImpl.findById(new Long(1))).thenReturn(Optional.ofNullable(user1));
        assertEquals(user1, userService.getUser(1));
    }

    @DisplayName("should be thrown IdNotExistServiceException")
    @Test
    public void testGetUserIdNotExist() throws IdNotExistServiceException {
        when(userDAOImpl.findById(new Long(1))).thenReturn(Optional.ofNullable(null));
        assertThrows(IdNotExistServiceException.class, () -> {
            userService.getUser(1);
        });
    }

    @DisplayName("should be returned list of User")
    @Test
    public void testGetUsers() throws PaginationException {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(users, pageable, users.size());
        when(userDAOImpl.findAll(pageable)).thenReturn(page);
        assertEquals(users, userService.getUsers(1, 10));
    }

    @DisplayName("should be thrown PaginationException")
    @Test
    public void testGetUsersPaginationException() throws PaginationException {
        assertThrows(PaginationException.class, () -> {
            userService.getUsers(0, 10);
        });
    }

}
