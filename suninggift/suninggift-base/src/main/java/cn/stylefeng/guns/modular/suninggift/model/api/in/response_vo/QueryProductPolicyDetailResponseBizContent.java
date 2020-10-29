package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.DebitCreateAoSettleDetail;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.DebitCreateAoDetail;
import lombok.Data;

import java.util.List;

/**
 * 政策拆分响应类
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2019-08-03 14:08
 */
@Data
public class QueryProductPolicyDetailResponseBizContent {

    private String allocationType;

    private String fundInvertNo;

    private String fundTotalMoney;

    private String isAdvanceFund;

    private String violateHandleType;

    private String violateReceivables;

    private String benefitsMoney;

    private List<DebitCreateAoSettleDetail> settleDetails;

    private List<DebitCreateAoDetail> freezeDetails;

}
