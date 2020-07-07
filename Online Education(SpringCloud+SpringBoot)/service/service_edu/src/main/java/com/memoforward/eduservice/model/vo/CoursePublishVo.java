package com.memoforward.eduservice.model.vo;

import lombok.Data;

@Data
public class CoursePublishVo {

    // 课程id
    private String id;
    // 课程名称
    private String title;
    // 课程封面地址
    private String cover;
    // 课程课时数
    private Integer lessonNum;
    // 课程简介
    private String description;
    // 课程价格
    private String price;
    // 课程讲师姓名
    private String teacherName;
    // 课程一级分类id
    private String oneSubject;
    // 课程二级分类id
    private String twoSubject;

}
