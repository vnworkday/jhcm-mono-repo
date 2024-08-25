package io.github.ntduycs.jhcm.{{service}}.domain.repository;

import io.github.ntduycs.jhcm.{{service}}.domain.entity.{{Entity}};
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface {{Entity}}Repository {
  Optional<{{Entity}}> findById(@Param("id") Integer id);

  Optional<{{Entity}}> findByCode(@Param("code") String code);

  List<{{Entity}}> findAll();

  Integer countAll();
}
