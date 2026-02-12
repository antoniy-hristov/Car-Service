package com.carservice.services;

import com.carservice.data.entities.Role;
import com.carservice.data.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getRoleByAuthority(String authority) {
        return roleRepository.findRoleByAuthority(authority);
    }
}
