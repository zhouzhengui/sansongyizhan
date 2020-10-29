package cn.stylefeng.guns.modular.suninggift.controller;


import cn.stylefeng.guns.modular.suninggift.annotation.UnicomMallValidatorHandlerFactory;
import cn.stylefeng.guns.modular.suninggift.component.SuNingUnicomMallInterfaceServiceFacade;
import cn.stylefeng.guns.modular.suninggift.component.validatorhandler.UnicomMallValidatorHandler;
import cn.stylefeng.guns.modular.suninggift.enums.ResponseStatusEnum;
import cn.stylefeng.guns.modular.suninggift.model.MessagerVo;
import cn.stylefeng.guns.modular.suninggift.model.UnicomOutBizRequest;
import cn.stylefeng.guns.modular.suninggift.utils.CommonUtil;
import cn.stylefeng.guns.modular.suninggift.utils.UnicomMallCommonParamValidator;
import cn.stylefeng.roses.core.base.controller.BaseController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 苏宁-联通新用户入网业务
 */
@Controller
@Slf4j
@RequestMapping("/opennew")
public class UnicomSuNingOutInterfaceController extends BaseController {

  @Autowired
  private UnicomMallValidatorHandlerFactory unicomMallValidatorHandlerFactory;

  @Autowired
  private SuNingUnicomMallInterfaceServiceFacade suNingUnicomMallInterfaceServiceFacade;

  /**
   * 集团电商新入网提供外放统一入口
   * @param request
   * @param response
   * @return
   */
  @ApiOperation(value = "苏宁-联通集团电商新入网统一入口", notes = "苏宁-联通集团电商新入网统一入口")
  @PostMapping(value = "/gateway" ,produces = {"application/json;charset=UTF-8"})
  @ResponseBody
  public MessagerVo unicomMallOutInterfaceEntrance(HttpServletRequest request, HttpServletResponse response) {
    MessagerVo messagerVo = new MessagerVo();

    try {
      //获取请求参数
      String jsonBody = CommonUtil.parseRequest(request);
      log.info("苏宁-联通集团电商新入网统一入口请求入参,{}" , jsonBody);

      //校验公共参数
      MessagerVo messagerComVo = UnicomMallCommonParamValidator.validateCommonParam(jsonBody);
      if(messagerComVo != null) {
        messagerComVo.setCode("20001");
        messagerComVo.setMsg("请求公共参数校验失败");
        log.info("校验公共参数异常报文,{}" , JSON.toJSONString(messagerComVo));
        return messagerComVo;
      }

      //获取校验器
      UnicomMallValidatorHandler validatorHandler = unicomMallValidatorHandlerFactory.getValidatorHandler(jsonBody);
      if(validatorHandler == null){
        messagerVo.setCode("20002");
        messagerVo.setMsg("参数缺失，校验处理器没有找到");
        messagerVo.setSubCode(ResponseStatusEnum.CONTROLLER_ERROR_PARAMS.getCode());
        messagerVo.setSubMsg("参数缺失，校验处理器没有找到");
        log.info("获取校验器异常报文,{}" , JSON.toJSONString(messagerVo));
        return messagerVo;
      }

      //执行校验处理
      UnicomOutBizRequest unicomOutBizRequest = JSONObject.parseObject(jsonBody ,UnicomOutBizRequest.class);
      MessagerVo executeMessagerVo = validatorHandler.execute(unicomOutBizRequest);
      if(!executeMessagerVo.isSuccess()) {
        executeMessagerVo.setCode("20003");
        executeMessagerVo.setMsg("参数校验失败");
        log.info("执行校验处理异常报文,{}" , JSON.toJSONString(executeMessagerVo));
        return executeMessagerVo;
      }

      UnicomOutBizRequest executeOutBizRequest = (UnicomOutBizRequest) executeMessagerVo.getData();
      MessagerVo unifyProcess = suNingUnicomMallInterfaceServiceFacade.unifyProcess(executeOutBizRequest);
      if(unifyProcess != null && !unifyProcess.isSuccess()) {
        unifyProcess.setCode("20004");
        unifyProcess.setMsg("业务处理失败");
        log.info("业务处理失败报文,{}" , JSON.toJSONString(unifyProcess));
        return unifyProcess;
      }

      //响应成功给调用方
      unifyProcess.setCode("10000");
      unifyProcess.setMsg("业务处理成功");
      log.info("业务处理成功报文,{}" , JSON.toJSONString(unifyProcess));
      return unifyProcess;
    } catch (IOException e) {
      e.printStackTrace();
      log.error("集团新入网请求处理异常,{}" ,e);
      //响应异常
      messagerVo.setCode(ResponseStatusEnum.EXCEPTION.getCode());
      messagerVo.setMsg("数据处理过程出现异常");
      messagerVo.setSubCode(ResponseStatusEnum.EXCEPTION.getCode());
      messagerVo.setSubMsg("数据处理过程出现异常");
    }

    return messagerVo;
  }

}
