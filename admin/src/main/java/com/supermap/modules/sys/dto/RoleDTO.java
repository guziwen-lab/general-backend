package com.supermap.modules.sys.dto;

import com.supermap.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 角色表
 *
 * @author gzw
 */
@EqualsAndHashCode(callSuper = true)
@Schema(title = "角色表")
@Data
public class RoleDTO extends PageParam {

    /**
     * 角色id
     */
    @Schema(title = "角色id")
    private Long roleId;
    /**
     * 角色名称
     */
    @Schema(title = "角色名称")
    private String roleName;
    /**
     * 备注
     */
    @Schema(title = "备注")
    private String remark;
    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @Schema(title = "更新时间")
    private Date updateTime;

}
