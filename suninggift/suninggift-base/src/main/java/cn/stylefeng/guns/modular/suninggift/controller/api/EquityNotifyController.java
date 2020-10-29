package cn.stylefeng.guns.modular.suninggift.controller.api;

import cn.stylefeng.guns.modular.suninggift.model.OutBizResponse;
import cn.stylefeng.guns.modular.suninggift.service.RightsNotifyService;
import cn.stylefeng.guns.modular.suninggift.utils.CommonUtil;
import cn.stylefeng.roses.core.base.controller.BaseController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/api/equity")
public class EquityNotifyController extends BaseController {

  @Autowired
  private RightsNotifyService rightsNotifyService;

  /**
   * 权益派券通知、调拨放款成功通知
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping("/rights/receviceEquityStatus")
  @ResponseBody
  public OutBizResponse rightsNotify(HttpServletRequest request, HttpServletResponse response) {
    OutBizResponse outBizResponse = new OutBizResponse();

    try {
      // 获取参数
      String jsonBody = CommonUtil.parseRequest(request);
      log.info("派券通知请求入参:" + jsonBody);

      // 拼装加签参数
      String signContent = rightsNotifyService.getMD5SignContent(jsonBody);
      log.info("=拼装加签参数=" + signContent);
      // 验签
      boolean result = rightsNotifyService.md5Sign(jsonBody, signContent);
      if (!result) {
        outBizResponse.setCode("20001");
        outBizResponse.setMsg("验签失败");
        return outBizResponse;
      }

      // 更新派券状态
      Map<String, String> updateMap = rightsNotifyService.updateRightsOrder(jsonBody);
      if (!"10000".equals(updateMap.get("code"))) {

        outBizResponse.setCode(updateMap.get("code"));
        outBizResponse.setMsg(updateMap.get("msg"));
        log.info("=派发权益券通知失败=" + signContent + "==" + JSON.toJSONString(outBizResponse));
        return outBizResponse;
      }

      outBizResponse.setCode(updateMap.get("code"));
      outBizResponse.setMsg(updateMap.get("msg"));

      log.info("=派发权益券通知成功=" + signContent);
    } catch (Exception e) {
      e.printStackTrace();
      outBizResponse.setCode("40004");
      outBizResponse.setMsg("派券通知异常");
      log.info("派券通知异常" + e);
    }

    return outBizResponse;

  }

  /**
   * 调拨创建
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping("rights/deployCreate")
  @ResponseBody
  public OutBizResponse deployCreate(HttpServletRequest request, HttpServletResponse response) {
    OutBizResponse outBizResponse = new OutBizResponse();

    try {
      // 获取参数
      String jsonBody = CommonUtil.parseRequest(request);
      log.info("派券通知请求入参:" + jsonBody);

      // 拼装加签参数
      String signContent = rightsNotifyService.getMD5SignContent(jsonBody);
      log.info("=拼装加签参数=" + signContent);
      // 验签
      boolean result = rightsNotifyService.md5Sign(jsonBody, signContent);
      if (!result) {
        outBizResponse.setCode("20001");
        outBizResponse.setMsg("验签失败");
        return outBizResponse;
      }

      // 解析请求参数
      JSONObject bizObject = JSONObject.parseObject(jsonBody);
      JSONObject modelObject = bizObject.getJSONObject("model");

      // 获取订单号
      String outOrderNo = modelObject.getString("outOrderNo");
      String deployStatus = modelObject.getString("deployStatus");

      if ("0".equals(deployStatus)) {
        // 重新调拨创建、调拨订阅
        Map<String, String> deployMap = rightsNotifyService.deployCreate(outOrderNo);
        if (!"10001".equals(deployMap.get("code"))) {

          outBizResponse.setCode(deployMap.get("code"));
          outBizResponse.setMsg(deployMap.get("msg"));
          log.info("=重新调拨创建、调拨订阅失败=" + JSON.toJSONString(outBizResponse));
          return outBizResponse;
        }

        outBizResponse.setCode("10001");
        outBizResponse.setMsg(deployMap.get("msg"));
      } else if ("1".equals(deployStatus)) {
        // 重新调拨订阅
        Map<String, String> deployMap = rightsNotifyService.deploySubscriber(outOrderNo);
        if (!"10001".equals(deployMap.get("code"))) {

          outBizResponse.setCode(deployMap.get("code"));
          outBizResponse.setMsg(deployMap.get("msg"));
          log.info("=重新调拨订阅失败=" + JSON.toJSONString(outBizResponse));
          return outBizResponse;
        }

        outBizResponse.setCode("10001");
        outBizResponse.setMsg(deployMap.get("msg"));
      }

      log.info("=重新调拨创建、调拨订阅成功=" + signContent);

    } catch (Exception e) {
      e.printStackTrace();
      log.info("重新调拨创建、调拨订阅异常" + e);
      outBizResponse.setCode("40004");
      outBizResponse.setMsg("派券通知异常");
    }

    return outBizResponse;

  }

  /**
   * 苏宁订单解冻能力
   * @param request
   * @param response
   * @return
   */
  @ResponseBody
  @RequestMapping("/unicom.sn.order.unfreeze")
  public OutBizResponse unicomMallOrderUnfreeze(HttpServletRequest request, HttpServletResponse response) {
    OutBizResponse messagerVo = new OutBizResponse();

    try {
      //获取请求参数
      String jsonBody = CommonUtil.parseRequest(request);
      log.info("苏宁订单解冻能力请求入参:" + jsonBody);
      if(CommonUtil.isEmpty(jsonBody)) {
        messagerVo.setCode("20001");
        messagerVo.setMsg("苏宁订单解冻能力请求参数为空");
        log.info("苏宁订单解冻能力响应报文：" + JSON.toJSONString(messagerVo));
        return messagerVo;
      }

      //苏宁订单解冻能力
      OutBizResponse unfreezeMessagerVo = rightsNotifyService.unicomMallOrderUnfreeze(jsonBody);
      if("10000".equals(unfreezeMessagerVo.getSubCode())) {
        messagerVo.setCode("10000");
        messagerVo.setMsg("苏宁订单解冻能力成功");
        messagerVo.setSubCode(unfreezeMessagerVo.getSubCode());
        messagerVo.setSubMsg(unfreezeMessagerVo.getSubMsg());
      }else {
        messagerVo.setCode("20002");
        messagerVo.setMsg("苏宁订单解冻能力失败");
        messagerVo.setSubCode(unfreezeMessagerVo.getSubCode());
        messagerVo.setSubMsg(unfreezeMessagerVo.getSubMsg());
      }

    } catch (Exception e) {
      e.printStackTrace();

      messagerVo.setCode("20003");
      messagerVo.setMsg("苏宁订单解冻能力请求异常");
      log.info("苏宁订单解冻能力请求异常:" + e);

    }

    return messagerVo;

  }

