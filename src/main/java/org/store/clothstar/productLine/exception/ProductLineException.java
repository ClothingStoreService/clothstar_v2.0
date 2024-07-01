package org.store.clothstar.productLine.exception;

import org.store.clothstar.common.exception.BaseException;
import org.store.clothstar.common.exception.ExceptionType;

public class ProductLineException extends BaseException {

    public ProductLineException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
