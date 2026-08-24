package com.example.football_tournament_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RoundNotCompletedException extends RuntimeException {
    public RoundNotCompletedException(String message) {
        super(message);
    }
}
