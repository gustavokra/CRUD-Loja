package com.kraemer.infra.database.mysql.repositories;

import java.util.List;
import java.util.stream.Collectors;

import com.kraemer.domain.entities.AddressBO;
import com.kraemer.domain.entities.enums.EnumDataBase;
import com.kraemer.domain.entities.vo.QueryFieldVO;
import com.kraemer.domain.repositories.AddressRepository;
import com.kraemer.domain.utils.ListUtil;
import com.kraemer.domain.utils.StringUtil;
import com.kraemer.infra.database.mysql.mappers.MysqlAddressMapper;
import com.kraemer.infra.database.mysql.model.MySqlAddress;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MySqlAddressRepositoryImp implements AddressRepository {

    @Override
    public AddressBO create(AddressBO addressBO) {
        MySqlAddress mysqlAddress = MysqlAddressMapper.toEntity(addressBO);
        mysqlAddress.persist();
        return MysqlAddressMapper.toBO(mysqlAddress);
    }

    @Override
    public AddressBO merge(AddressBO addressBO) {
        MySqlAddress mySqlAddress = MysqlAddressMapper.toEntity(addressBO);
        MySqlAddress.getEntityManager().merge(mySqlAddress);
        return MysqlAddressMapper.toBO(mySqlAddress);
    }

    @Override
    public List<AddressBO> listBy(List<QueryFieldVO> queryFieldInfos) {
        var queryParameters = ListUtil.stream(queryFieldInfos)
                        .filter(queryField -> queryField.getFieldValue() != null)
                        .collect(Collectors.toMap(
                            queryField -> StringUtil.replaceDot(queryField.getFieldName()),
                            QueryFieldVO::getFieldValue));

        var query = new StringBuilder();

        ListUtil.stream(queryFieldInfos).forEach(queryField -> {
            String formatedFielValue = queryField.getFieldValue() != null 
            ? " = :".concat(StringUtil.replaceDot(queryField.getFieldName()))
            : " IS NULL";

            if(StringUtil.isNullOrEmpty(query.toString())) {
                query.append(queryField.getFieldName()).append(formatedFielValue);
            } else {
                query.append(" AND ").append(queryField.getFieldName()).append(formatedFielValue);
            }
        });

        return ListUtil.stream(MySqlAddress.list(query.toString(), queryParameters))
            .map(entityAddress -> MysqlAddressMapper.toBO((MySqlAddress) entityAddress))
            .collect(Collectors.toList());
    }

    @Override
    public List<AddressBO> returnAll() {
        return ListUtil.stream(MySqlAddress.listAll())
        .map(entityAddress -> MysqlAddressMapper.toBO((MySqlAddress) entityAddress))
        .collect(Collectors.toList());
    }

    @Override
    public AddressBO findFirstBy(List<QueryFieldVO> queryFieldInfo) {
        return ListUtil.first(listBy(queryFieldInfo));
    }

    @Override
    public EnumDataBase getDataBase() {
        return EnumDataBase.MYSQL;
    }
    
}
