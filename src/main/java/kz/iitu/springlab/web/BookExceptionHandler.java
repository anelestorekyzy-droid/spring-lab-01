package kz.iitu.springlab.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice(assignableTypes = BookRestController.class)
public class BookExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail handleNotFound(
            ResponseStatusException ex) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getReason() != null
                        ? ex.getReason()
                        : "Book not found"
        );

        problem.setTitle("Book Not Found");

        return problem;
    }
}
