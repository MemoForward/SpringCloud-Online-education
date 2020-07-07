package com.memoforward.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.memoforward.eduservice.client.VodClient;
import com.memoforward.eduservice.model.EduCourse;
import com.memoforward.eduservice.mapper.EduCourseMapper;
import com.memoforward.eduservice.model.EduCourseDescription;
import com.memoforward.eduservice.model.EduVideo;
import com.memoforward.eduservice.model.vo.CourseInfoVo;
import com.memoforward.eduservice.model.vo.CoursePublishVo;
import com.memoforward.eduservice.service.IEduChapterService;
import com.memoforward.eduservice.service.IEduCourseDescriptionService;
import com.memoforward.eduservice.service.IEduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.memoforward.eduservice.service.IEduVideoService;
import com.memoforward.servicebase.exceptionhandler.MyException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author memoforward
 * @since 2020-07-04
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements IEduCourseService {

    @Autowired
    EduCourseMapper courseMapper;

    @Autowired
    IEduCourseDescriptionService courseDescriptionService;

    @Autowired
    IEduVideoService videoService;

    @Autowired
    IEduChapterService chapterService;

    @Autowired
    VodClient vodClient;

    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        // 1. 像课程表添加课程基本信息
        // courseInfoVo --> educourse
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
//        System.out.println(eduCourse);
        int insert = courseMapper.insert(eduCourse);
        if (insert <= 0) throw new MyException(20001, "添加课程失败！");
        // mp 自动生成id 并set进对象，然后进行insert！！
        String cid = eduCourse.getId();
        // 2. 向课程简介表添加数据
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setId(cid);
        courseDescription.setDescription(courseInfoVo.getDescription());
        courseDescriptionService.save(courseDescription);
        return cid;
    }

    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        EduCourse course = courseMapper.selectById(courseId);
        EduCourseDescription courseDescription = courseDescriptionService.getById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(course, courseInfoVo);
        courseInfoVo.setDescription(courseDescription.getDescription());
        return courseInfoVo;
    }

    @Override
    @Transactional
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        // 1. 修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int update = courseMapper.updateById(eduCourse);
        if (update == 0) throw new MyException(20001, "修改课程信息失败！");

        // 修改描述表
        EduCourseDescription courseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVo, courseDescription);
        boolean u = courseDescriptionService.updateById(courseDescription);
        if (!u) throw new MyException(20001, "修改课程信息失败");
    }

    @Override
    public CoursePublishVo publishCourseInfo(String courseId) {
        CoursePublishVo courseInfo = courseMapper.getPublishCourseInfo(courseId);
        return courseInfo;
    }

    @Override
    @Transactional
    public void removeCourseById(String courseId) {
        // 0. 删视频
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        wrapper.select("video_source_id");
        List<EduVideo> videos = videoService.list(wrapper);
        List<String> aliyunVideoIds = new ArrayList<>();
        for (EduVideo video : videos) {
            if (!StringUtils.isEmpty(video.getVideoSourceId())) {
                aliyunVideoIds.add(video.getVideoSourceId());
            }
        }
        if (aliyunVideoIds.size() > 0) {
            vodClient.removeAliyunVideos(aliyunVideoIds);
        }
        // 1. 删小节
        videoService.removeByCourseId(courseId);

        // 2. 删章节
        chapterService.removeByCourseId(courseId);

        // 3. 删课程和简介
        courseDescriptionService.removeById(courseId);
//        int a = 1/0;
        int delete = courseMapper.deleteById(courseId);
        if (delete == 0) throw new MyException(20001, "删除失败");
    }

}
