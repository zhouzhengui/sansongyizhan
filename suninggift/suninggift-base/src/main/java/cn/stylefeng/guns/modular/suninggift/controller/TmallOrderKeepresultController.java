package cn.stylefeng.guns.modular.suninggift.controller;

import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.suninggift.easyExcel.EasyExcelService;
import cn.stylefeng.guns.modular.suninggift.easyExcel.model.TmallOrderExportModel;
import cn.stylefeng.guns.modular.suninggift.enums.QimenOrderStatesEnum;
import cn.stylefeng.guns.modular.suninggift.utils.DesUtils;
import cn.stylefeng.guns.modular.suninggift.easyExcel.easyExcelHandler.CommExcelHandler;
import cn.stylefeng.guns.modular.suninggift.entity.vo.RequestVo;
import cn.stylefeng.guns.modular.suninggift.enums.ProcessStatesEnum;
import cn.stylefeng.guns.modular.suninggift.model.result.OrderInfoResult;
import cn.stylefeng.guns.modular.suninggift.service.TmallOrderKeepresultService;
import cn.stylefeng.guns.modular.suninggift.service.TmllOrderDisposeService;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassNameTmallOrderKeepresultController
 * @Description TODO天猫订单履约、毁约控制类
 * @Author tangxiong
 * @Date 2020/2/24 16:08
 **/
@RestController
@Slf4j
@RequestMapping("/tmall")
public class TmallOrderKeepresultController {

    @Autowired
    private TmallOrderKeepresultService tmallOrderKeepresultService;

    @Autowired
    private TmllOrderDisposeService tmllOrderDisposeService;

    @Autowired
    private EasyExcelService easyExcelService;

    @ApiOperation(value = "请求天猫订单履约、毁约", notes = "请求天猫订单履约、毁约")
    @PostMapping("/order/keepresult")
    public Map<String, Object> tmallOrderKeepresult(@RequestBody RequestVo requestVo) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();
        log.info("请求天猫订单履约、毁约参数：" + JSONObject.toJSONString(requestVo));
        String bizContent = requestVo.getBiz_content();
        String token = requestVo.getToken();
        if (StringUtils.isEmpty(bizContent)) {
            resultMap.put("code", 20000);
            resultMap.put("msg", "参数无效");
            return resultMap;
        }

        if (StringUtils.isEmpty(token)) {
            resultMap.put("code", 20000);
            resultMap.put("msg", "签名不能为空");
            return resultMap;
        }

        String data = bizContent;
        String encryData = DesUtils.encryptHex(data);
        if (!token.equals(encryData)) {
            resultMap.put("code", 30000);
            resultMap.put("msg", "验签失败");
            return resultMap;
        }

        paramMap = JSONObject.parseObject(data, Map.class);

