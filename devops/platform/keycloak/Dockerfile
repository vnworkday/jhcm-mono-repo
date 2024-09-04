FROM quay.io/keycloak/keycloak:25.0.4 AS build

ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true
ENV KC_DB=mysql
ENV KC_FEATURES=token-exchange

# Add provider JAR files
#ADD --chown=keycloak:keycloak --chmod=644 $jar_url /opt/keycloak/providers/$jar_name.jar

WORKDIR /opt/keycloak

RUN /opt/keycloak/bin/kc.sh build

FROM quay.io/keycloak/keycloak:25.0.4 AS final

COPY --from=build /opt/keycloak /opt/keycloak

ENTRYPOINT [ "/opt/keycloak/bin/kc.sh" ]