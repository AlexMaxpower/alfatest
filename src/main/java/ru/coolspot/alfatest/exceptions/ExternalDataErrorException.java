package ru.coolspot.alfatest.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExternalDataErrorException extends RuntimeException {
    public ExternalDataErrorException(String message) {
        super(message);
        log.error(message);
    }
}