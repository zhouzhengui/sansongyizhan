package cn.stylefeng.guns.modular.suninggift.service;

import cn.stylefeng.guns.modular.suninggift.model.constant.CardCenterInterfInfo;
import cn.stylefeng.guns.modular.suninggift.model.constant.SysConstant;
import cn.stylefeng.guns.sys.modular.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-05-10 16:49
 */
@Service
@Slf4j
public class SystemService {

    @Autowired
    private InInterfaceService inInterfaceService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 新增ftp数据
     * @param
     * @return
     */
    public String flushInterfaceCache() {
        try{
            //刷新政策缓存
            if(null != redisUtil.get(SysConstant.QUERY_POLICY_LIST_MAP)){
                Map<String, CardCenterInterfInfo> a = (Map) redisUtil.get(SysConstant.QUERY_POLICY_LIST_MAP);
                Iterator<String> iterator = a.keySet().iterator();
                //加载政策缓存
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    CardCenterInterfInfo cardCenterInterfInfo = a.get(key);
                    inInterfaceService.queryPolicyList(cardCenterInterfInfo, key.split("-")[1], true);
                }
            }

            //刷新queryAgencyOrStore缓存
            if(null != redisUtil.get(SysConstant.QUERY_AGENCY_OR_STORE_MAP)){
                Map<String, CardCenterInterfInfo> a = (Map) redisUtil.get(SysConstant.QUERY_AGENCY_OR_STORE_MAP);
                Iterator<String> iterator = a.keySet().iterator();
                //加载政策缓存
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    CardCenterInterfInfo cardCenterInterfInfo = a.get(key);
                    inInterfaceService.queryAgencyOrStore(cardCenterInterfInfo, true);
                }
            }

            //刷新queryAgencygath缓存
            if(null != redisUtil.get(SysConstant.QUERY_AGENCYGATH_MAP)){
                Map<String, CardCenterInterfInfo> a = (Map) redisUtil.get(SysConstant.QUERY_AGENCYGATH_MAP);
                Iterator<String> iterator = a.keySet().iterator();
                //加载政策缓存
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    CardCenterInterfInfo cardCenterInterfInfo = a.get(key);
                    inInterfaceService.queryAgencygath(cardCenterInterfInfo, true);
                }
            }

            //刷新queryCarrierAccountInfo缓存
            if(null != redisUtil.get(SysConstant.QUERY_CARRIER_ACCOUNT_INFO_MAP)){
                Map<String, CardCenterInterfInfo> a = (Map) redisUtil.get(SysConstant.QUERY_CARRIER_ACCOUNT_INFO_MAP);
                Iterator<String> iterator = a.keySet().iterator();
                //加载政策缓存
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    CardCenterInterfInfo cardCenterInterfInfo = a.get(key);
                    inInterfaceService.queryCarrierAccountInfo(cardCenterInterfInfo, true);
                }
            }

            //刷新queryCarrierInfo缓存
            if(null != redisUtil.get(SysConstant.QUERY_CARRIER_INFO_MAP)){
                Map<String, CardCenterInterfInfo> a = (Map) redisUtil.get(SysConstant.QUERY_CARRIER_INFO_MAP);
                Iterator<String> iterator = a.keySet().iterator();
                //加载政策缓存
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    CardCenterInterfInfo cardCenterInterfInfo = a.get(key);
                    inInterfaceService.queryCarrierInfo(cardCenterInterfInfo, true);
                }
            }

            //刷新queryCarrierInfo缓存
            if(null != redisUtil.get(SysConstant.QUERY_PRODUCT_POLICY_DETAIL_MAP)){
                Map<String, CardCenterInterfInfo> a = (Map) redisUtil.get(SysConstant.QUERY_PRODUCT_POLICY_DETAIL_MAP);
                Iterator<String> iterator = a.keySet().iterator();
                //加载政策缓存
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    CardCenterInterfInfo cardCenterInterfInfo = a.get(key);
                    inInterfaceService.queryProductPolicyDetail(key.split("-")[1], key.split("-")[2], cardCenterInterfInfo, true);
                }
            }
        }catch (Exception e){
            log.error("系统异常", e);
            return "系统异常";
        }
        return null;
    }
//
//    @Cacheable
//    public void aa(){
//
//    }

}
