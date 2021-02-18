# MJC-shcool-module 3. Restfull application
## web service for Gift Certificates
### List of requests:
- 1) Read gift certificate by id, "method": "GET", header: Content-Type: application/json, url: http://localhost:8080/api/giftCertificates/1
- 2) Read all gift certificates, "method": "GET", header: Content-Type: application/json, url: http://localhost:8080/api/gift_certificates
- 3) Read all gift certificates with pagination, "method": "GET", header: Content-Type: application/json, url: http://localhost:8080/api/gift_certificates?page=1&size=10
- 4) Read all gift certificates with sort, "method": "GET", header: Content-Type: application/json, url: http://localhost:8080/api/giftCertificates?sortType=id&orderType=desc
- 5) Read gift certificates by several tag's name, "method": "GET", header: Content-Type: application/json, url: http://localhost:8080/api/giftCertificates?search=tag&values=спорт&values=Активный%20отдых&sortType=name&orderType=asc
- 6) Read gift certificates by part of name or description, "method": "GET", header: Content-Type: application/json, url: http://localhost:8080/api/giftCertificates?search=name&value=дел&sortType=id&orderType=asc
- 7) Delete gift certificate by id, "method": "DELETE", header: Content-Type: application/json, url: http://localhost:8080/api/giftCertificates/10
- 8) Update gift certificate by one or more fields , "method": "PATCH", header: Content-Type: application/json, url: http://localhost:8080/api/giftCertificates
- 9) Create gift certificate , "method": "POST", header: Content-Type: application/json, url: http://localhost:8080/api/giftCertificates
- 10) Read tag by id, "method": "GET", header: Content-Type: application/json, url: http://localhost:8080/api/tags/1
- 11) Read all tags with pagination, "method": "GET", header: Content-Type: application/json, url: http://localhost:8080/api/tags?page=1&size=10
- 12) Delete tag by id, "method": "DELETE", header: Content-Type: application/json, url: http://localhost:8080/api/tags/10
- 13) Create tag , "method": "POST", header: Content-Type: application/json, url: http://localhost:8080/api/tags
- 14) Read user by id  "method": "GET", header: Content-Type: application/json, url: http://localhost:8080/api/users/100
- 15) Read all users with pagination "method": "GET", header: Content-Type: application/json, url: http://localhost:8080/api/users?page=1&size=5
- 16) Create order "method": "POST", header: Content-Type: application/json, url: http://localhost:8080/api/orders/users/1
- 17) Read order by id "method": "GET", header: Content-Type: application/json, url: http://localhost:8080/api/orders/1
- 18) Read all order by user id with pagination "method": "GET", header: Content-Type: application/json, url: http://localhost:8080/api/orders/users/1?page=1&size=5
- 19) Read widely used by user tag with most cost "method": "GET", header: Content-Type: application/json, url: http://localhost:8080/api/tags/toptag/users/1