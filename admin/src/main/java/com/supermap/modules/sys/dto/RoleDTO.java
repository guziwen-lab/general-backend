package com.supermap.modules.sys.dto;

import com.supermap.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
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

    @Schema(title = "开始时间")
    private Timestamp startTime;

    @Schema(title = "结束时间")
    private Timestamp endTime;

    @Schema(title = "关键词")
    private String keyword;

}
