package com.epam.esm.service.util;

import com.epam.esm.service.constant.ServiceConstant;
import com.epam.esm.service.exception.PaginationException;

public class PaginationUtil {


    public static Integer checkPage(Integer page) throws PaginationException {
        if (page < ServiceConstant.ONE) {
            if (page.equals(ServiceConstant.ZERO)) {
                throw new PaginationException(ServiceConstant.KEY_PAGINATION);
            }
            throw new PaginationException(ServiceConstant.KEY_PAGINATION_NEGATIVE);
        }
        return page;
    }

    public static Integer checkSizePage(Integer size) throws PaginationException {
        if (size < ServiceConstant.ZERO) {
            throw new PaginationException(ServiceConstant.KEY_PAGINATION_NEGATIVE);
        }
        return size;
    }
}
