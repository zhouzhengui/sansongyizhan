package cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 调拨创建接口请求体
 */
@Data
public class DebitCreateAo implements Serializable {


    /**
     * agencyNum : AGID201912098743647447       商家编码
     * allocationType : INSTANT_ALLOCATION      调拨类型 INSTANT_ALLOCATION.立即调拨;TODAY_TOTAL_ALLOCATION.当日夜间汇总调拨;拨;TODAY_SINGLE_ALLOCATION..当日夜间单笔调拨
     * carrierDept : 201910281133               商户部门:注册来源二级单位-gdUnicom
     * carrierNo : CID201910287027740250        商户机构编码
     * carrierOrg : 201910281133                商户机构:注册来源一级单位-CUCC
     * cityName : 0                             城市名称
     * contractFund : 960                       签约金额
     * createUserName : 201910281133            创建者
     * custName : 0                             客户姓名
     * custPhone : 13536373462                  签约手机
     * freezeDetails :                          冻结明细
     * freezeExecAccount : gzlplink12@126.com   冻结账户（用于自动转支付）
     * freezeExecAuthCode : 0                   冻结账户授权码
     * freezeExecChannelType : alipay           冻结的渠道编号，枚举值  alipay, wxpay, bankcard
     * freezeExecPid : 2088531117626761         冻结发起pid(用于自动转支付）
     * fundCarrierAccount : gz_yingfeng@163.com     供应商账户
     * fundCarrierAccountType : alipay              供应商入账账户类型
     * fundCarrierName : 广州应枫科技有限公司       供应商名称
     * fundCarrierNo : 2019120910125892000132013582760750   供应商编号
     * fundInvertNo : 40001                         垫资方编号
     * fundOrderTotalMoney : 960                    订单总金额
     * fundTotalMoney : 800                         垫资总金额
     * goodsId : 2661                               产品product_id
     * isAdvanceFund : 0                            是否需要垫资 0,需要垫资;1.不需要垫资
     * needPayPerMonth : 0                          每期应从冻结账户扣除金额
     * notifyUrl : http://www.gzlplink.com/gzlp_equity/notify/fund/deploy       通知地址
     * orderPayAmount : 0                           订单首付金额[例如:非冻结部分]
     * orderPriceAmount : 960                       订单总价[商品整体原价-出货价格]
     * orderStatus : 0                              订单状态[-3-订单异常,-2-全额退款,-1-部分退款,0-初始状态,1-交易关闭,2-部分成功,3-交易成功]
     * orderTitle : 138元畅享套餐                   订单标题
     * orderTradeAmount : 960                       订单交易金额[例如:送花呗金额]
     * orderTradeFqNum : 24                         订单分期期数
     * orderTradeStatus : WAIT_BUYER_PAY            订单交易状态: TRADE_SUCCESS:交易成功, TRADE_FINISHED:交易完结, TRADE_CLOSED:交易关闭, WAIT_TO_REFUND:等待退款, TRADE_REFUND:退款成功, WAIT_BUYER_PAY:交易创建
     * orderTradeType : 1001                        支付方式[例如:1001-线上支付宝实时支付1002-线上支付宝花呗分期2101-线下支付宝二维码实时支付2201-线下APP扫码实时支付2202-线下POST扫码实时支付]
     * outOrderNo : 20200211100054236048720993      商户订单号
     * outTradeNo : 20200211100054606719932215      交易流水号[正常无用]
     * phoneBelong : 0                              手机号码归属地[city_code]
     * policyNo : PPID202001032044919745            产品政策编号:由短日期[PPID20180310]+10位纯数字随机码生成
     * settleDetails :                              拆账列表
     * provinceName : 0                             省份名称
     * storeNum : STID201912091812205037            门店编码
     * violateHandleType : UNICOM_MODEL             违约处理类型,COMMON_MODEL 普通违约处理,UNICOM_MODEL 联通支付违约处理'
     * violateReceivables : 960                     违约应收款
     */

        /**
         *
         */
        private Integer orderId;

        /**
         * 商户订单号
         */
        private String outOrderNo;

        /**
         * 交易流水号[正常无用]
         */
        private String outTradeNo;

        /**
         * 订单标题
         */
        private String orderTitle;

        /**
         * 订单内容[待定]
         */
        private String orderBody;

        /**
         * 产品product_id
         */
        private String goodsId;

        /**
         * 订单总价[商品整体原价-出货价格]
         */
        private BigDecimal orderPriceAmount;

        /**
         * 订单首付金额[例如:非冻结部分]
         */
        private BigDecimal orderPayAmount;

