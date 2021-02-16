package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.impl.UserDAOImpl;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class UserServiceImplTest {

    private static User user1 = new User(1,"firstName1","lastName1", null);
    private static User user2 = new User(2,"firstName2","lastName2", null);
    private static List<User> users=new ArrayList<>();

    @BeforeAll
    public static void init() {
       users.add(user1);
       users.add(user2);
    }


    @Mock
    private UserDAOImpl userDAOImpl;

    @Mock
    private GiftCertificateDAO giftCertificateDAO;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService = new UserServiceImpl();

    @DisplayName("should be returned User")
    @Test
    public void getUser() throws IdNotExistServiceException {
        when(userDAOImpl.getUser(1)).thenReturn(user1);
        assertEquals(user1, userService.getUser(1));
    }

    @DisplayName("should be thrown IdNotExistServiceException")
    @Test
    public void getUserIdNotExist() throws IdNotExistServiceException {
        when(userDAOImpl.getUser(1)).thenReturn(null);
        assertThrows(IdNotExistServiceException.class, () -> {
            userService.getUser(1);
        });
    }

    @DisplayName("should be returned list of User")
    @Test
    public void getUsers() throws PaginationException {
        when(userDAOImpl.getUsers(1,10)).thenReturn(users);
        assertEquals(users, userService.getUsers(1,10));
    }

    @DisplayName("should be thrown PaginationException")
    @Test
    public void getUsersPaginationException() throws PaginationException {
        assertThrows(PaginationException.class, () -> {
            userService.getUsers(0,10);
        });
    }
}
