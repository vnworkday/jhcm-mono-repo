package io.github.ntduycs.jhcm.account.service.user;

import io.github.ntduycs.jhcm.account.domain.entity.User;
import io.github.ntduycs.jhcm.account.service.user.model.GetUserResponse;
import io.github.ntduycs.jhcm.account.service.user.model.ListUserResponse;
import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.springframework.aot.generate.Generated;

@AnnotateWith(Generated.class)
@Mapper(componentModel = "spring")
public interface UserMapper {
  GetUserResponse fromUserToGetResponse(User user);

  ListUserResponse.Item fromUserToListResponse(User user);
}