        /**
         * 订单退款金额[累计]
         */
        private BigDecimal orderTradeRefund;

        /**
         * 订单交易金额[例如:送花呗金额]
         */
        private BigDecimal orderTradeAmount;

        /**
         * 订单分期费率[4.5%填写4.5]
         */
        private BigDecimal orderTradeFqRate;

        /**
         * 订单分期期数
         */
        private Integer orderTradeFqNum;

        /**
         * 支付方式[例如:1001-线上支付宝实时支付1002-线上支付宝花呗分期2101-线下支付宝二维码实时支付2201-线下APP扫码实时支付2202-线下POST扫码实时支付]
         */
        private Integer orderTradeType;

        /**
         * 订单交易状态: TRADE_SUCCESS:交易成功, TRADE_FINISHED:交易完结, TRADE_CLOSED:交易关闭, WAIT_TO_REFUND:等待退款,
         * TRADE_REFUND:退款成功, WAIT_BUYER_PAY:交易创建
         */
        private String orderTradeStatus;

        /**
         * 订单状态[-3-订单异常,-2-全额退款,-1-部分退款,0-初始状态,1-交易关闭,2-部分成功,3-交易成功]
         */
        private Integer orderStatus;

        /**
         * 客户姓名
         */
//    @NotNull(message = "{客户姓名不能为空}", groups = {First.class})
        private String custName;

        /**
         * 商户机构编号:由短日期[CID20180310]+8位纯数字随机码生成
         */
        private String carrierNo;

        /**
         * 产品政策编号:由短日期[PPID20180310]+10位纯数字随机码生成
         */
        private String policyNo;

        /**
         * 店员编号[主键]:由短日期[CKID20180310]+10位纯数字随机码生成
         */
        private String clerkNo;

        /**
         * 商户机构:注册来源一级单位-CUCC
         */
        private String carrierOrg;

        /**
         * 商户部门:注册来源二级单位-gdUnicom
         */
        private String carrierDept;

        /**
         * 资金方编号
         */
        private String fundPartyNo;

        /**
         * 资金方名称
         */
        private String fundPartyName;

        /**
         * 资金方出账账户
         */
        private String fundPartyAccount;

        /**
         * 资金方出账账户类型
         */
        private String fundPartyAccountType;

        /**
         * 垫资方编号
         */

        private String fundInvertNo;

        /**
         * 垫资方名称
         */
        private String fundInvertName;

        /**
         * 垫资方出账账户
         */
        private String fundInvertAccount;

        /**
         * 垫资方出账账户类型
         */
        private String fundInvertAccountType;

        /**
         * 供应商编号
         */
        private String fundCarrierNo;

        /**
         * 供应商名称
         */
//    @NotNull(message = "{供应商名称不能为空}", groups = {First.class})
        private String fundCarrierName;

        /**
         * 供应商账户
         */
//    @NotNull(message = "{供应商账户不能为空}", groups = {First.class})
        private String fundCarrierAccount;

        /**
         * 供应商入账账户类型
         */
//    @NotNull(message = "{供应商入账账户类型不能为空}", groups = {First.class})
        private String fundCarrierAccountType;

        /**
         * 订单总金额
         */
//    @NotNull(message = "{订单总金额不能为空}", groups = {First.class})
        private BigDecimal fundOrderTotalMoney;

        /**
         * 垫资总金额
         */
        private BigDecimal fundTotalMoney;

        /**
         * 垫资每期总金额
         */
        private BigDecimal fundPerMonthMoney;

        /**
         * 垫资每期收益
         */
        private BigDecimal fundPerMonthProfit;

        /**
         * 垫资每期本金
         */
        private BigDecimal fundPerMonthReal;

        /**
         * 垫资方首期总金额
         */
        private BigDecimal fundFirstMonthMoney;

        /**
         * 垫资方首期本金
         */
        private BigDecimal fundFirstMonthReal;

        /**
         * 垫资方首期收益
         */
        private BigDecimal fundFirstMonthProfit;


        /**
         * 垫资方收取费率
         */
        private BigDecimal fundInvertRate;

        /**
         * 资金方费率
         */
        private BigDecimal fundPartyRate;

        /**
         * 资金批次编号
         */
        private String batchPlanId;

        /**
         * 垫资方费率
         */

        private String fundInvertRateType;

        /**
         * 资金方费率
         */
        private String fundPartyRateType;

        /**
         * 资金批次名称
         */
        private String batchPlanName;

        /**
         * 垫资出账状态
         */
        private String fundInvertStatus;

