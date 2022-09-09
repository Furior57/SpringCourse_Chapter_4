package com.rest.configuration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
// Наш класс будет расширять класс со страшным названием :)
// AbstractAnnotationConfigDispatcherServletInitializer, этот класс отвечает за настройки
// диспетчера сервлетов. В нем необходимо имплементировать три метода.
public class MyWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    // Никаких RootConfigClass у нас нет, поэтому вернем null
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }
    // Сюда мы передаем массив с классом настроек
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{MyConfig.class};
    }
    // Сюда передаем массив с URL диспетчера сервлетов, как всегда, у нас этот URL - /
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
    // Нам осталось только добавить Tomcat, это мы сделаем без описания. На этом настройка
    // конфигурации закончена.
}