  /**
   * 查询联通苏宁新用户入网订单号卡的消息状态
   * @param request
   * @param response
   * @return
   */
  @ResponseBody
  @RequestMapping("/sn.card.query.by.mobile")
  public OutBizResponse snCardQueryByMobile(HttpServletRequest request, HttpServletResponse response) {
    OutBizResponse messagerVo = new OutBizResponse();

    try {
      //获取请求参数
      String jsonBody = CommonUtil.parseRequest(request);
      log.info("查询联通苏宁新用户入网订单号卡的消息状态请求入参:" + jsonBody);
      if(CommonUtil.isEmpty(jsonBody)) {
        messagerVo.setCode("20001");
        messagerVo.setMsg("查询联通苏宁新用户入网订单号卡的消息状态请求参数为空");
        log.info("查询联通苏宁新用户入网订单号卡的消息状态响应报文：" + JSON.toJSONString(messagerVo));
        return messagerVo;
      }

      //查询联通苏宁新用户入网订单号卡的消息状态
      OutBizResponse unfreezeMessagerVo = rightsNotifyService.snCardQueryByMobile(jsonBody);
      if("10000".equals(unfreezeMessagerVo.getSubCode())) {
        messagerVo.setCode("10000");
        messagerVo.setMsg("查询联通苏宁新用户入网订单号卡的消息状态成功");
        messagerVo.setSubCode(unfreezeMessagerVo.getSubCode());
        messagerVo.setSubMsg(unfreezeMessagerVo.getSubMsg());
      }else {
        messagerVo.setCode("20002");
        messagerVo.setMsg("查询联通苏宁新用户入网订单号卡的消息状态失败");
        messagerVo.setSubCode(unfreezeMessagerVo.getSubCode());
        messagerVo.setSubMsg(unfreezeMessagerVo.getSubMsg());
      }

    } catch (Exception e) {
      e.printStackTrace();

      messagerVo.setCode("20003");
      messagerVo.setMsg("查询联通苏宁新用户入网订单号卡的消息状态请求异常");
      log.info("查询联通苏宁新用户入网订单号卡的消息状态请求异常:" + e);

    }

    return messagerVo;

  }

}
