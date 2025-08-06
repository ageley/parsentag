package com.parsentag.service.exception;

import com.parsentag.exception.AbstractParsentagException;

public class TooManyImagesUploadedException extends AbstractParsentagException {
    public TooManyImagesUploadedException(int imageMaxSizesCount, int sizesCount) {
        super(String.format("Number of image sizes for a single photo should be less than %d, but received: %d",
                imageMaxSizesCount, sizesCount));
    }
}
