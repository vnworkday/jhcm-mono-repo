package io.github.ntduycs.jhcm.account.service.user;

import io.github.ntduycs.jhcm.account.domain.entity.User;
import io.github.ntduycs.jhcm.account.service.user.model.GetUserResponse;
import io.github.ntduycs.jhcm.account.service.user.model.ListUserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  GetUserResponse fromUserToGetResponse(User user);

  ListUserResponse.Item fromUserToListResponse(User user);
}
