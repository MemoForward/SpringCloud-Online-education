<template>
  <div class="app-container">
    <h2 style="text-align: center;">发布新课程</h2>

    <el-steps :active="2" process-status="wait" align-center style="margin-bottom: 40px;">
      <el-step title="填写课程基本信息" />
      <el-step title="创建课程大纲" />
      <el-step title="最终发布" />
    </el-steps>

    <el-button type="text" @click="openChapterDialog()">添加章节</el-button>

    <ul class="chapterList">
      <li v-for="chapter in chapters" :key="chapter.id">
        <p>
          {{ chapter.title }}
          <span class="acts">
            <el-button style type="text" @click="openVideo(chapter.id)">添加小节</el-button>
            <el-button style type="text" @click="openEditChatper(chapter.id)">编辑</el-button>
            <el-button type="text" @click="removeChapter(chapter.id)">删除</el-button>
          </span>
        </p>

        <!-- 视频 -->
        <ul class="chapterList videoList">
          <li v-for="video in chapter.children" :key="video.id">
            <p>
              {{ video.title }}
              <span class="acts">
                <el-button style type="text" @click="openVideoEditChapter(video.id)">编辑</el-button>
                <el-button type="text" @click="removeVideo(video.id)">删除</el-button>
              </span>
            </p>
          </li>
        </ul>
      </li>
    </ul>
    <el-form label-width="120px">
      <el-form-item>
        <el-button @click="previous">上一步</el-button>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="next">下一步</el-button>
      </el-form-item>
    </el-form>

    <!-- 添加和修改章节表单 -->
    <el-dialog :visible.sync="dialogChapterFormVisible" title="添加章节">
      <el-form :model="chapter" label-width="120px">
        <el-form-item label="章节标题">
          <el-input v-model="chapter.title" />
        </el-form-item>
        <el-form-item label="章节排序">
          <el-input-number v-model="chapter.sort" :min="0" controls-position="right" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogChapterFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdate">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 添加和修改课时表单 -->
    <el-dialog :visible.sync="dialogVideoFormVisible" title="添加课时">
      <el-form :model="video" label-width="120px">
        <el-form-item label="课时标题">
          <el-input v-model="video.title" />
        </el-form-item>
        <el-form-item label="课时排序">
          <el-input-number v-model="video.sort" :min="0" controls-position="right" />
        </el-form-item>
        <el-form-item label="是否免费">
          <el-radio-group v-model="video.isFree">
            <el-radio :label="1">免费</el-radio>
            <el-radio :label="0">默认</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="上传视频">
          <el-upload
            :on-success="handleVodUploadSuccess"
            :on-remove="handleVodRemove"
            :before-remove="beforeVodRemove"
            :on-exceed="handleUploadExceed"
            :file-list="fileList"
            :action="BASE_API+'/eduvod/video/uploadAliyunVideo'"
            :limit="1"
            class="upload-demo"
          >
            <el-button size="small" type="primary">上传视频</el-button>
            <el-tooltip placement="right-end">
              <div slot="content">
                最大支持1G，
                <br />支持3GP、ASF、AVI、DAT、DV、FLV、F4V、
                <br />GIF、M2T、M4V、MJ2、MJPEG、MKV、MOV、MP4、
                <br />MPE、MPG、MPEG、MTS、OGG、QT、RM、RMVB、
                <br />SWF、TS、VOB、WMV、WEBM 等视频格式上传
              </div>
              <i class="el-icon-question" />
            </el-tooltip>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVideoFormVisible = false">取 消</el-button>
        <el-button :disabled="saveVideoBtnDisabled" type="primary" @click="saveOrUpdateVideo">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import chapter from "@/api/edu/chapter";
