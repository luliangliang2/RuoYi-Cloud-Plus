package org.dromara.system.listener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.SpringUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.core.utils.ValidatorUtils;
import org.dromara.common.excel.core.ExcelListener;
import org.dromara.common.excel.core.ExcelResult;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.system.domain.bo.SysUserBo;
import org.dromara.system.domain.vo.SysPostVo;
import org.dromara.system.domain.vo.SysRoleVo;
import org.dromara.system.domain.vo.SysUserImportVo;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.service.ISysConfigService;
import org.dromara.system.service.ISysPostService;
import org.dromara.system.service.ISysRoleService;
import org.dromara.system.service.ISysUserService;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 系统用户自定义导入
 *
 * @author Lion Li
 */
@Slf4j
public class SysUserImportListener extends AnalysisEventListener<SysUserImportVo> implements ExcelListener<SysUserImportVo> {

    private final ISysUserService userService;

    private final String password;

    private final HashMap<String, SysPostVo> postVoHashMap;

    private final HashMap<String, SysRoleVo> roleVoHashMap;

    private final Boolean isUpdateSupport;

    private final Long operUserId;

    private int successNum = 0;
    private int failureNum = 0;
    private final StringBuilder successMsg = new StringBuilder();
    private final StringBuilder failureMsg = new StringBuilder();

    public SysUserImportListener(Boolean isUpdateSupport) {
        String initPassword = SpringUtils.getBean(ISysConfigService.class).selectConfigByKey("sys.user.initPassword");
        this.userService = SpringUtils.getBean(ISysUserService.class);
        this.password = BCrypt.hashpw(initPassword);
        this.isUpdateSupport = isUpdateSupport;
        this.operUserId = LoginHelper.getUserId();
        this.postVoHashMap = SpringUtils.getBean(ISysPostService.class).selectPostAll()
            .stream().collect(Collectors.toMap(SysPostVo::getPostCode,
                x -> x, (existing, replacement) -> existing, HashMap::new));
        this.roleVoHashMap = SpringUtils.getBean(ISysRoleService.class).selectRoleAll()
            .stream().collect(Collectors.toMap(SysRoleVo::getRoleKey,
                x -> x, (existing, replacement) -> existing, HashMap::new));
    }

    @Override
    public void invoke(SysUserImportVo userVo, AnalysisContext context) {
        SysUserVo sysUser = this.userService.selectUserByUserName(userVo.getUserName());
        try {
            // 验证是否存在这个用户
            if (ObjectUtil.isNull(sysUser)) {
                SysUserBo user = BeanUtil.toBean(userVo, SysUserBo.class);
                validateUserDetails(user, userVo);
                ValidatorUtils.validate(user);
                validEntityBeforeSave(user);
                user.setPassword(password);
                user.setCreateBy(operUserId);
                userService.insertUser(user);
                successNum++;
                successMsg.append("<br/>").append(successNum).append("、账号 ").append(user.getUserName()).append(" 导入成功");
            } else if (isUpdateSupport) {
                Long userId = sysUser.getUserId();
                SysUserBo user = BeanUtil.toBean(userVo, SysUserBo.class);
                user.setUserId(userId);
                validateUserDetails(user, userVo);
                ValidatorUtils.validate(user);
                validEntityBeforeSave(user);
                userService.checkUserAllowed(user.getUserId());
                userService.checkUserDataScope(user.getUserId());
                user.setUpdateBy(operUserId);
                userService.updateUser(user);
                successNum++;
                successMsg.append("<br/>").append(successNum).append("、账号 ").append(user.getUserName()).append(" 更新成功");
            } else {
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、账号 ").append(sysUser.getUserName()).append(" 已存在");
            }
        } catch (Exception e) {
            failureNum++;
            String msg = "<br/>" + failureNum + "、账号 " + userVo.getUserName() + " 导入失败：";
            failureMsg.append(msg).append(e.getMessage());
            log.error(msg, e);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    /**
     * 验证用户详细信息的完整性和有效性，并设置部门ID、岗位ID和角色ID列表
     *
     * @param user   要验证和设置的用户业务对象
     * @param userVo 用户导入视图对象，包含岗位编码和角色权限字符串
     * @throws ServiceException 如果岗位编码或角色权限字符串为空，或者存在无效岗位编码或角色权限字符串时抛出异常
     */
    private void validateUserDetails(SysUserBo user, SysUserImportVo userVo) {
        List<String> postCodes = StringUtils.splitList(userVo.getPostCode());
        List<String> roleKeys = StringUtils.splitList(userVo.getRoleKey());

        // 校验岗位编码和角色权限字符串是否为空
        if (CollUtil.isEmpty(postCodes)) {
            throw new ServiceException("岗位编码为空");
        }
        if (CollUtil.isEmpty(roleKeys)) {
            throw new ServiceException("权限字符为空");
        }

        // 获取部门ID并校验岗位信息
        Long deptId = null;
        for (String code : postCodes) {
            SysPostVo postVo = postVoHashMap.get(code);
            if (ObjectUtil.isNull(postVo)) {
                throw new ServiceException("无效岗位编码: " + code);
            }
            // 设置部门ID
            if (deptId == null) {
                deptId = postVo.getDeptId();
            } else {
                // 校验岗位是否跨部门
                if (!Objects.equals(deptId, postVo.getDeptId())) {
                    throw new ServiceException("岗位禁止跨部门");
                }
            }
        }

        // 获取角色信息并校验
        Long[] roleIds = roleKeys.stream()
            .map(x -> {
                SysRoleVo roleVo = roleVoHashMap.get(x);
                if (ObjectUtil.isNull(roleVo)) {
                    throw new ServiceException("无效权限字符: " + x);
                }
                return roleVo.getRoleId();
            }).toArray(Long[]::new);

        // 设置用户对象的部门ID和岗位ID列表
        user.setDeptId(deptId);
        user.setPostIds(postCodes.stream()
            .map(code -> postVoHashMap.get(code).getPostId())
            .toArray(Long[]::new));
        user.setRoleIds(roleIds);
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysUserBo user) {
        if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            throw new ServiceException("[" + user.getPhonenumber() + "] 手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            throw new ServiceException("[" + user.getEmail() + "] 邮箱账号已存在");
        }
    }

    @Override
    public ExcelResult<SysUserImportVo> getExcelResult() {
        return new ExcelResult<SysUserImportVo>() {

            @Override
            public String getAnalysis() {
                if (failureNum > 0) {
                    failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
                    throw new ServiceException(failureMsg.toString());
                } else {
                    successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
                }
                return successMsg.toString();
            }

            @Override
            public List<SysUserImportVo> getList() {
                return null;
            }

            @Override
            public List<String> getErrorList() {
                return null;
            }
        };
    }
}
