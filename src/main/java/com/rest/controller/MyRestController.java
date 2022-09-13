package com.rest.controller;

import com.rest.entity.Employee;
import com.rest.service.exceptionHandling.NoSuchEmployeeException;
import com.rest.service.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    // Postman, она позволяет формировать запросы которые мы будем отправлять./

    // В request аннотацию мы передаем путь в таком формате: /employees/{id}, то что мы указываем
    // в фигурных скобках называется PathVariable, получить это значение можно указав параметром
    // метода аннотацию @PathVariable, куда мы передаем тип данных переменной и ее название.
    // Название должно совпадать с тем что мы указали в фигурных скобках, иначе ide не поймет
    // откуда брать значение. Внутри метода все, как обычно, вызываем метод, который предоставляет
    // нам одного работника по его id, а jackson переводит это все в json./
    @GetMapping("/employees/{id}")
    public Employee getEmployee(@PathVariable int id) {
        Employee employee = service.getEmployee(id);
        // Сделаем проверку на то, нашелся ли нужный работник, если нет, то нам необходимо
        // выбросить исключение, мы создадим это исключение в пакете exceptionHandling
        // и назовем NoSuchEmployeeException, перейдем в этот класс.
        // Теперь нам необходимо возбудить исключение и передать в него сообщение.
        // Смотрим ниже как это делается.
        if (employee == null) {
            // Здесь мы создали исключение и передали в него сообщение.
            throw new NoSuchEmployeeException("There is no employee with id=" +
                    id + " in database");
            // Теперь нам необходимо обработать исключение и передать в браузер json
            // с его описанием. Для этого здесь, в контроллере мы создадим отдельный метод,
            // который и будет обрабатывать исключения, назовем его handleException и
            // перейдем к нему.
        }

        return employee;
    }

    // Методы обрабатывающие исключения помечаются аннотацией @ExceptionHandler.
    // Возвращать мы будем объект ResponseEntity<T>, это класс наследник класса HTTPEntity,
    // который позволяет формировать HTTP запрос или ответ. Содержит в себе
    // хедер и тело. Нас сейчас интересует наследник этого класса, отвечающий за
    // формирование ответа. Это параметризируемый класс и мы укажем что он работает
    // с нашим классом EmployeeIncorrectData, этот класс передастся в тело ответа.
    // В итоге наших манипуляций jackson сериализует объект EmployeeIncorrectData в json мы его
    // вернем в ответе.
    // Аргументом передадим выброшенное исключение из которого получаем сообщение и
    // передаем в конструктор класса EmployeeIncorrectData, затем возвращаем объект
    // ResponseEntity, внутрь передадим сообщение об ошибке и http статус.
//    @ExceptionHandler
//    public ResponseEntity<EmployeeIncorrectData> handleException(NoSuchEmployeeException e) {
//        EmployeeIncorrectData data = new EmployeeIncorrectData();
//        data.setInfo(e.getMessage());
//        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
//    }
    // Теперь нам необходимо обработать случай, когда вместо числа для id мы передадим строку,
    // создадим еще один метод, этот метод будет работать вообще на все исключения.
    // Делается он аналогично первому случаю. Название метода оставим такое же, просто
    // перегрузим его. Аргументом передаем объект класса Exception и больше ничего не меняем.
    // Так же создается объект класса EmployeeIncorrectData в который мы запишем сообщение
    // полученное от исключения. В статусе вернем ошибку 400 «ошибка запроса»./
//    @ExceptionHandler
//    public ResponseEntity<EmployeeIncorrectData> handleException(Exception e) {
//        EmployeeIncorrectData data = new EmployeeIncorrectData();
//        data.setInfo(e.getMessage());
//        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
//    }
    // Теперь мы определяем метод, который будет реагировать на POST запросы на адрес /employees
    // Как нам получить информацию о работнике из запроса? Очень просто:
    // @RequestBody - этой аннотацией мы получаем тело запроса, а так как там json
    // с описанием работника, то мы можем этот json сразу присвоить объекту Employee,
    // в этом нам поможет jackson. Далее все элементарно, добавляем его в базу, возвращаем
    // этот же объект в ответе, после сохранения в базе данных у него появится поле id.
    @PostMapping("/employees")
    public Employee addNewEmployee(@RequestBody Employee employee) {
        service.saveEmployee(employee);
        return employee;
    }

    // Изменение существующего работника./
    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee employee) {
        service.saveEmployee(employee);
        return employee;
    }
    // Удаление работника
    @DeleteMapping("/employees/{id}")
    public String deleteEmployee(@PathVariable int id) {
        Employee employee = service.getEmployee(id);
        if (employee == null) {
            throw new NoSuchEmployeeException("There is no employee with id=" +
                    id + " in database");
        } else service.deleteEmployee(id);

        return "Employee with ID="+id+" was deleted.";
    }

}
