package com.parsentag.repository.exception;

import com.parsentag.exception.AbstractParsentagException;

public class ImageDownloadException extends AbstractParsentagException {
    public ImageDownloadException(Exception e) {
        super(e);
    }
}
