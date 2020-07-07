package com.memoforward.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.memoforward.eduservice.model.EduChapter;
import com.memoforward.eduservice.mapper.EduChapterMapper;
import com.memoforward.eduservice.model.EduCourse;
import com.memoforward.eduservice.model.EduVideo;
import com.memoforward.eduservice.model.chapter.ChapterVo;
import com.memoforward.eduservice.model.chapter.VideoVo;
import com.memoforward.eduservice.service.IEduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.memoforward.eduservice.service.IEduVideoService;
import com.memoforward.servicebase.exceptionhandler.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements IEduChapterService {

    @Autowired
    EduChapterMapper chapterMapper;

    @Autowired
    IEduVideoService videoService;

    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        List<ChapterVo> finalChapters = new ArrayList<>();
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id", courseId);
        List<EduChapter> chapters = chapterMapper.selectList(wrapperChapter);
        for(EduChapter chapter: chapters){
//            System.out.println(chapter);
            ChapterVo chapterVo = new ChapterVo();
            chapterVo.setId(chapter.getId());
            chapterVo.setTitle(chapter.getTitle());
            // 根据章节id查询小节
            QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
            wrapperVideo.eq("chapter_id", chapter.getId());
            List<EduVideo> videos = videoService.list(wrapperVideo);
            // 封装小节
            List<VideoVo> videoVos = new ArrayList<>();
            for(EduVideo video: videos){
                VideoVo videoVo = new VideoVo();
                videoVo.setId(video.getId());
                videoVo.setTitle(video.getTitle());
                videoVos.add(videoVo);
            }
            chapterVo.setChildren(videoVos);
            finalChapters.add(chapterVo);
        }
        return finalChapters;
    }

    // 删除章节
    @Override
    public boolean deleteChapter(String chapterId) {
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("chapter_id", chapterId);
        int count = videoService.count(wrapperVideo);
        if(count > 0){ // 能查询出小节,不能执行删除
            throw new MyException(20001, "章节下还有数据，请清除后再删除！");
        }else{ // 章下面没有小节，可以删除
            // 删除章节
            int delete = chapterMapper.deleteById(chapterId);
            return delete > 0;
        }
    }

    @Override
    public void removeByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        chapterMapper.delete(wrapper);
    }
}
