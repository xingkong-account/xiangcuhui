<template>
    <div class="video-review-page">
        <h1>视频审核</h1>
        <el-table
            :data="videos"
            style="width: 100%"
            :highlight-current-row="true"
            @row-click="handleRowClick">
            <el-table-column prop="title" label="标题"></el-table-column>
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
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button @click="approveVideo(scope.row)" type="success">通过</el-button>
                    <el-button @click="rejectVideo(scope.row)" type="danger">拒绝</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-button @click="goBack" type="default">返回</el-button>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            videos: [],  // 存储待审核的视频列表
            selectedVideo: null  // 当前选择的视频
        };
    },
    methods: {
        // 获取待审核的视频列表
        async fetchVideos() {
            try {
                const response = await axios.get('http://localhost:8081/api/videos/review-video');
                this.videos = response.data;
            } catch (error) {
                this.$message.error('获取待审核视频失败: ' + error.message);
            }
        },
        async approveVideo(video) {
            try {
                await axios.post(`http://localhost:8081/api/videos/${video.id}/approve`, {
                    ...video,
                    status: '已审核',
                    reviewed_at: new Date().toISOString(),
                    reviewer: 'admin'
                });
                this.$message.success('视频审核通过');
                this.fetchVideos();
            } catch (error) {
                this.$message.error('审核视频失败: ' + error.message);
            }
        },
        async rejectVideo(video) {
            try {
                await axios.post(`http://localhost:8081/api/videos/${video.id}/reject`, {
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
        handleRowClick(row) {
            this.selectedVideo = row;
        },
        goBack() {
            this.$router.push('/');
        }
    },
    created() {
        this.fetchVideos();
    }
};
</script>

<style scoped>
.video-review-page {
    padding: 20px;
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
