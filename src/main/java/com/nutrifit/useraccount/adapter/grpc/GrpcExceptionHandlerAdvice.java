package com.nutrifit.useraccount.adapter.grpc;

import com.nutrifit.useraccount.exception.ResourceNotFoundException;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

@GrpcAdvice
public class GrpcExceptionHandlerAdvice {

    @GrpcExceptionHandler(ResourceNotFoundException.class)
    public StatusRuntimeException handleResourceNotFoundException(ResourceNotFoundException ex) {
        return Status.NOT_FOUND.withDescription(ex.getMessage()).withCause(ex).asRuntimeException();
    }

    @GrpcExceptionHandler(IllegalArgumentException.class)
    public StatusRuntimeException handleIllegalArgumentException(IllegalArgumentException ex) {
        return Status.INVALID_ARGUMENT.withDescription(ex.getMessage()).withCause(ex).asRuntimeException();
    }

    @GrpcExceptionHandler(MethodArgumentNotValidException.class)
    public StatusRuntimeException handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder("Validation failed: ");
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
            errorMessage.append(fieldError.getField()).append(" - ").append(fieldError.getDefaultMessage()).append("; ")
        );
        return Status.INVALID_ARGUMENT.withDescription(errorMessage.toString()).withCause(ex).asRuntimeException();
    }
}
