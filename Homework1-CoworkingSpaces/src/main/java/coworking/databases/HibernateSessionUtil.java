package coworking.databases;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(){
        if (sessionFactory == null){
            try{
                sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
                return sessionFactory;
            } catch (HibernateException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }
    }

