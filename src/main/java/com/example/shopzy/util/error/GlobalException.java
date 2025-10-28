package com.example.shopzy.util.error;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.example.shopzy.domain.response.RestResponse;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = {
            IdInvalidException.class,
    })
    public ResponseEntity<RestResponse<Object>> handleIdException(Exception ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Invalid ID");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = {
            EmailInvalidException.class,
    })
    public ResponseEntity<RestResponse<Object>> handleEmailException(Exception ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Invalid Email");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = {
            NoResourceFoundException.class,
    })
    public ResponseEntity<RestResponse<Object>> handleNotFoundException(NoResourceFoundException ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setError("Not Found");
        res.setMessage(ex.getMessage()); // hoặc custom: "API endpoint không tồn tại"
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> validationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();

        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getBody().getDetail());

        List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());
        res.setMessage(errors.size() > 1 ? errors : errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<RestResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Invalid parameter type");

        String paramName = ex.getName();
        String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "Unknown";
        String invalidValue = ex.getValue() != null ? ex.getValue().toString() : "null";

        res.setMessage(String.format("Parameter '%s' should be of type %s but value '%s' is invalid",
                paramName, requiredType, invalidValue));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = {
            StorageException.class,
            // FileUploadException.class,
    })
    public ResponseEntity<RestResponse<Object>> handleFileUploadException(Exception ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage(ex.getMessage());
        res.setError("Exception upload file...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    // @ExceptionHandler(value = {
    // StorageException.class,
    // })
    // public ResponseEntity<RestResponse<Object>>
    // handleFileUploadException(Exception ex) {
    // RestResponse<Object> res = new RestResponse<Object>();
    // res.setStatusCode(HttpStatus.BAD_REQUEST.value());
    // res.setError(ex.getMessage());
    // res.setMessage("Exception upload file...");
    // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    // }

    // @ExceptionHandler(value = {
    // PermissionException.class,
    // })
    // public ResponseEntity<RestResponse<Object>>
    // handlePermissionException(Exception ex) {
    // RestResponse<Object> res = new RestResponse<Object>();
    // res.setStatusCode(HttpStatus.FORBIDDEN.value());
    // res.setError(ex.getMessage());
    // res.setMessage("Forbidden");
    // return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res); // 403 (đăng
    // nhập nhưng chưa có quyền)
    // }
}