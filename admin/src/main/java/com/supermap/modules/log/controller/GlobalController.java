package com.supermap.modules.log.controller;

import java.util.Arrays;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.supermap.common.valid.group.Add;
import com.supermap.common.valid.group.Update;
import com.supermap.common.pojo.R;
import com.supermap.modules.log.dto.GlobalDTO;
import com.supermap.modules.log.dto.GlobalSaveDTO;
import com.supermap.modules.log.entity.GlobalEntity;
import com.supermap.modules.log.service.GlobalService;

/**
 * 全局日志表
 *
 * @author gzw
 */
@Tag(name = "全局日志表")
@RestController
@RequestMapping("/log/global")
public class GlobalController {

    private final GlobalService globalService;

    public GlobalController(GlobalService globalService) {
        this.globalService = globalService;
    }

    @Operation(summary = "分页查询")
    @PostMapping("/page")
    public R<Page<GlobalEntity>> page(@RequestBody GlobalDTO dto) {
        Page<GlobalEntity> page = globalService.queryPage(dto);
        return R.ok(page);
    }

    @Operation(summary = "根据主键查询")
    @GetMapping("/info/{id}")
    public R<GlobalEntity> info(@PathVariable("id") Long id) {
        GlobalEntity global = globalService.getById(id);
        return R.ok(global);
    }

    @Operation(summary = "保存")
    @PostMapping("/save")
    public R<Long> save(@RequestBody @Validated(Add.class) GlobalSaveDTO dto) {
        Long id = globalService.saveDTO(dto);
        return R.ok(id);
    }

    @Operation(summary = "修改")
    @PutMapping("/update")
    public R<Void> update(@RequestBody @Validated(Update.class) GlobalSaveDTO dto) {
        globalService.updateDTOById(dto);
        return R.ok();
    }

    @Operation(summary = "删除")
    @PostMapping("/delete")
    public R<Void> delete(@RequestBody Long[] ids) {
        globalService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
