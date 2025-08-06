package com.parsentag.service.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommandPayload {
    @EqualsAndHashCode.Include
    private long userId;
    @EqualsAndHashCode.Include
    private long chatId;
    @EqualsAndHashCode.Include
    private int messageId;
    private Command command;
}
