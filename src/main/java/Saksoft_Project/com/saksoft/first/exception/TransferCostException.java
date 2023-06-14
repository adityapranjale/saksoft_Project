package Saksoft_Project.com.saksoft.first.exception;

import org.springframework.http.HttpStatus;

public class TransferCostException extends Exception {
    public TransferCostException(String message, HttpStatus badRequest) {
        super(message);
    }
}
