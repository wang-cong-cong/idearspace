package cn.itcast.test;

import cn.itcast.dao.CustomerDao;
import cn.itcast.domain.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * @author cong
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CustomerTest {

    @Autowired
    private CustomerDao customerDao;

    /**
     * 多条件查询
     */
    @Test
    public void test2(){
        Specification<Customer> spec = new Specification<Customer>() {
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                //获取属性
                Path<Object> path = root.get("custName");
                Path<Object> path1 = root.get("custIndustry");

                //构造查询
                Predicate p1 = cb.equal(path,"黑马传智播客");
                Predicate p2 = cb.equal(path1,"it教育");

                //将以上两个查询连起来
                Predicate and = cb.and(p1, p2);
                return and;
            }
        };

        Customer one = customerDao.findOne(spec);
        System.out.println(one);
    }


    /**
     * 根据条件模糊查询
     */
    @Test
    public void test3(){
        Specification<Customer> spec = new Specification<Customer>() {
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                //获取属性
                Expression<String> custName = root.get("custName").as(String.class);
                //构造查询
                Predicate like = cb.like(custName, "黑马%");
                return like;
            }
        };

        List<Customer> all = customerDao.findAll(spec);
        for (Customer customer : all) {
            System.out.println(customer);
        }
    }


    /**
     * 根据条件模糊查询进行排序
     */
    @Test
    public void test4(){
        Specification<Customer> spec = new Specification<Customer>() {
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                //获取属性
                Expression<String> custName = root.get("custName").as(String.class);


                //构造查询
                Predicate like = cb.like(custName, "黑马%");
                return like;
            }
        };

        //设置排序字段和排序方式
        Sort s = new Sort(Sort.Direction.DESC,"custId");

        List<Customer> all = customerDao.findAll(spec,s);
        for (Customer customer : all) {
            System.out.println(customer);
        }
    }


    /**
     * 分页
     */
    @Test
    public void test5(){

        Specification spec = null;

        Pageable pageable = new PageRequest(0,2);

        Page all = customerDao.findAll(null, pageable);
        System.out.println(all);
        System.out.println(all.getTotalElements());
        System.out.println(all.getTotalPages());
        System.out.println(all.getSize());
    }
}
