package com.parsentag.wrapper.exception;

import com.parsentag.exception.AbstractParsentagException;
import software.amazon.awssdk.core.exception.SdkException;

public class AwsIntegrationException extends AbstractParsentagException {
    public AwsIntegrationException(SdkException e) {
        super(e);
    }
}
