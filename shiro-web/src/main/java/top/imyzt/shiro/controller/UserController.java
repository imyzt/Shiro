package top.imyzt.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.imyzt.shiro.vo.User;

@Controller
public class UserController {

    @PostMapping(value = "/subLogin", produces = "application/json;charset=utf8")
    public @ResponseBody String subLogin(User user) {

        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(user.getUname(), user.getUpass());

        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return e.getMessage();
        }

        if (subject.hasRole("admin")) {
            return "是admin角色";
        }

        subject.checkPermission("delUser");
        return "无admin角色";
    }

//    @RequiresRoles("admin")
    @GetMapping("testRoleAdmin")
    public @ResponseBody String testRoleAdmin() {
        return "testRoleAdmin";
    }

    @RequiresPermissions("delUser")
//    @RequiresRoles("superAdmin")
    @GetMapping("testRoleSuperAdmin")
    public @ResponseBody String testRoleSuperAdmin() {
        return "testRoleSuperAdmin";
    }

}
