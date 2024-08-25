package io.github.ntduycs.jhcm.account.config;

import io.github.ntduycs.jhcm.base.domain.DefaultDatasourceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({DefaultDatasourceConfig.class})
public class DataSourceConfig {}
