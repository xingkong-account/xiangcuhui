<template>
    <div class="video-list">
        <div class="header">
            <el-button class="back-button" @click="goBack">返回</el-button>
            <h2 class="title">视频列表</h2>
        </div>
        <div class="search-bar">
            <el-input
                v-model="searchQuery"
                placeholder="搜索"
                prefix-icon="el-icon-search"
                class="search-input"
            />
            <button @click="searchVideos">搜索</button>
        </div>

        <div v-if="videos.length === 0" class="no-videos">暂无视频</div>
        <div v-else class="video-container">
            <div v-if="videos.length > 0" class="video-item-large">
                <div class="video-thumbnail" @click="playVideo(videos[0])">
                    <img :src="videos[0].thumbnail" alt="视频缩略图" />
                    <div class="video-play-button">▶</div>
                </div>
                <p>{{ truncateDescription(videos[0].description) }}</p>
            </div>

            <div class="video-row">
                <div
                    v-for="(video) in videos.slice(1)"
                    :key="video.id"
                    class="video-item"
                >
                    <div class="video-thumbnail" @click="playVideo(video)">
                        <img :src="video.thumbnail" alt="视频缩略图" />
                        <div class="video-play-button">▶</div>
                        <div class="video-description">
                            {{ truncateDescription(video.description) }}
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <el-dialog
            v-if="currentVideo"
            :visible.sync="videoDialogVisible"
            width="80%"
            center
            @close="closeVideoDialog"
        >
            <div class="video-player">
                <video
                    ref="videoPlayer"
                    controls
                    autoplay
                    :src="currentVideo.url"
                    style="width: 100%;"
                ></video>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            videos: [],
            searchQuery: '',
            videoDialogVisible: false,
            currentVideo: null
        };
    },
    methods: {
        async fetchVideos() {
            try {
                const response = await axios.get(this.$baseUrl + '/api/videos/all-checked');
                this.videos = response.data;
            } catch (error) {
                console.error('获取视频失败:', error);
                this.$message.error('获取视频失败，请稍后再试。');
            }
        },
        searchVideos() {

        },
        truncateDescription(description) {
            return description.length > 10 ? description.slice(0, 10) + '...' : description;
        },
        playVideo(video) {
            this.currentVideo = video;
            this.videoDialogVisible = true;
            console.log('播放视频:', video.url);
        },
        closeVideoDialog() {
            const videoElement = this.$refs.videoPlayer;
            if (videoElement) {
                videoElement.pause();
                videoElement.currentTime = 0;
            }
            this.videoDialogVisible = false;
            this.currentVideo = null;
        },
        goBack(){
            this.$router.push("/");
        }
    },
    created() {
        this.fetchVideos();
    }
};
</script>

<style scoped>
.header {
    align-items: center;
    margin-bottom: 20px;
}

.back-button{
     margin-left: 80px;
}

.video-list {
    padding: 20px;
    background-image: url('@/assets/images/VIdeoBg.png');
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    background-attachment: fixed;
    height: 100%;
    width: 100%;
}
.title{
    font-size: 20px;
    font-weight: 300;
    margin-left: 5%;
    color: rgb(231,244,237);
}
.search-bar {
    display: flex;
    justify-content: flex-start;
    margin-bottom: 20px;
    margin-left: 5%;
}

.search-bar input {
    padding: 10px;
    border-radius: 20px;
    border: none;
    margin-right: 10px;
    width: 200px;
}

.search-input {
    width: 200px;
    border-radius: 20px;
}


.search-bar button {
    padding: 10px 20px;
    border-radius: 20px;
    background-color: rgb(101,172,140);
    color: white;
    border: none;
    cursor: pointer;
    font-size: 16px;
}

.search-bar button:hover {
    padding: 10px 20px;
    border-radius: 20px;
    background-color: rgb(85, 145, 118);
    border-color: rgb(85, 145, 118);
    cursor: pointer;
}

.no-videos {
    text-align: center;
    font-size: 28px;
    color: #606266;
}

.video-container {
    display: flex;
    flex-direction: column;
    gap: 20px;
}

.video-item-large {
    display: flex;
    justify-content: center;
    width: 100%;
    height: 300px;
}

.video-item-large .video-thumbnail {
    width: 90%;
    height: 100%;
}

.video-description {
    position: absolute;
    bottom: 0;
    width: 100%;
    background-color: rgb(101, 172, 140);
    height: 10%;
    color: black;
    padding: 10px;
    font-size: 14px;
    text-align: center;
    transition: color 0.3s;
}

.video-thumbnail:hover .video-description {
    color: white;
    background-color: rgb(101, 172, 140);
}

.video-play-button {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    font-size: 20px;
    color: white;
    background-color: rgb(27,37,38);
    border-radius: 50%;
    width: 50px;
    height: 50px;
    display: flex;
    justify-content: center;
    align-items: center;
    scale: 1.5;
}

.video-play-button:hover {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    font-size: 20px;
    color: white;
    background-color: rgb(209, 64, 65);
    border-radius: 50%;
    width: 50px;
    height: 50px;
    display: flex;
    justify-content: center;
    align-items: center;
}

.video-row {
    display: flex;
    justify-content: space-between;
    gap: 70px;
    flex-wrap: wrap;
    scale: 0.9;
    overflow: hidden;
}

.video-item {
    flex: 1 1 30%;
    background: #fff;
    border-radius: 10px;
    padding: 0;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    display: flex;
    flex-direction: column;
    overflow: hidden;
}

.video-thumbnail {
    position: relative;
    cursor: pointer;
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    border-radius: 10px;
    overflow: hidden;
}

.video-thumbnail img {
    width: 100%;
    height: 300px;
    object-fit: cover;
    border-radius: inherit;
}

.video-description {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: rgb(196,200,203);
    padding: 5px 0;
    text-align: center;
    font-size: 26px;
    color: black;
    transition: color 0.3s;
}

.video-thumbnail:hover .video-description {
    color: white;
    background-color: rgb(100,171,141);
}

p {
    margin: 10px 0 0;
    text-align: center;
    font-size: 14px;
    color: #333;
}

.video-player {
    text-align: center;
}
</style>

