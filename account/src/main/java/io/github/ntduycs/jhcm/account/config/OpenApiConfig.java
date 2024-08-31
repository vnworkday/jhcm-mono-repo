package io.github.ntduycs.jhcm.account.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info =
        @Info(
            summary = "Account Service REST API",
            title = "Account Service REST API",
            description = "Account Service REST API",
            version = "1.0.0",
            contact = @Contact(name = "Duy Nguyen", email = "ntduy.cs@gmail.com")),
    servers = {
      @Server(url = "/account/", description = "Access from Api Docs GW"),
      @Server(url = "/", description = "Access from application")
    })
public class OpenApiConfig {}
