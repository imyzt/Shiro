package top.imyzt;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * Created by imyzt
 * 2018/9/8 11:00 <br>
 */
public class IniRealmTest {

    @Test
    public void testIniRealm() {

        IniRealm iniRealm = new IniRealm("classpath:user.ini");

        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(iniRealm);

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("yzt", "123");
        subject.login(token);

        System.out.println("是否认证通过: " + subject.isAuthenticated());

        // 检查用户是否具备 admin 角色
        subject.checkRole("admin");

        // 检查 用户 角色 是否具备 delUser 权限
        subject.checkPermission("delAllUser");
    }

}
