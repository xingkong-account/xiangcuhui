<template>
    <div class="approved-video-page">
        <div style="flex: 1">
            <h2>已审核视频</h2>
            <el-button @click="goBack" type="default">返回</el-button>
        </div>
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
            <el-table-column prop="reviewed_at" label="审核时间">
                <template slot-scope="scope">
                    {{ formatDate(scope.row.reviewed_at) }}
                </template>
            </el-table-column>
            <el-table-column prop="reviewer" label="审核员"></el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button @click="confirmDelete(scope.row)" type="danger">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            videos: [],  // 存储已审核的视频列表
            selectedVideo: null  // 当前选择的视频
        };
    },
    methods: {
        // 获取已审核的视频列表
        async fetchVideos() {
            try {
                const response = await axios.get(this.$baseUrl + '/api/videos/all');
                this.videos = response.data;
            } catch (error) {
                this.$message.error('获取已审核视频失败: ' + error.message);
            }
        },
        // 删除视频前的确认操作
        confirmDelete(video) {
            this.$confirm('确认删除该视频吗？', '删除确认', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.deleteVideo(video);
            }).catch(() => {
                this.$message.info('已取消删除');
            });
        },
        async deleteVideo(video) {
            try {
                await axios.delete(this.$baseUrl + `/api/videos/${video.id}`);
                this.$message.success('视频删除成功');
                this.fetchVideos();
            } catch (error) {
                this.$message.error('删除视频失败: ' + error.message);
            }
        },
        handleRowClick(row) {
            this.selectedVideo = row;
        },
        formatDate(dateString) {
            if (!dateString) return '';
            const date = new Date(dateString);
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            const hours = String(date.getHours()).padStart(2, '0');
            const minutes = String(date.getMinutes()).padStart(2, '0');
            const seconds = String(date.getSeconds()).padStart(2, '0');
            return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
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
.approved-video-page {
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
