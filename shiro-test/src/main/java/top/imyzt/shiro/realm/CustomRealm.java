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

import java.util.*;

/**
 * Created by imyzt
 * 2018/9/8 12:36 <br>
 * 自定义realm
 */
public class CustomRealm extends AuthorizingRealm {

    Map<String, String> users = new HashMap<String, String>();
    Map<String, String> user_roles = new HashMap<String, String>();
    Map<String, String> role_permission = new HashMap<String, String>();

    {
        users.put("y", "527c6edfbe919bdffffebb1f27d541ab");

        user_roles.put("y", "admin");

        role_permission.put("admin", "addUser");

        super.setName("customRealm");
    }

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
     * 模拟,只有一个权限
     * @param role
     */
    private HashSet<String> getPermissionByRole(Set<String> role) {
        Iterator<String> iterator = role.iterator();
        HashSet<String> set = new HashSet<>(1);
        set.add(role_permission.get(iterator.next()));
        return set;
    }

    /**
     * 模拟,只有一个角色
     */
    private Set<String> getRolesByUsername(String username) {
        Set<String> role = new HashSet<>(1);
        role.add(user_roles.get(username));
        return role;
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
     * 模拟从数据库获取凭证
     */
    private String getPasswordByUsername(String username) {
        return users.get(username);
    }
}
