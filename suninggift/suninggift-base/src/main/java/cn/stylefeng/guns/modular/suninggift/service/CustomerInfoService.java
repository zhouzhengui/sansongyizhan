package cn.stylefeng.guns.modular.suninggift.service;

import cn.stylefeng.guns.modular.suninggift.entity.CustomerInfo;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Map;

public interface CustomerInfoService extends IService<CustomerInfo> {

  public Map<String, String> saveCustomerInfo(JSONObject jsonBodyObject);

}
