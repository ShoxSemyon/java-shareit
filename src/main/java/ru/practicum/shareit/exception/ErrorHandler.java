package ru.practicum.shareit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.user.UserAllreadyExcetprion;
import ru.practicum.shareit.user.UserNotFoundException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({UserAllreadyExcetprion.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAllreadyError(final RuntimeException e) {
        log.info(e.getMessage());
        return new ErrorResponse("E001", e.getMessage());
    }

    @ExceptionHandler({UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundError(final RuntimeException e) {
        log.info(e.getMessage());
        return new ErrorResponse("E001", e.getMessage());
    }

    @AllArgsConstructor
    @Getter
    public static class ErrorResponse {
        String error;
        String msg;
    }
}
