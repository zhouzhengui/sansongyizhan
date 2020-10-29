package cn.stylefeng.guns.modular.suninggift.service;

import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.CheckUserPhoneAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.RspData;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.UserProductRecommendAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.*;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.CheckUserPhoneRspAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.UserProductRecommendRspAo;
import cn.stylefeng.guns.modular.suninggift.model.constant.CardCenterInterfInfo;
import cn.stylefeng.guns.modular.suninggift.tools.SysConfigServiceTool;
import cn.stylefeng.guns.modular.suninggift.utils.CmpayUtil;
import cn.stylefeng.guns.modular.suninggift.utils.LinoHttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class OperatorAPIService {

    @Autowired
    private SysConfigServiceTool sysConfigServiceTool;

    /**
     * 三户校验接口
     * @param userPhone
     * @return
     */
    public RspData<CheckUserPhoneRspAo> threeCertCheck(String userPhone, String clientId){
        RspData<CheckUserPhoneRspAo> rspDataAo = new RspData<>();
        try {
            CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByClientId(clientId);;
            String cardCenterUrl = cardCenterInterfInfo.getHaokaUrl();
            String clientKey = cardCenterInterfInfo.getClientKey();


            CheckUserPhoneAo checkUserPhone = new CheckUserPhoneAo();
            checkUserPhone.setUserPhone(userPhone);

            String reqJson = JSONObject.toJSONString(checkUserPhone);
            log.info("三户校验接口解密请求>>>" + reqJson);
            String json = CmpayUtil.encryptAgReqData(clientId, clientKey, reqJson);
            String repString = cardCenterUrl+"/lpuf/gzlp.unicom.check.phone";
            Map<String, String> httpPostRsp = LinoHttpUtil.httpPost(json, repString, "UTF-8", 10000);
            if(null!= httpPostRsp && "200".equals(String.valueOf(httpPostRsp.get("code")))) {
                String responseString = String.valueOf(httpPostRsp.get("msg"));
                JSONObject parseObject = JSON.parseObject(responseString);
                String rspData = parseObject.getString("rspData");
                String decryptAgRespData = CmpayUtil.decryptAgRespData("muz32AD+fCRgGc/e/+H97u3wJEcEIu5I", rspData);
                log.info("三户校验接口解密响应>>>" + decryptAgRespData);

                rspDataAo = JSONObject.parseObject(decryptAgRespData, new TypeReference<RspData<CheckUserPhoneRspAo>>(){});
                /*CheckUserPhoneRspAo checkUserPhoneRspAo = JSONObject.parseObject(JSONObject.toJSONString(
                        rspDataAo.getData()),CheckUserPhoneRspAo.class);
                rspDataAo.setData(checkUserPhoneRspAo);*/

                return rspDataAo;

            }else {
                log.info("三户校验接口请求异常");
                rspDataAo.setCode("50001");
                rspDataAo.setMsg("三户校验接口请求异常");
                return rspDataAo;
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("系统异常",e);
            rspDataAo.setCode("50002");
            rspDataAo.setMsg("系统异常");
            return rspDataAo;
        }

    }

    /**
     * 运营商策略查询
     * @param userPhone
     * @return
     */
    public RspData<UserProductRecommendRspAo> queryOperatorStrategy(String userPhone, String clientId){
        RspData<UserProductRecommendRspAo> rspDataAo = new RspData<>();
        try {

            CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByClientId(clientId);
            String cardCenterUrl = cardCenterInterfInfo.getHaokaUrl();
            String clientKey = cardCenterInterfInfo.getClientKey();


            UserProductRecommendAo checkUserPhone = new UserProductRecommendAo();
            checkUserPhone.setUserPhone(userPhone);

            String reqJson = JSONObject.toJSONString(checkUserPhone);
            log.info("运营商策略查询接口解密请求>>>" + reqJson);
            String json = CmpayUtil.encryptAgReqData(clientId, clientKey, reqJson);
            String repString = cardCenterUrl+"/lpuf/gzlp.unicom.user.product.recommend";
            Map<String, String> httpPostRsp = LinoHttpUtil.httpPost(json, repString, "UTF-8", 10000);
            if(null!= httpPostRsp && "200".equals(String.valueOf(httpPostRsp.get("code")))) {
                String responseString = String.valueOf(httpPostRsp.get("msg"));
                JSONObject parseObject = JSON.parseObject(responseString);
                String rspData = parseObject.getString("rspData");
                String decryptAgRespData = CmpayUtil.decryptAgRespData("muz32AD+fCRgGc/e/+H97u3wJEcEIu5I", rspData);
                log.info("运营商策略查询接口响应>>>" + decryptAgRespData);

                rspDataAo = JSONObject.parseObject(decryptAgRespData, new TypeReference<RspData<UserProductRecommendRspAo>>(){});
                /*UserProductRecommendRspAo productRecommendRspAo = JSONObject.parseObject(JSONObject.toJSONString(
                        rspDataAo.getData()),UserProductRecommendRspAo.class);
                rspDataAo.setData(productRecommendRspAo);*/

                return rspDataAo;

            }else {
                log.info("运营商策略查询接口请求异常");
                rspDataAo.setCode("50001");
                rspDataAo.setMsg("运营商策略查询接口请求异常");
                return rspDataAo;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.error("系统异常",e);
            rspDataAo.setCode("50002");
            rspDataAo.setMsg("系统异常");
            return rspDataAo;
        }
    }

}
