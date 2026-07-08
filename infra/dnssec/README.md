# DNSSEC + CAA Deployment Notes

Deployment-layer requirements for FAPI 2.0 compliance. No application code.
Apply at DNS provider for each production API domain.

## DNSSEC

Enable DNSSEC at registrar + authoritative DNS for the API zone(s).
Protects against DNS spoofing that could lead to rogue domain-validated certs.

Verify after activation:
```bash
dig +dnssec api.openapi.example.ch
delv api.openapi.example.ch
```

`AD` flag must be set in the response.

## CAA records (RFC 8659)

Restrict which CAs may issue certificates for the domain. Example for a zone
using Let's Encrypt for server certs:

```
openapi.example.ch.  IN  CAA  0 issue "letsencrypt.org"
openapi.example.ch.  IN  CAA  0 issuewild ";"
openapi.example.ch.  IN  CAA  0 iodef "mailto:security@openapi.example.ch"
```

If a private CA issues client certs (FAPI mTLS), no public CAA entry needed
since clients connect directly; CAA covers the server cert only.

Verify:
```bash
dig CAA openapi.example.ch
```

## HSTS preload

`Strict-Transport-Security` already set in [../nginx/nginx.conf](../nginx/nginx.conf).
Submit domain at https://hstspreload.org once stable.
