import request from '@/utils/request'

export default{

    // 1. 添加课程信息
    addCourseInfo(courserInfo){
        return request({
            url:'/eduservice/course/addCourseInfo',
            method:'post',
            data:courserInfo
        })
    },

    // 2. 查询所有讲师
    getListTeacher(){
        return request({
            url:`/eduservice/teacher/findAll`,
            method:'get'
        })
    },

    // 3. 根据课程id获得课程信息
    getCourseInfoById(id){
        return request({
            url:`/eduservice/course/${id}/getCourseInfo`,
            method:'get'
        })
    },

    // 4. 修改课程byid
    updateCourseInfo(courseInfo){
        return request({
            url:'/eduservice/course/updateCourseInfo',
            method:'post',
            data:courseInfo
        })
    },

    // 5. 课程确认信息
    getPublishCourseInfo(id){
        return request({
            url:`/eduservice/course/${id}/getPublishCourseInfo`,
            method:'get'
        })
    },

    // 6. 课程发布
    publishCourse(id){
        return request({
            url:`/eduservice/course/${id}/publishCourse`,
            method:'get'
        })
    },

    // 7. 课程查询
    getListCourse(current, limit, courseQuery){
        return request({
            url:`/eduservice/course/pageListCourses/${current}/${limit}`,
            method:'post',
            data:courseQuery
        })
    },

    // 8. 课程删除
    removeCourseById(id){
        return request({
            url:`/eduservice/course/${id}/removeCourse`,
            method:'delete'
        })
    }
}