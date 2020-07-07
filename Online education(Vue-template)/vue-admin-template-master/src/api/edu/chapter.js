import request from '@/utils/request'

export default {

    getAllChapterVideo(courseId) {
        return request({
            url: `/eduservice/chapter/${courseId}/getChapterVideo`,
            method: 'get'
        })
    },

    // 添加章节
    addChapter(chapter) {
        return request({
            url: '/eduservice/chapter/addChapter',
            method: 'post',
            data: chapter
        })
    },
    // 修改章节
    updateChapter(chapter) {
        return request({
            url: '/eduservice/chapter/updateChapter',
            method: 'post',
            data: chapter
        })
    },


    // 根据id查询章节
    getChapterById(chapterId) {
        return request({
            url: `/eduservice/chapter/${chapterId}/getChapterInfo`,
            method: 'get'
        })
    },

    // 删除章节
    deleteChapter(chapterId) {
        return request({
            url: `/eduservice/chapter/${chapterId}/deleteChapter`,
            method:'delete',
        })
    }



}
