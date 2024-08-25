#!/usr/bin/env sh

set -e

if [ -z "$2" ]; then
  echo "Usage: $0 <service-name> <module-name>"
  exit 1
fi

# Make sure module name is in PascalCase
if ! echo "$2" | grep -qE '^[A-Z][a-z]+([A-Z][a-z]+)*$'; then
  echo "Module name must be in PascalCase. Example: User"
  exit 1
fi

SERVICE_NAME=$1
MODULE_NAME=$2

SRC_DIR=$SERVICE_NAME/src/main/java/io/github/ntduycs/jhcm/$SERVICE_NAME
TEST_DIR=$SERVICE_NAME/src/test/java/io/github/ntduycs/jhcm/$SERVICE_NAME

PACKAGE_NAME=$(echo "$MODULE_NAME" | tr '[:upper:]' '[:lower:]')
MODULE_TEMPLATE_DIR=scripts/template/module

# mkdir -p "$MODULE_DIR"
mkdir -p "$SRC_DIR"/service/"$PACKAGE_NAME"/model
mkdir -p "$SRC_DIR"/service/"$PACKAGE_NAME"/usecase
mkdir -p "$TEST_DIR"/controller/"$PACKAGE_NAME"
mkdir -p "$TEST_DIR"/service/"$PACKAGE_NAME"/usecase

cp "$MODULE_TEMPLATE_DIR"/TemplateController.java "$SRC_DIR"/controller/"$MODULE_NAME"Controller.java
cp "$MODULE_TEMPLATE_DIR"/TemplateService.java "$SRC_DIR"/service/"$PACKAGE_NAME"/"$MODULE_NAME"Service.java
cp "$MODULE_TEMPLATE_DIR"/TemplateMapper.java "$SRC_DIR"/service/"$PACKAGE_NAME"/"$MODULE_NAME"Mapper.java
cp "$MODULE_TEMPLATE_DIR"/TemplateServiceTest.java "$TEST_DIR"/service/"$PACKAGE_NAME"/"$MODULE_NAME"ServiceTest.java

# Traverse all files in the module directory and replace the module name with the given module name
find "$SRC_DIR" -type f -exec sed -i "s/{{service}}/$SERVICE_NAME/g" {} \;
find "$SRC_DIR" -type f -exec sed -i "s/{{Module}}/$MODULE_NAME/g" {} \;
find "$SRC_DIR" -type f -exec sed -i "s/{{module}}/$(echo "$MODULE_NAME" | tr '[:lower:]' '[:upper:]')/g" {} \;

find "$TEST_DIR" -type f -exec sed -i "s/{{service}}/$SERVICE_NAME/g" {} \;
find "$TEST_DIR" -type f -exec sed -i "s/{{Module}}/$MODULE_NAME/g" {} \;
find "$TEST_DIR" -type f -exec sed -i "s/{{module}}/$(echo "$MODULE_NAME" | tr '[:lower:]' '[:upper:]')/g" {} \;

echo "🚀 Module $SERVICE_NAME - $MODULE_NAME created!"