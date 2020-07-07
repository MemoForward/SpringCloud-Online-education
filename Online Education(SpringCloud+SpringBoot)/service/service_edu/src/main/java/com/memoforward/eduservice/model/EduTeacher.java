package com.memoforward.eduservice.model;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author memoforward
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EduTeacher implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 讲师ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 讲师姓名
     */
    private String name;

    /**
     * 讲师简介
     */
    private String intro;

    /**
     * 讲师资历
     */
    private String career;

    /**
     * 头衔 1高级 2首席
     */
    private Integer level;

    /**
     * 讲师头像
     */
    private String avatar;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 逻辑删除标志
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
