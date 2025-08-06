package com.parsentag.service.exception;

import com.parsentag.exception.AbstractParsentagException;

public class ImageTooBigException extends AbstractParsentagException {
    public ImageTooBigException(int imageMaxFileSizeBytes, Integer fileSizeBytes) {
        super(String.format("Image file size should be less than %d bytes, but received: %d bytes",
                imageMaxFileSizeBytes, fileSizeBytes));

    }
}
