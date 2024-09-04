<template>
    <el-container class="m">
        <el-header class="header">
            <span class="header-title">个人会员管理</span>
        </el-header>
        <el-main>
            <el-card class="member-card">
                <div slot="header" class="card-header">
                    <el-button type="default" @click="goBack" class="back-button">
                        <i class="el-icon-arrow-left"></i> 返回
                    </el-button>
                    <el-button type="primary" @click="navigateToAddPage" class="add-individual">
                        新增会员
                    </el-button>
                </div>
                <el-table :data="members" class="member-table">
                    <el-table-column prop="id" label="ID" width="60"></el-table-column>
                    <el-table-column prop="name" label="姓名" width="150"></el-table-column>
                    <el-table-column prop="phone" label="电话" width="150"></el-table-column>
                    <el-table-column prop="status" label="状态" width="120"></el-table-column>
                    <el-table-column prop="created_at" label="创建时间" width="200">
                        <template slot-scope="scope">
                            {{ formatDate(scope.row.created_at) }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="updated_at" label="更新时间" width="200">
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
.m {
    background-image: url("@/assets/images/register.jpg");
}
.header {
    background-color: #f5f5f5;
    padding: 0 20px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-bottom: 1px solid #ddd;
}

.header-title {
    font-size: 20px;
    font-weight: bold;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.back-button {
    background-color: #e0e0e0;
    border-color: #e0e0e0;
}

.add-individual {
    background-color: rgb(101, 172, 140);
    border-color: rgb(101, 172, 140);
    color: #fff;
}

.add-individual:hover {
    background-color: rgb(85, 145, 118);
    border-color: rgb(85, 145, 118);
}

.edit-button {
    background-color: #4CAF50;
    border-color: #4CAF50;
}

.delete-button {
    background-color: #FF5722;
    border-color: #FF5722;
}

.el-button:hover.edit-button {
    background-color: #45a049;
    border-color: #45a049;
}

.el-button:hover.delete-button {
    background-color: #e64a19;
    border-color: #e64a19;
}

.member-card {
    margin: 20px auto;
}

.member-table {
    margin-top: 20px;
}

.dialog-footer {
    display: flex;
    justify-content: flex-end;
}
</style>
