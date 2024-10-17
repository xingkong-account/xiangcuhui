<template>
    <div class="video-review-page">
        <h1 class="video-check-title">视频审核</h1>

        <!-- 桌面端布局 -->
        <el-table v-show="!isMobile"
            v-if="!isMobile"
            :data="videos"
            style="width: 100%"
            :highlight-current-row="true"
            @row-click="handleRowClick">
            <el-table-column prop="title" label="标题"></el-table-column>
            <el-table-column prop="uploader" label="上传人"></el-table-column>
            <el-table-column prop="description" label="描述"></el-table-column>
            <el-table-column prop="thumbnail" label="缩略图">
                <template slot-scope="scope">
                    <img :src="scope.row.thumbnail" alt="缩略图" class="thumbnail-img"/>
                </template>
            </el-table-column>
            <el-table-column prop="url" label="视频">
                <template slot-scope="scope">
                    <video v-if="scope.row.url" controls :src="scope.row.url" class="video-preview">
                        您的浏览器不支持播放此视频。
                    </video>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="160">
                <template slot-scope="scope">
                    <el-button @click="approveVideo(scope.row)" type="success" size="mini">通过</el-button>
                    <el-button @click="confirmRejectVideo(scope.row)" type="danger" size="mini">拒绝</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 手机端布局 -->
        <div v-show="isMobile" class="mobile-card-list">
            <el-card
                v-for="(video, index) in videos"
                :key="index"
                class="mobile-card"
                shadow="hover">
                <div class="mobile-card-content">
                    <h3 class="mobile-card-title">{{ video.title }}</h3>
                    <p class="mobile-card-uploader">上传人: {{ video.uploader }}</p>
                    <p class="mobile-card-description">{{ video.description }}</p>
                    <div class="mobile-thumbnail-container">
                        <img :src="video.thumbnail" alt="缩略图" class="mobile-thumbnail-img" />
                    </div>
                    <video v-if="video.url" controls :src="video.url" class="mobile-video-preview">
                        您的浏览器不支持播放此视频。
                    </video>
                    <div class="mobile-card-actions" v-if="isAdmin">
                        <el-button @click="approveVideo(video)" type="success" size="mini">通过</el-button>
                        <el-button @click="confirmRejectVideo(video)" type="danger" size="mini">拒绝</el-button>
                    </div>
                </div>
            </el-card>
        </div>

        <el-button @click="goBack" type="default">返回</el-button>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            videos: [],
            selectedVideo: null,
            isAdmin: false,
            isMobile: false
        };
    },
    methods: {
        checkIfMobile(){
            this.isMobile = window.innerWidth <= 768;
        },
        checkIfAdmin(){
            this.isAdmin = sessionStorage.getItem("usertype") === "管理员";
        },
        async fetchVideos() {
            try {
                const response = await axios.get(this.$baseUrl + '/api/videos/review-video');
                this.videos = response.data;
            } catch (error) {
                this.$message.error('获取待审核视频失败: ' + error.message);
            }
        },
        async approveVideo(video) {
            const reviewer = localStorage.getItem('username') || 'admin';
            try {
                await axios.post(this.$baseUrl + `/api/videos/${video.id}/approve`, {
                    ...video,
                    status: '已审核',
                    reviewed_at: new Date().toISOString(),
                    reviewer: reviewer
                });
                this.$message.success('视频审核通过');
                this.fetchVideos();
            } catch (error) {
                this.$message.error('审核视频失败: ' + error.message);
            }
        },
        async rejectVideo(video) {
            try {
                await axios.post(this.$baseUrl + `/api/videos/${video.id}/reject`, {
                    ...video,
                    status: '已拒绝',
                    reviewed_at: new Date().toISOString(),
                    reviewer: 'admin'
                });
                this.$message.success('已拒绝');
                this.fetchVideos();
            } catch (error) {
                this.$message.error('拒绝视频失败: ' + error.message);
            }
        },
        confirmRejectVideo(video) {
            this.$confirm('您确定要拒绝吗？', '确认拒绝', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.rejectVideo(video);
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消操作'
                });
            });
        },
        handleRowClick(row) {
            this.selectedVideo = row;
        },
        goBack() {
            this.$router.push('/');
        }
    },
    created() {
        this.fetchVideos();
        this.checkIfAdmin();
    },
    mounted() {
        this.checkIfMobile();
        window.addEventListener('resize', this.checkIfMobile);
    },
    beforeDestroy() {
        window.removeEventListener('resize', this.checkIfMobile);
    }
};
</script>

<style scoped>
.mobile-card-list {
    display: flex;
    flex-direction: column;
    gap: 16px;
}

.mobile-card-content {
    display: flex;
    flex-direction: column;
}

.mobile-card-title {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 10px;
}

.mobile-card-uploader,
.mobile-card-description {
    font-size: 14px;
    margin-bottom: 8px;
}

.mobile-thumbnail-container {
    margin-bottom: 10px;
}

.mobile-thumbnail-img {
    width: 100%;
    height: auto;
    border-radius: 4px;
}

.mobile-video-preview {
    width: 100%;
    height: auto;
    margin-bottom: 10px;
}

.mobile-card-actions {
    display: flex;
    justify-content: space-between;
}
.video-check-title{
    text-align: center;
    font-size: 2rem;
    color: rgb(85,97,83);
    font-weight: 500;
}
.video-review-page {
    padding: 20px;
    background-image: url("@/assets/images/login.jpg");
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    background-attachment: fixed;
    height: 100vh;
    width: 100vw;
}

.thumbnail-img {
    width: 100px;
    height: auto;
}

.video-preview {
    width: 200px;
    height: auto;
}
</style>
