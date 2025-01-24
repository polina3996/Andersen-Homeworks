package coworking.config;

import coworking.controller.AdminController;
import coworking.controller.CustomerController;
import coworking.repository.ReservationRepository;
import coworking.repository.UserRepository;
import coworking.repository.WorkspaceRepository;
import coworking.service.ReservationService;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
@ComponentScan(basePackages = "coworking")
public class AppConfig {

    // Utility
    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }

    @Bean
    public SessionFactory sessionFactory() {
        try {
            return new org.hibernate.cfg.Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (HibernateException e) {
            throw new IllegalStateException("Failed to create SessionFactory: " + e.getMessage(), e);
        }
    }

}
