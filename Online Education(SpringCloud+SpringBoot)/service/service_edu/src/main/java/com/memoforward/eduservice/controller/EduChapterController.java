package com.memoforward.eduservice.controller;


import com.memoforward.commonutils.R;
import com.memoforward.eduservice.model.EduChapter;
import com.memoforward.eduservice.model.chapter.ChapterVo;
import com.memoforward.eduservice.service.IEduChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

@Api(description = "课程章节操作")
@RestController
@CrossOrigin
@RequestMapping("/eduservice/chapter")
public class EduChapterController {

    @Autowired
    private IEduChapterService chapterService;

    @ApiOperation("查找某个课程的章节小节")
    @GetMapping("/{courseId}/getChapterVideo")
    public R getChapterVideo(@PathVariable String courseId){
        List<ChapterVo> chapters = chapterService.getChapterVideoByCourseId(courseId);
        return R.ok().data("items", chapters);
    }

    @ApiOperation("填加章节")
    @PostMapping("/addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter){
        chapterService.save(eduChapter);
        return R.ok();
    }

    @ApiOperation("查询章节并返回")
    @GetMapping("/{chapterId}/getChapterInfo")
    public R getChapterInfo(@PathVariable String chapterId){
        EduChapter chapter = chapterService.getById(chapterId);
        return R.ok().data("chapter", chapter);
    }

    @ApiOperation("更新章节")
    @PostMapping("/updateChapter")
    public R updateChapter(@RequestBody EduChapter chapter){
        chapterService.updateById(chapter);
        return R.ok();
    }

    @ApiOperation("删除章节")
    @DeleteMapping("/{chapterId}/deleteChapter")
    public R deleteChapterById(@PathVariable String chapterId){
        boolean result = chapterService.deleteChapter(chapterId);
        return result ? R.ok() : R.error();
    }
}
