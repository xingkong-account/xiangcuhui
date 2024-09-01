<template>
    <div class="edit-personal">
        <el-card>
            <div slot="header">
                <span>编辑个人会员</span>
            </div>
            <el-form :model="member" ref="form" label-width="100px" class="edit-form">
                <el-form-item label="姓名">
                    <el-input v-model="member.name" placeholder="请输入姓名"></el-input>
                </el-form-item>
                <el-form-item label="电话">
                    <el-input v-model="member.phone" placeholder="请输入电话"></el-input>
                </el-form-item>
                <el-form-item label="状态" v-if="isAdmin">
                    <el-select v-model="member.status" placeholder="请选择状态">
                        <el-option label="待审核" value="待审核"></el-option>
                        <el-option label="已审核" value="已审核"></el-option>
                        <el-option label="已拒绝" value="已拒绝"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm">提交</el-button>
                    <el-button @click="cancelEdit">取消</el-button>
                </el-form-item>
            </el-form>
        </el-card>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            member: {
                id: null,
                name: '',
                phone: '',
                status: '待审核'
            },
            isAdmin: false
        };
    },
    methods: {
        async fetchMemberData() {
            const memberId = this.$route.params.id;
            try {
                const response = await axios.get(this.$baseUrl + `/api/personal/${memberId}`);
                this.member = response.data;
            } catch (error) {
                console.error('Failed to fetch member data:', error);
                this.$message.error('无法获取会员数据');
            }
        },
        async submitForm() {
            try {
                await axios.post(this.$baseUrl + `/api/update-personal/${this.member.id}`, this.member);
                this.$message.success('会员信息更新成功');
                this.$router.push('/individual-members');
            } catch (error) {
                console.error('Failed to update member:', error);
                this.$message.error('会员信息更新失败');
            }
        },
        cancelEdit() {
            this.$router.push('/individual-members');
        },
        checkIfAdmin() {
            const username = sessionStorage.getItem('username');
            this.isAdmin = (username === 'admin');
        }
    },
    created() {
        this.fetchMemberData();
        this.checkIfAdmin();
    },
    beforeDestroy() {
        if (this.redirectTimer) {
            clearTimeout(this.redirectTimer);
        }
    }
};
</script>

<style scoped>
.edit-personal {
    padding: 20px;
}
.edit-form {
    max-width: 600px;
    margin: 0 auto;
}
</style>
