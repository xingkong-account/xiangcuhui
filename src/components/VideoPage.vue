<template>
        <!-- 展示视频列表 -->
        <div class="video-list">
            <h2>视频列表</h2>
            <div v-if="videos.length === 0" class="no-videos">
                暂无视频
            </div>
            <div v-else class="video-container">
                <div v-for="video in videos" :key="video.id" class="video-item">
                    <video controls :src="video.url" class="video-element"></video>
                    <p>{{ video.description }}</p>
                </div>
            </div>
        </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            videos: [],
            uploadHeaders: {
                'Authorization': 'Bearer <your-token>'  // 如果需要身份验证，可以在这里设置
            },
            uploadData: {

            }
        };
    },
    methods: {
        async fetchVideos() {
            try {
                const response = await axios.get(this.$baseUrl + '/api/videos/all');
                this.videos = response.data;
            } catch (error) {
                console.error('获取视频失败:', error);
                this.$message.error('获取视频失败，请稍后再试。');
            }
        },
        handleUploadSuccess() {
            this.$message.success('视频上传成功！');
            this.fetchVideos();  // 上传成功后刷新视频列表
        },
        handleUploadError(error) {
            this.$message.error('视频上传失败，请稍后再试');
            console.error('上传错误:', error);
        }
    },
    created() {
        this.fetchVideos();
    }
};
</script>

<style scoped>
.video-page {
    padding: 20px;
}

.upload-section {
    margin-bottom: 20px;
}

.upload-demo {
    border: 2px dashed #409EFF;
    padding: 20px;
    border-radius: 5px;
    text-align: center;
    background: #f5f5f5;
    cursor: pointer;
}

.upload-demo i {
    font-size: 24px;
    color: #409EFF;
}

.upload-demo .el-upload__text {
    margin-top: 10px;
    font-size: 16px;
    color: #606266;
}

.upload-demo .el-upload__tip {
    margin-top: 10px;
    font-size: 12px;
    color: #c0c4cc;
}

.video-list {
    margin-top: 20px;
}

.no-videos {
    text-align: center;
    color: #606266;
}

.video-container {
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
}

.video-item {
    flex: 1 1 300px;
    background: #fff;
    border: 1px solid #dcdfe6;
    border-radius: 5px;
    padding: 10px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.video-element {
    width: 100%;
    height: 200px;
    object-fit: cover;
}
</style>