        /**
         * 垫资出账时间
         */
        private Date fundInvertTime;

        /**
         * 冻结发起pid(用于自动转支付）
         */
        private String freezeExecPid;

        /**
         * 冻结账户pid（用于自动转支付）
         */
        private String freezeExecAccount;

        /**
         * 冻结账户授权码
         */
        private String freezeExecAuthCode;

        /**
         * 冻结的渠道编号类型，枚举值  alipay, wxpay, bankcard
         */
        private String freezeExecChannelType;

        /**
         * 每期应从冻结账户扣除金额
         */
        private BigDecimal needPayPerMonth;

        /**
         * 取消订单的原因
         */
        private String cancelRegard;

        /**
         * 取消订单的时间
         */
        private Date cancelDate;

        /**
         * 签约手机
         */
        private String custPhone;

        /**
         * 系统集成商PID[ISV]
         */
//    @NotNull(message = "{系统集成商PID}", groups = {First.class})
        private String isvPid;

        /**
         * 手机号码归属地[city_code]
         */
        private String phoneBelong;

        /**
         * 签约金额
         */
        private BigDecimal contractFund;

        /**
         * 展示地址
         */
        private String showUrl;

        /**
         * 通知地址
         */
        private String notifyUrl;

        /**
         * 审批账号:来源于系统的user_name,默认admin
         */
        private String approve;

        /**
         * 扩展字段:{"ext_name":"ext_content"}
         */
        private String extString;

        /**
         * 保留字段
         */
        private String outString;

        /**
         * 资金操作流水的状态。 目前支持： l INIT：初始 l PROCESSING：处理中 l SUCCESS：成功 l FAIL：失败 l CLOSED：关闭
         */
//    @NotNull(message = "{资金操作流水的状态不能为空}", groups = {First.class})
        private String operationStatus;

        /**
         * 拆账列表
         */
        private String settleDetails;


        /**
         * 冻结参数详情
         */
        private String freezeDetails;

        /**
         * 是否需要垫资 0,需要垫资;1.不需要垫资
         */
        private Integer isAdvanceFund;

        /**
         * 调拨类型 INSTANT_ALLOCATION.立即调拨;TODAY_TOTAL_ALLOCATION.当日夜间汇总调拨;拨;TODAY_SINGLE_ALLOCATION..当日夜间单笔调拨
         */
        private String allocationType;

        /**
         * 省份编号
         */
        private String provinceCode;

        /**
         * 城市编号
         */
        private String cityCode;

        /**
         * 省份
         */
        private String provinceName;

        /**
         * 地市
         */
        private String cityName;

        /**
         * 数据库状态 -1删除  0冻结  1正常
         */
        private Integer dbStatus;

        /**
         * 创建日期
         */
        private Date instDate;

        /**
         * 修改日期
         */
        private Date updtDate;



        /**
         * 业务方订单成功时间。调拨时间已业务方成功时间为准，不以 instDate 为准
         */
        private Date bizSuccessDate;

        /**
         * 调拨成功时间
         */
        private Date deploySuccessDate;


        /**
         * 数据删除日期
         */
        private Date deleteDate;

        /**
         * 所属商城：预留字段
         */
        private String merchantId;

        /**
         * 创建者
         */
        private String createUserName;

        /**
         * 更新者
         */
        private String updateUserName;

        /**
         * 可以查看的机构
         */
        private String canViewOrgNo;

        private String agencyNum;

        private String storeNum;

        private String fundPartyCode;

        /** 无条件退款支持天数 */
        private Integer refundSupportDays;

        /** 违约处理类型,COMMON_MODEL 普通违约处理,UNICOM_MODEL 联通支付违约处理 */
        private String violateHandleType;

        /** 违约应收款 */
        private BigDecimal violateReceivables;

