import request from '@/utils/request'

export default{

    // 1. 讲师列表
    getTeacherListPage(current, limit, teacherQuery){
        return request({
            url: `/eduservice/teacher/pageTeacherCondition/${current}/${limit}`,
            method: 'post',
            // 后端使用RequestBody获取数据,因此前端要用json
            // data表示把对象转换成json
            data: teacherQuery
        })
    },

    // 2. 删除某个讲师
    removeTeacherById(id){
        return request({
            url:`/eduservice/teacher/${id}/remove`,
            method:"delete",
        })
    },

    // 3.添加讲师
    addTeacher(teacher){
        return request({
            url: '/eduservice/teacher/addTeacher',
            method: 'post',
            data: teacher
        })
    },

    // 4. 修改讲师
    updateTeacher(teacher){
        return request({
            url:'/eduservice/teacher/updateTeacher',
            method:'post',
            data:teacher
        })
    },

    // 5. 根据id查询讲师
    getTeacherById(id){
        return request({
            url:`/eduservice/teacher/${id}/getTeacher`,
            method:'get'
        })
    },
}
