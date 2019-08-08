package com.itcast.utils;



import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author cong
 */
public class JPAUtile {

    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myJpa");

    public static EntityManager getEntityManager(){
        return entityManagerFactory.createEntityManager();
    }
}
