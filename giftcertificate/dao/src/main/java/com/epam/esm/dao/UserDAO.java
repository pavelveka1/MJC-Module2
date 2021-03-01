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


    Page<User> findAll(Pageable pageable);

    User findByUsername(String username);

    User save(User user);

}
