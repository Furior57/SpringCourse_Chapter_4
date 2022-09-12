package com.rest.service.exceptionHandling;

// Этот класс будет расширять класс RuntimeException и реализовать конструктор, который будет
// принимать сообщение об ошибке. Вернемся в MyRestController на 53 строку
public class NoSuchEmployeeException extends RuntimeException {

    public NoSuchEmployeeException(String message) {
        super(message);
    }
}
