<template>
    <div class="team-members">
        <h2 class="section-header">团体会员展示</h2>
        <p class="section-subheader">Members Display</p>
        <div class="card-container">
            <el-card class="box-card" v-for="team in teamMembers" :key="team.id">
                <div slot="header" class="card-header">
                    <span>{{ team.name }}</span>
                </div>
                <div class="text item">
                    <el-image :src="team.image_url" alt="团队图片" class="team-image"></el-image>
                </div>
                <div class="text item description">
                    简介: {{ team.description }}
                </div>
                <div class="button-group">
                    <el-link :href="team.website" target="_blank" class="website-link">访问官网</el-link>
                    <el-button type="primary" @click="showDetails(team)" class="details-button">查看详情</el-button>
                </div>
            </el-card>
        </div>

        <el-dialog :visible.sync="dialogVisible" title="团队会员详情" width="50%">
            <div style="max-height: 400px; overflow-y: auto;">
                <img :src="selectedTeam.image_url" alt="团队图片" style="width: 100%; height: auto; margin-bottom: 10px;" />
                <p><strong>名称:</strong> {{ selectedTeam.name }}</p>
                <p><strong>电话:</strong> {{ selectedTeam.phone || '无' }}</p>
                <p><strong>简介:</strong> {{ selectedTeam.description }}</p>
                <p><strong>官网:</strong> <a :href="selectedTeam.website" target="_blank">{{ selectedTeam.website }}</a></p>
                <p><strong>更新时间:</strong> {{ formatDate(selectedTeam.updated_at) }}</p>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">关闭</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            teamMembers: [],
            dialogVisible: false,
            selectedTeam: {},
            total: 0,
            currentPage:1,
            pageSoze: 100
        };
    },
    created() {
        this.fetchTeamMembers();
    },
    methods: {
        async fetchTeamMembers() {
            try {
                const response = await axios.get(this.$baseUrl + '/api/team-members', {
                    params: {
                        pageNum: this.currentPage,
                        pageSize: this.pageSoze
                    }
                });
                this.teamMembers = response.data.data;
                this.total = response.data.total;
            } catch (error) {
                console.error('Failed to fetch team members:', error);
            }
        },
        showDetails(team) {
            this.selectedTeam = team;
            this.dialogVisible = true;
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
        }
    }
};
</script>

<style scoped>
.team-members {
    padding: 20px;
    background-image: url('@/assets/images/teamBg.jpg');
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    background-attachment: fixed;
    height: 100%;
    width: 100%;
}

.section-header {
    color: rgb(85,97,83);
    font-size: 30px;
    margin-bottom: 20px;
    margin-left: 70px;
    font-weight: 400;
}

.section-subheader {
    color: rgb(85,97,83);
    font-size: 18px;
    margin-bottom: 20px;
    margin-left: 70px;
}

.card-container {
    display: flex;
    flex-wrap: wrap;
    gap: 30px;
    justify-content: center;
    scale: 0.9;
}

.box-card {
    flex: 1 1 calc(30% - 30px);
    box-sizing: border-box;
    border-radius: 30px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    scale: 0.9;
}

.box-card:hover{
    transform: translateY(-10px);
}
.card-header {
    font-size: 22px;
    color: #333;
    text-align: center;
    font-weight: 400;
}

.team-image {
    border-radius: 30px;
    width: 100%;
    height: 250px;
    object-fit: cover;
}

.description {
    margin-top: 10px;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
}

.button-group {
    display: flex;
    justify-content: space-between;
    margin-top: 20px;
}

.website-link {
    margin-right: 10px;
    color: #409EFF;
    font-size: 20px;
}

.details-button {
    padding: 10px 20px;
    border-radius: 20px;
    background-color: rgb(85, 145, 118);
    border-color: rgb(85, 145, 118);
    color: #fff;
    cursor: pointer;
}

.details-button:hover {
    padding: 10px 20px;
    border-radius: 20px;
    background-color: rgb(147,181,165);
    border-color: rgb(147,181,165);
    cursor: pointer;
}

.dialog-footer {
    text-align: right;
}
</style>

