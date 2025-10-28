package com.supermap.modules.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supermap.common.pojo.R;
import com.supermap.common.valid.group.Add;
import com.supermap.common.valid.group.Update;
import com.supermap.modules.sys.dto.RoleDTO;
import com.supermap.modules.sys.dto.RoleSaveDTO;
import com.supermap.modules.sys.entity.RoleEntity;
import com.supermap.modules.sys.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 角色表
 *
 * @author gzw
 */
@Tag(name = "角色表")
@RestController
@RequestMapping("/sys/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(summary = "列表")
    @PostMapping("/list")
    public R<Page<RoleEntity>> list(@RequestBody RoleDTO dto) {
        Page<RoleEntity> page = roleService.queryPage(dto);
        return R.ok(page);
    }

    @Operation(summary = "信息")
    @GetMapping("/info/{roleId}")
    public R<RoleEntity> info(@PathVariable("roleId") Long roleId) {
        RoleEntity role = roleService.getById(roleId);
        return R.ok(role);
    }

    @Operation(summary = "保存")
    @PostMapping("/save")
    public R<Void> save(@RequestBody @Validated(Add.class) RoleSaveDTO dto) {
        roleService.saveDTO(dto);
        return R.ok();
    }

    @Operation(summary = "修改")
    @PutMapping("/update")
    public R<Void> update(@RequestBody @Validated(Update.class) RoleSaveDTO dto) {
        roleService.updateDTO(dto);
        return R.ok();
    }

    @Operation(summary = "删除")
    @PostMapping("/delete")
    public R<Void> delete(@RequestBody Long[] roleIds) {
        roleService.removeByIds(Arrays.asList(roleIds));
        return R.ok();
    }

}
