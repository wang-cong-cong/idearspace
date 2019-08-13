package cn.itcast.dao;

import cn.itcast.domain.LinkMan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author cong
 */
public interface LinkMansDao extends JpaRepository<LinkMan,Long>, JpaSpecificationExecutor<LinkMan> {
}
