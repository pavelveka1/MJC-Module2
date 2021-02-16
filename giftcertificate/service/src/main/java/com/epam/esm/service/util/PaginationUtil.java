package com.epam.esm.service.util;

import com.epam.esm.service.exception.PaginationException;

public class PaginationUtil {

    private static final Integer ONE = 1;
    private static final Integer ZERO = 0;
    private static final String KEY_PAGINATION = "pagination";

    public static Integer checkPage(Integer page) throws PaginationException {
        if (page < ONE) {
            if (page.equals(ZERO)) {
                throw new PaginationException(KEY_PAGINATION);
            }
            page = Math.abs(page);
        }
        return page;
    }

    public static Integer checkSizePage(Integer size) {
        if (size < ONE) {
            size = Math.abs(size);
        }
        return size;
    }
}
