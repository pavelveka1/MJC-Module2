package com.epam.esm.dao;

import com.epam.esm.entity.Role;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RoleDAO extends PagingAndSortingRepository<Role, Long> {

    Role findRoleByName(String roleName);
}
