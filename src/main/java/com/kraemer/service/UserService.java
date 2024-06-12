package com.kraemer.service;

import java.util.List;

import com.kraemer.domain.entities.dto.UserDTO;
import com.kraemer.domain.entities.enums.EnumDBImpl;
import com.kraemer.domain.entities.enums.EnumErrorCode;
import com.kraemer.domain.entities.vo.QueryFieldInfoVO;
import com.kraemer.domain.usecases.CreateUser;
import com.kraemer.domain.usecases.DisableUser;
import com.kraemer.domain.usecases.FindUserBy;
import com.kraemer.domain.usecases.ListAllUsers;
import com.kraemer.domain.usecases.ListUsersBy;
import com.kraemer.domain.usecases.UpdateUserInfo;
import com.kraemer.domain.utils.StringUtil;
import com.kraemer.domain.utils.exception.CrudException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserService extends AbstractService {

    @Transactional
    public UserDTO create(UserDTO dto, EnumDBImpl dbImpl) {
        var repository = dbFactory.getImpl(dbImpl);
        var createUser = new CreateUser(repository);

        return createUser.execute(dto);
    }

    public List<UserDTO> listAll(EnumDBImpl dbImpl) {
        var repository = dbFactory.getImpl(dbImpl);
        var listAll = new ListAllUsers(repository);

        return listAll.execute(true);
    }

    public List<UserDTO> listBy(String document, EnumDBImpl dbImpl) {

        if (StringUtil.isNullOrEmpty(document)) {
            throw new CrudException(EnumErrorCode.CAMPO_OBRIGATORIO, "document");
        }

        var repository = dbFactory.getImpl(dbImpl);
        var findAllBy = new ListUsersBy(repository);
        var queryFieldUserId = new QueryFieldInfoVO("document", document);

        return findAllBy.execute(List.of(queryFieldUserId), true);
    }

    public UserDTO findBy(Long userId, EnumDBImpl dbImpl) {

        if (userId == null) {
            throw new CrudException(EnumErrorCode.CAMPO_OBRIGATORIO, "userId");
        }

        var repository = dbFactory.getImpl(dbImpl);
        var findUserBy = new FindUserBy(repository);
        var queryFieldUserId = new QueryFieldInfoVO("id", userId);

        return findUserBy.execute(List.of(queryFieldUserId), true);
    }

    @Transactional
    public UserDTO updateInfo(UserDTO dto, Long userId, EnumDBImpl dbImpl) {
        var repository = dbFactory.getImpl(dbImpl);
        var updateUserInfo = new UpdateUserInfo(repository);

        return updateUserInfo.execute(dto, userId);
    }

    @Transactional
    public UserDTO disable(Long userId, EnumDBImpl dbImpl) {
        var repository = dbFactory.getImpl(dbImpl);
        var disableUser = new DisableUser(repository);

        return disableUser.execute(userId);
    }

    public UserDTO findByLogin(String username, String password, EnumDBImpl dbImpl) {

        if (StringUtil.isNullOrEmpty(username)) {
            throw new CrudException(EnumErrorCode.CAMPO_OBRIGATORIO, username);
        }

        if (StringUtil.isNullOrEmpty(password)) {
            throw new CrudException(EnumErrorCode.CAMPO_OBRIGATORIO, password);
        }

        var repository = dbFactory.getImpl(dbImpl);
        var findUserBy = new FindUserBy(repository);
        var queryFieldUsername = new QueryFieldInfoVO("username", username);
        var queryFieldPassword = new QueryFieldInfoVO("password", password);

        return findUserBy.execute(List.of(queryFieldUsername, queryFieldPassword), true);
    }

}