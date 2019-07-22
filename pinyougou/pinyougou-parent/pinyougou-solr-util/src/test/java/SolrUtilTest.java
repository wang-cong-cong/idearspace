import com.pinyougou.solrutil.SolrUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * 测试类
 * @author cong
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/applicationContext*.xml")
public class SolrUtilTest {

    @Autowired
    private SolrUtil solrUtil;

    @Test
    public void execute(){
        solrUtil.importItemData();
    }
}
