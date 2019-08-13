package com.itcast.test;

import com.itcast.utils.JPAUtile;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;

/**
 * @author cong
 */
public class HibernteTest2 {


    /**
     * 查询全部
     */
    @Test
    public void findAll(){
        EntityManager em = null;
        EntityTransaction transaction =null;
        try {
            em = JPAUtile.getEntityManager();
             transaction = em.getTransaction();
            transaction.begin();

            //创建query对象
            String jpql = "from Customer";
            Query query = em.createQuery(jpql);
            //查询并返回结果集
            List resultList = query.getResultList();
            for (Object o : resultList) {
                System.out.println(o);
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }finally {
            em.close();
         }

    }

    @Test
    public void findAllDesc(){
        EntityManager em = null;
        EntityTransaction transaction =null;
        try {
            em = JPAUtile.getEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            //创建query对象
            String jpql = "from Customer order by custId desc";
            Query query = em.createQuery(jpql);
            //查询并返回结果集
            List resultList = query.getResultList();
            for (Object o : resultList) {
                System.out.println(o);
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }finally {
            em.close();
        }

    }

    @Test
    public void findAllCount(){
        EntityManager em = null;
        EntityTransaction transaction =null;
        try {
            em = JPAUtile.getEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            //创建query对象
            String jpql = "select count(custId) from  Customer";
            Query query = em.createQuery(jpql);
            //查询并返回最后一个结果集
            Object singleResult = query.getSingleResult();
            System.out.println(singleResult);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }finally {
            em.close();
        }

    }

    @Test
    public void findAllPage(){
        EntityManager em = null;
        EntityTransaction transaction =null;
        try {
            em = JPAUtile.getEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            //创建query对象
            String jpql = " from  Customer";
            Query query = em.createQuery(jpql);
            //设置查询索引和每页查询条数
            query.setFirstResult(0);
            query.setMaxResults(2);
            //查询结果集
            List list = query.getResultList();
            for (Object o : list) {
                System.out.println(o);
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }finally {
            em.close();
        }

    }

    @Test
    public void findAllCondion(){
        EntityManager em = null;
        EntityTransaction transaction =null;
        try {
            em = JPAUtile.getEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            //创建query对象
            String jpql = " from  Customer where custName  like ?";
            Query query = em.createQuery(jpql);
            //查询并返回最后一个结果集
           query.setParameter(1,"传%");
            List list = query.getResultList();
            for (Object o : list) {
                System.out.println(o);
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }finally {
            em.close();
        }

    }
}
