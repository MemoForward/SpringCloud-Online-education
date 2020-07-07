package com.memoforward.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.memoforward.commonutils.R;
import com.memoforward.eduservice.model.EduCourse;
import com.memoforward.eduservice.model.vo.CourseInfoVo;
import com.memoforward.eduservice.model.vo.CoursePublishVo;
import com.memoforward.eduservice.model.vo.CourseQuery;
import com.memoforward.eduservice.service.IEduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author memoforward
 * @since 2020-07-04
 */
@Api(description = "课程管理模块")
@RestController
@CrossOrigin
@RequestMapping("/eduservice/course")
public class EduCourseController {

    @Autowired
    IEduCourseService courseService;

    @ApiOperation("分页查询课程 + 条件")
    @PostMapping("/pageListCourses/{current}/{limit}")
    public R pageListCourses(@PathVariable Integer current,
                             @PathVariable Integer limit,
                             @RequestBody(required = false) CourseQuery courseQuery){
        Page<EduCourse> coursePage = new Page<>(current, limit);
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(courseQuery.getTitle())) wrapper.like("title", courseQuery.getTitle());
        if(!StringUtils.isEmpty(courseQuery.getStatus())) wrapper.eq("status", courseQuery.getStatus());
        wrapper.orderByDesc("gmt_create");
        courseService.page(coursePage, wrapper);
        List<EduCourse> courses = coursePage.getRecords();
        long total = coursePage.getTotal();
        return R.ok().data("courses", courses).data("total",total);
    }

    // 添加课程基本信息
    @ApiOperation("添加课程")
    @PostMapping("/addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        String id = courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId", id);
    }

    @ApiOperation("根据id查询课程信息")
    @GetMapping("/{courseId}/getCourseInfo")
    public R getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfo = courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfo", courseInfo);
    }

    @ApiOperation("修改课程信息")
    @PostMapping("/updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    @ApiOperation("根据id查询课程确认信息")
    @GetMapping("/{courseId}/getPublishCourseInfo")
    public R getPublishCourseInfo(@PathVariable String courseId){
        CoursePublishVo coursePublish = courseService.publishCourseInfo(courseId);
        return R.ok().data("publishCourse", coursePublish);
    }

    @ApiOperation("发布课程")
    @GetMapping("/{courseId}/publishCourse")
    public R publishCourse(@PathVariable String courseId){
        EduCourse course = new EduCourse();
        course.setStatus("Normal");
        course.setId(courseId);
        courseService.updateById(course);
        return R.ok();
    }

    @ApiOperation("删除课程")
    @DeleteMapping("/{courseId}/removeCourse")
    public R removeCourse(@PathVariable String courseId){
        // 所有与该课程相关的内容都会被删除
        courseService.removeCourseById(courseId);
        return R.ok();
    }
}
