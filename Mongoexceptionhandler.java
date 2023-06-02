import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class MongoDBExceptionHandler {

    @ExceptionHandler(MongoException.class)
    public ResponseEntity<ErrorResponse> handleMongoException(MongoException ex) {
        // Log the exception for troubleshooting
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setMessage("An error occurred while accessing MongoDB: " + ex.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(MongoTimeoutException.class)
    public ResponseEntity<ErrorResponse> handleMongoTimeoutException(MongoTimeoutException ex) {
        // Log the exception for troubleshooting
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        errorResponse.setMessage("MongoDB connection timed out: " + ex.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }

    // Add more exception handlers for other MongoDB-related exceptions

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        // Log the exception for troubleshooting
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setMessage("An error occurred: " + ex.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
