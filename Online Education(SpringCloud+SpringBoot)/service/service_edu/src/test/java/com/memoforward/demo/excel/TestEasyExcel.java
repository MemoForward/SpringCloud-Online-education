package com.memoforward.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {

    // 创建方法返回list集合
    public static List<DemoData> getData(){
        List<DemoData> list = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            DemoData data = new DemoData();
            data.setSno(i);
            data.setSname("cxy"+Integer.valueOf(i).toString());
            list.add(data);
        }
        return list;
    }

    //
}
