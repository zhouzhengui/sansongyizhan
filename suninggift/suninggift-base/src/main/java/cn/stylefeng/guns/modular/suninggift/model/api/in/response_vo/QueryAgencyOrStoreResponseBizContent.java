package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

import lombok.Data;

import java.util.List;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2019-08-03 14:08
 */
@Data
public class QueryAgencyOrStoreResponseBizContent {

    private QueryAgencyOrStoreResponseBizContentCdisAgency cdisAgency;
    private List<QueryAgencyOrStoreResponseBizContentCdisAgencyGaths> cdisAgencyGaths;
    private List<QueryAgencyOrStoreResponseBizContentCdisAgencyTasks> cdisAgencyTasks;

    private QueryAgencyOrStoreResponseBizContentCdisStore cdisStore;
    private List<QueryAgencyOrStoreResponseBizContentCdisStoreGaths> cdisStoreGaths;
    private List<QueryAgencyOrStoreResponseBizContentCdisStoreTasks> cdisStoreTasks;
}
