package io.github.ntduycs.jhcm.account.config;

import io.github.ntduycs.jhcm.base.http.HttpExceptionHandler;
import io.github.ntduycs.jhcm.base.http.HttpLoggingConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({HttpLoggingConfig.class, HttpExceptionHandler.class})
public class HttpConfig {}
