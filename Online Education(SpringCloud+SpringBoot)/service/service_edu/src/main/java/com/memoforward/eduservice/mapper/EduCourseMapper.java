package com.memoforward.eduservice.mapper;

import com.memoforward.eduservice.model.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.memoforward.eduservice.model.vo.CoursePublishVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author memoforward
 * @since 2020-07-04
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    CoursePublishVo getPublishCourseInfo(@Param("courseId") String courseId);

}
