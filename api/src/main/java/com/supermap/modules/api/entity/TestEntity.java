package com.supermap.modules.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.List;

import com.supermap.common.type.JsonbTypeHandler;
import com.supermap.modules.api.vo.ObjVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * api测试
 *
 * @author gzw
 */
@Schema(title = "api测试")
@Data
@TableName(value = "api_test", autoResultMap = true)
public class TestEntity implements Serializable {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(title = "id")
    private Long id;

    @Schema(title = "名称")
    private String name;

    @Schema(title = "对象")
    @TableField(typeHandler = JsonbTypeHandler.class)
    private ObjVO obj;

    @Schema(title = "对象数组")
    @TableField(typeHandler = JsonbTypeHandler.class)
    private List<ObjVO> objArray;

}
