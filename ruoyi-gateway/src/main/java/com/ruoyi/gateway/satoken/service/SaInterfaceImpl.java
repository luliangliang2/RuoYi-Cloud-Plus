package com.ruoyi.gateway.satoken.service;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SaInterfaceImpl implements StpInterface {

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
//        LoginUser loginUser = LoginHelper.getLoginUser();
//        UserType userType = UserType.getUserType(loginUser.getUserType());
//        if (userType == UserType.SYS_USER) {
//            return new ArrayList<>(loginUser.getPermissions());
//        } else if (userType == UserType.APP_USER) {
//            // app端权限返回 自行根据业务编写
//        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
//        LoginUser loginUser = LoginHelper.getLoginUser();
//        UserType userType = UserType.getUserType(loginUser.getUserType());
//        if (userType == UserType.SYS_USER) {
//            return new ArrayList<>(loginUser.getRoles());
//        } else if (userType == UserType.APP_USER) {
//            // app端权限返回 自行根据业务编写
//        }
        return new ArrayList<>();
    }
}
