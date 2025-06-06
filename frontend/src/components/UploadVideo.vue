<template>
    <div class="upload-video-page" :class="{ 'mobile-layout': isMobile }">
        <!-- 电脑端布局 -->
        <el-form v-if="!isMobile" :model="videoForm" :rules="rules" ref="videoForm" label-width="120px" class="video-form">
            <el-form-item label="视频标题" prop="title">
                <el-input v-model="videoForm.title" placeholder="请输入视频标题"></el-input>
            </el-form-item>
            <el-form-item label="视频描述" prop="description">
                <el-input type="textarea" v-model="videoForm.description" placeholder="请输入视频描述"></el-input>
            </el-form-item>
            <el-form-item label="视频缩略图" prop="thumbnail">
                <el-upload
                    class="upload-demo thumbnail-upload"
                    drag
                    :action="$baseUrl + '/api/upload'"
                    list-type="picture"
                    :show-file-list="false"
                    :before-upload="beforeThumbnailUpload"
                    :on-success="handleThumbnailSuccess"
                    :on-error="handleError"
                    :limit="1"
                    accept="image/*"
                >
                    <i class="el-icon-upload"></i>
                    <div class="el-upload__text">将缩略图拖到此处，或<em>点击上传</em></div>
                    <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过1MB</div>
                </el-upload>
                <el-image v-if="videoForm.thumbnail" :src="videoForm.thumbnail" style="width: 100px; margin-top: 10px;"></el-image>
            </el-form-item>
            <el-form-item label="视频文件" prop="url">
                <el-upload
                    class="upload-demo"
                    drag
                    :action="$baseUrl + '/api/videos/upload'"
                    :data="uploadData"
                    :before-upload="beforeVideoUpload"
                    :on-success="handleVideoSuccess"
                    :on-error="handleError"
                    :limit="1"
                >
                    <i class="el-icon-upload"></i>
                    <div class="el-upload__text">将视频拖到此处，或<em>点击上传</em></div>
                    <div class="el-upload__tip" slot="tip">只能上传mp4文件，且不超过500MB</div>
                </el-upload>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="submitForm">提交</el-button>
                <el-button plain @click="goBack">取消</el-button>
            </el-form-item>
        </el-form>

        <!-- 手机端布局 -->
        <el-form v-if="isMobile" :model="videoForm" :rules="rules" ref="videoForm" label-width="90px" class="video-form mobile-form">
            <el-form-item label="标题" prop="title">
                <el-input v-model="videoForm.title" placeholder="请输入视频标题"></el-input>
            </el-form-item>
            <el-form-item label="描述" prop="description">
                <el-input type="textarea" v-model="videoForm.description" placeholder="请输入视频描述"></el-input>
            </el-form-item>
            <el-form-item label="缩略图" prop="thumbnail">
                <el-upload
                    class="upload-demo thumbnail-upload"
                    drag
                    :action="$baseUrl + '/api/upload'"
                    list-type="picture"
                    :show-file-list="false"
                    :before-upload="beforeThumbnailUpload"
                    :on-success="handleThumbnailSuccess"
                    :on-error="handleError"
                    :limit="1"
                    accept="image/*"
                >
                    <i class="el-icon-upload"></i>
                    <div class="el-upload__text">拖到此处，或<em>点击上传</em></div>
                    <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过1MB</div>
                </el-upload>
                <el-image v-if="videoForm.thumbnail" :src="videoForm.thumbnail" style="width: 80px; margin-top: 10px;"></el-image>
            </el-form-item>
            <el-form-item label="视频文件" prop="url">
                <el-upload
                    class="upload-demo"
                    drag
                    :action="$baseUrl + '/api/videos/upload'"
                    :data="uploadData"
                    :before-upload="beforeVideoUpload"
                    :on-success="handleVideoSuccess"
                    :on-error="handleError"
                    :limit="1"
                >
                    <i class="el-icon-upload"></i>
                    <div class="el-upload__text">拖到此处，或<em>点击上传</em></div>
                    <div class="el-upload__tip" slot="tip">只能上传mp4文件，且不超过500MB</div>
                </el-upload>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="submitForm">提交</el-button>
                <el-button plain @click="goBack">取消</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
export default {
    data() {
        return {
            videoForm: {
                title: '',
                url: '',
                description: '',
                thumbnail: ''  // 视频缩略图
            },
            isMobile: false,
            uploadData: {},
            rules: {
                title: [
                    { required: true, message: '请输入视频标题', trigger: 'blur' }
                ],
                description: [
                    { required: true, message: '请输入视频描述', trigger: 'blur' }
                ],
                thumbnail: [
                    { required: true, message: '请上传视频缩略图', trigger: 'change' }
                ],
                url: [
                    { required: true, message: '请上传视频文件', trigger: 'change' }
                ]
            }
        };
    },
    mounted() {
        this.checkIfMobile();
        window.addEventListener('resize', this.checkIfMobile);
    },
    beforeDestroy() {
        window.removeEventListener('resize', this.checkIfMobile);
    },
    methods: {
        checkIfMobile(){
            this.isMobile = window.innerWidth <= 768;
        },
        beforeThumbnailUpload(file) {
            const isImage = file.type.startsWith('image/');
            if (!isImage) {
                this.$message.error('只能上传图片文件!');
            }
            const isLt1MB = file.size / 1024 <= 1024;
            if (!isLt1MB) {
                this.$message.error('图片文件大小不能超过 1MB!');
            }
            return isImage && isLt1MB;
        },
        beforeVideoUpload(file) {
            const isVideo = file.type.startsWith('video/');
            if (!isVideo) {
                this.$message.error('只能上传视频文件!');
            }
            const isLt500MB = file.size / 1024 / 1024 <= 500;
            if (!isLt500MB) {
                this.$message.error('视频文件大小不能超过 500MB!');
            }
            return isVideo && isLt500MB;
        },
        handleThumbnailSuccess(response) {
            this.videoForm.thumbnail = response.url;
            this.$message.success('缩略图上传成功!');
        },
        handleVideoSuccess(response) {
            this.videoForm.url = response.url;
            this.$message.success('视频上传成功!');
        },
        handleError() {
            this.$message.error('上传失败，请重试!');
        },
        submitForm() {
            this.$refs.videoForm.validate((valid) => {
                if (valid) {
                    console.log('视频表单数据:', this.videoForm);
                    this.$axios.post(this.$baseUrl + '/api/videos/add', this.videoForm)
                        .then(() => {
                            this.$message.success('视频信息提交成功!');
                            this.goBack();
                        })
                        .catch(() => {
                            this.$message.error('提交失败，请重试!');
                        });
                } else {
                    this.$message.error('请填写所有必填项!');
                }
            });
        },
        goBack() {
            this.$router.push("/");
        }
    }
};
</script>

<style scoped>
.upload-video-page {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    max-height: 150vh;
    background-color: #f9f9f9;
}

.video-form {
    background-color: #fff;
    padding: 30px;
    border-radius: 8px;
    width: 100%;
    max-width: 1000px;
}

.thumbnail-upload-item {
    display: flex;
    flex-direction: column;
}

.thumbnail-upload {
    width: 100%;
    min-height: 180px;
}

.el-upload__text {
    margin-bottom: 10px;
}

.el-form-item {
    margin-bottom: 20px;
}
</style>