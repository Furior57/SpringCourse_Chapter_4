package com.rest.service.exceptionHandling;

// В этом классе есть всего одно поле, info, оно будет содержать строку с описанием проблемы,
// также создадим пустой конструктор и геттер и сеттер для этого поля. Теперь перейдем в
// MyRestController внутрь метода getEmployee()

public class EmployeeIncorrectData {

    private String info;

    public EmployeeIncorrectData() {
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