        /** 违约月份累计 */
        private Integer violateMonths;

//    /**
//     * agencyNum : AGID201912098743647447       商家编码
//     */
//    private String agencyNum;
//
//    /**
//     * 调拨类型 INSTANT_ALLOCATION.立即调拨;TODAY_TOTAL_ALLOCATION.当日夜间汇总调拨;拨;TODAY_SINGLE_ALLOCATION..当日夜间单笔调拨
//     */
//    private String allocationType;
//
//    /**
//     *  商户部门:注册来源二级单位-gdUnicom
//     */
//    private String carrierDept;
//
//    /**
//     * 商户机构编码
//     */
//    private String carrierNo;
//
//    /**
//     * 商户机构:注册来源一级单位-CUCC
//     */
//    private String carrierOrg;
//
//    /**
//     * 城市名称
//     */
//    private String cityName;
//
//    /**
//     * 签约金额(元)
//     */
//    private BigDecimal contractFund;
//
//    /**
//     * 创建者
//     */
//    private String createUserName;
//
//    /**
//     * 客户姓名
//     */
//    private String custName;
//
//    /**
//     * 签约手机
//     */
//    private String custPhone;
//
//    /**
//     * 冻结明细
//     */
//    private String freezeDetails;
////    private List<DebitCreateAoDetail> freezeDetails;
//
//    /**
//     * 冻结账户（用于自动转支付）
//     */
//    private String freezeExecAccount;
//
//    /**
//     * 冻结账户授权码
//     */
//    private String freezeExecAuthCode;
//
//    /**
//     * 冻结的渠道编号，枚举值  alipay, wxpay, bankcard
//     */
//    private String freezeExecChannelType;
//
//    /**
//     * 冻结发起pid(用于自动转支付）
//     */
//    private String freezeExecPid;
//
//    /**
//     * 供应商账户
//     */
//    private String fundCarrierAccount;
//
//    /**
//     * 供应商入账账户类型
//     */
//    private String fundCarrierAccountType;
//
//    /**
//     * 供应商名称
//     */
//    private String fundCarrierName;
//
//    /**
//     * 供应商编号
//     */
//    private String fundCarrierNo;
//
//    /**
//     * 垫资方编号
//     */
//    private String fundInvertNo;
//
//    /**
//     * 订单总金额
//     */
//    private BigDecimal fundOrderTotalMoney;
//
//    /**
//     * 垫资总金额
//     */
//    private BigDecimal fundTotalMoney;
//
//    /**
//     * 产品product_id
//     */
//    private String goodsId;
//
//    /**
//     * 是否需要垫资 0,需要垫资;1.不需要垫资
//     */
//    private Integer isAdvanceFund;
//
//    /**
//     * 每期应从冻结账户扣除金额
//     */
//    private BigDecimal needPayPerMonth;
//
//    /**
//     * 通知地址
//     */
//    private String notifyUrl;
//
//    /**
//     * 订单首付金额[例如:非冻结部分]
//     */
//    private BigDecimal orderPayAmount;
//
//    /**
//     * 订单总价[商品整体原价-出货价格]
//     */
//    private BigDecimal orderPriceAmount;
//
//    /**
//     * 订单状态[-3-订单异常,-2-全额退款,-1-部分退款,0-初始状态,1-交易关闭,2-部分成功,3-交易成功]
//     */
//    private Integer orderStatus;
//
//    /**
//     * 订单标题
//     */
//    private String orderTitle;
//
//    /**
//     * 订单交易金额[例如:送花呗金额]
//     */
//    private BigDecimal orderTradeAmount;
//
//    /**
//     *  订单分期期数
//     */
//    private Integer orderTradeFqNum;
//
//    /**
//     * 订单交易状态: TRADE_SUCCESS:交易成功, TRADE_FINISHED:交易完结, TRADE_CLOSED:交易关闭, WAIT_TO_REFUND:等待退款, TRADE_REFUND:退款成功, WAIT_BUYER_PAY:交易创建
//     */
//    private String orderTradeStatus;
//
//    /**
//     * 支付方式[例如:1001-线上支付宝实时支付1002-线上支付宝花呗分期2101-线下支付宝二维码实时支付2201-线下APP扫码实时支付2202-线下POST扫码实时支付]
//     */
//    private Integer orderTradeType;
//
//    /**
//     * 商户订单号
//     */
//    private String outOrderNo;
//
//    /**
//     * 交易流水号[正常无用]
//     */
//    private String outTradeNo;
//
//    /**
//     * 手机号码归属地[city_code]
//     */
//    private String phoneBelong;
//
//    /**
//     *  产品政策编号:由短日期[PPID20180310]+10位纯数字随机码生成
//     */
//    private String policyNo;
//
//    /**
//     * 拆账列表
//     */
//    private String settleDetails;
////    private List<DebitCreateAoSettleDetail> settleDetails;
//
//    /**
//     * 省份名称
//     */
//    private String provinceName;
//
//    /**
//     * 门店编码
//     */
//    private String storeNum;
//
//    /**
//     * 违约处理类型,COMMON_MODEL 普通违约处理,UNICOM_MODEL 联通支付违约处理'
//     */
//    private String violateHandleType;
//
//    /**
//     * 违约应收款
//     */
//    private BigDecimal violateReceivables;


}
