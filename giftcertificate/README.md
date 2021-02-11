# MJC-shcool-module 2. Restfull application
## web service for Gift Certificates
### List of requests:
- 1) Read gift certificate by id, "method": "GET", header: Content-Type: application/json, url: http://localhost:8080/controller/api/giftCertificates/1
- 2) Read all gift certificates, "method": "GET", header: Content-Type: application/json, url: http://localhost:8080/controller/api/gift_certificates
- 3) Read all gift certificates with sort, "method": "GET", header: Content-Type: application/json, url: http://localhost:8080/controller/api/giftCertificates?sortType=id&orderType=desc
- 4) Read gift certificates by tag's name, "method": "GET", header: Content-Type: application/json, url: http://localhost:8080/controller/api/giftCertificates?search=tag&value=спорт&sortType=name&orderType=asc
- 5) Read gift certificates by part of name or description, "method": "GET", header: Content-Type: application/json, url: http://localhost:8080/controller/api/giftCertificates?search=name&value=дел&sortType=id&orderType=asc
- 6) Delete gift certificate by id, "method": "DELETE", header: Content-Type: application/json, url: http://localhost:8080/controller/api/giftCertificates/10
- 7) Update gift certificate , "method": "PUT", header: Content-Type: application/json, url: http://localhost:8080/controller/api/giftCertificates
- 8) Create gift certificate , "method": "POST", header: Content-Type: application/json, url: http://localhost:8080/controller/api/giftCertificates
- 9) Read tag by id, "method": "GET", header: Content-Type: application/json, url: http://localhost:8080/controller/api/tags
- 10) Read all tags, "method": "GET", header: Content-Type: application/json, url: http://localhost:8080/controller/api/tags/1
- 11) Delete tag by id, "method": "DELETE", header: Content-Type: application/json, url: http://localhost:8080/controller/api/tags/10
- 12) Create tag , "method": "POST", header: Content-Type: application/json, url: http://localhost:8080/controller/api/tags



