<template>
    <div class="approved-video-page" :style="{ backgroundImage: `url('/mnt/data/fc6c974cbaa769c48ed15bde8e12940.png')`, backgroundSize: 'cover', backgroundPosition: 'center' }">
        <div class="header">
            <el-button @click="goBack" type="default" class="back-button">返回</el-button>
            <h2 class="video-title">{{ isAdmin ? '全部视频' : '我发布的视频' }}</h2>
        </div>

        <el-table
            v-if="isAdmin"
            :data="videos"
            style="width: 100%; background-color: white;"
            :highlight-current-row="true"
            @row-click="handleRowClick"
            class="video-table"
        >
            <el-table-column prop="uploader" label="上传人" width="120"></el-table-column>
            <el-table-column prop="title" label="标题"></el-table-column>
            <el-table-column prop="status" label="状态" width="100"></el-table-column>
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
            <el-table-column label="操作" width="180">
                <template slot-scope="scope">
                    <el-button @click="editVideo(scope.row)" type="warning" class="edit-button">编辑</el-button>
                    <el-button @click="confirmDelete(scope.row)" type="danger" class="delete-button">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-table
            v-else
            :data="myVideos"
            style="width: 100%; background-color: white;"
            :highlight-current-row="true"
            @row-click="handleRowClick"
            class="video-table"
        >
            <el-table-column prop="uploader" label="发布人" width="120"></el-table-column>
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
                    <el-button @click="editVideo(scope.row)" type="warning" class="edit-button">编辑</el-button>
                    <el-button @click="confirmDelete(scope.row)" type="danger" class="delete-button">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!--编辑视频的对话框-->
        <el-dialog
            :visible.sync="dialogVisible"
            title="编辑视频"
            max-width="50%"
            @close="handleCancel"
        >
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
                        还可以输入：{{100 - video.description.length}}/100 字
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
                    <el-image
                        v-if="videoForm.thumbnail"
                        :src="videoForm.thumbnail"
                        style="width: 100px; margin-top: 10px;"
                    ></el-image>
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
                    <video
                        v-if="videoForm.url"
                        :src="videoForm.url"
                        style="width: 100px; margin-top: 10px;"
                    ></video>
                </el-form-item>
                <el-form-item label="状态" v-if="isAdmin">
                    <el-select v-model="video.status" placeholder="选择状态">
                        <el-option label="待审核" value="待审核"></el-option>
                        <el-option label="通过" value="已审核"></el-option>
                        <el-option label="拒绝" value="已拒绝"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="状态" v-else>
                    <el-select v-model="video.status" placeholder="选择状态" :disabled="true">
                        <el-option label="待审核" value="待审核"></el-option>
                        <el-option label="通过" value="已审核"></el-option>
                        <el-option label="拒绝" value="已拒绝"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="updateVideo" class="update-btn">更新</el-button>
                    <el-button type="primary" @click="handleCancel" class="cancel-btn">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>


<script>
import axios from 'axios';

export default {
    data() {
        return {
            videos:[],
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
            myVideos: [],
            selectedVideo: null,
            username: '',
            uploader: '',
            videoForm: [],
            isAdmin: false,
            dialogVisible: false,
        };
    },
    methods: {
        editVideo(video) {
            this.dialogVisible = true;
            this.fetchVideoDetails(video.id);
        },
        fetchVideoDetails(id) {
            axios.get(this.$baseUrl + `/api/videos/${id}`).then(response => {
                this.video = response.data;
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
            if (!this.isAdmin){
                this.video.status = "待审核";
            }
            axios.post(this.$baseUrl + '/api/videos/update', this.video)
                .then(() => {
                    this.$message.success('更新成功');
                    this.dialogVisible = false;
                    if (this.isAdmin){
                        this.fetchVideos();
                    }
                    else {
                        this.fetchMyVideos();
                    }
                })
                .catch(() => {
                    this.$message.error('更新失败');
                });
        },
        checkIfAdmin() {
            this.isAdmin = (sessionStorage.getItem("usertype") === "管理员");
        },
        handleCancel() {
            this.dialogVisible = false;
        },
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
                if (this.isAdmin) {
                    await this.fetchVideos();
                } else {
                    await this.fetchMyVideos();
                }
            } catch (error) {
                this.$message.error('删除视频失败: ' + error.message);
            }
        },
        confirmDelete(video) {
            this.$confirm('确定要删除该视频吗？', '确认删除', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.deleteVideo(video);
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        // editVideo(video) {
        //     this.$router.push({ name: 'EditVideo', params: { id: video.id } });
        // },
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
.update-btn {
    background-color: rgb(101,172,140);
    border-color: rgb(101,172,140);
    color: white;
    border-radius: 5px;
    transition: background-color 0.3s ease, transform 0.2s ease;
}

.update-btn:hover {
    background-color: rgb(91, 155, 126) !important;
    border-color: rgb(91, 155, 126) !important;
    cursor: pointer;
    transform: scale(1.05);
}

.update-btn:active {
    background-color: rgb(101,172,140);
    transform: scale(0.98);
}

.cancel-btn {
    background-color: #f56c6c;
    color: white;
    border-radius: 5px;
    transition: background-color 0.3s ease, transform 0.2s ease;
}

.cancel-btn:hover {
    background-color: #ff9999;
    transform: scale(1.05);
}

.cancel-btn:active {
    background-color: #d9534f;
    transform: scale(0.98);
}

.edit-dialog .el-dialog__header {
    background-color: #f5f7fa;
    border-bottom: none;
}

.edit-dialog .el-dialog__body {
    padding: 20px;
}

.edit-dialog .el-form {
    margin-top: 15px;
}

.edit-dialog .el-dialog__footer {
    border-top: none;
    text-align: right;
    padding-top: 20px;
}

.approved-video-page {
    height: 100vh;
    width: 100vw;
    padding: 20px;
}

.header {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
}

.back-button {
    background-color: #b3d4c4;
    color: rgb(85,97,83);
    justify-content: flex-start;
    border-radius: 30px;
    width: 6%;
    font-weight: 600;
    font-size: 16px;
}

.back-button:hover {
    background-color: rgb(91, 155, 126) !important;
    border-color: rgb(91, 155, 126) !important;
    cursor: pointer;
}

.video-title {
    font-size: 28px;
    font-weight: 500;
    color: rgb(85,97,83);
    margin-left: 40%;
}

.video-table {
    border-radius: 8px;
    overflow: hidden;
    background-color: #fff;
    font-size: 16px;
}

.thumbnail-img {
    width: 140px;
    height: 80px;
    border-radius: 8px;
}

.video-preview {
    width: 150px;
    height: auto;
}

.el-table__header-wrapper {
    background-color: #f5f5f5;
    color: #333;
}

.el-table-column--last {
    text-align: center;
}

.el-button {
    margin-right: 5px;
}

.edit-button {
    background-color: rgb(147, 181, 165);
    color: white;
}

.edit-button:hover {
    background-color: rgb(129, 158, 144);
    cursor: pointer;
}

.edit-button:active {
    background-color: rgb(110, 135, 122);
}

.delete-button {
    color: white;
}

.delete-button:hover {
    background-color: rgb(144, 72, 72);
    cursor: pointer;
}

.delete-button:active {
    background-color: rgb(120, 60, 60);
}

@media (max-width: 768px) {
    .video-title {
        font-size: 20px;
        margin-left: 0;
        text-align: center;
    }

    .header {
        flex-direction: column;
        align-items: flex-start;
    }

    .back-button {
        width: 100%;
        margin-bottom: 10px;
    }
}
</style>
