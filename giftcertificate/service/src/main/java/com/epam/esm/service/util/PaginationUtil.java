package com.epam.esm.service.util;

import com.epam.esm.service.exception.PaginationException;

public class PaginationUtil {

    private static final Integer ONE = 1;
    private static final Integer ZERO = 0;
    private static final String KEY_PAGINATION = "pagination";
    private static final String KEY_PAGINATION_NEGATIVE = "pagination_negative_value";

    public static Integer checkPage(Integer page) throws PaginationException {
        if (page < ONE) {
            if (page.equals(ZERO)) {
                throw new PaginationException(KEY_PAGINATION);
            }
            throw new PaginationException(KEY_PAGINATION_NEGATIVE);
        }
        return page;
    }

    public static Integer checkSizePage(Integer size) throws PaginationException {
        if (size < ZERO) {
            throw new PaginationException(KEY_PAGINATION_NEGATIVE);
        }
        return size;
    }
}
