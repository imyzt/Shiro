package top.imyzt;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import javax.sql.DataSource;

/**
 * Created by imyzt
 * 2018/9/8 11:16 <br>
 */
public class JdbcRealm {

    DruidDataSource datasource = new DruidDataSource();

    {
        datasource.setUrl("jdbc:mysql://localhost:3306/study_shiro");
        datasource.setUsername("root");
        datasource.setPassword("root");
    }

    @Test
    public void testJdbcRealm() {

        org.apache.shiro.realm.jdbc.JdbcRealm jdbcRealm = new org.apache.shiro.realm.jdbc.JdbcRealm();
        jdbcRealm.setDataSource(datasource);

        // 默认不开启验证权限.必须手动开启
        jdbcRealm.setPermissionsLookupEnabled(true);

        // 自定义认证SQL
        String sql = "select password from test_users where username = ?";
        jdbcRealm.setAuthenticationQuery(sql);

        String rolesSql = "select role_name from test_user_roles where username = ?";
        jdbcRealm.setUserRolesQuery(rolesSql);

        String permissionSql = "select permission from test_roles_permissions where role_name = ?";
        jdbcRealm.setPermissionsQuery(permissionSql);

        // 构建securityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        // 主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("y", "1");
        subject.login(token);

        System.out.println("是否认证成功: " + subject.isAuthenticated());

        // 检查是否具备 admin 角色
        subject.checkRole("admin");

        // 检查该用户 角色 是否具有 delUser 权限
        subject.checkPermission("delUser");

    }


}
