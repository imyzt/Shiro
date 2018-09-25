package top.imyzt.shiro.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import top.imyzt.shiro.dao.UserDao;
import top.imyzt.shiro.vo.User;

import javax.annotation.Resource;
import java.util.*;


@Component
public class UserDaoImpl implements UserDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public User getPasswordByUsername(String username) {
        String sql = "select username, password from users where username = ?";
        List<User> query = jdbcTemplate.query(sql, new String[]{username}, ((resultSet, i) -> {
            User user = new User();
            user.setUname(resultSet.getString("username"));
            user.setUpass(resultSet.getString("password"));
            return user;
        }));
        if (CollectionUtils.isEmpty(query)) {
            return null;
        }
        return query.get(0);
    }

    @Override
    public List<String> getRolesByUsername(String username) {
        String sql = "select role_name from user_roles where username = ?";
        return jdbcTemplate.query(sql,
                new String[]{username},
                (resultSet, i) -> resultSet.getString("role_name"));
    }

    @Override
    public List<String> getPermissionByRole(String role) {
        String sql = "SELECT permission FROM roles_permissions WHERE role_name = ?";
        return jdbcTemplate.query(sql, new String[]{role}, (resultSet, i) -> resultSet.getString("permission"));
    }
}
