package com.memoforward.eduservice.service;

import com.memoforward.eduservice.model.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.memoforward.eduservice.model.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author memoforward
 * @since 2020-07-04
 */
public interface IEduChapterService extends IService<EduChapter> {

    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    boolean deleteChapter(String chapterId);

    void removeByCourseId(String courseId);
}
