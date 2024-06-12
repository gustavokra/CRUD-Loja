package com.kraemer.domain.usecases;

import java.util.List;
import java.util.stream.Collectors;

import com.kraemer.domain.entities.UserBO;
import com.kraemer.domain.entities.dto.UserDTO;
import com.kraemer.domain.entities.enums.EnumErrorCode;
import com.kraemer.domain.entities.mappers.UserMapper;
import com.kraemer.domain.repositories.IUserRepository;
import com.kraemer.domain.utils.ListUtil;
import com.kraemer.domain.utils.exception.CrudException;

public class ListAllUsers {

    private IUserRepository userRepository;

    public ListAllUsers(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> execute(boolean throwsException) {
        try {
            List<UserBO> usersBO = userRepository.findAll();
            
            return ListUtil.stream(usersBO)
                    .map(UserMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (CrudException ce) {
            throw new CrudException(EnumErrorCode.ERRO_LISTAR_USUARIOS);
        }

    }
}