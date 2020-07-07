package com.memoforward.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.memoforward.eduservice.listener.SubjectExcelListener;
import com.memoforward.eduservice.model.EduSubject;
import com.memoforward.eduservice.mapper.EduSubjectMapper;
import com.memoforward.eduservice.model.excel.SubjectData;
import com.memoforward.eduservice.model.subject.OneSubject;
import com.memoforward.eduservice.model.subject.TwoSubject;
import com.memoforward.eduservice.service.IEduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author memoforward
 * @since 2020-07-04
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements IEduSubjectService {

    @Autowired
    private EduSubjectMapper subjectMapper;

    // 保存课程分类
    @Override
    public void saveSubject(MultipartFile file, IEduSubjectService eduSubjectService) {
        try {
            EasyExcel.read(file.getInputStream(), SubjectData.class, new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }


    // 获得所有一级二级分类
    @Override
    public List<OneSubject> getAllOneTwoSubjects() {
        // 创建list集合，用于存储最终树结构
        List<OneSubject> finalSubjectList = new ArrayList<>();

        // 1. 查询所有一级分类
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id", "0");
        List<EduSubject> _oneSubjects = subjectMapper.selectList(wrapperOne);
        // 2. 封装一级分类
        for(EduSubject _oneSubject: _oneSubjects){
            OneSubject oneSubject = new OneSubject();
            oneSubject.setId(_oneSubject.getId());
            oneSubject.setTitle(_oneSubject.getTitle());
            // 二级分类封装
            List<TwoSubject> twoSubjects = new ArrayList<>();
            QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
            wrapperTwo.eq("parent_id", _oneSubject.getId());
            List<EduSubject> _twoSubjects = subjectMapper.selectList(wrapperTwo);
            for(EduSubject _twoSubject: _twoSubjects){
                TwoSubject twoSubject = new TwoSubject();
                twoSubject.setId(_twoSubject.getId());
                twoSubject.setTitle(_twoSubject.getTitle());
                twoSubjects.add(twoSubject);
            }
            oneSubject.setChildren(twoSubjects);
            finalSubjectList.add(oneSubject);
        }

        return finalSubjectList;
    }

}
