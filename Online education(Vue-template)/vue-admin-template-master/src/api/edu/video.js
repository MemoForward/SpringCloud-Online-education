import request from '@/utils/request'

export default {

    // 添加小节
    addVideo(video) {
        return request({
            url: '/eduservice/video/addVideo',
            method: 'post',
            data: video
        })
    },
    // 修改小节
    updateVideo(video) {
        return request({
            url: '/eduservice/video/updateVideo',
            method: 'post',
            data: video
        })
    },


   // 删除小节
    deleteVideo(videoId) {
        return request({
            url: `/eduservice/video/${videoId}/deleteVideo`,
            method:'delete',
        })
    },
    
    // 查询小节
    getVideoById(videoId) {
        return request({
            url: `/eduservice/video/${videoId}/getVideoInfo`,
            method: 'get'
        })
    },

    // 删除视频
    removeAliyunVideo(id){
        return request({
            url:`/eduvod/video/${id}/removeAliyunVideo`,
            method:'delete'
        })
    }




}