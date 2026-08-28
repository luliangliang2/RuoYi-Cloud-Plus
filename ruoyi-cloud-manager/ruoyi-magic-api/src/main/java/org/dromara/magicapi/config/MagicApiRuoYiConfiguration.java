package org.dromara.magicapi.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.jwt.SaJwtUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.service.PermissionService;
import org.dromara.common.core.utils.SpringUtils;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.common.tenant.helper.TenantHelper;
import org.ssssssss.magicapi.core.annotation.MagicModule;
import org.ssssssss.magicapi.core.context.MagicUser;
import org.ssssssss.magicapi.core.exception.MagicLoginException;
import org.ssssssss.magicapi.core.interceptor.Authorization;
import org.ssssssss.magicapi.core.interceptor.AuthorizationInterceptor;
import org.ssssssss.magicapi.core.servlet.MagicHttpServletRequest;
import org.ssssssss.magicapi.modules.DynamicModule;
import org.ssssssss.script.MagicScriptContext;
import org.ssssssss.script.annotation.Comment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Magic API 与 RuoYi 登录态、租户和 Spring 容器集成。
 *
 * @author ruoyi
 */
@Configuration
public class MagicApiRuoYiConfiguration {

    @Bean
    public AuthorizationInterceptor ruoyiMagicAuthorizationInterceptor() {
        return new RuoYiMagicAuthorizationInterceptor();
    }

    @Bean
    public RuoYiMagicModule ruoyiMagicModule() {
        return new RuoYiMagicModule();
    }

    public static class RuoYiMagicAuthorizationInterceptor implements AuthorizationInterceptor {

        private static final Pattern JWT_PATTERN = Pattern.compile("eyJ[^,\\s]*\\.[^,\\s]*\\.[^,\\s]*");

        @Override
        public boolean requireLogin() {
            return true;
        }

        @Override
        public MagicUser getUserByToken(String token) throws MagicLoginException {
            String tokenValue = normalizeToken(token);
            if (StrUtil.isBlank(tokenValue)) {
                throw new MagicLoginException("未能读取到有效 token");
            }
            if (StpUtil.getLoginIdByToken(tokenValue) == null) {
                throw new MagicLoginException("token 无效或已过期");
            }
            JSONObject payloads = getJwtPayloads(tokenValue);
            Object loginId = payloads.get(LoginHelper.USER_KEY);
            String username = payloads.getStr(LoginHelper.USER_NAME_KEY);
            if (loginId == null || StrUtil.isBlank(username)) {
                throw new MagicLoginException("token 无效");
            }
            StpUtil.setTokenValue(tokenValue);
            return new MagicUser(Convert.toStr(loginId), username, tokenValue);
        }

        @Override
        public boolean allowVisit(MagicUser user, MagicHttpServletRequest request, Authorization authorization) {
            return checkPermission(authorization);
        }

        private boolean checkPermission(Authorization authorization) {
            if (LoginHelper.isSuperAdmin()) {
                return true;
            }
            return switch (authorization) {
                case SAVE, LOCK, UNLOCK, PUSH, RELOAD -> hasPermission("tool:magic:save");
                case DELETE -> hasPermission("tool:magic:remove");
                case DOWNLOAD -> hasPermission("tool:magic:download");
                case UPLOAD -> hasPermission("tool:magic:upload");
                case VIEW, NONE -> hasAnyPermission("tool:magic:list", "tool:magic:view");
            };
        }

        private boolean hasPermission(String permission) {
            return getPermissions().contains(permission);
        }

        private boolean hasAnyPermission(String... permissions) {
            Set<String> userPermissions = getPermissions();
            for (String permission : permissions) {
                if (userPermissions.contains(permission)) {
                    return true;
                }
            }
            return false;
        }

        private Set<String> getPermissions() {
            Set<String> permissions = new HashSet<>();
            var loginUser = LoginHelper.getLoginUser();
            if (loginUser != null && loginUser.getMenuPermission() != null) {
                permissions.addAll(loginUser.getMenuPermission());
            }
            try {
                permissions.addAll(StpUtil.getPermissionList());
            } catch (NotLoginException ignored) {
                // Authorization 已在 getUserByToken 中解析，权限兜底走系统权限服务。
            }
            if (permissions.isEmpty()) {
                PermissionService permissionService = SpringUtils.getBean(PermissionService.class);
                Long userId = getUserId();
                if (userId != null) {
                    permissions.addAll(permissionService.getMenuPermission(userId));
                }
            }
            return permissions;
        }

