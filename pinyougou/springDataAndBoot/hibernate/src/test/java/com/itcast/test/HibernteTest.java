package com.itcast.test;

import com.itcast.pojo.Customer;

import com.itcast.utils.JPAUtile;
import org.junit.Test;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * @author cong
 */

public class HibernteTest {

    @Test
    public void  test1(){
       /* //创建实体类管理器工厂
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("myJpa");
        //创建实体类管理器
        EntityManager entityManager = factory.createEntityManager();*/

       //调用工具类创建实体类管理器
        EntityManager entityManager = JPAUtile.getEntityManager();
        //获取事务对象
        EntityTransaction transaction = entityManager.getTransaction();
        //开启事务
        transaction.begin();

        Customer customer = new Customer();
        customer.setCustName("传智");

        entityManager.persist(customer);

        //提交事务
        transaction.commit();
        //释放资源
        entityManager.close();
    }
}
