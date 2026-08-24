package com.example.football_tournament_api.exception;

public class InvalidTournamentTypeException extends RuntimeException {
    public InvalidTournamentTypeException(String message) {
        super(message);
    }
}
