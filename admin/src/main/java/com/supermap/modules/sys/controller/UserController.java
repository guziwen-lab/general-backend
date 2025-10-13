package com.supermap.modules.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supermap.common.pojo.R;
import com.supermap.common.valid.group.Add;
import com.supermap.common.valid.group.Update;
import com.supermap.modules.sys.dto.UserDTO;
import com.supermap.modules.sys.dto.UserSaveDTO;
import com.supermap.modules.sys.entity.UserEntity;
import com.supermap.modules.sys.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 用户表
 *
 * @author gzw
 */
@Tag(name = "用户表")
@RestController
@RequestMapping("/sys/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequiresPermissions("sys:user:select")
    @Operation(summary = "分页查询用户")
    @PostMapping("/list")
    public R<Page<UserEntity>> list(@RequestBody UserDTO dto) {
        Page<UserEntity> page = userService.queryPage(dto);
        return R.ok(page);
    }

    @Operation(summary = "信息")
    @GetMapping("/info/{userId}")
    public R<UserEntity> info(@PathVariable("userId") Long userId) {
        UserEntity user = userService.getById(userId);
        return R.ok(user);
    }

    @Operation(summary = "保存")
    @PostMapping("/save")
    @RequiresPermissions("sys:user:save")
    public R<Void> save(@RequestBody @Validated(Add.class) UserSaveDTO dto) {
        userService.saveDTO(dto);
        return R.ok();
    }

    @Operation(summary = "修改登录用户的信息")
    @PutMapping("/update")
    public R<Void> update(@RequestBody @Validated(Update.class) UserSaveDTO dto) {
        userService.updateLoginUser(dto);
        return R.ok();
    }

    @Operation(summary = "重置用户密码")
    @PutMapping("/reset")
    @RequiresRoles(value = "admin")
    public R<Void> reset(@RequestParam String username) {
        userService.reset(username);
        return R.ok();
    }

    @Operation(summary = "重置全部用户密码")
    @PutMapping("/reset-all")
    @RequiresRoles(value = "admin")
    public R<Void> resetAll() {
        userService.resetAll();
        return R.ok();
    }

    @Operation(summary = "删除")
    @PostMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    public R<Void> delete(@RequestBody Long[] userIds) {
        userService.removeByIds(Arrays.asList(userIds));
        return R.ok();
    }

}
