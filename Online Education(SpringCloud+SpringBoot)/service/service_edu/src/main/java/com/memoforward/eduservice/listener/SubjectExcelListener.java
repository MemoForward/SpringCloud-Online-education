package com.memoforward.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.memoforward.eduservice.model.EduSubject;
import com.memoforward.eduservice.model.excel.SubjectData;
import com.memoforward.eduservice.service.IEduSubjectService;
import com.memoforward.servicebase.exceptionhandler.MyException;

import java.util.Map;

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {

    private IEduSubjectService subjectService;

    public SubjectExcelListener(IEduSubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public SubjectExcelListener() {
    }

    // 读取内容，一行行读
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        // 一行行读，第一个值是一级分类，第二个值是二级分类,每读一行，执行一次invoke
        if(subjectData == null){
            throw new MyException(20001, "文件数据为空");
        }
        EduSubject existOneSubject = this.existOneSubject(subjectData.getOneSubjectName());
        if(existOneSubject == null){ // 没有相同的一级分类，进行添加
            EduSubject eduSubject = new EduSubject();
            eduSubject.setParentId("0");
            eduSubject.setTitle(subjectData.getOneSubjectName());
            subjectService.save(eduSubject);
        }
        // TODO: 这步获取到pid的值，不知道怎么优化
        String pid = this.existOneSubject(subjectData.getOneSubjectName()).getId();

        EduSubject existTwoSubject = this.existTwoSubject(subjectData.getTwoSubjectName(), pid);
        if(existTwoSubject == null){
            EduSubject eduSubject = new EduSubject();
            eduSubject.setParentId(pid);
            eduSubject.setTitle(subjectData.getTwoSubjectName());
            subjectService.save(eduSubject);
        }


    }

    private EduSubject existOneSubject(String name){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", "0");
        EduSubject oneSubject = subjectService.getOne(wrapper);
        return oneSubject;
    }

    private EduSubject existTwoSubject(String name, String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", pid);
        EduSubject oneSubject = subjectService.getOne(wrapper);
        return oneSubject;
    }



    @Override
    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
