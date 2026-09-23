package com.example.football_tournament_api.exception;


import com.example.football_tournament_api.dto.error.ErrorResponse;
import com.example.football_tournament_api.dto.error.ValidationError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import javax.management.BadAttributeValueExpException;
import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExistsException(AlreadyExistsException exception){
        ErrorResponse response=new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                exception.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception){
        ErrorResponse response=new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }

@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException exception){
        List<ValidationError> validationErrors=exception.getBindingResult()
                .getFieldErrors().stream()
                .map(fieldError -> new ValidationError(
                        fieldError.getDefaultMessage(),
                        fieldError.getField(),
                        fieldError.getRejectedValue()!=null ? fieldError.getRejectedValue().toString() :"null"
                )).toList();

        ErrorResponse response=new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed",
                validationErrors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException exception){


        ErrorResponse response=new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidTournamentTypeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTournamentTypeException(InvalidTournamentTypeException exception){


        ErrorResponse response=new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(InvalidMatchScoreException.class)
    public ResponseEntity<ErrorResponse> handleInvalidMatchScoreException(InvalidMatchScoreException exception){


        ErrorResponse response=new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RoundNotCompletedException.class)
    public ResponseEntity<ErrorResponse> handleRoundNotCompletedException(RoundNotCompletedException exception){


        ErrorResponse response=new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception){

        ErrorResponse response=new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Invalid stat type or sort order. Allowed stat types are:[CLEAN_SHEETS, GOALS_SCORED, GOALS_CONCEDED, POINTS, WON, LOST, DRAWN, GOAL_DIFFERENCE, PLAYED] Sort order:[MAX, MIN]",
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException exception) {
        ErrorResponse response=new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Requested endpoint or resource was not found",
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {

        String message=String.format("Required query parameter '%s' of type %s is missing",exception.getParameterName(),exception.getParameterType());
        ErrorResponse response=new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private final Map<String, String> constraintMessageMap = Map.of(
            "idx_head_coaches_name_active", "This head coach already exists.",
            "idx_players_email_active", "This player email is already in use.",
            "idx_teams_name_active", "This team already exists",
            "idx_head_coach_id_active","This head coach already assigned to other team"
    );
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException( DataIntegrityViolationException exception) {

        String rootMessage=exception.getMostSpecificCause().getMessage();
        String errorMessage="Data integrity violation occurred";
        for (Map.Entry<String, String> entry : constraintMessageMap.entrySet()) {
            if (rootMessage != null && rootMessage.contains(entry.getKey())) {
                errorMessage = entry.getValue();
                break;
            }
        }
        ErrorResponse response=new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
              // exception.getMessage(),
                errorMessage,
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
