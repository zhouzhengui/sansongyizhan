package cn.stylefeng.guns.modular.suninggift.controller;

import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.suninggift.entity.OrderInfo;
import cn.stylefeng.guns.modular.suninggift.enums.QimenOrderStatesEnum;
import cn.stylefeng.guns.modular.suninggift.model.params.OrderInfoParam;
import cn.stylefeng.guns.modular.suninggift.service.OrderInfoService;
import cn.stylefeng.guns.modular.suninggift.enums.ProcessStatesEnum;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 控制器
 *
 * @author zba
 * @Date 2020-02-24 12:38:15
 */
@Controller
@RequestMapping("/orderInfo")
public class OrderInfoController extends BaseController {

    private String PREFIX = "/orderInfo";

    @Autowired
    private OrderInfoService orderInfoService;

    /**
     * 跳转到主页面
     *
     * @author zba
     * @Date 2020-02-24
     */
    @RequestMapping("")
    public String index() {
        getHttpServletRequest().setAttribute("status", QimenOrderStatesEnum.toList());//订单类型枚举
        getHttpServletRequest().setAttribute("processStates",ProcessStatesEnum.toList());//流程状态类型枚举
        return PREFIX + "/orderInfo.html";
    }

    /**
     * 新增页面
     *
     * @author zba
     * @Date 2020-02-24
     */
    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/orderInfo_add.html";
    }

    /**
     * 编辑页面
     *
     * @author zba
     * @Date 2020-02-24
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/orderInfo_edit.html";
    }

    /**
     * 新增接口
     *
     * @author zba
     * @Date 2020-02-24
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(OrderInfoParam orderInfoParam) {
        this.orderInfoService.add(orderInfoParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author zba
     * @Date 2020-02-24
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(OrderInfoParam orderInfoParam) {
        this.orderInfoService.update(orderInfoParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author zba
     * @Date 2020-02-24
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(OrderInfoParam orderInfoParam) {
        this.orderInfoService.delete(orderInfoParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author zba
     * @Date 2020-02-24
     */
    @RequestMapping("/detail")
    @ResponseBody
    public ResponseData detail(OrderInfoParam orderInfoParam) {
        OrderInfo detail = this.orderInfoService.getById(orderInfoParam.getOutTradeNo());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author zba
     * @Date 2020-02-24
     */
    @ResponseBody
    @RequestMapping("/list")
    public LayuiPageInfo list(OrderInfoParam orderInfoParam) {
        return this.orderInfoService.findPageBySpec(orderInfoParam);
    }

}


