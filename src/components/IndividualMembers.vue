<template>
    <el-container>
        <el-header class="header">
        </el-header>
        <el-main>
            <el-card>
                <div slot="header">
                    <el-button type="default" @click="goBack" style="margin-right: 20px;">
                        <i class="el-icon-arrow-left"></i> 返回
                    </el-button>
                    <span>个人会员管理</span>
                    <el-button type="primary" @click="navigateToAddPage" style="float: right;" class="add-individual">新增会员</el-button>

                </div>
                <el-table :data="members" style="width: 100%">
                    <el-table-column prop="id" label="ID" width="50"></el-table-column>
                    <el-table-column prop="name" label="姓名" width="150"></el-table-column>
                    <el-table-column prop="phone" label="电话" width="150"></el-table-column>
                    <el-table-column prop="status" label="状态" width="100"></el-table-column>
                    <el-table-column prop="created_at" label="创建时间" width="180">
                        <template slot-scope="scope">
                            {{ formatDate(scope.row.created_at) }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="updated_at" label="更新时间" width="180">
                        <template slot-scope="scope">
                            {{ formatDate(scope.row.updated_at) }}
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="180">
                        <template slot-scope="scope">
                            <el-button
                                size="mini"
                                type="primary"
                                @click="editMember(scope.row)"
                                :disabled="!canEdit(scope.row)"
                                class="edit-button"
                            >编辑</el-button>
                            <el-button
                                size="mini"
                                type="danger"
                                @click="confirmDelete(scope.row.id)"
                                :disabled="!isAdmin"
                                class="delete-button"
                            >删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-card>

            <!-- 删除确认对话框 -->
            <el-dialog
                title="确认删除"
                :visible.sync="dialogVisible"
                width="30%"
                @close="resetDialog">
                <span>您确定要删除该会员吗？</span>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="resetDialog" type="info">取消</el-button>
                    <el-button type="primary" @click="deleteMember">确定</el-button>
                </div>
            </el-dialog>
        </el-main>
    </el-container>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            members: [],
            dialogVisible: false,
            currentMemberId: null,
            isAdmin: false,
            currentUserId: null
        };
    },
    methods: {
        async fetchMembers() {
            try {
                const response = await axios.get(this.$baseUrl + '/api/personal-list');
                this.members = response.data;
            } catch (error) {
                console.error('Failed to fetch personal members:', error);
            }
        },
        confirmDelete(id) {
            this.currentMemberId = id;
            this.dialogVisible = true;
        },
        async deleteMember() {
            if (this.currentMemberId) {
                try {
                    await axios.delete(this.$baseUrl + `/api/delete-personal/${this.currentMemberId}`);
                    this.$message.success('会员删除成功');
                    this.fetchMembers();
                    this.dialogVisible = false;
                } catch (error) {
                    console.error('Failed to delete member:', error);
                    this.$message.error('会员删除失败');
                }
            }
        },
        resetDialog() {
            this.dialogVisible = false;
            this.currentMemberId = null;
        },
        navigateToAddPage() {
            this.$router.push('/add-personal');
        },
        editMember(member) {
            this.$router.push({ path: `/edit-personal/${member.id}` });
        },
        checkIfAdmin() {
            const username = sessionStorage.getItem('username');
            this.isAdmin = (username === 'admin');
        },
        getCurrentUserId() {
            this.currentUserId = sessionStorage.getItem('userId');
        },
        canEdit(member) {
            return this.isAdmin || this.currentUserId === member.id.toString();
        },
        formatDate(date) {
            return new Date(date).toLocaleString();
        },
        goBack() {
            this.$router.go(-1)
        }
    },
    created() {
        this.fetchMembers();
        this.checkIfAdmin();
        this.getCurrentUserId();
    }
};
</script>

<style scoped>
.edit-button {
    background-color: #4CAF50; /* 绿色背景色 */
    border-color: #4CAF50; /* 同样的边框颜色 */
}

.delete-button {
    background-color: #FF5722; /* 红色背景色 */
    border-color: #FF5722; /* 同样的边框颜色 */
}
/* 为按钮添加 hover 效果 */
.el-button:hover.edit-button {
    background-color: #45a049; /* 绿色背景色 hover 效果 */
    border-color: #45a049; /* 同样的边框颜色 */
}

.el-button:hover.delete-button {
    background-color: #e64a19; /* 红色背景色 hover 效果 */
    border-color: #e64a19; /* 同样的边框颜色 */
}

.add-individual{
    background-color: #FFC107; /* 设置为你想要的颜色 */
    border-color: #FFC107; /* 设置边框颜色 */
    color: #fff;
}

.add-individual:hover {
    background-color: #FFD54F; /* 悬停时的背景色 */
    border-color: #FFD54F; /* 悬停时的边框颜色 */
}
</style>
