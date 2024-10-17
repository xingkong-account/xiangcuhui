<template>
    <div class="team-member-review" v-if="isAdmin">
        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <el-button type="default" @click="goBack" style="margin-right: 20px;">
                    <i class="el-icon-arrow-left"></i> 返回
                </el-button>
                <span>团队会员审核</span>
            </div>
            <el-table :data="members" style="width: 100%">
                <el-table-column prop="name" label="团队名称" width="150"></el-table-column>
                <el-table-column prop="email" label="团队邮箱" width="150"></el-table-column>
                <el-table-column prop="phone" label="联系电话" width="150"></el-table-column>
                <el-table-column prop="image_url" label="团体图片" width="120">
                    <template v-slot="scope">
                        <div class="image-container">
                            <img :src="scope.row.image_url" alt="团体图片" class="team-image">
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="created_at" label="注册日期" width="180">
                    <template slot-scope="scope">
                        {{ formatDate(scope.row.created_at) }}
                    </template>
                </el-table-column>
                <el-table-column prop="created_at" label="修改日期" width="180">
                    <template slot-scope="scope">
                        {{ formatDate(scope.row.updated_at) }}
                    </template>
                </el-table-column>
                <el-table-column prop="status" label="审核状态" width="120">
                    <template slot-scope="scope">
                        <el-tag :type="getStatusTagType(scope.row.status)">
                            {{ scope.row.status }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="250">
                    <template slot-scope="scope">
                        <el-button
                            v-if="scope.row.status === '待审核'"
                            size="small"
                            type="primary"
                            @click="approveMember(scope.row)"
                            class="approve-button"
                        >
                            通过
                        </el-button>
                        <el-button
                            v-if="scope.row.status === '待审核'"
                            size="small"
                            type="danger"
                            @click="confirmRejectMember(scope.row)"
                            class="reject-button"
                        >
                            拒绝
                        </el-button>
                        <el-button
                            size="small"
                            @click="openDialog(scope.row)"
                        >
                            查看详情
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-card>

        <el-pagination
            @size-change="handlePageSizeChange"
            @current-change="handlePageChange"
            :current-page="currentPage"
            :page-size="pageSize"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            style="margin-left: 80px"
            class="pagination"
        ></el-pagination>

        <!-- 详情对话框 -->
        <el-dialog
            title="团队会员详情"
            :visible.sync="dialogVisible"
            width="60%"
            @close="resetDialog"
            :before-close="resetDialog">
            <div v-html="dialogContent"></div>
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetDialog">关闭</el-button>
            </div>
        </el-dialog>
    </div>
    <div class="image-container" v-else>
        <img src="@/assets/images/403.png">
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            members: [],
            isAdmin: false,
            dialogVisible: false,
            dialogContent: '',
            redirectTimer: null,
            total: 0,
            currentPage: 1,
            pageSize: 10
        };
    },
    created() {
        this.fetchMembers();
        this.checkIfAdmin();
    },
    beforeDestroy() {
        if (this.redirectTimer) {
            clearTimeout(this.redirectTimer);
        }
    },
    methods: {
        handlePageChange(page) {
            this.currentPage = page;
            this.fetchMembers();
        },
        handlePageSizeChange(size){
            this.pageSize = size;
            this.currentPage = 1;
            this.fetchMembers();
        },
        async fetchMembers() {
            try {
                const response = await axios.get(this.$baseUrl + '/api/unchecked-teams',{
                    params: {
                        pageNum: this.currentPage,
                        pageSize: this.pageSize
                    }
                });
                this.members = response.data.data;
                this.total = response.data.total;
                // await this.$alert("总共" + this.total);
                if (this.members.length === 0) {
                    this.$message.info("暂无数据");
                }
            } catch (error) {
                console.error('Failed to fetch team members:', error);
            }
        },
        confirmRejectMember(member) {
            this.$confirm('您确定要拒绝此团队会员吗？', '确认拒绝', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.rejectMember(member);
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消操作'
                });
            });
        },
        formatDate(dateString) {
            try {
                const date = new Date(dateString);
                if (isNaN(date.getTime())) {
                    console.error('Invalid date:', dateString);
                    return dateString;
                }
                const year = date.getFullYear();
                const month = ('0' + (date.getMonth() + 1)).slice(-2); // 月份从0开始，需要加1并补零
                const day = ('0' + date.getDate()).slice(-2);
                const hours = ('0' + date.getHours()).slice(-2);
                const minutes = ('0' + date.getMinutes()).slice(-2);
                const seconds = ('0' + date.getSeconds()).slice(-2);

                return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
            } catch (error) {
                console.error('Failed to format date:', error);
                return dateString;
            }
        },
        getStatusTagType(status) {
            switch (status) {
                case '待审核':
                    return 'warning';
                case '已审核':
                    return 'success';
                case '已拒绝':
                    return 'danger';
                default:
                    return '';
            }
        },
        async approveMember(member) {
            try {
                await axios.post(this.$baseUrl + `/api/team-members/${member.id}/approve`);
                this.$message.success('团队会员审核通过');
                this.fetchMembers();
            } catch (error) {
                console.error('Failed to approve team member:', error);
                this.$message.error('审核失败');
            }
        },
        async rejectMember(member) {
            try {
                await axios.post(this.$baseUrl + `/api/team-members/${member.id}/reject`);
                this.$message.success('团体会员已拒绝');
                this.fetchMembers();
            } catch (error) {
                console.error('Failed to reject team member:', error);
                this.$message.error('拒绝失败');
            }
        },
        checkIfAdmin() {
            const usertype = sessionStorage.getItem('usertype');
            this.isAdmin = (usertype === '管理员');
            if (!this.isAdmin) {
                this.$message.warning('您没有权限访问此页面');
                setTimeout(() => {
                }, 3000);
            }
        },
        openDialog(member) {
            this.dialogContent = `
                <p><strong>团队名称:</strong> ${member.name}</p>
                <p><strong>图片:</strong></p>
                <img src="${member.image_url}" alt="Team Image" style="max-width: 100%; height: auto; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);">
                <p><strong>联系电话:</strong> ${member.phone}</p>
                <p><strong>注册日期:</strong> ${this.formatDate(member.created_at)}</p>
                <p><strong>审核状态:</strong> ${member.status}</p>
                <p><strong>简介:</strong> ${member.description}</p>
                <p><strong>官网链接:</strong> <a href="${member.website}" target="_blank">${member.website}</a></p>
            `;
            this.dialogVisible = true;
        },
        resetDialog() {
            this.dialogVisible = false;
            this.dialogContent = '';
        },
        goBack(){
            this.$router.go(-1);
        }
    }
};
</script>

<style scoped>
.image-container {
    text-align: center;
    margin: 20px 0;
}

.image-container img {
    max-width: 100%;
    height: auto;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.image-container img.thumbnail {
    width: auto;
    max-width: 200px;
    height: auto;
}

.team-image {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    scale: 0.8;
    justify-content: center;
    align-items: center;
    margin-right: 40px;
}

.team-member-review {
    padding: 20px;
}
.box-card {
    max-width: 100%;
    margin: 0 auto;
}
.image-container {
    text-align: center;
    margin: 20px 0;
}
.dialog-footer {
    text-align: right;
}
.approve-button {
    background-color: #4CAF50;
    border-color: #4CAF50;
}

.reject-button {
    background-color: #FF5722;
    border-color: #FF5722;
}

.el-button:hover.approve-button {
    background-color: #45a049;
    border-color: #45a049;
}

.el-button:hover.reject-button {
    background-color: #e64a19;
    border-color: #e64a19;
}
</style>
