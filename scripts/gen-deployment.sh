#!/usr/bin/env sh

set -e

ENVIRONMENT=$1
SERVICE=$2

if [ -z "$ENVIRONMENT" ]; then
  echo "Please provide an environment. E.g. dev, staging, prod"
  exit 1
fi

if [ -z "$SERVICE" ]; then
  echo "Please provide a service. E.g. account, notification"
  exit 1
fi

# Create directory if it doesn't exist
mkdir -p devops/deployment/"$ENVIRONMENT"/"$SERVICE"

# Copy manifest files from templates to deployment directory
cp scripts/template/deployment/* devops/deployment/"$ENVIRONMENT"/"$SERVICE"

# Replace placeholders in manifest files
find devops/deployment/"$ENVIRONMENT"/"$SERVICE" -type f -exec sed -i "s/{{service}}/$SERVICE/g" {} \;

# Add new path to the ingress file
INGRESS_FILE="devops/deployment/$ENVIRONMENT/ingress.yaml"
if ! grep -q "path: /$SERVICE" "$INGRESS_FILE"; then
  sed -i "/paths:/a\\
        - path: /$SERVICE\\
          pathType: Prefix\\
          backend:\\
            service:\\
              name: $SERVICE\\
              port:\\
                name: http" "$INGRESS_FILE"
fi

echo "🚀 Deployment generated successfully"