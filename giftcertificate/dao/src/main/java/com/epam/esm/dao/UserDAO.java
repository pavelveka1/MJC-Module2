package com.epam.esm.dao;

import com.epam.esm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface UserDAO.
 * Contains methods for work with User class
 */
@Repository
public interface UserDAO extends PagingAndSortingRepository<User, Long> {

    /**
     * Method finds all user with pagination
     *
     * @param pageable object for pagination
     * @return Page of User
     */
    Page<User> findAll(Pageable pageable);

    /**
     * Mehtod finds User by user name
     *
     * @param username name of User
     * @return User
     */
    User findByUsername(String username);

}
