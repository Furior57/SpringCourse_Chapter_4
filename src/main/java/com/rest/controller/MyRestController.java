package com.rest.controller;

import com.rest.entity.Employee;
import com.rest.service.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
// В этот раз мы пометим контроллер необычной для нас аннотацией @RestController
// Она говорит о том, что данный контроллер управляет REST запросами и ответами.
// Теперь мы перейдем в наш старый проект и скопируем из него entity, dao и services,
// вставим в наш проект и пропишем корректные пути. Нам нет смысла писать заново то
// что мы уже написали, логика работы будет точно такая же. Мы будем получать всех работников,
// получать конкретного работника по id, обновлять и удалять данные о работнике, так же по id.


@RestController
// Ну и конечно определим @RequestMapping
@RequestMapping("/api")
public class MyRestController {
    // Определим объект интерфейса EmployeeService, с его помощью мы будем работать в БД
    @Autowired
    private EmployeeService service;
    // Теперь определим метод которым мы будем получать всех работников, ранее в таблице
    // мы описывали URL для этого метода, вызываться он будет по адресу GET/api/employees.
    // Внутри все просто, кладем в список результат работы метода getAllEmployees() нашего сервиса.
    // Spring с помощью библиотеки jackson-databind конвертирует наших работников в json
    // и в теле ответа будет показан этот json
    @GetMapping("/employees")
    public List<Employee> showAllEmployees() {
        List<Employee> allEmployees = service.getAllEmployees();
        return allEmployees;
    }

    // Сейчас мы работаем с GET запросом и можем просто прописать его в браузере, в адресной строке.
    // Однако вспомним, что существуют еще и POST запросы и их параметры передаются в теле
    // запроса, в браузере мы уже так сделать не сможем. Поэтому скачаем небольшую программу
    // Postman, она позволяет формировать запросы которые мы будем отправлять.

}
