package oah.project.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import oah.project.ucenter.feignclient.CheckCodeClient;
import oah.project.ucenter.mapper.XcUserMapper;
import oah.project.ucenter.model.dto.AuthParamsDto;
import oah.project.ucenter.model.dto.XcUserExt;
import oah.project.ucenter.model.po.XcUser;
import oah.project.ucenter.service.AuthService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @ClassName PasswordAuthServiceImpl
 * @Description 账号密码方式
 * @Author _oah
 * @Date 2023.12.25 17:16
 * @Version 1.0
 */
@Service("password_authservice")
public class PasswordAuthServiceImpl implements AuthService {


    @Autowired
    private XcUserMapper xcUserMapper;


    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private CheckCodeClient checkCodeClient;



    @Override
    public XcUserExt execute(AuthParamsDto authParamsDto) {
        // 账号
        String username = authParamsDto.getUsername();

        // 输入的验证码
        String checkcode = authParamsDto.getCheckcode();
        // 验证码对应的key
        String checkcodekey = authParamsDto.getCheckcodekey();

        if(StringUtils.isEmpty(checkcode) || StringUtils.isEmpty(checkcodekey)) {
            throw new RuntimeException("请输入验证码");
        }

        // 远程调用验证码服务接口取校验验证码
        Boolean verify = checkCodeClient.verify(checkcodekey, checkcode);
        if(verify == null || !verify) {
            throw new RuntimeException("验证码输入错误");
        }

        // 账号是否存在
        // 根据username账号查询数据库
        XcUser xcUser = xcUserMapper.selectOne(new LambdaQueryWrapper<XcUser>().eq(XcUser::getUsername, username));

        // 查到用户不存在，要返回null即可，spring security 框架抛出异常用户不存在
        if(xcUser == null) {
            throw new RuntimeException("账号不存在");
        }
        // 验证密码是否正确
        // 如果查到了用户拿到正确的密码，最终封装成一个UserDetails对象给spring security 框架返回，有框架进行密码比对
        String passwordDb = xcUser.getPassword();
        // 拿到用户输入的密码
        String passwordForm = authParamsDto.getPassword();
        // 校验密码
        boolean matches = passwordEncoder.matches(passwordForm, passwordDb);
        if(!matches) {
            throw new RuntimeException("账号密码错误");
        }

        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(xcUser, xcUserExt);

        return xcUserExt;
    }
}
