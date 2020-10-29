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
public class PolicyListResponseBizContent {

    private PolicyListResponseBizContentProductPolicy cdisProductPolicy;//主政策信息

    private List<PolicyListResponseBizContentPolicy> specList;//政策明细，未配置则没有该节点
}
