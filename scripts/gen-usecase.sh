#!/usr/bin/env sh

set -e

# Make sure module and use-case names are provided
if [ -z "$3" ]; then
  echo "Usage: $0 <service-name> <module-name> <use-case-name>"
  exit 1
fi

# Make sure module name is in PascalCase
if ! echo "$2" | grep -qE '^[A-Z][a-z]+([A-Z][a-z]+)*$'; then
  echo "Module name must be in PascalCase. Example: User"
  exit 1
fi

# Make sure use-case name is in PascalCase
if ! echo "$3" | grep -qE '^[A-Z][a-z]+([A-Z][a-z]+)*$'; then
  echo "Use-case name must be in PascalCase. Example: GetUser"
  exit 1
fi

SERVICE_NAME=$1
MODULE_NAME=$2
USE_CASE_NAME=$3

SRC_DIR=$SERVICE_NAME/src/main/java/io/github/ntduycs/jhcm/$SERVICE_NAME
TEST_DIR=$SERVICE_NAME/src/test/java/io/github/ntduycs/jhcm/$SERVICE_NAME

MODULE_DIR_NAME=$(echo "$MODULE_NAME" | tr '[:upper:]' '[:lower:]')
USE_CASE_DIR="$SRC_DIR"/service/"$MODULE_DIR_NAME"/usecase
USE_CASE_TEST_DIR="$TEST_DIR"/service/"$MODULE_DIR_NAME"/usecase
MODEL_DIR="$SRC_DIR"/service/"$MODULE_DIR_NAME"/model
CONTROLLER_TEST_DIR="$TEST_DIR"/controller/"$MODULE_DIR_NAME"

cp scripts/template/usecase/TemplateUseCase.java "$USE_CASE_DIR"/"$USE_CASE_NAME"UseCase.java
cp scripts/template/usecase/TemplateRequest.java "$MODEL_DIR"/"$USE_CASE_NAME"Request.java
cp scripts/template/usecase/TemplateResponse.java "$MODEL_DIR"/"$USE_CASE_NAME"Response.java
cp scripts/template/usecase/TemplateUseCaseTest.java "$USE_CASE_TEST_DIR"/"$USE_CASE_NAME"UseCaseTest.java
cp scripts/template/usecase/TemplateControllerTest.java "$CONTROLLER_TEST_DIR"/"$USE_CASE_NAME"ControllerTest.java

# Replace the use-case name with the given use-case name
# TemplateMapper -> <use-case-name>Mapper
# templateMapper -> <use-case-name>Mapper (camelCase)
find "$SRC_DIR" -type f -exec sed -i "s/{{service}}/$SERVICE_NAME/g" {} \;
find "$SRC_DIR" -type f -exec sed -i "s/{{Module}}/$MODULE_NAME/g" {} \;
find "$SRC_DIR" -type f -exec sed -i "s/{{module}}/$(echo "$MODULE_NAME" | tr '[:upper:]' '[:lower:]')/g" {} \;
find "$SRC_DIR" -type f -exec sed -i "s/{{Usecase}}/$USE_CASE_NAME/g" {} \;
find "$SRC_DIR" -type f -exec sed -i "s/{{usecase}}/$(echo "$USE_CASE_NAME" | tr '[:upper:]' '[:lower:]')/g" {} \;

find "$TEST_DIR" -type f -exec sed -i "s/{{service}}/$SERVICE_NAME/g" {} \;
find "$TEST_DIR" -type f -exec sed -i "s/{{Module}}/$MODULE_NAME/g" {} \;
find "$TEST_DIR" -type f -exec sed -i "s/{{module}}/$(echo "$MODULE_NAME" | tr '[:upper:]' '[:lower:]')/g" {} \;
find "$TEST_DIR" -type f -exec sed -i "s/{{Usecase}}/$USE_CASE_NAME/g" {} \;
find "$TEST_DIR" -type f -exec sed -i "s/{{usecase}}/$(echo "$USE_CASE_NAME" | tr '[:upper:]' '[:lower:]')/g" {} \;


echo "🚀 Use-case $USE_CASE_NAME created in module $SERVICE_NAME - $MODULE_NAME!"