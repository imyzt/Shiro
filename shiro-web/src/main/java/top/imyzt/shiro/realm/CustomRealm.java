package top.imyzt.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import top.imyzt.shiro.dao.UserDao;
import top.imyzt.shiro.vo.User;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by imyzt
 * 2018/9/8 12:36 <br>
 * 自定义realm
 */
public class CustomRealm extends AuthorizingRealm {

    @Resource
    private UserDao userDao;

    /**
     * 自定义授权
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        String username = (String) principals.getPrimaryPrincipal();

        Set<String> role = getRolesByUsername(username);

        Set<String> permission = getPermissionByRole(role);

        SimpleAuthorizationInfo simpleAuthorizationInfo =
                new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(role);
        simpleAuthorizationInfo.setStringPermissions(permission);
        return simpleAuthorizationInfo;
    }

    /**
     * 从数据库获取权限信息
     */
    private HashSet<String> getPermissionByRole(Set<String> roles) {
        List<String> permissionByRole = new ArrayList<>();
        roles.forEach(role -> permissionByRole.addAll(userDao.getPermissionByRole(role)));
        return new HashSet<>(permissionByRole);
    }

    /**
     * 从数据库获取角色信息
     */
    private Set<String> getRolesByUsername(String username) {

        List<String> roles = userDao.getRolesByUsername(username);
        return new HashSet<>(roles);
    }

    /**
     * 自定义认证
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String username = (String) token.getPrincipal();

        String password = getPasswordByUsername(username);

        if (null != password) {
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, password, "customRealm");
            // 加盐
            simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("yyy"));
            return simpleAuthenticationInfo;
        }
        return null;
    }

    /**
     * 从数据库获取凭证
     */
    private String getPasswordByUsername(String username) {

        User user = userDao.getPasswordByUsername(username);

        if (user != null) {
            return user.getUpass();
        }

        return null;
    }
}

