package io.github.ntduycs.jhcm.account.domain.repository;

import io.github.ntduycs.jhcm.account.domain.entity.User;
import io.github.ntduycs.jhcm.account.service.user.model.ListUserRequest;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRepository {
  Optional<User> findById(@Param("id") Integer id);

  Optional<User> findByCode(@Param("code") String code);

  List<User> search(@Param("request") ListUserRequest request);

  List<User> findAll();

  Integer countAll();

  Integer count(@Param("request") ListUserRequest request);
}
