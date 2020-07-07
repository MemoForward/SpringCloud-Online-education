package com.memoforward.eduservice.service;

import com.memoforward.eduservice.model.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.memoforward.eduservice.model.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author memoforward
 * @since 2020-07-04
 */
public interface IEduSubjectService extends IService<EduSubject> {

    void saveSubject(MultipartFile file, IEduSubjectService eduSubjectService);

    List<OneSubject> getAllOneTwoSubjects();
}
