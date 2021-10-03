package com.bloggingapp.servicesImpl;

import com.bloggingapp.entities.RoleEntity;
import com.bloggingapp.exceptions.ApplicationException;
import com.bloggingapp.repositories.RoleRepository;
import com.bloggingapp.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository repository){
        this.roleRepository = repository;
    }

    @Override
    public RoleEntity save(RoleEntity roleEntity) {

        Optional<RoleEntity> doesExistRole = roleRepository.findByRoleName(roleEntity.getRoleName());

        if (!doesExistRole.isEmpty()){
            throw new ApplicationException("Already Exist {"+ roleEntity.getRoleName()+"} role!");
        }

        return roleRepository.save(roleEntity);

    }
}
