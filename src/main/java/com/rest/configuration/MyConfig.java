package com.rest.configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

// Прежде всего мы пометим его как @Configuration
@Configuration
// Теперь установим пакет в котором Spring будет искать компоненты, указывать надо начиная
// с директории вложенной в source директорию.
@ComponentScan(basePackages = "com.rest")
// Включаем поддержку SpringMVC
@EnableWebMvc
// Разрешаем использование аннотации @Transactional для других классов, чтобы
// автоматически создавать транзакции, менеджер транзакций определен ниже, аналогично
// строке <tx:annotation-driven transaction-manager="transactionManager"/> из XML конфигурации
@EnableTransactionManagement
public class MyConfig {
    // Теперь нам надо прописать конфигурацию для базы данных для этого нам необходимо создать бин
    // который будет инициализироваться сразу при запуске Spring
    @Bean
    // Этот бин будет не классом, а методом возвращающим объект типа DataSource, дословный перевод
    // "источник данных". Этот класс является альтернативой JDBC-подключения к базе данных,
    // для создания этого объекта мы воспользуемся классом ComboPooledDataSource из библиотеки
    // c3p0, как мы помним, эта библиотека ответственна за создание пула подключений к базе
    // данных и контроля этих подключений.
    public DataSource dataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        // Теперь нам нужно задать драйвер подключения, подключение может выкинуть исключение,
        // поэтому обернем это все в блок try-catch
        try {
            dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
            // Прописываем URL подключения
            dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/my_db?useSSL=false&serverTimezone=UTC");
            // Далее задаем пользователя и пароль для подключения
            dataSource.setUser("bestuser");
            dataSource.setPassword("bestuser");
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
        // И возвращаем объект подключения. Еще раз. Создаем объект ComboPoolDataSource, чтобы не
        // беспокоиться о пуле подключений, указываем используемый драйвер, указываем URL
        // подключения к базе данных, указываем логин-пароль.
        return dataSource;
    }

    // Теперь надо создать SessionFactory, чтобы не объявлять ее каждый раз в коде.
    @Bean
    // Возвращать будем специализированный класс содержащийся в Spring.
    public LocalSessionFactoryBean sessionFactory() {
        // Создаем экземпляр этого класса
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        // Указываем источник данных
        sessionFactoryBean.setDataSource(dataSource());
        // Устанавливаем пакет в котором будут искаться entity-сущности
        sessionFactoryBean.setPackagesToScan("com.rest.entity");
        // Нам необходимо установить диалект SQL и вывод команд в консоль, делать это
        // мы будем с помощью класса Properties пакета java.util. Этот класс наследник
        // HashTable и в нем свойства хранятся строкой в паре ключ-значение.
        // Мы можем добавлять в него свойства как вручную, так и загрузить их из
        // текстового файла. Нам он нужен, чтобы загрузить эти свойства в sessionFactoryBean.
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.setProperty("hibernate.show_sql", "true");
        sessionFactoryBean.setHibernateProperties(properties);
        // И возвращаем объект сессии. На этом настройка Hibernate закончена.
        return sessionFactoryBean;
    }

    // Теперь нам нужен бин для HibernateTransactionManager, создаем его, внутри определяем
    // объект этого класса. Нам нужно присвоить ему SessionFactory, однако наш метод
    // SessionFactory() возвращает LocalSessionFactoryBean, как это исправить?
    // Посмотрев внимательно на этот класс мы видим что он имплементирует интерфейс
    // FactoryBean<SessionFactory>, этот интерфейс параметризуется тем классом, что нам нужен.
    // Перейдем в FactoryBean и увидим, что там прописан дженерик и имеется метод,
    // getObject(), который возвращает объект дженерик-типа. Значит для получения
    // SessionFactory нам нужно вызвать этот метод на методе sessionFactory().
    // В конце возвращаем объект TransactionManager.
    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }
    // Работа с XML файлом несколько проще на вид, там все то, что мы здесь прописали делается
    // под капотом, но нам важно понимать, что именно происходит в Spring.
    // С настройкой Hibernate мы закончили. Пробежимся еще раз по нашим действиям.
    // Прописали путь к базе данных и указали логин с паролем в объекте DataSource.
    // Создали SessionFactory, указали источник данных, указали пакет в котором будут
    // искаться entity. С помощью класса Properties задали диалект SQL и вывод команд в консоль.
    // Создали HibernateTransactionManager и передали в него нашу сессию.

    // Следующая часть наших настроек касалась MVC, у нас был файл WEB.xml в котором
    // мы прописывали DispatcherServlet, здесь мы этот файл создавать не будем, но
    // в этом случае в pom.xml мы должны прописать плагин maven-war-plugin, он заменяет
    // WEB.xml. Так же мы должны создать класс в котором опишем настройки DispatcherServlet.
    // В пакете com.rest.configuration создадим класс MyWebInitializer и перейдем в него.
}
