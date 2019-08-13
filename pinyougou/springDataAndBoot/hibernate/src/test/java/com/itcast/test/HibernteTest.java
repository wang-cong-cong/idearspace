package com.itcast.test;

import com.itcast.pojo.Customer;

import com.itcast.utils.JPAUtile;
import netscape.javascript.JSUtil;
import org.junit.Test;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.temporal.JulianFields;

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


    @Test
    public void testFind(){
        EntityManager em = JPAUtile.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        //根据id查询的两种
        //Customer customer = em.getReference(Customer.class, 1l);
        Customer customer = em.find(Customer.class, 1L);

        System.out.println(customer);
        transaction.commit();
        em.close();
    }

    @Test
    public void testRemove(){
        EntityManager em = JPAUtile.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        //先根据id查询
        Customer customer = em.getReference(Customer.class, 1l);
       //再删除
        em.remove(customer);

        transaction.commit();
        em.close();
    }

    @Test
    public void testMerge(){
        EntityManager em = JPAUtile.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        //根据id查询并修改
        Customer customer = em.getReference(Customer.class, 2l);
        customer.setCustIndustry("it教育");
        //调用方法修改
        em.merge(customer);
        transaction.commit();
        em.close();
    }
}
