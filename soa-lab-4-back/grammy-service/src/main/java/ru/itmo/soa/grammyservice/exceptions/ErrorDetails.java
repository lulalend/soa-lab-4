package ru.itmo.soa.grammyservice.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorDetails {
    private int code;
    private String message;
}

