/*
 * Author(s): Rajae Robinson, Sydney Chambers
 */

package controller;


import model.Advisor;
import model.Complaint;
import model.Login;
import model.Query;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.PersistenceException;

public class SessionFactoryBuilder {
    private static SessionFactory sf = null;

    public static SessionFactory getSessionFactory() {
        try {
            if(sf == null) {
                sf = new Configuration()
                        .configure("hibernate.cfg.xml")
                        .addAnnotatedClass(Login.class)
                        .addAnnotatedClass(Advisor.class)
                        .addAnnotatedClass(Complaint.class)
                        .addAnnotatedClass(Query.class)
                        .buildSessionFactory();
            }
            return sf;
        } catch (PersistenceException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public static void closeSessionFactory() {
        if(sf != null) {
            sf.close();
        }
    }
}
