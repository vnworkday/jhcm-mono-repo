package io.github.ntduycs.jhcm.account.service.title;

import io.github.ntduycs.jhcm.account.domain.entity.Title;
import io.github.ntduycs.jhcm.account.service.title.model.GetTitleResponse;
import io.github.ntduycs.jhcm.account.service.title.model.ListTitleResponse;
import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.springframework.aot.generate.Generated;

@AnnotateWith(Generated.class)
@Mapper(componentModel = "spring")
public interface TitleMapper {
  GetTitleResponse fromTitleToGetResponse(Title title);

  ListTitleResponse.Item fromTitleToListResponse(Title title);
}
