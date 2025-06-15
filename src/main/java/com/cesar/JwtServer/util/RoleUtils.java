package com.cesar.JwtServer.util;

import com.cesar.JwtServer.persistence.entity.RoleEntity;
import com.cesar.JwtServer.persistence.entity.RoleEnum;
import com.cesar.JwtServer.persistence.repository.RoleRepository;
import org.springframework.stereotype.Component;

@Component
public class RoleUtils {

    public RoleEntity getUserRole(){
        if(userRole == null){
            userRole = roleRepo.findByName(RoleEnum.USER)
                    .orElseThrow(() -> new RuntimeException("Default USER role not found"));
        }
        return userRole;
    }


    public RoleUtils(RoleRepository roleRepo){
        this.roleRepo = roleRepo;
    }

    private RoleEntity userRole;
    private final RoleRepository roleRepo;
}