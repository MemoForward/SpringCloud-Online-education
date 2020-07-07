package com.memoforward.eduservice.service;

import com.memoforward.eduservice.model.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.memoforward.eduservice.model.vo.CourseInfoVo;
import com.memoforward.eduservice.model.vo.CoursePublishVo;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author memoforward
 * @since 2020-07-04
 */
public interface IEduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVo courseInfoVo);

    CourseInfoVo getCourseInfo(String courseId);

    void updateCourseInfo(CourseInfoVo courseInfoVo);

    CoursePublishVo publishCourseInfo(String courseId);

    void removeCourseById(String courseId);
}
