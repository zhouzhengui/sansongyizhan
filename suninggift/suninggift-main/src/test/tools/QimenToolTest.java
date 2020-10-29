package tools;

import cn.stylefeng.guns.GunsApplication;
import cn.stylefeng.guns.modular.suninggift.tools.QimenServiceTool;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-04-26 10:39
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GunsApplication.class)
public class QimenToolTest {

    @Autowired
    private QimenServiceTool qimenServiceTool;

    @Test
    public void aa(){
        log.info("1============");
        qimenServiceTool.notifySuningOrderStatus("121212", "1212","12121212");
        log.info("2=============");
    }

    @Test
    public void aas(){
        CdisContOrder cdisContOrder = new CdisContOrder();
        cdisContOrder.setOutOrderNo("12132r32r");
        CdisContOrder oled = MpsClientUtil.selectByPrimaryKey(CdisContOrder.Class ,cdisContOrder);

    }

}
