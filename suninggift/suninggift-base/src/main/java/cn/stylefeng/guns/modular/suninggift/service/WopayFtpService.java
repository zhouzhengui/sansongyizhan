package cn.stylefeng.guns.modular.suninggift.service;

import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.suninggift.entity.WopayFtp;
import cn.stylefeng.guns.modular.suninggift.model.params.WopayFtpParam;
import cn.stylefeng.guns.modular.suninggift.model.result.WopayFtpResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 联通ftp推送表 服务类
 * </p>
 *
 * @author cms
 * @since 2020-05-07
 */
public interface WopayFtpService extends IService<WopayFtp> {

    /**
     * 新增
     *
     * @author cms
     * @Date 2020-05-07
     */
    void add(WopayFtpParam param);

    /**
     * 新增
     *
     * @author cms
     * @Date 2020-05-07
     */
    boolean add(WopayFtp wopayFtp);

    /**
     * 删除
     *
     * @author cms
     * @Date 2020-05-07
     */
    void delete(WopayFtpParam param);

    /**
     * 更新
     *
     * @author cms
     * @Date 2020-05-07
     */
    void update(WopayFtpParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author cms
     * @Date 2020-05-07
     */
    WopayFtpResult findBySpec(WopayFtpParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author cms
     * @Date 2020-05-07
     */
    List<WopayFtpResult> findListBySpec(WopayFtpParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author cms
     * @Date 2020-05-07
     */
     LayuiPageInfo findPageBySpec(WopayFtpParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author cms
     * @Date 2020-05-07
     */
     boolean uploadFtp(Date startTime, Date endTime);

}
