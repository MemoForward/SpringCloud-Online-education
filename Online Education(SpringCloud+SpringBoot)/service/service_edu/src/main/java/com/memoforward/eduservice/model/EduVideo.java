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
 * 课程视频
 * </p>
 *
 * @author memoforward
 * @since 2020-07-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EduVideo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 视频ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 课程ID
     */
    private String courseId;

    /**
     * 章节ID
     */
    private String chapterId;

    /**
     * 节点名称
     */
    private String title;

    /**
     * 云端视频资源
     */
    private String videoSourceId;

    /**
     * 原始文件名称
     */
    private String videoOriginalName;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 播放次数
     */
    private Long playCount;

    /**
     * 是否可以试听：0收费 1免费
     */
    private Integer isFree;

    /**
     * 视频时长（秒）
     */
    private Float duration;

    /**
     * Empty未上传 Transcoding转码中  Normal正常
     */
    private String status;

    /**
     * 视频源文件大小（字节）
     */
    private Long size;

    /**
     * 乐观锁
     */
    private Long version;

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