        private Long getUserId() {
            Long userId = LoginHelper.getUserId();
            if (userId != null) {
                return userId;
            }
            String tokenValue = normalizeToken(StpUtil.getTokenValue());
            if (StrUtil.isBlank(tokenValue)) {
                return null;
            }
            return Convert.toLong(getJwtPayloads(tokenValue).get(LoginHelper.USER_KEY));
        }

        private JSONObject getJwtPayloads(String tokenValue) {
            return SaJwtUtil.getPayloadsNotCheck(tokenValue, StpUtil.getLoginType(), SaManager.getConfig().getJwtSecretKey());
        }

        private String normalizeToken(String token) {
            if (StrUtil.isBlank(token)) {
                return token;
            }
            token = URLDecoder.decode(token.trim(), StandardCharsets.UTF_8);
            token = StrUtil.removePrefixIgnoreCase(token, "Bearer ");
            Matcher matcher = JWT_PATTERN.matcher(token);
            if (matcher.find()) {
                return matcher.group();
            }
            return token;
        }
    }

    @MagicModule("ruoyi")
    public static class RuoYiMagicModule implements DynamicModule<RuoYiMagicModule> {

        @Override
        public RuoYiMagicModule getDynamicModule(MagicScriptContext context) {
            return this;
        }

        @Comment("获取当前登录用户ID")
        public Long userId() {
            return LoginHelper.getUserId();
        }

        @Comment("获取当前登录用户名")
        public String username() {
            return LoginHelper.getUsername();
        }

        @Comment("获取当前租户ID")
        public String tenantId() {
            return TenantHelper.getTenantId();
        }

        @Comment("判断当前用户是否超级管理员")
        public boolean superAdmin() {
            return LoginHelper.isSuperAdmin();
        }

        @Comment("检查当前用户是否拥有权限")
        public boolean hasPermission(@Comment(name = "permission", value = "权限标识") String permission) {
            return StpUtil.hasPermission(permission);
        }

        @Comment("获取Spring Bean")
        public Object bean(@Comment(name = "name", value = "Bean名称") String name) {
            return SpringUtils.getBean(name);
        }

        @Comment("获取Spring Bean")
        public Object bean(@Comment(name = "type", value = "Bean类型") Class<?> type) {
            return SpringUtils.getBean(type);
        }

        @Comment("构造RuoYi成功响应")
        public R<Object> ok(@Comment(name = "data", value = "响应数据") Object data) {
            return R.ok(data);
        }

        @Comment("构造RuoYi失败响应")
        public R<Object> fail(@Comment(name = "msg", value = "错误消息") String msg) {
            return R.fail(msg);
        }

        @Comment("忽略租户执行")
        public Object ignoreTenant(@Comment(name = "function", value = "回调函数") java.util.function.Function<Object[], Object> function) {
            return TenantHelper.ignore(() -> function.apply(new Object[0]));
        }

        @Comment("动态租户执行")
        public Object dynamicTenant(
            @Comment(name = "tenantId", value = "租户ID") String tenantId,
            @Comment(name = "function", value = "回调函数") java.util.function.Function<Object[], Object> function) {
            return TenantHelper.dynamic(tenantId, () -> function.apply(new Object[0]));
        }

        @Comment("获取当前登录用户摘要")
        public Map<String, Object> user() {
            return Map.of(
            		LoginHelper.USER_KEY, Convert.toStr(LoginHelper.getUserId(), ""),
            		LoginHelper.USER_NAME_KEY, Convert.toStr(LoginHelper.getUsername(), ""),
            		LoginHelper.TENANT_KEY, Convert.toStr(TenantHelper.getTenantId(), ""),
            		LoginHelper.DEPT_KEY, Convert.toStr(LoginHelper.getDeptId(), "")
            );
        }
    }
}
