package cn.itcast.dao;

import cn.itcast.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 定义好符合Spring Data JPA规范的Dao层接口
 * jpql语句的crud基本查询
 * @author cong
 */
public interface CustomerDao extends JpaRepository<Customer,Long>, JpaSpecificationExecutor<CustomerDao> {

    /**
     * SpringDataJPA原生注解查询
     * @return
     */
    @Query(value = "from Customer ")
    public List<Customer> findAll();

    @Query(value = "from Customer where custName = ?1 ")
    public Customer findCustomerById(String custName);

    @Query(value = "update Customer set custName= ?1 where custId = ?2")
    @Modifying//声明当前是更新操作
    public void updateCustomer(String custName,Long custId);


    /**
     * 使用query注解的sql方式，jpql方式查询
     * @param
     */
    @Query(value = "select * from cst_customer",nativeQuery = true)
    public List<Object []> findAllCustomer();


    public Customer findByCustName(String customer);



    public List<Customer> findByCustNameLike(String customer);

}
