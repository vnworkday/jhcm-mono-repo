# DevOps module

## Configure your `/etc/hosts`

Append the following entries to your `/etc/hosts` file (with `sudo` permission):

```bash
# other entries...
127.0.0.1 argocd.jhcm.io
127.0.0.1 keycloak.jhcm.io
127.0.0.1 api.jhcm.io
```

## Ingress Nginx

Using the following command to bootstrap Ingress Nginx:

```bash
kubectl apply -n ingress-nginx -f platform/ingress-nginx
```

## ArgoCD

Using the following command to bootstrap ArgoCD:

```bash
kubectl apply -n argocd -f platform/argocd
```

This will create the ArgoCD namespace and deploy the ArgoCD components. Please note that, for simplifying the
deployment,
the TLS is completely disabled. This is not recommended for production environments.

You can change this by modifying the `argocd/1__configmap.yaml` file and adding the necessary configuration (see: [ArgoCD TLS configuration](https://argo-cd.readthedocs.io/en/stable/operator-manual/tls/))

```yaml
# other configurations
---
apiVersion: v1
kind: ConfigMap
metadata:
  labels:
    app.kubernetes.io/name: argocd-cmd-params-cm
    app.kubernetes.io/part-of: argocd
  name: argocd-cmd-params-cm
data:
  server.insecure: "true"
  reposerver.disable.tls: "true"
  dexserver.disable.tls: "true"
---
# other configurations
```

Access to the ArgoCD UI console at [http://argocd.jhcm.io](http://argocd.jhcm.io) using the default credentials:

- Username: `admin`
- Password: obtained via the following command:

```bash
kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d; echo
# or via `agocd` CLI, if you have it installed
argocd admin initial-password -n argocd
```

## Keycloak

Using the following command to bootstrap Keycloak:

```bash
# Build the customized Keycloak image locally
docker build -t jhcm-keycloak:1.0.0 platform/keycloak
# Deploy the Keycloak manifests
kubectl apply -n keycloak -f platform/keycloak
```

Access to the Keycloak UI console at [http://keycloak.jhcm.io](http://keycloak.jhcm.io) using the default credentials:

- Username: `admin`
- Password: `admin`