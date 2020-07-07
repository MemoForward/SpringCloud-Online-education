package com.memoforward.eduservice.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;

import java.lang.reflect.Field;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 课程科目
 * </p>
 *
 * @author memoforward
 * @since 2020-07-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EduSubject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程类别ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 类别名称
     */
    private String title;

    /**
     * 父ID
     */
    private String parentId;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
