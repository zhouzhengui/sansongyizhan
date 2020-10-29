package cn.stylefeng.guns.modular.suninggift.entity.vo;

import lombok.Data;

/**
 * @ClassNameRequestVo
 * @Description TODO
 * @Author tangxiong
 * @Date 2019/10/19 17:59
 **/
@Data
public class RequestVo {
    private String charset;

    private String format;

    private String method;

    private String token;

    private String version;

    private String biz_content;
}
