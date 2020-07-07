package com.memoforward.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.memoforward.commonutils.R;
import com.memoforward.eduservice.model.EduTeacher;
import com.memoforward.eduservice.model.vo.TeacherQuery;
import com.memoforward.eduservice.service.IEduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author memoforward
 * @since 2020-06-29
 */
@RestController
@CrossOrigin
@RequestMapping("/eduservice/teacher")
@Api(value = "onlineEducation",description = "讲师模块接口")
public class EduTeacherController {

    @Autowired
    IEduTeacherService teacherService;

    // 1. 查询所有讲师
    @GetMapping("/findAll")
    @ApiOperation(value = "查询所有讲师")
    public R findAllTeachers(){
        List<EduTeacher> teachers = teacherService.list(null);
        return R.ok().data("items", teachers);
    }

    // 2. 删除某位讲师
    @DeleteMapping("/{teacherId}/remove")
    @ApiOperation(value = "删除某位讲师")
    public R removeTeacher(@PathVariable("teacherId") String id){
        boolean isDelete = teacherService.removeById(id);
        return isDelete ? R.ok() : R.error();
    }


    // 3. 分页查询
    /*
    @para: current 当前页
    @para: limit 显示条数
     */
    @GetMapping("pageTeacher/{current}/{limit}")
    @ApiOperation(value = "分页查询讲师")
    public R pageListTeacher(@PathVariable Integer current,
                             @PathVariable Integer limit){
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);
        teacherService.page(pageTeacher, null);
        long total = pageTeacher.getTotal();
        List<EduTeacher> records = pageTeacher.getRecords();
        return R.ok().data("total", total).data("records", records);
    }

    // 4. 多条件组合查询 + 分页
    @PostMapping("/pageTeacherCondition/{current}/{limit}")
    @ApiOperation(value = "讲师条件查询+分页")
    public R pageTeacherCondition(@PathVariable Integer current,
                                  @PathVariable Integer limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery){
        // 创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);

        // 动态sql
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        if(!StringUtils.isEmpty(name)) wrapper.like("name", name);
        if(!StringUtils.isEmpty(level)) wrapper.eq("level", level);
        if(!StringUtils.isEmpty(begin)) wrapper.ge("gmt_create", begin);
        if(!StringUtils.isEmpty(end)) wrapper.le("gmt_modified", end);
        wrapper.orderByDesc("gmt_create");

        // 分页查询
        teacherService.page(pageTeacher, wrapper);
        long total = pageTeacher.getTotal();
        List<EduTeacher> records = pageTeacher.getRecords();
        return R.ok().data("total", total).data("records", records);
    }

    // 5. 添加讲师
    @PostMapping("/addTeacher")
    @ApiOperation(value = "添加讲师")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean save = teacherService.save(eduTeacher);
        return save ? R.ok() : R.error();
    }

    // 6. 根据id查询讲师
    @GetMapping("/{id}/getTeacher")
    @ApiOperation(value = "根据id查询讲师")
    public R getTeacherById(@PathVariable String id){
        EduTeacher eduTeacher = teacherService.getById(id);
        return R.ok().data("teacher", eduTeacher);
    }

    // 7. 修改讲师
    @PostMapping("/updateTeacher")
    @ApiOperation(value = "修改讲师")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean update = teacherService.updateById(eduTeacher);
        return update ? R.ok() : R.error();
    }
}
