package com.parsentag.service.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ImagePayload {
    private long userId;
    private long chatId;
    private int messageId;
    @EqualsAndHashCode.Include
    private String key;
    private String fileId;
}
