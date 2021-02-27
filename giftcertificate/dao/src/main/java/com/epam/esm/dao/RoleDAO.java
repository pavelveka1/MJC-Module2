package com.epam.esm.dao;

import com.epam.esm.entity.Role;

public interface RoleDAO {

    Role getRoleByName(String roleName);
}
