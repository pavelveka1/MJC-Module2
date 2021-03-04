package com.epam.esm.dao;

import com.epam.esm.entity.Role;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface TagDAO.
 * Contains methods for work with Tag class
 */
@Repository
public interface RoleDAO extends PagingAndSortingRepository<Role, Long> {

    /**
     * Find Role by name
     *
     * @param roleName name of role
     * @return Role
     */
    Role findRoleByName(String roleName);
}
