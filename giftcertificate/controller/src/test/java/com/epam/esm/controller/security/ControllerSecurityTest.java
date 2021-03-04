package com.epam.esm.controller.security;

import com.epam.esm.controller.AuthenticationRestController;
import com.epam.esm.controller.security.config.ApplicationConfigurationDevProfile;
import com.epam.esm.service.dto.AuthenticationRequestDto;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.TagDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = ApplicationConfigurationDevProfile.class)
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class ControllerSecurityTest {

    private static final String URI_CERTIFICATES = "/api/certificates";
    private static final String URI_CERTIFICATE = "/api/certificates/1";
    private static final String URI_CERTIFICATE_TWO = "/api/certificates/2";
    private static final String URI_TAGS = "/api/tags";
    private static final String URI_TAG = "/api/tags/1";
    private static final String URI_USERS = "/api/users";
    private static final String URI_ORDER = "/api/orders";
    private static final String URI_TOP_TAG = "/api/tags/toptag/users/1";
    private static final OrderDto order = new OrderDto();
    private static final String AUTHORISATION_HEADER = "Authorization";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json";
    private static final AuthenticationRequestDto userAuthentication = new AuthenticationRequestDto();
    private static final AuthenticationRequestDto adminAuthentication = new AuthenticationRequestDto();
    private static final List<GiftCertificateDto> certificates = new ArrayList<>();
    private static GiftCertificateDto giftCertificateDto = new GiftCertificateDto(1, "Поeлет на дельтоплане", "Test description", 10, 20, null, null, new ArrayList<>());
    private static GiftCertificateDto giftCertificateDto1 = new GiftCertificateDto(0, "Test name", "Test description", 10, 20, null, null, new ArrayList<>());
    private static GiftCertificateDto giftCertificateDto2 = new GiftCertificateDto();

    private static TagDto tagDto = new TagDto();

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private AuthenticationRestController authenticationController;

    @BeforeAll
    public static void init() {
        adminAuthentication.setUsername("Admin");
        adminAuthentication.setPassword("password");
        userAuthentication.setUsername("User");
        userAuthentication.setPassword("password");
        certificates.add(giftCertificateDto);
        order.setCertificates(certificates);
        tagDto.setName("TestTag");
        giftCertificateDto2.setName("New name");

    }

    @Test
    public void getTagsPermitAll() throws Exception {
        ResponseEntity<String> result = template.getForEntity(URI_TAGS, String.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void getCertificatesPermitAll() throws Exception {
        ResponseEntity<String> result = template.getForEntity(URI_CERTIFICATES, String.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void getUsersForbiddenGuest() throws Exception {
        ResponseEntity<String> result = template.getForEntity(URI_USERS, String.class);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void getUsersPermitUser() throws Exception {
        ResponseEntity<String> result = template.getForEntity(URI_USERS, String.class);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void getTopTagPermitUser() throws Exception {
        String token = getToken(userAuthentication);
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORISATION_HEADER, token);
        ResponseEntity<TagDto> result = template.exchange(URI_TOP_TAG, HttpMethod.GET, new HttpEntity<>(headers), TagDto.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void getTopTagForbiddenGuest() throws Exception {
        ResponseEntity<String> result = template.getForEntity(URI_USERS, String.class);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void makeOrderPermitUser() throws Exception {
        String token = getToken(userAuthentication);
        HttpHeaders headers = getHeaders(token);
        HttpEntity<OrderDto> entity = new HttpEntity<>(order, headers);
        ResponseEntity<OrderDto> result = template.exchange(URI_ORDER, HttpMethod.POST, entity, OrderDto.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void makeOrderForbiddenGuest() throws Exception {
        ResponseEntity<OrderDto> result = template.postForEntity(URI_ORDER, order, OrderDto.class);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void createTagPermitAdmin() throws Exception {
        String token = getToken(adminAuthentication);
        HttpHeaders headers = getHeaders(token);
        HttpEntity<TagDto> entity = new HttpEntity<>(tagDto, headers);
        ResponseEntity<TagDto> result = template.exchange(URI_TAGS, HttpMethod.POST, entity, TagDto.class);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    public void createTagForbiddenUser() throws Exception {
        String token = getToken(userAuthentication);
        HttpHeaders headers = getHeaders(token);
        HttpEntity<TagDto> entity = new HttpEntity<>(tagDto, headers);
        ResponseEntity<TagDto> result = template.exchange(URI_TAGS, HttpMethod.POST, entity, TagDto.class);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void createCertificateForbiddenUser() throws Exception {
        String token = getToken(userAuthentication);
        HttpHeaders headers = getHeaders(token);
        HttpEntity<GiftCertificateDto> entity = new HttpEntity<>(giftCertificateDto1, headers);
        ResponseEntity<GiftCertificateDto> result = template.exchange(URI_CERTIFICATES, HttpMethod.POST, entity,
                GiftCertificateDto.class);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void createCertificateForbiddenGuest() throws Exception {
        ResponseEntity<GiftCertificateDto> result = template.postForEntity(URI_CERTIFICATES, giftCertificateDto1, GiftCertificateDto.class);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void createCertificatePermitAdmin() throws Exception {
        String token = getToken(adminAuthentication);
        HttpHeaders headers = getHeaders(token);
        HttpEntity<GiftCertificateDto> entity = new HttpEntity<>(giftCertificateDto1, headers);
        ResponseEntity<GiftCertificateDto> result = template.exchange(URI_CERTIFICATES, HttpMethod.POST, entity,
                GiftCertificateDto.class);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    public void updateCertificateForbiddenUser() throws Exception {
        String token = getToken(userAuthentication);
        HttpHeaders headers = getHeaders(token);
        HttpEntity<GiftCertificateDto> entity = new HttpEntity<>(giftCertificateDto2, headers);
        template.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<GiftCertificateDto> result = template.exchange(URI_CERTIFICATE, HttpMethod.PATCH, entity,
                GiftCertificateDto.class);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void updateCertificateForbiddenGuest() throws Exception {
        HttpEntity<GiftCertificateDto> entity = new HttpEntity<>(giftCertificateDto2);
        template.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<GiftCertificateDto> result = template.exchange(URI_CERTIFICATE, HttpMethod.PATCH, entity,
                GiftCertificateDto.class);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void updateCertificatePermitAdmin() throws Exception {
        String token = getToken(adminAuthentication);
        HttpHeaders headers = getHeaders(token);
        HttpEntity<GiftCertificateDto> entity = new HttpEntity<>(giftCertificateDto2, headers);
        template.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<GiftCertificateDto> result = template.exchange(URI_CERTIFICATE_TWO, HttpMethod.PATCH, entity,
                GiftCertificateDto.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void deleteTagForbiddenUser() throws Exception {
        String token = getToken(userAuthentication);
        HttpHeaders headers = getHeaders(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> result = template.exchange(URI_TAG, HttpMethod.DELETE, entity,
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void deleteTagForbiddenGuest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE_HEADER, CONTENT_TYPE_VALUE);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> result = template.exchange(URI_TAG, HttpMethod.DELETE, entity,
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void deleteTagPermitadmin() throws Exception {
        String token = getToken(adminAuthentication);
        HttpHeaders headers = getHeaders(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> result = template.exchange(URI_TAG, HttpMethod.DELETE, entity,
                String.class);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    public void deleteCertificateForbiddenUser() throws Exception {
        String token = getToken(userAuthentication);
        HttpHeaders headers = getHeaders(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> result = template.exchange(URI_CERTIFICATE, HttpMethod.DELETE, entity,
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void deleteCertificateForbiddenGuest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE_HEADER, CONTENT_TYPE_VALUE);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> result = template.exchange(URI_CERTIFICATE, HttpMethod.DELETE, entity,
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void deleteCertificatePermitadmin() throws Exception {
        String token = getToken(adminAuthentication);
        HttpHeaders headers = getHeaders(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> result = template.exchange(URI_CERTIFICATE, HttpMethod.DELETE, entity,
                String.class);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    private String getToken(AuthenticationRequestDto authenticationRequestDto) {
        String responce = authenticationController.login(authenticationRequestDto).toString();
        int firstIndex = responce.indexOf("=") + 1;
        int lastIndex = responce.lastIndexOf("}");
        return responce.substring(firstIndex, lastIndex);
    }

    private HttpHeaders getHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORISATION_HEADER, token);
        headers.add(CONTENT_TYPE_HEADER, CONTENT_TYPE_VALUE);
        return headers;
    }
}
