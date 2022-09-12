package com.rest.service.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
// Внутри все элементарно. Помечаем класс аннотацией и переносим внутрь два метода которые мы написали
// на прошлой лекции. Все./
@ControllerAdvice
public class EmployeeGlobalExceptionHandling {

    @ExceptionHandler
    public ResponseEntity<EmployeeIncorrectData> handleException(NoSuchEmployeeException e) {
        EmployeeIncorrectData data = new EmployeeIncorrectData();
        data.setInfo(e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<EmployeeIncorrectData> handleException(Exception e) {
        EmployeeIncorrectData data = new EmployeeIncorrectData();
        data.setInfo(e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