import video from "@/api/edu/video";
export default {
  data() {
    return {
      saveBtnDisabled: false,
      saveVideoBtnDisabled: false,
      chapterVideoList:[],
      courseId: -1,
      chapters: [],
      dialogChapterFormVisible: false,
      dialogVideoFormVisible: false,
      chapter: {
        title: "",
        sort: 0
      },
      video: {
        title: "",
        sort: 0,
        free: 0,
        videoSourceId: ""
      },
      fileList: [], //上传文件列表
      BASE_API: process.env.BASE_API // 接口API地址
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      if (this.$route.params && this.$route.params.id) {
        this.courseId = this.$route.params.id;
        this.getChapterVideo();
      }
    },
    //上传视频成功调用的方法
    handleVodUploadSuccess(response, file, fileList) {
      //上传视频id赋值
      this.video.videoSourceId = response.data.videoId;
      //上传视频名称赋值
      this.video.videoOriginalName = file.name;
      // this.fileList = fileList;
      // console.log(fileList);
    },
    handleUploadExceed() {
      this.$message.warning("想要重新上传视频，请先删除已上传的视频");
    },
    handleVodRemove(){
      // 调用接口删除视频
      video.removeAliyunVideo(this.video.videoSourceId)
      .then(response => {
        this.$message.success("删除成功");
        // 文件列表清空
        this.fileList = []
        this.video.videoSourceId =""
        this.video.videoOriginalName=""
      })

    },
    // 删除文件之前，点了×的操作
    beforeVodRemove(file, fileList) {
      return this.$confirm(`确定移除${file.name}？`)
    },

    openChapterDialog() {
      this.dialogChapterFormVisible = true;
      this.chapter.title = "";
      this.chapter.sort = 0;
    },

    // 获得该课程所有章节
    getChapterVideo() {
      chapter.getAllChapterVideo(this.courseId).then(response => {
        // console.log(response.data)
        this.chapters = response.data.items;
      });
    },

    /* ------------- 章节操作 ---------------- */
    // 添加章节
    addChapter() {
      if (this.courseId == -1) {
        this.$message.error("未指定课程！");
        return;
      }
      this.chapter.courseId = this.courseId;
      chapter.addChapter(this.chapter).then(response => {
        this.dialogChapterFormVisible = false;
        this.$message.success("添加成功");
        this.getChapterVideo();
      });
    },

    // 修改章节
    updateChapter() {
      chapter.updateChapter(this.chapter).then(response => {
        this.dialogChapterFormVisible = false;
        this.$message.success("修改成功");
        this.getChapterVideo();
      });
    },

    // 章节添加修改
    saveOrUpdate() {
      if (this.chapter.id) {
        this.updateChapter();
      } else {
        this.addChapter();
      }
    },

    //删除章节
    removeChapter(chapterId) {
      this.$confirm("此操作将删除小节, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        //点击确定，执行then方法
        //调用删除的方法
        chapter.deleteChapter(chapterId).then(response => {
          //删除成功
          //提示信息
          this.$message({
            type: "success",
            message: "删除成功!"
          });
          //刷新页面
          this.getChapterVideo();
        });
      }); //点击取消，执行catch方法
    },

    // 点开修改章节按钮
    openEditChatper(chapterId) {
      this.dialogChapterFormVisible = true;
      chapter.getChapterById(chapterId).then(response => {
        this.chapter = response.data.chapter;
      });
    },

    /*-------- 小节 操作 ----------*/
    openVideo(chapterId) {
      this.dialogVideoFormVisible = true;
      this.video = {};
      this.fileList = [];
      this.video.chapterId = chapterId;
      this.video.courseId = this.courseId;
    },

    // 添加小节
    addVideo() {
      this.video.courseId = this.courseId;
      video.addVideo(this.video).then(response => {
        this.$message.success("添加小节成功");
        this.dialogVideoFormVisible = false;
        this.getChapterVideo();
      });
    },

    openVideoEditChapter(videoId) {
      this.dialogVideoFormVisible = true;
      video.getVideoById(videoId).then(response => {
        this.video = response.data.video;
      });
    },
    // 修改小节
    updateVideo() {
      video.updateVideo(this.video).then(response => {
        this.dialogVideoFormVisible = false;
        this.$message.success("修改成功");
        this.getChapterVideo();
      });
    },

    // 删除小节
    removeVideo(videoId) {
      this.$confirm("此操作将删除章节, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        //点击确定，执行then方法
        //调用删除的方法
        video.deleteVideo(videoId).then(response => {
          //删除成功
          //提示信息
          this.$message({
            type: "success",
            message: "删除成功!"
          });
          //刷新页面
          this.getChapterVideo();
        });
      }); //点击取消，执行catch方法
    },

    saveOrUpdateVideo() {
      if (this.video.id) {
        this.updateVideo();
      } else {
        this.addVideo();
      }
    },

    previous() {
      this.$router.push({ path: "/course/info/" + this.courseId });
    },
    next() {
      //跳转到第二步
      this.$router.push({ path: "/course/publish/" + this.courseId });
    }
  }
};
</script>

<style scoped>
.chapterList {
  position: relative;
  list-style: none;
  margin: 0;
  padding: 0;
}
.chapterList li {
  position: relative;
}
.chapterList p {
  float: left;
  font-size: 20px;
  margin: 10px 0;
  padding: 10px;
  height: 70px;
  line-height: 50px;
  width: 100%;
  border: 1px solid #ddd;
}
.chapterList .acts {
  float: right;
  position: relative;
  z-index: 999;
  font-size: 14px;
}

.videoList {
  padding-left: 50px;
}
.videoList p {
  float: left;
  font-size: 14px;
  margin: 10px 0;
  padding: 10px;
  height: 50px;
  line-height: 30px;
  width: 100%;
  border: 1px dotted #ddd;
}
</style>