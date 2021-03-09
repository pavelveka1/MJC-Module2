package com.epam.esm.constant;

public class ControllerConstant {

    public static final String ADMIN_ENDPOINT = "/api/**";
    public static final String LOGIN_ENDPOINT = "/api/auth/**";
    public static final String ORDER_ENDPOINT = "/api/orders";
    public static final String READ_ENDPOINT = "/api/**";
    public static final String ACTUATOR_ENDPOINT = "/actuator/**";
    public static final String READ_TAGS_ENDPOINT = "/api/tags";
    public static final String READ_TAG_ENDPOINT = "/api/tags/*";
    public static final String READ_TOP_TAG_USER = "/api/tags/toptag/**";
    public static final String READ_USERS = "/api/users";
    public static final String READ_USER = "/api/users/*";
    public static final String READ_CERTIFICATES_ENDPOINT = "/api/certificates";
    public static final String READ_CERTIFICATE_ENDPOINT = "/api/certificates/**";
    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";

    public static final String TOKEN = "token";
    public static final String DUPLICATE_USERNAME = "duplicate_username";
    public static final String AUTHENTICATION_EXCEPTION = "authentication_exception";
    public static final String USER_DTO_NOT_VALID = "user_not_valid";

    public static final String DEFAULT_PAGE_SIZE = "100";
    public static final String DEFAULT_PAGE_NUMBER = "1";
    public static final String DEFAULT_NAME_OR_DESCRIPTION = "%";
    public static final String DEFAULT_SORT_ORDER = "asc";
    public static final String DEFAULT_SORT_TYPE = "id";
    public static final String CERTIFICATE_DTO_NOT_VALID = "GiftCertificateDto_not_valid";

    public static final Integer ZERO = 0;
    public static final String KEY_VALIDATION = "order_not_valid";

    public static final String NAME_PATTERN = ".{2,45}";
    public static final String DESCRIPTION_PATTERN = ".{2,300}";
    public static final int MIN_PRICE = 0;
    public static final int MIN_DURATION = 0;
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String PRICE = "price";
    public static final String DURATION = "duration";
    public static final String CERTIFICATE_NAME_INCORRECT = "certificate.name.incorrect";
    public static final String CERTIFICATE_DESCRIPTION_INCORRECT = "certificate.description.incorrect";
    public static final String CERTIFICATE_PRICE_INCORRECT = "certificate.price.incorrect";
    public static final String CERTIFICATE_DURATION_INCORRECT = "certificate.duration.incorrect";

    public static final String CERTIFICATES = "certificates";
    public static final String EMPTY_ORDER = "order.empty";
    public static final String TAG_NAME_INCORRECT = "tag.name.incorrect";

    public static final String USER_NAME_PATTERN = ".{2,20}";
    public static final String PASSWORD_PATTERN = ".{4,20}";

}
