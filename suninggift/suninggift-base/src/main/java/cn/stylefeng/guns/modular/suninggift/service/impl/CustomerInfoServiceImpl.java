package cn.stylefeng.guns.modular.suninggift.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.stylefeng.guns.modular.suninggift.entity.CustomerInfo;
import cn.stylefeng.guns.modular.suninggift.mapper.CustomerInfoMapper;
import cn.stylefeng.guns.modular.suninggift.service.CustomerInfoService;
import cn.stylefeng.guns.modular.suninggift.utils.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerInfoServiceImpl extends ServiceImpl<CustomerInfoMapper,CustomerInfo> implements
    CustomerInfoService {


  /**
   * 保存客户信息到customer_info表
   * @param jsonBodyObject
   * @return
   */
  @Override
  public Map<String, String> saveCustomerInfo(JSONObject jsonBodyObject) {
    Map<String, String> resultMap = new HashMap<String, String>();

    CustomerInfo customerInfo = new CustomerInfo();
    // 客户编码
    String custNo = "UID" + DateUtil.commontime + RandomUtil.randomNumbers(8);
    customerInfo.setCustNo(custNo);
    // 客户名称
    customerInfo.setCustName(jsonBodyObject.getString("custName"));
    // 客户联系方式
    customerInfo.setCustPhone(jsonBodyObject.getString("contractPhone"));
    // 客户身份证号码
    customerInfo.setCertNo(jsonBodyObject.getString("certNo"));
    // 证件类型
    customerInfo.setCertType(1);
    // 省编码 + 市编码
    String phoneBelong = jsonBodyObject.getString("phoneBelong");
    customerInfo.setPhoneBelong(phoneBelong);
    //登录账号:如用户支付宝账户编码
    //customerInfo.setLoginNo("");
    // 支付宝授权编码
    //customerInfo.setCustUserid(jsonBodyObject.getString("userId"));
    //单点登录类型:1-支付宝2-微信,3-QQ,4-新浪
    customerInfo.setUseridType(1);
    //客户类型:例如学生，1-老存量用户，2-新入网客户
    customerInfo.setCustType(2);
    //付费类型:1-预付费,2-后付费,3-准后付费
    customerInfo.setFundType(1);
    String[] phoneBelongArray = phoneBelong.split(",");
    if(phoneBelongArray.length == 1){
      // 所属省份编码
      customerInfo.setCustProvince(phoneBelongArray[0]);
    }else if(phoneBelongArray.length == 2){
      // 所属省份编码
      customerInfo.setCustProvince(phoneBelongArray[0]);
      // 所属城市编码
      customerInfo.setCustCity(phoneBelongArray[1]);
    }
    // 客户地址
    customerInfo.setCustAddress(jsonBodyObject.getString("postAddress"));
    //原产品名称
    customerInfo.setCombo(jsonBodyObject.getString("orderTitle"));
    //三户校验返回custId
    customerInfo.setCustId("0");
    // 号码预占到期时间:当前时间添加一个月
    Date occupationDate = DateUtil.month(1);
    customerInfo.setOccupationDate(occupationDate);
    // 身份验证信息：校验响应编码
    customerInfo.setCertCheckCode(jsonBodyObject.getString("certCheckCode"));
    // 身份验证信息：校验响应结果
    customerInfo.setCertCheckMsg(jsonBodyObject.getString("certCheckMsg"));
    //邮递收件人名称
    customerInfo.setPostCustName(jsonBodyObject.getString("postCustName"));
    //邮递收件人电话
    customerInfo.setPostCustPhone(jsonBodyObject.getString("postPhone"));
    //邮递收件人省市区编码
    customerInfo.setPostAreaCode(jsonBodyObject.getString("postAreaCode"));
    //邮递地址
    customerInfo.setPostAddress(jsonBodyObject.getString("postAddress"));
    //占用关键值
    customerInfo.setProkey(jsonBodyObject.getString("prokey"));
    //首月资费编码
    customerInfo.setFirstMonthId(jsonBodyObject.getString("firstMonthId"));

    customerInfo.setCreateTime(new Date());

    try {
      boolean save = this.save(customerInfo);
      if(save){
        resultMap.put("status" ,"10000");
        resultMap.put("msg" ,"保存用户信息成功");
        resultMap.put("custNo" ,custNo);
      }else{
        resultMap.put("status" ,"30001");
        resultMap.put("msg" ,"保存用户信息失败");
      }

    } catch (Exception e) {
      e.printStackTrace();
      log.error("保存用户信息异常,{}" ,e);
      resultMap.put("status" ,"30002");
      resultMap.put("msg" ,"保存用户信息异常");
    }
    log.info("保存用户信息结果,{}" ,JSONObject.toJSONString(resultMap));
    return resultMap;
  }

}
