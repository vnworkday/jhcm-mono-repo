package io.github.ntduycs.jhcm.account.service.title.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import io.github.ntduycs.jhcm.account.domain.entity.Title;
import io.github.ntduycs.jhcm.account.domain.repository.TitleRepository;
import io.github.ntduycs.jhcm.account.helper.ResourceCodeHelper;
import io.github.ntduycs.jhcm.account.service.title.TitleMapper;
import io.github.ntduycs.jhcm.account.service.title.model.CreateTitleRequest;
import io.github.ntduycs.jhcm.account.service.title.model.CreateTitleResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateTitleUseCaseTest {
  @Mock private TitleRepository titleRepository;
  @Mock private TitleMapper titleMapper;
  @Mock private ResourceCodeHelper resourceCodeHelper;

  @InjectMocks private CreateTitleUseCase useCase;

  @Test
  void shouldSuccess_givenValidRequest_whenTitlesFound_thenReturnsTitle() {
    when(titleMapper.fromCreateRequestToTitle(any(CreateTitleRequest.class)))
        .thenReturn(new Title());
    when(resourceCodeHelper.generate(any())).thenReturn("1");
    when(titleMapper.fromTitleToCreateResponse(any(Title.class)))
        .thenReturn(new CreateTitleResponse().setCode("1"));

    assertDoesNotThrow(() -> useCase.handle(new CreateTitleRequest()));

    verify(titleRepository, times(1)).insert(any(Title.class));
    verify(resourceCodeHelper, times(1)).generate(any());
  }
}
