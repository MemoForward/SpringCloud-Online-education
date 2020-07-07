package com.memoforward.eduservice.service;

import com.memoforward.eduservice.model.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author memoforward
 * @since 2020-07-04
 */
public interface IEduVideoService extends IService<EduVideo> {

    void removeByCourseId(String courseId);
}
