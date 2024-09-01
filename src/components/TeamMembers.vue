<template>
    <div class="team-members">
        <el-card class="box-card" v-for="team in teamMembers" :key="team.id">
            <div slot="header" class="clearfix">
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
            selectedTeam: {}
        };
    },
    created() {
        this.fetchTeamMembers();
    },
    methods: {
        async fetchTeamMembers() {
            try {
                const response = await axios.get(this.$baseUrl + '/api/team-members?type=团体会员');
                this.teamMembers = response.data;
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
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
    padding: 20px;
}
.box-card {
    width: 300px;
    padding: 20px;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
}
.team-image {
    width: 100%;
    height: auto;
    margin-bottom: 10px;
}
.text.item {
    margin-bottom: 10px;
}
.text.item.description {
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
    overflow: hidden;
    text-overflow: ellipsis;
    margin-bottom: 10px;
}
.button-group {
    margin-top: auto;
    display: flex;
    justify-content: space-between;
    width: 100%;
}
.website-link {
    color: #409EFF;
    text-decoration: none;
}
.details-button {
    margin-left: 10px;
}
</style>
