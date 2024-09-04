#!/usr/bin/env sh

set -e

kubectl apply -f devops/platform/ingress-nginx
kubectl apply -f devops/platform/argocd
docker build -t jhcm-keycloak:1.0.0 devops/platform/keycloak
kubectl apply -f devops/platform/keycloak

kubectl create namespace jhcm