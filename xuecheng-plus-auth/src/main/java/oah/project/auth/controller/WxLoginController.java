package oah.project.auth.controller;

import lombok.extern.slf4j.Slf4j;
import oah.project.ucenter.model.po.XcUser;
import oah.project.ucenter.service.WxAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * @ClassName WxLoginController
 * @Description TODO
 * @Author _oah
 * @Date 2023.12.30 15:01
 * @Version 1.0
 */
@Slf4j
@Controller
public class WxLoginController {

    @Autowired
    private WxAuthService wxAuthService;


    @RequestMapping("/wxLogin")
    public String wxLogin(String code, String state) throws IOException {
        log.debug("微信扫码回调, code:{}, state:{}", code, state);

        // 远程调用微信申请令牌，拿到令牌查询用户信息，将用户信息写入本项目数据库
        XcUser xcUser = wxAuthService.wxAuth(code);

//        XcUser xcUser = new XcUser();
//        // 暂时硬编写，目的是调试环境
//        xcUser.setUsername("t1");
        if(xcUser == null) {
            return "redirect:http://www.51xuecheng.cn/error.html";
        }
        String username = xcUser.getUsername();
        return "redirect:http://www.51xuecheng.cn/sign.html?username=" + username + "&authType=wx";


    }


}
