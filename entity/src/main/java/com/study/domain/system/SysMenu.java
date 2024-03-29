package com.study.domain.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.study.domain.base.BaseEntity;
import com.study.domain.base.MyTreeNote;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Objects;

@Data
@ApiModel(description = "菜单")
@TableName("sys_menu")
public class SysMenu extends BaseEntity implements MyTreeNote<SysMenu> {

    private static final long serialVersionUID = 1L;

    public SysMenu(Long parentId) {
        this.parentId = parentId;
    }

    @ApiModelProperty(value = "所属上级")
    @TableField("parent_id")
    private Long parentId;

    @ApiModelProperty(value = "名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "类型(1:菜单,2:按钮)")
    @TableField("type")
    private Integer type;

    @ApiModelProperty(value = "路由地址")
    @TableField("path")
    private String path;

    @ApiModelProperty(value = "组件路径")
    @TableField("component")
    private String component;

    @ApiModelProperty(value = "权限标识")
    @TableField("perms")
    private String perms;

    @ApiModelProperty(value = "图标")
    @TableField("icon")
    private String icon;

    @ApiModelProperty(value = "排序")
    @TableField("sort_value")
    private Integer sortValue;

    @ApiModelProperty(value = "状态(0:禁止,1:正常)")
    @TableField("status")
    private Integer status;

    // 下级列表
    @TableField(exist = false)
    private List<SysMenu> children;
    //是否选中
    @TableField(exist = false)
    private boolean isSelect;

    @Override
    public SysMenu parent() {
        return parentId == 0L ? null : new SysMenu(parentId);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof SysMenu && Objects.equals(getId(), ((SysMenu) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

