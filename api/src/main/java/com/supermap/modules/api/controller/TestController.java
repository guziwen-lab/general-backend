package com.supermap.modules.api.controller;

import java.util.Arrays;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supermap.shiro.LoginUser;
import com.supermap.shiro.LoginUserContextHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.supermap.common.valid.group.Add;
import com.supermap.common.valid.group.Update;
import com.supermap.common.pojo.R;
import com.supermap.modules.api.dto.TestDTO;
import com.supermap.modules.api.dto.TestSaveDTO;
import com.supermap.modules.api.entity.TestEntity;
import com.supermap.modules.api.service.TestService;

/**
 * api测试
 *
 * @author gzw
 */
@Tag(name = "api测试")
@RestController
@RequestMapping("/api/test")
@AllArgsConstructor
public class TestController {

    private final TestService testService;

    @Operation(summary = "分页查询")
    @PostMapping("/page")
    @RequiresGuest
    public R<Page<TestEntity>> page(@RequestBody TestDTO dto) {
        Page<TestEntity> page = testService.queryPage(dto);
        return R.ok(page);
    }

    @Operation(summary = "根据主键查询")
    @GetMapping("/info/{id}")
    public R<TestEntity> info(@PathVariable("id") Long id) {
        LoginUser loginUser = LoginUserContextHandler.getLoginUser();
        System.out.println(loginUser.getUsername());
        TestEntity test = testService.getById(id);
        return R.ok(test);
    }

    @Operation(summary = "保存")
    @PostMapping("/save")
    public R<Long> save(@RequestBody @Validated(Add.class) TestSaveDTO dto) {
        Long id = testService.saveDTO(dto);
        return R.ok(id);
    }

    @Operation(summary = "修改")
    @PutMapping("/update")
    public R<Void> update(@RequestBody @Validated(Update.class) TestSaveDTO dto) {
        testService.updateDTOById(dto);
        return R.ok();
    }

    @Operation(summary = "删除")
    @PostMapping("/delete")
    public R<Void> delete(@RequestBody Long[] ids) {
        testService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
