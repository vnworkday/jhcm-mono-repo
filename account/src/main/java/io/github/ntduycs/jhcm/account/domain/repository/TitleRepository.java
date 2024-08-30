package io.github.ntduycs.jhcm.account.domain.repository;

import io.github.ntduycs.jhcm.account.domain.entity.Title;
import io.github.ntduycs.jhcm.account.service.title.model.ListTitleRequest;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TitleRepository {
  Optional<Title> findById(@Param("id") Integer id);

  Optional<Title> findByCode(@Param("code") String code);

  Optional<Title> findByName(@Param("name") String name);

  List<Title> findAll();

  List<Title> search(@Param("request") ListTitleRequest request);

  Integer countAll();

  Integer count(@Param("request") ListTitleRequest request);

  Integer insert(Title title);
}
