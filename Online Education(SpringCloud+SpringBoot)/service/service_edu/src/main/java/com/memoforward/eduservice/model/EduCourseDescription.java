package com.memoforward.eduservice.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 课程简介
 * </p>
 *
 * @author memoforward
 * @since 2020-07-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EduCourseDescription implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 课程简介
     */
    private String description;

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
