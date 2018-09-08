package top.imyzt;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import top.imyzt.shiro.realm.CustomRealm;

/**
 * Created by imyzt
 * 2018/9/8 12:44 <br>
 * 自定义realm验证
 */
public class CustomRealmTest {

    @Test
    public void testCustomRealm() {

        CustomRealm customRealm = new CustomRealm();

        // MD5 + Salt
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 加密算法
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        // 加密次数
        hashedCredentialsMatcher.setHashIterations(1);
        customRealm.setCredentialsMatcher(hashedCredentialsMatcher);

        DefaultSecurityManager defaultSecurityManager =
                new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("y", "1");
        subject.login(token);

        System.out.println("是否验证通过: " + subject.isAuthenticated());

        // 自定义Realm的验证角色和权限信息
        subject.checkRole("admin");
        subject.checkPermission("addUser");
    }

    @Test
    public void md5(){
        Md5Hash md5Hash = new Md5Hash("1", "yyy");
        System.out.println(md5Hash.toString());
    }
}
