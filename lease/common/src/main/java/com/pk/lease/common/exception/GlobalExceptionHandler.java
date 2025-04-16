package com.pk.lease.common.exception;

import com.pk.lease.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        //e.getStackTrace();
        e.printStackTrace();
        return Result.fail();
    }

    @ExceptionHandler(LeaseException.class)
    @ResponseBody
    public Result error(LeaseException e){
        //e.getStackTrace();
        e.getMessage();

        e.printStackTrace();
        return Result.fail(e.getCode(),e.getMessage());
    }
}
