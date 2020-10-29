package cn.stylefeng.guns.modular.suninggift.service.impl;

import cn.stylefeng.guns.base.pojo.page.LayuiPageFactory;
import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.suninggift.model.constant.WopayFtpInfo;
import cn.stylefeng.guns.modular.suninggift.entity.WopayFtp;
import cn.stylefeng.guns.modular.suninggift.mapper.WopayFtpMapper;
import cn.stylefeng.guns.modular.suninggift.model.params.WopayFtpParam;
import cn.stylefeng.guns.modular.suninggift.model.result.WopayFtpResult;
import cn.stylefeng.guns.modular.suninggift.service.WopayFtpService;
import cn.stylefeng.guns.modular.suninggift.utils.ChatbotSendUtil;
import cn.stylefeng.guns.modular.suninggift.utils.SFtpUtil;
import cn.stylefeng.guns.sys.modular.consts.service.SysConfigService;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 联通ftp推送表 服务实现类
 * </p>
 *
 * @author cms
 * @since 2020-05-07
 */
@Slf4j
@Service
public class WopayFtpServiceImpl extends ServiceImpl<WopayFtpMapper, WopayFtp> implements WopayFtpService {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");

    private SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private ChatbotSendUtil chatbotSendUtil;

    @Override
    public void add(WopayFtpParam param){
        WopayFtp entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public boolean add(WopayFtp wopayFtp) {
        return this.save(wopayFtp);
    }

    @Override
    public void delete(WopayFtpParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(WopayFtpParam param){
        WopayFtp oldEntity = getOldEntity(param);
        WopayFtp newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public WopayFtpResult findBySpec(WopayFtpParam param){
        return null;
    }

    @Override
    public List<WopayFtpResult> findListBySpec(WopayFtpParam param){
        return null;
    }

    @Override
    public LayuiPageInfo findPageBySpec(WopayFtpParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    @Override
    public boolean uploadFtp(Date startTime, Date endTime) {

        log.info("联通天猫ftp定时任务开始...");
        String ftpTime = sdf.format(startTime);
        String mkrTime = sdf2.format(startTime);


        try{
//            1、收集数据
            //获取沃乐送ftp推送的原始数据
            QueryWrapper queryWrapper = new QueryWrapper<WopayFtp>();
            queryWrapper.between("credit_date", startTime, endTime);
            List<WopayFtp> wopayFtps = this.baseMapper.selectList(queryWrapper);
            int successCount = wopayFtps.size();//成功笔数
            List<String> succeeResult  = new ArrayList<>();
            //支付成功添加首行
            succeeResult.add(ftpTime + "|" + successCount);
//            文件首行：日期|笔数
            for (WopayFtp wopayFtp : wopayFtps){
                //tradeNo|tradeFlowNo|phoneNo|stagesCode|productId|creditDate|finishDate|tradeStatus|refundDate|userName|idCard|productName|productDescription|fqNum|orderAmount|fqSellerPercent|fqRate|
                // payUserId|province|city|businessType
                succeeResult.add(
                        wopayFtp.getTradeNo() + "|" +
                        (wopayFtp.getTradeFlowNo() == null ? "" : wopayFtp.getTradeFlowNo()) + "|" +
                        (wopayFtp.getPhoneNo() == null ? "" : wopayFtp.getPhoneNo()) + "|" +
                        (wopayFtp.getStagesCode() == null ? "" : wopayFtp.getStagesCode()) + "|" +
                        (wopayFtp.getProductId() == null ? "" : wopayFtp.getProductId()) + "|" +
                        (wopayFtp.getCreditDate() == null ? "" : sdf3.format(wopayFtp.getCreditDate())) + "|" +
                        (wopayFtp.getFinishDate() == null ? "" : sdf3.format(wopayFtp.getFinishDate())) + "|" +
                        (wopayFtp.getTradeStatus() == null ? "" : wopayFtp.getTradeStatus()) + "|" +
                        (wopayFtp.getRefundDate() == null ? "" : sdf3.format(wopayFtp.getRefundDate()))+"|" +
                        (wopayFtp.getUserName() == null ? "" : wopayFtp.getUserName()) + "|" +
                        (wopayFtp.getIdCard() == null ? "" : wopayFtp.getIdCard()) + "|" +
                        (wopayFtp.getProductName() == null ? "" : wopayFtp.getProductName()) + "|" +
                        (wopayFtp.getProductDescription() == null ? "" : wopayFtp.getProductDescription()) + "|" +
                        (wopayFtp.getFqNum() == null ? "" : wopayFtp.getFqNum()) + "|" +
                        (wopayFtp.getOrderAmount() == null ? "" : wopayFtp.getOrderAmount().multiply(new BigDecimal(100)).intValue()) + "|"+
                        (wopayFtp.getFqSellerPercent() == null ? "" : wopayFtp.getFqSellerPercent().intValue()) + "|" +
                        (wopayFtp.getFqRate() == null ? "" : wopayFtp.getFqRate()) + "|" +
                        (wopayFtp.getPayUserId() == null ? "" : wopayFtp.getPayUserId()) + "|" +
                        (wopayFtp.getProvince() == null ? "" : wopayFtp.getProvince()) + "|" +
                        (wopayFtp.getCity() == null ? "" : wopayFtp.getCity()) + "|" +
                        (wopayFtp.getBusinessType() == null ? "" : wopayFtp.getBusinessType()));
            }

            //2、生产对账文件
            // 最后保存路径
            String successFileName = "TM_ORDER_FQ_TM_LEPENG_" + ftpTime + ".dat"; // 新的文件名
            String newPicPath = "/data/lepeng/"; // 文件物理路径
//            if (!rootPath.endsWith(File.separator)) {
//                newPicPath = rootPath + "/" + relativePaths;
//            }
            if (new File(newPicPath).exists() == false) {
                new File(newPicPath).mkdirs();
            }

            String sfileurl = newPicPath + successFileName;//文件路径加文件名
            File sfilePath  = new File(sfileurl);
            //创建本地文件
            try{
                BufferedWriter sbufferedWriter = null;
                try {
                    if(!sfilePath.exists()){
                        //如果不存在则创建
                        sfilePath.createNewFile();
                    }
                    sbufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sfilePath),"UTF-8"));
                    for (String info : succeeResult) {
                        sbufferedWriter.write(info);
                        sbufferedWriter.newLine();
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                    log.error("创建文件失败",e);
                    return false;
                }finally{
                    try {
                        if(null != sbufferedWriter){
                            sbufferedWriter.flush();
                            sbufferedWriter.close();
                        }
                        log.info("创建文件成功");
                    } catch (IOException e) {
                        e.printStackTrace();
                        log.error("创建文件失败",e);
                        return false;
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
                log.error("导出excel异常",e);
                return false;
            }

            //3、上传对账文件
            //获取对账sftp链接信息
            WopayFtpInfo wopayFtp = JSONObject.parseObject(sysConfigService.getByCode("wopayFtp"), WopayFtpInfo.class);
            String url = wopayFtp.getUrl();//"123.125.97.251"
            int port = wopayFtp.getPort();//21;
            String username = wopayFtp.getUsername();//"fenqi";
            String password = wopayFtp.getPassword();//"odQn%43Rp";
            String path = wopayFtp.getPath()+ "/" + mkrTime + "/";//"/daily";  // linux中只需要传用户根目录的相对路径
            SFtpUtil sftp = new SFtpUtil(username, password,url, port);
            sftp.login();
            try{
                log.info("path" + path);
                if (sftp.stat(path) == null) {
                    sftp.createDir(path);
                }

                boolean upload = sftp.upload(path, sfileurl);
                log.info("上传情况：" + upload);

            }catch (Exception e) {
                log.error("上传ftp报错", e);
                return false;
            }finally {
                sftp.logout();
            }
            log.info("商城文件结束");

            //4、删除本地对账文件
            sfilePath.delete();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            log.error("定时任务出错" + e.getMessage());
            //发邮件 给负责人
            String content = JSONObject.toJSONString(e);
            chatbotSendUtil.sendMsg(content);
            return false;

        }
    }

    private Serializable getKey(WopayFtpParam param){
        return param.getTradeNo();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private WopayFtp getOldEntity(WopayFtpParam param) {
        return this.getById(getKey(param));
    }

    private WopayFtp getEntity(WopayFtpParam param) {
        WopayFtp entity = new WopayFtp();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
