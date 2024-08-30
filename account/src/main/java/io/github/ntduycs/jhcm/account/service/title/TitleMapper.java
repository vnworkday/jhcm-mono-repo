package io.github.ntduycs.jhcm.account.service.title;

import io.github.ntduycs.jhcm.account.domain.entity.Title;
import io.github.ntduycs.jhcm.account.service.title.model.CreateTitleRequest;
import io.github.ntduycs.jhcm.account.service.title.model.CreateTitleResponse;
import io.github.ntduycs.jhcm.account.service.title.model.GetTitleResponse;
import io.github.ntduycs.jhcm.account.service.title.model.ListTitleResponse;
import io.github.ntduycs.jhcm.base.domain.enums.Status;
import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.aot.generate.Generated;

@AnnotateWith(Generated.class)
@Mapper(componentModel = "spring")
public interface TitleMapper {
  GetTitleResponse fromTitleToGetResponse(Title title);

  ListTitleResponse.Item fromTitleToListResponse(Title title);

  @Mapping(target = "version", constant = "1")
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "updatedAt", expression = "java(java.time.Instant.now())")
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
  @Mapping(target = "code", ignore = true)
  Title fromCreateRequestToTitle(CreateTitleRequest request);

  @Mapping(target = "updater", ignore = true)
  @Mapping(target = "creator", ignore = true)
  CreateTitleResponse fromTitleToCreateResponse(Title title);

  default Status toStatus(String status) {
    return Status.fromCode(status);
  }
}
