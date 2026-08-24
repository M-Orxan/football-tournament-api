package com.example.football_tournament_api.exception;

public class InvalidMatchScoreException extends RuntimeException {
    public InvalidMatchScoreException(String message) {
        super(message);
    }
}
