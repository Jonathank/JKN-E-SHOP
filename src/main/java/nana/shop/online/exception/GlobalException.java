package nana.shop.online.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(HomeCategoryException.class)
    public ResponseEntity<ErrorDetails> handleSellerException(HomeCategoryException ex, WebRequest req) {
        return buildErrorResponse(ex, req);
    }
    @ExceptionHandler(CouponException.class)
    public ResponseEntity<ErrorDetails> handleSellerException(CouponException ex, WebRequest req) {
        return buildErrorResponse(ex, req);
    }
    @ExceptionHandler(SellerException.class)
    public ResponseEntity<ErrorDetails> handleSellerException(SellerException ex, WebRequest req) {
        return buildErrorResponse(ex, req);
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorDetails> handleProductException(ProductException ex, WebRequest req) {
        return buildErrorResponse(ex, req);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDetails> handleUserException(UserException ex, WebRequest req) {
        return buildErrorResponse(ex, req);
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorDetails> handleOrderException(OrderException ex, WebRequest req) {
        return buildErrorResponse(ex, req);
    }

    @ExceptionHandler(PaymentOrderException.class)
    public ResponseEntity<ErrorDetails> handlePaymentOrderException(PaymentOrderException ex, WebRequest req) {
        return buildErrorResponse(ex, req);
    }

    @ExceptionHandler(ReviewException.class)
    public ResponseEntity<ErrorDetails> handleReviewException(ReviewException ex, WebRequest req) {
        return buildErrorResponse(ex, req);
    }

   
    private ResponseEntity<ErrorDetails> buildErrorResponse(Exception ex, WebRequest req) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setError(ex.getMessage());
        errorDetails.setDetails(req.getDescription(false));
        errorDetails.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
