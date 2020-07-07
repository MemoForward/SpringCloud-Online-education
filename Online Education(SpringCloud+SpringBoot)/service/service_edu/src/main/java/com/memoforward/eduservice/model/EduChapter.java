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
 * 课程
 * </p>
 *
 * @author memoforward
 * @since 2020-07-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EduChapter implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 章节ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 课程ID
     */
    private String courseId;

    /**
     * 章节名称
     */
    private String title;

    /**
     * 显示排序
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
