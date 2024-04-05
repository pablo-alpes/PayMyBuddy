# Security Policy
### version 05 - 04 - 2024

## Configuration setup for auth and access
- Usage of simple authenticators and roles for users with encryption SHA256 and https connection
- URIs are restricted following most major default configuration used by Spring Security with exposure limited to specific users and authentication validation.


## Supported Versions

| ------- | ------------------ | ------- | ------------------ | ------- |
| Version | Dependency        | Version | At risk          | Comments 
| ------- | ------------------ | ------- | ------------------ | ------- |
| 8.3.0   | MySQL driver       | MySQL  | :white_check_mark: | below there's kwown vulnerabilities. |
| 5.0.x   | Spring Security    | 5.1.x   | :white_check_mark: | |
| < 4.0   | :x:                |   | :white_check_mark: | |

## Reporting a Vulnerability

Use this section to tell people how to report a vulnerability.

Tell them where to go, how often they can expect to get an update on a
reported vulnerability, what to expect if the vulnerability is accepted or
declined, etc.
