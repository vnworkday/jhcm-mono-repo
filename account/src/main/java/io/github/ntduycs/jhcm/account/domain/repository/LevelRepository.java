package io.github.ntduycs.jhcm.account.domain.repository;

import io.github.ntduycs.jhcm.account.domain.entity.Level;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LevelRepository {
  Optional<Level> findById(@Param("id") Integer id);

  Optional<Level> findByCode(@Param("code") String code);

  List<Level> findAll();

  Integer countAll();
}
