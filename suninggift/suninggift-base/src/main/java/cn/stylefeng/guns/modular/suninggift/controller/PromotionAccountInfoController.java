package cn.stylefeng.guns.modular.suninggift.controller;

import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.suninggift.model.params.PromotionAccountInfoParam;
import cn.stylefeng.guns.modular.suninggift.service.PromotionAccountInfoService;
import cn.stylefeng.guns.modular.suninggift.entity.PromotionAccountInfo;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 推广商信息表，收集跟支付宝、微信等平台交互的账户信息控制器
 *
 * @author cms
 * @Date 2020-02-21 11:22:03
 */
@Controller
@RequestMapping("/promotionAccountInfo")
public class PromotionAccountInfoController extends BaseController {

    private String PREFIX = "/promotionAccountInfo";

    @Autowired
    private PromotionAccountInfoService promotionAccountInfoService;

    /**
     * 跳转到主页面
     *
     * @author cms
     * @Date 2020-02-21
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/promotionAccountInfo.html";
    }

    /**
     * 新增页面
     *
     * @author cms
     * @Date 2020-02-21
     */
    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/promotionAccountInfo_add.html";
    }

    /**
     * 编辑页面
     *
     * @author cms
     * @Date 2020-02-21
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/promotionAccountInfo_edit.html";
    }

    /**
     * 新增接口
     *
     * @author cms
     * @Date 2020-02-21
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(PromotionAccountInfoParam promotionAccountInfoParam) {
        this.promotionAccountInfoService.add(promotionAccountInfoParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author cms
     * @Date 2020-02-21
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(PromotionAccountInfoParam promotionAccountInfoParam) {
        this.promotionAccountInfoService.update(promotionAccountInfoParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author cms
     * @Date 2020-02-21
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(PromotionAccountInfoParam promotionAccountInfoParam) {
        this.promotionAccountInfoService.delete(promotionAccountInfoParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author cms
     * @Date 2020-02-21
     */
    @RequestMapping("/detail")
    @ResponseBody
    public ResponseData detail(PromotionAccountInfoParam promotionAccountInfoParam) {
        PromotionAccountInfo detail = this.promotionAccountInfoService.getById(promotionAccountInfoParam.getId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author cms
     * @Date 2020-02-21
     */
    @ResponseBody
    @RequestMapping("/list")
    public LayuiPageInfo list(PromotionAccountInfoParam promotionAccountInfoParam) {
        return this.promotionAccountInfoService.findPageBySpec(promotionAccountInfoParam);
    }

}


