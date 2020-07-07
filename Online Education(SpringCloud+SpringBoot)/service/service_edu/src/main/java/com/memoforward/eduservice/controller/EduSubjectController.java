package com.memoforward.eduservice.controller;


import com.memoforward.commonutils.R;
import com.memoforward.eduservice.model.subject.OneSubject;
import com.memoforward.eduservice.service.IEduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author memoforward
 * @since 2020-07-04
 */
@RestController
@Api(description = "课程分类模块")
@CrossOrigin
@RequestMapping("/eduservice/subject")
public class EduSubjectController {

    @Autowired
    IEduSubjectService subjectService;

    // 1. 添加课程分类
    // 获取上传过来的文件，把文件内容读取出来
    @ApiOperation("根据excel添加课程到数据库")
    @PostMapping("/addSubject")
    public R addSubject(MultipartFile file){
        // 上传
        subjectService.saveSubject(file, subjectService);
        return R.ok();
    }

    // 2. 课程分类列表（树形）
    @ApiOperation("返回树形结构的所有分类")
    @GetMapping("/getAllSubject")
    public R getAllSubject(){
        // 得到所有一级和二级分类
        List<OneSubject> oneSubjects = subjectService.getAllOneTwoSubjects();
        return R.ok().data("items", oneSubjects);
    }

}
