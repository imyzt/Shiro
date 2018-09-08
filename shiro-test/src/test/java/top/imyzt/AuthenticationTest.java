package top.imyzt;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by imyzt
 * 2018/9/8 10:19 <br>
 * shiro 认证和授权
 */
public class AuthenticationTest {

    // 构建realm环境
    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();

    @Before
    public void addUser() {
//        simpleAccountRealm.addAccount("yzt", "123");
        simpleAccountRealm.addAccount("yzt", "123", "admin", "superAdmin");
    }

    // 认证
    @Test
    public void testAuthentication() {

        // 构建securityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm);

        // 主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken account = new UsernamePasswordToken("yzt", "123");
        subject.login(account);

        System.out.println("是否认证成功: " + subject.isAuthenticated());

        subject.logout();
        System.out.println("是否认证成功: " + subject.isAuthenticated());
    }

    // 授权
    @Test
    public void testAuthorization() {

        // 构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm);

        // 主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        // 创建认证的token
        UsernamePasswordToken token = new UsernamePasswordToken("yzt", "123");
        subject.login(token);

        System.out.println("是否认证成功: " + subject.isAuthenticated());

        // 检查用户是否具备该角色
        subject.checkRole("admin");
        subject.checkRoles("superAdmin", "admin");

    }
}
