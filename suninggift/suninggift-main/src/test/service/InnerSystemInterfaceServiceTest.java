package service;

import cn.stylefeng.guns.GunsApplication;
import cn.stylefeng.guns.modular.suninggift.entity.WopayFtp;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.ApiInBizResponse;
import cn.stylefeng.guns.modular.suninggift.service.InnerSystemInterfaceService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-03-09 17:59
 */

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GunsApplication.class)
public class InnerSystemInterfaceServiceTest {

    @Autowired
    private InnerSystemInterfaceService innerSystemInterfaceService;


    //新增ftp数据
    @Test
    public void addFtpTest(){
        long l = System.currentTimeMillis();
        String aa = "";
        WopayFtp wopayFtp = new WopayFtp();
        wopayFtp.setTradeNo("1212"+System.currentTimeMillis());
        wopayFtp.setTradeFlowNo("1212");
        wopayFtp.setPhoneNo("1212");
        wopayFtp.setStagesCode("1400");
        wopayFtp.setProductId("1212");
        wopayFtp.setCreditDate(new Date());
        wopayFtp.setFinishDate(new Date());
        wopayFtp.setTradeStatus("1");
        wopayFtp.setRefundDate(new Date());
        wopayFtp.setUserName("1212");
        wopayFtp.setIdCard("1212");
        wopayFtp.setProductName("1212");
        wopayFtp.setProductDescription("1212");
        wopayFtp.setFqNum(1);
        wopayFtp.setOrderAmount(new BigDecimal("1212"));
        wopayFtp.setFqSellerPercent(new BigDecimal("1212"));
        wopayFtp.setFqRate(new BigDecimal("1212"));
        wopayFtp.setPayUserId("1212");
        wopayFtp.setProvince("1212");
        wopayFtp.setCity("1212");
        wopayFtp.setBusinessType("2");
        ApiInBizResponse apiInBizResponse = innerSystemInterfaceService.addFtp(wopayFtp);
        System.out.println(JSONObject.toJSONString(apiInBizResponse));
        long l2 = System.currentTimeMillis();
        System.out.println("耗时"+(l2-l));
    }
}