        resultMap = tmallOrderKeepresultService.orderKeepresult(paramMap);
        return resultMap;
    }

    @ApiOperation(value = "请求天猫订单履约、毁约", notes = "请求天猫订单履约、毁约")
    @PostMapping("/cmcc/order/keepresult")
    public Map<String, Object> tmallCmccOrderKeepresult(@RequestBody RequestVo requestVo) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();
        log.info("请求天猫订单履约、毁约参数：" + JSONObject.toJSONString(requestVo));
        String bizContent = requestVo.getBiz_content();
        String token = requestVo.getToken();
        if (StringUtils.isEmpty(bizContent)) {
            resultMap.put("code", 20000);
            resultMap.put("msg", "参数无效");
            return resultMap;
        }

        if (StringUtils.isEmpty(token)) {
            resultMap.put("code", 20000);
            resultMap.put("msg", "签名不能为空");
            return resultMap;
        }

        String data = bizContent;
        String encryData = DesUtils.encryptHex(data);
        if (!token.equals(encryData)) {
            resultMap.put("code", 30000);
            resultMap.put("msg", "验签失败");
            return resultMap;
        }

        paramMap = JSONObject.parseObject(data, Map.class);

        resultMap = tmallOrderKeepresultService.cmccOrderKeepresult(paramMap);
        return resultMap;
    }

    @ApiOperation(value = "升档业务办理结果查询", notes = "升档业务办理结果查询")
    @PostMapping("/orderQuery")
    // @ResponseBody
    public Map<String, Object> orderQuery(@RequestBody Map<String, Object> paramMap) {
        Map<String, Object> resultMap = new HashMap<>();
        log.info("升档业务办理结果查询请求参数：" + JSONObject.toJSONString(paramMap));
        if (paramMap == null || paramMap.size() <= 0) {
            resultMap.put("code", 20000);
            resultMap.put("msg", "参数无效");
            return resultMap;
        }
        String token = (String) paramMap.get("token");
        if (StringUtils.isEmpty(token)) {
            resultMap.put("code", 20000);
            resultMap.put("msg", "签名不能为空");
            return resultMap;
        }

        paramMap.remove("token");
        String data = JSONObject.toJSONString(paramMap);
        String encryData = DesUtils.encryptHex(data);
       /* if (!token.equals(encryData)) {
            resultMap.put("code", 30000);
            resultMap.put("msg", "验签失败");
            return resultMap;
        }*/
        String outOrderNo = (String) paramMap.get("outOrderNo");
        if (StringUtils.isEmpty(outOrderNo)) {
            resultMap.put("code", 20000);
            resultMap.put("msg", "订单号不能为空");
            return resultMap;
        }

        resultMap = tmallOrderKeepresultService.orderQuery(paramMap);
        return resultMap;
    }

    @ApiOperation(value = "pot订单查询", notes = "pot订单查询")
    @PostMapping("/queryOrderList")
    // @ResponseBody
    public LayuiPageInfo queryOrderList(@RequestBody Map<String, Object> paramMap) {
        LayuiPageInfo pageInfo = new LayuiPageInfo();
        //Map<String, Object> paramMap = new HashMap<>();
       /* String outTradeNo = request.getParameter("outTradeNo");
        String phone = request.getParameter("phone");
        String limit = request.getParameter("limit");
        String page = request.getParameter("page");*/
       /* paramMap.put("outTradeNo", outTradeNo);
        paramMap.put("phone", phone);*/
        log.info("pot订单查询入参：" + JSONObject.toJSONString(paramMap));
        /*if (paramMap == null || paramMap.size() <= 0) {
            resultMap.put("code", 20000);
            resultMap.put("msg", "参数无效");
            return pageInfo;
        }*/
        /*String token = (String) paramMap.get("token");
        if (StringUtils.isEmpty(token)) {
            resultMap.put("code", 20000);
            resultMap.put("msg", "签名不能为空");
            return resultMap;
        }*/

      /*  paramMap.remove("token");
        String data = JSONObject.toJSONString(paramMap);
        String encryData = DesUtils.encryptHex(data);*/
       /* if (!token.equals(encryData)) {
            resultMap.put("code", 30000);
            resultMap.put("msg", "验签失败");
            return resultMap;
        }*/
        //DateUtil.parseTime("", "yyyy-MM-dd HH:mm:ss");
       /*String limit = request.getParameter("limit");
       String page = request.getParameter("page");
       String outTradeNo = request.getParameter("outTradeNo");
       String sDate = request.getParameter("sDate");
       String eDate = request.getParameter("eDate");
       if(StringUtils.isEmpty(limit) || StringUtils.isEmpty(page)){
           pageInfo.setMsg("limit 或者 page 不能为空！");
           return pageInfo;
       }*/
        pageInfo = tmllOrderDisposeService.tmallQueryOrderList(paramMap);
        return pageInfo;
    }


    @ApiOperation(value = "pot订单导出", notes = "pot订单导出")
    @RequestMapping("/exportOrder")
    public void exportOrder(@RequestBody Map<String, Object> paramMap, HttpServletResponse response) {
        LayuiPageInfo pageInfo = new LayuiPageInfo();
        log.info("pot订单查询入参：" + JSONObject.toJSONString(paramMap));
        pageInfo = tmllOrderDisposeService.tmallQueryOrderList(paramMap);
        List<OrderInfoResult>  orderList = pageInfo.getData();
        List<TmallOrderExportModel> orderInfoList = JSONObject.parseArray(
                JSONObject.toJSONString(orderList), TmallOrderExportModel.class);

        CommExcelHandler commExcelHandler = new CommExcelHandler();
        if(orderInfoList != null && orderInfoList.size() > 0){
            for(TmallOrderExportModel tmallOrderExportModel:orderInfoList){
                tmallOrderExportModel.setStatus(QimenOrderStatesEnum.byCode(tmallOrderExportModel.getStatus()).getDec());
                tmallOrderExportModel.setProcessStates(ProcessStatesEnum.byCode(tmallOrderExportModel.getProcessStates()).getDec());
            }
        }
        // 默认列宽
        Map<Integer, Integer> columnWidthMap = new HashMap<>();
        Field[] declaredFields = TmallOrderExportModel.class.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            if (declaredFields[i].isAnnotationPresent(ExcelProperty.class)) {
                columnWidthMap.put(i, 5000);
            }
        }

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("IO异常", e);
            throw new RuntimeException("download file error");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "天猫订单" + sdf.format(new Date()) + ".xlsx";
        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.ms-excel");
        response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");

        easyExcelService.writeExcelWithModelAndHandler(orderInfoList, TmallOrderExportModel.class,
                commExcelHandler, columnWidthMap, ExcelTypeEnum.XLSX, "天猫订单", out);
    }
}
