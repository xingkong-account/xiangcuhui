<template>
    <div class="update-video-container">
        <el-form :model="video" label-width="120px">
            <el-form-item label="视频标题">
                <el-input v-model="video.title"></el-input>
            </el-form-item>
            <el-form-item label="描述信息">
                <el-input
                    type="textarea"
                    v-model="video.description"
                    :maxlength="100"
                    show-word-limit
                ></el-input>
                <div class="word-count">
                    还可以输入：{{ video.description.length }}/100 字
                </div>
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
                    :before-upload="beforeVideoUpload"
                    :on-success="handleVideoSuccess"
                    :on-error="handleError"
                    :limit="1"
                >
                    <i class="el-icon-upload"></i>
                    <div class="el-upload__text">将视频拖到此处，或<em>点击上传</em></div>
                    <div class="el-upload__tip" slot="tip">只能上传mp4文件，且不超过500MB</div>
                </el-upload>
                <video v-if="videoForm.url" :src="videoForm.url" style="width: 100px; margin-top: 10px;"></video>
            </el-form-item>

            <el-form-item label="Status" v-if="isAdmin">
                <el-select v-model="video.status" placeholder="Select Status">
                    <el-option label="待审核" value="待审核"></el-option>
                    <el-option label="已审核" value="已审核"></el-option>
                    <el-option label="已拒绝" value="已拒绝"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" @click="updateVideo">更新</el-button>
                <el-button type="primary" @click="handleCancel">取消</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
import axios from "axios";
export default {
    data() {
        return {
            video: {
                id: null,
                title: '',
                description: '',
                thumbnail: '',
                url: '',
                status: '',
                updated_at: new Date(),
                reviewed_at: null,
                reviewer: sessionStorage.getItem("username"),
            },
            videoForm: [],
            isAdmin: false
        };
    },
    created() {
        const videoId = this.$route.params.id;
        this.fetchVideoDetails(videoId);
        this.checkIfAdmin();
    },
    methods: {
        fetchVideoDetails(id) {
            axios.get(this.$baseUrl + `/api/videos/${id}`).then(response => {
                this.video = response.data;
                // this.$alert(this.video);
                if (this.video.thumbnail) {
                    this.videoForm.thumbnail = response.data.thumbnail;
                }
                if (this.video.video) {
                   this.videoForm.url = response.data.url;
                }
            });
        },
        beforeThumbnailUpload(file) {
            this.videoForm.thumbnail = '';
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
        handleRemoveThumbnail() {
            this.video.thumbnail = '';
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
            this.video.thumbnail = response.url;
            this.$message.success('缩略图上传成功!');
        },
        handleVideoSuccess(response) {
            this.videoForm.url = response.url;
            this.video.url = response.url;
            this.$message.success('视频上传成功!');
        },
        handleError() {
            this.$message.error('上传失败，请重试!');
        },
        updateVideo() {
            axios.post(this.$baseUrl + '/api/videos/update', this.video)
                .then(() => {
                    this.$message.success('更新成功');
                    this.$router.push('/video-manage');
                })
                .catch(() => {
                    this.$message.error('更新失败');
                });
        },
        checkIfAdmin(){
            this.isAdmin = (sessionStorage.getItem("usertype") === "管理员");
        },
        handleCancel(){
            this.$router.go(-1);
        }
    }
};
</script>

<style scoped>
.word-count {
    margin-top: 5px;
    font-size: 12px;
    color: #999;
}
</style>

