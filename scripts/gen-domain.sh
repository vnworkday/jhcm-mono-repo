#!/usr/bin/env sh

set -e

if [ -z "$2" ]; then
  echo "Usage: $0 <service-name> <domain-name>"
  exit 1
fi

# Make sure domain name is in PascalCase
if ! echo "$2" | grep -qE '^[A-Z][a-z]+([A-Z][a-z]+)*$'; then
  echo "Domain name must be in PascalCase. Example: User"
  exit 1
fi

SERVICE_NAME=$1
DOMAIN_NAME=$2

SRC_DIR=$SERVICE_NAME/src/main/java/io/github/ntduycs/jhcm/$SERVICE_NAME
RESOURCE_DIR=$SERVICE_NAME/src/main/resources
DOMAIN_TEMPLATE_DIR=scripts/template/domain

mkdir -p "$SRC_DIR"/domain/entity
mkdir -p "$SRC_DIR"/domain/repository
mkdir -p "$SRC_DIR"/domain/handler

mkdir -p "$RESOURCE_DIR"/mybatis/mapper

cp "$DOMAIN_TEMPLATE_DIR"/TemplateEntity.java "$SRC_DIR"/domain/entity/"$DOMAIN_NAME".java
cp "$DOMAIN_TEMPLATE_DIR"/TemplateRepository.java "$SRC_DIR"/domain/repository/"$DOMAIN_NAME"Repository.java
cp "$DOMAIN_TEMPLATE_DIR"/TemplateMapper.xml "$RESOURCE_DIR"/mybatis/mapper/"$DOMAIN_NAME"Mapper.xml

# Traverse all files in the domain directory and replace the domain name with the given domain name
find "$SRC_DIR" -type f -exec sed -i "s/{{service}}/$SERVICE_NAME/g" {} \;
find "$SRC_DIR" -type f -exec sed -i "s/{{Entity}}/$DOMAIN_NAME/g" {} \;
find "$SRC_DIR" -type f -exec sed -i "s/{{entity}}/$(echo "$DOMAIN_NAME" | tr '[:lower:]' '[:upper]')/g" {} \;

LOWER_DOMAIN_NAME=$(echo "$DOMAIN_NAME" | tr '[:upper:]' '[:lower:]')

find "$RESOURCE_DIR" -type f -exec sed -i "s/{{service}}/$SERVICE_NAME/g" {} \;
find "$RESOURCE_DIR" -type f -exec sed -i "s/{{Entity}}/$DOMAIN_NAME/g" {} \;
find "$RESOURCE_DIR" -type f -exec sed -i "s/{{entity}}/$LOWER_DOMAIN_NAME/g" {} \;

echo "🚀 Domain $SERVICE_NAME - $DOMAIN_NAME created!"