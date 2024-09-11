<template>
    <div class="approved-video-page">
        <div style="flex: 1" v-if="isAdmin">
            <h2 class="video-title">全部视频</h2>
            <el-button @click="goBack" type="default">返回</el-button>
        </div>
        <div style="flex: 1" v-else>
            <h2 class="video-title">我发布的视频</h2>
            <el-button @click="goBack" type="default">返回</el-button>
        </div>
        <el-table
            v-if="isAdmin"
            :data="videos"
            style="width: 100%"
            :highlight-current-row="true"
            @row-click="handleRowClick">
            <el-table-column prop="uploader" label="上传人"></el-table-column>
            <el-table-column prop="title" label="标题"></el-table-column>
            <el-table-column prop="status" label="状态"></el-table-column>
            <el-table-column prop="description" label="描述"></el-table-column>
            <el-table-column prop="thumbnail" label="缩略图">
                <template slot-scope="scope">
                    <img :src="scope.row.thumbnail" alt="缩略图" class="thumbnail-img" />
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
                    <el-button @click="editVideo(scope.row)" type="warning" size="mini">编辑</el-button>
                    <el-button @click="confirmDelete(scope.row)" type="danger" size="mini">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-table
            v-else
            :data="myVideos"
            style="width: 100%"
            :highlight-current-row="true"
            @row-click="handleRowClick">
            <el-table-column prop="uploader" label="发布人"></el-table-column>
            <el-table-column prop="created_at" label="发布时间">
                <template slot-scope="scope">
                    {{ formatDate(scope.row.created_at) }}
                </template>
            </el-table-column>
            <el-table-column prop="updated_at" label="更新时间">
                <template slot-scope="scope">
                    {{ formatDate(scope.row.updated_at) }}
                </template>
            </el-table-column>
            <el-table-column prop="title" label="标题"></el-table-column>
            <el-table-column prop="description" label="描述"></el-table-column>
            <el-table-column prop="thumbnail" label="缩略图">
                <template slot-scope="scope">
                    <img :src="scope.row.thumbnail" alt="缩略图" class="thumbnail-img" />
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
                    <el-button @click="editVideo(scope.row)" type="warning" size="mini">编辑</el-button>
                    <el-button @click="confirmDelete(scope.row)" type="danger" size="mini">删除</el-button>
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
            videos: [],
            myVideos: [],
            selectedVideo: null,
            isAdmin: false,
            username: '',
            uploader: ''
        };
    },
    methods: {
        async fetchVideos() {
            try {
                const response = await axios.get(this.$baseUrl + '/api/videos/all');
                this.videos = response.data;
            } catch (error) {
                this.$message.error('获取已审核视频失败: ' + error.message);
            }
        },
        async fetchMyVideos() {
            try {
                const response = await axios.get(this.$baseUrl + '/api/videos/my-videos', {
                    params: {
                        uploader: this.uploader
                    }
                });
                this.myVideos = response.data;
            } catch (error) {
                this.$message.error('获取我发布的视频失败: ' + error.message);
            }
        },
        async deleteVideo(video) {
            try {
                await axios.delete(this.$baseUrl + `/api/videos/${video.id}`);
                this.$message.success('视频删除成功');
                // Refresh data based on user role
                if (this.isAdmin) {
                    await this.fetchVideos();
                } else {
                    await this.fetchMyVideos();
                }
            } catch (error) {
                this.$message.error('删除视频失败: ' + error.message);
            }
        },
        editVideo(video) {
            this.$router.push({ name: 'EditVideo', params: { id: video.id } });
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
        },
        async checkUserRole() {
            const username = sessionStorage.getItem('username');
            if (!username) {
                console.log('未登录或无法获取用户名');
                return '';
            }
            try {
                this.isAdmin = sessionStorage.getItem("usertype") === "管理员";
                this.uploader = username;
                if (this.isAdmin) {
                    this.fetchVideos();
                } else {
                    this.fetchMyVideos();
                }
            } catch (error) {
                console.log('获取用户角色失败:', error);
                throw error;
            }
        }
    },
    created() {
        this.username = sessionStorage.getItem('username');
        if (this.username) {
            this.checkUserRole()
                .catch(error => {
                    console.error('获取用户角色失败:', error);
                });
        }
    }
};
</script>


<style scoped>
.video-title{
    text-align: center;
}
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
