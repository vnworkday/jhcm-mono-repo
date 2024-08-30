package io.github.ntduycs.jhcm.account.domain.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SequenceRepository {
  int getNextSequenceValue(@Param("name") String name);
}
