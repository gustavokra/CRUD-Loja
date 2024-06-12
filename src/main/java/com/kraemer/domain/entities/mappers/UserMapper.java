package com.kraemer.domain.entities.mappers;

import com.kraemer.domain.entities.UserBO;
import com.kraemer.domain.entities.dto.UserDTO;
import com.kraemer.domain.entities.vo.CreatedAtVO;

public class UserMapper {

    public static UserDTO toDTO(UserBO bo) {
        var dto = new UserDTO();
        dto.setId(bo.getId());
        dto.setDocument(bo.getDocument());
        dto.setName(bo.getName());
        dto.setUsername(bo.getUsername());
        dto.setPassword(bo.getPassword());
        dto.setCreatedAt(bo.getCreatedAt() != null ? bo.getCreatedAt().getValue() : null);
        dto.setDisabledAt(bo.getDisabledAt());
        dto.setUpdatedAt(bo.getUpdatedAt());

        return dto;
    }

    public static UserBO toBO(UserDTO dto) {
        var bo = new UserBO(dto.getId(),
                dto.getDocument(),
                dto.getName(),
                dto.getUsername(),
                dto.getPassword(),
                new CreatedAtVO(dto.getCreatedAt()),
                dto.getUpdatedAt(),
                dto.getDisabledAt());

        return bo;
    }

}