package com.kraemer.domain.usecases.user;

import java.util.List;
import java.util.stream.Collectors;

import com.kraemer.domain.entities.UserBO;
import com.kraemer.domain.entities.dto.UserDTO;
import com.kraemer.domain.entities.enums.EnumErrorCode;
import com.kraemer.domain.entities.mappers.UserMapper;
import com.kraemer.domain.entities.vo.QueryFieldInfoVO;
import com.kraemer.domain.repositories.ICrudRepository;
import com.kraemer.domain.utils.ListUtil;
import com.kraemer.domain.utils.exception.CrudException;

public class FindUsersBy {

    private ICrudRepository<UserBO> crudRepository;

    public FindUsersBy(ICrudRepository<UserBO> crudRepository) {
        this.crudRepository = crudRepository;
    }

    public List<UserDTO> execute(List<QueryFieldInfoVO> queryFields, boolean throwsException) {
        var usersBO = crudRepository.findAllBy(queryFields);

        if (ListUtil.isNullOrEmpty(usersBO) && throwsException) {
            var fields = ListUtil.stream(queryFields)
                    .map(QueryFieldInfoVO::getFieldName)
                    .collect(Collectors.joining(", "));

            throw new CrudException(EnumErrorCode.ITEM_NAO_ENCONTRADO_FILTROS, fields);
        }

        return ListUtil.stream(usersBO)
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

}
