import cn.itcast.dao.CustomerDao;
import cn.itcast.domain.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author cong
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class CustomerTest {

    @Autowired
    private CustomerDao customerDao;

    @Test
    public void test1(){
        List<Customer> all = customerDao.findAll();
        System.out.println(all);
    }


    @Test
    public void test2(){
        Customer customerById = customerDao.findCustomerById("传智");
        System.out.println(customerById);
    }

    @Test
    @Transactional//jpql修改需要加事务
    @Rollback(value = false)
    public void test3(){
        customerDao.updateCustomer("播客",2L);
    }


    @Test
    public void test4(){
        List<Object[]> allCustomer = customerDao.findAllCustomer();
        for (Object[] objects : allCustomer) {
            System.out.println(Arrays.toString(objects));
        }
    }

    @Test
    public void test5(){
        Customer name = customerDao.findByCustName("播客");
        System.out.println(name);
    }


    @Test
    public void test6(){
        List<Customer> byCustNameLike = customerDao.findByCustNameLike("播%");
        for (Customer customer : byCustNameLike) {
            System.out.println(customer);
        }
    }

}
