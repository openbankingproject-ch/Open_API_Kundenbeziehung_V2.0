# Local Development Certificates

Generate dev certs for Nginx mTLS termination. Output files go in this directory.
Production certs MUST come from a trusted CA and be rotated regularly.

## Layout

```
infra/certs/
  ca.key            Root CA private key
  ca.crt            Root CA certificate (self-signed)
  server.key        Nginx server private key
  server.csr        Nginx server CSR
  server.crt        Nginx server certificate (signed by ca.crt)
  client.key        Test client private key
  client.csr        Test client CSR
  client.crt        Test client certificate (signed by ca.crt)
  ca-bundle.pem     CA bundle used by Nginx to verify clients (== ca.crt for single-CA dev)
```

## Generation

```bash
cd infra/certs

# 1. Root CA
openssl genrsa -out ca.key 4096
openssl req -x509 -new -nodes -key ca.key -sha256 -days 3650 \
  -subj "/C=CH/ST=ZH/L=Zurich/O=OpenApiDev/OU=Dev/CN=OpenApi Dev Root CA" \
  -out ca.crt

# 2. Nginx server cert
openssl genrsa -out server.key 2048
openssl req -new -key server.key \
  -subj "/C=CH/ST=ZH/L=Zurich/O=OpenApiDev/OU=Dev/CN=api.openapi.local" \
  -out server.csr
openssl x509 -req -in server.csr -CA ca.crt -CAkey ca.key -CAcreateserial \
  -out server.crt -days 365 -sha256 \
  -extfile <(printf "subjectAltName=DNS:api.openapi.local,DNS:localhost,IP:127.0.0.1")

# 3. Test client cert (TPP / FAPI client simulator)
openssl genrsa -out client.key 2048
openssl req -new -key client.key \
  -subj "/C=CH/ST=ZH/L=Zurich/O=TestTPP/OU=Dev/CN=test-client-001" \
  -out client.csr
openssl x509 -req -in client.csr -CA ca.crt -CAkey ca.key -CAcreateserial \
  -out client.crt -days 365 -sha256

# 4. Nginx CA bundle (clients verified against these CAs)
cp ca.crt ca-bundle.pem
```

## Verification

```bash
# Without client cert -> handshake fails
curl -v --cacert ca.crt https://api.openapi.local/onboarding/initialization

# With client cert -> 200
curl -v --cacert ca.crt --cert client.crt --key client.key \
  -X POST https://api.openapi.local/onboarding/initialization \
  -H "Content-Type: application/json" \
  -d '{"cookiesAccepted":true,"cookieConsent":true,"dataProcessingConsent":true,"selectedCountry":"CH","serviceType":"account_opening"}'
```

## Host file

```
127.0.0.1  api.openapi.local
```

## Notes

- RSA 2048+ required by FAPI 2.0. EC keys also acceptable (P-256, P-384).
- All files in this directory are git-ignored.
- For prod, use ACME (Let's Encrypt) for server cert and a real CA hierarchy for client certs.
