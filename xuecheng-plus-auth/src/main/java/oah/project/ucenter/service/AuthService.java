package oah.project.ucenter.service;

import oah.project.ucenter.model.dto.AuthParamsDto;
import oah.project.ucenter.model.dto.XcUserExt;

/**
 * @ClassName AuthService
 * @Description 统一的认证接口
 * @Author _oah
 * @Date 2023.12.25 17:13
 * @Version 1.0
 */
public interface AuthService {


    /**
     * 认证方法
     * @param authParamsDto
     * @return
     */
    XcUserExt execute(AuthParamsDto authParamsDto);
}
