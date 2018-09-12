package top.imyzt.shiro.dao;

import top.imyzt.shiro.vo.User;

import java.util.List;
import java.util.Set;

public interface UserDao {

    User getPasswordByUsername(String username);

    List<String> getRolesByUsername(String username);

    List<String> getPermissionByRole(String role);
}
