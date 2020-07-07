<template>
  <div class="app-container">
    <el-form label-width="120px">
      <el-form-item label="讲师名称">
        <el-input v-model="teacher.name" />
      </el-form-item>
      <el-form-item label="讲师排序">
        <el-input-number v-model="teacher.sort" controls-position="right" />
      </el-form-item>
      <el-form-item label="讲师头衔">
        <el-select v-model="teacher.level" clearable placeholder="请选择">
          <el-option :value="1" label="高级讲师" />
          <el-option :value="2" label="首席讲师" />
        </el-select>
      </el-form-item>
      <el-form-item label="讲师资历">
        <el-input v-model="teacher.career" />
      </el-form-item>
      <el-form-item label="讲师简介">
        <el-input v-model="teacher.intro" :rows="10" type="textarea" />
      </el-form-item>

      <!-- 讲师头像：TODO -->
      <el-form-item label="讲师头像">
        <!-- 头衔缩略图 -->
        <pan-thumb :image="teacher.avatar" />
        <!-- 文件上传按钮 -->
        <el-button type="primary" icon="el-icon-upload" @click="imagecropperShow=true">更换头像</el-button>

        <!--
            v-show：是否显示上传组件
            :key：类似于id，如果一个页面多个图片上传控件，可以做区分
            :url：后台上传的url地址
            @close：关闭上传组件
            @crop-upload-success：上传成功后的回调 
            <input type="file" name="file"/>
        -->
        <image-cropper
          v-show="imagecropperShow"
          :width="100"
          :height="100"
          :key="imagecropperKey"
          :url="BASE_API+'/eduoss/fileoss'"
          field="file"
          @close="close"
          @crop-upload-success="cropSuccess"
        />
      </el-form-item>

      <el-form-item>
        <!-- saveBtnDisable 防止重复提交 -->
        <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import teacher from "@/api/edu/teacher.js";
import ImageCropper from '@/components/ImageCropper'
import PanThumb from '@/components/PanThumb'

export default {

  components:{ImageCropper, PanThumb},

  data() {
    return {
      teacher: {
        name: "",
        sort: 0,
        level: 1,
        career: "",
        intro: "",
        avatar: ""
      },
      saveBtnDisabled: false,
      imagecropperShow: false,
      imagecropperKey:0, // 上传组件key值
      BASE_API: process.env.BASE_API
      // BASE_API:"http://localhost:8002"
    };
  },

  created() {
    this.init();
  },

  watch: {
    // 监听
    $route(to, from) {
      // 路由发生变化，方法就会执行
      this.init();
    }
  },

  methods: {
    //页面初始化
    init() {
      if (this.$route.params && this.$route.params.id) {
        // 从路径中获取id值
        const id = this.$route.params.id;
        // 调用根据id的方法
        this.getInfo(id);
      } else {
        this.teacher = {};
        this.teacher.sort = 0;
        this.teacher.level = 1;
      }
    },

    // 根据讲师id查询查询
    getInfo(id) {
      teacher.getTeacherById(id).then(response => {
        this.teacher = response.data.teacher;
      });
    },

    saveOrUpdate() {
      if (this.teacher.id) {
        this.updateTeacher();
      } else {
        this.saveTeacher();
      }
    },

    saveTeacher() {
      teacher.addTeacher(this.teacher).then(response => {
        this.$message({
          type: "success",
          message: "添加成功！"
        });
        // 路由跳转
        this.$router.push({ path: "/teacher/table" });
      });
    },

    updateTeacher() {
      teacher.updateTeacher(this.teacher).then(response => {
        this.$message({
          type: "success",
          message: "修改成功！"
        });
        // 路由跳转
        this.$router.push({ path: "/teacher/table" });
      });
    },

    // 关闭上传弹窗
    close(){
      this.imagecropperShow = false;
    },

    // 头像上传后的回调
    cropSuccess(data){
      this.imagecropperShow = false
      // element-ui 自己请求地址然后返回给回调函数
      this.teacher.avatar = data.url
      // 上传组件初始化
      this.imagecropperKey = this.imagecropperKey + 1
    }

  }
};
</script>