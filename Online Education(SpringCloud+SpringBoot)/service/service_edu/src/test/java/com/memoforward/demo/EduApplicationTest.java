package com.memoforward.demo;

import com.alibaba.excel.EasyExcel;
import com.memoforward.demo.excel.DemoData;
import com.memoforward.demo.excel.DemoDataListener;
import com.memoforward.demo.excel.TestEasyExcel;
import com.memoforward.eduservice.EduApplication;
import com.memoforward.eduservice.mapper.EduCourseMapper;
import com.memoforward.eduservice.model.vo.CoursePublishVo;
import com.memoforward.eduservice.service.IEduSubjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = EduApplication.class)
public class EduApplicationTest {

    @Autowired
    IEduSubjectService subjectService;

    @Autowired
    EduCourseMapper courseMapper;

    // 测试EasyExcel写操作
    @Test
    public void testEasyExcelWrite(){
        // 实现excel写的操作
        // 1. 设置写入文件夹地址和excel文件名
        String filename = "/Users/memoforward/Documents/Project/Java/Online Education/service/service_edu/src/test/java/com/memoforward/demo/file/student.xlsx";

        // 2. 调用easyexcel里面的方法实现写操作
        EasyExcel.write(filename, DemoData.class).sheet("学生列表").doWrite(TestEasyExcel.getData());
    }

    // 测试EasyExcel读操作
    @Test
    public void testEasyExcelRead(){
        String filename = "/Users/memoforward/Documents/Project/Java/Online Education/service/service_edu/src/test/java/com/memoforward/demo/file/student.xlsx";
        EasyExcel.read(filename, DemoData.class, new DemoDataListener()).sheet().doRead();
    }


    @Test
    public void testMapperAutowired(){
        subjectService.getAllOneTwoSubjects();
    }

    @Test
    public void testCourseMapper(){
        CoursePublishVo courseInfo = courseMapper.getPublishCourseInfo("18");
        System.out.println(courseInfo);
    }

}
