package oah.project.ucenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import oah.project.ucenter.mapper.XcMenuMapper;
import oah.project.ucenter.mapper.XcUserMapper;
import oah.project.ucenter.model.dto.AuthParamsDto;
import oah.project.ucenter.model.dto.XcUserExt;
import oah.project.ucenter.model.po.XcMenu;
import oah.project.ucenter.model.po.XcUser;
import oah.project.ucenter.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author _oah
 * @Date 2023.12.25 16:06
 * @Version 1.0
 */
@Component
@Slf4j
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private XcUserMapper xcUserMapper;

    @Autowired
    private XcMenuMapper xcMenuMapper;


    @Autowired
    private ApplicationContext applicationContext;


    // 传入的请求认证的参数就是AuthParamsDto

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // 将传入的json转成AuthParamsDto对象
        AuthParamsDto authParamsDto = null;

        try {
            authParamsDto = JSON.parseObject(s, AuthParamsDto.class);
        } catch (Exception e) {
            throw new RuntimeException("请求认证参数不符合要求");
        }

        // 认证类型，有password，wx
        String authType = authParamsDto.getAuthType();
        String beanName = authType + "_authservice";
        // 根据认证类型，从spring容器取出指定的Bean
        AuthService authService = applicationContext.getBean(beanName, AuthService.class);
        // 调用统一execute方法完成认证
        XcUserExt xcUserExt = authService.execute(authParamsDto);
        // 封装xcUserExt用户信息为UserDetails
        // 根据UserDetails对象生成令牌
        UserDetails userPrincipal = getUserPrincipal(xcUserExt);

        return userPrincipal;
    }

    /**
     * 查询用户信息
     * @param xcUser
     * @return
     */
    public UserDetails getUserPrincipal(XcUserExt xcUser) {
        String password = xcUser.getPassword();
        // 权限
        String[] authorities = {"test"};
        // 根据用户id查询用户的权限
        List<XcMenu> xcMenus = xcMenuMapper.selectPermissionByUserId(xcUser.getId());
        if(xcMenus.size() > 0) {
            List<String> permissions = new ArrayList<>();
            xcMenus.forEach(m->{
                // 拿到了用户拥有的权限标识符
                permissions.add(m.getCode());
            });
            // 将permission转成数组
            authorities = permissions.toArray(new String[0]);
        }
        xcUser.setPassword(null);
        // 将用户信息转为json
        String userJson = JSON.toJSONString(xcUser);
        UserDetails userDetails = User.withUsername(userJson).password(password).authorities(authorities).build();
        return userDetails;
    }
}
