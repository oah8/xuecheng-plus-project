package oah.project.ucenter.service;

import oah.project.ucenter.model.po.XcUser;

/**
 * @ClassName WxAuthService
 * @Description 微信扫码接入
 * @Author _oah
 * @Date 2023.12.30 21:08
 * @Version 1.0
 */
public interface WxAuthService {


    /**
     * 微信扫码认证，申请令牌，携带令牌查询用户信息、保存用户信息到数据库
     * @param code 授权码
     * @return
     */
    public XcUser wxAuth(String code);

}
