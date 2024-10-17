<template>
    <div class="individual-member-review" v-if="isAdmin">
        <div class="header-container">
            <el-button type="default" @click="goBack" class="go-back-button">
                <i class="el-icon-arrow-left"></i> 返回
            </el-button>
            <span class="header-title">个人会员审核</span>
        </div>

        <!-- 桌面端表格布局 -->
        <div class="table-container" v-show="!isMobile">
            <el-table :data="members" style="width: 100%;" border>
                <el-table-column
                    prop="name"
                    label="姓名"
                    width="150">
                </el-table-column>
                <el-table-column
                    prop="email"
                    label="邮箱"
                    width="180"
                ></el-table-column>
                <el-table-column
                    prop="type"
                    label="会员类型"
                    width="100"
                ></el-table-column>
                <el-table-column prop="phone" label="联系方式" width="150"></el-table-column>
                <el-table-column
                    prop="created_at"
                    label="注册日期"
                    width="180"
                    :formatter="(row, column, cellValue) => formatDate(cellValue)"
                ></el-table-column>
                <el-table-column
                    prop="updated_at"
                    label="修改日期"
                    width="180"
                    :formatter="(row, column, cellValue) => formatDate(cellValue)"
                ></el-table-column>
                <el-table-column prop="status" label="审核状态" width="120">
                    <template slot-scope="scope">
                        <el-tag :type="getStatusTagType(scope.row.status)" class="status-tag">
                            {{ scope.row.status }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="200">
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
                            v-else
                            size="small"
                            @click="viewDetails(scope.row)"
                        >
                            查看详情
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <!-- 手机端卡片布局 -->
        <div v-show="isMobile" class="mobile-card-list">
            <el-card
                v-for="(member, index) in members"
                :key="index"
                class="mobile-card"
                shadow="hover">
                <div class="mobile-card-content">
                    <h3 class="mobile-card-title">姓名：{{ member.name }}</h3>
                    <p class="mobile-card-type">邮箱: {{ member.email }}</p>
                    <p class="mobile-card-type">会员类型: {{ member.type }}</p>
                    <p class="mobile-card-phone">联系方式: {{ member.phone }}</p>
                    <p class="mobile-card-created-at">注册日期: {{ formatDate(member.created_at) }}</p>
                    <p class="mobile-card-updated-at">修改日期: {{ formatDate(member.updated_at) }}</p>
                    <el-tag :type="getStatusTagType(member.status)" class="status-tag">
                       {{ member.status }}
                    </el-tag>

                    <div class="mobile-card-actions">
                        <el-button
                            v-if="member.status === '待审核'"
                            size="small"
                            type="primary"
                            @click="approveMember(member)"
                        >
                            通过
                        </el-button>
                        <el-button
                            v-if="member.status === '待审核'"
                            size="small"
                            type="danger"
                            @click="confirmRejectMember(member)"
                        >
                            拒绝
                        </el-button>
                        <el-button
                            v-else
                            size="small"
                            @click="viewDetails(member)"
                        >
                            查看详情
                        </el-button>
                    </div>
                </div>
            </el-card>
        </div>

        <!-- 添加分页组件 -->
        <el-pagination
            @size-change="handlePageSizeChange"
            @current-change="handlePageChange"
            :current-page="currentPage"
            :page-size="pageSize"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            class="pagination"
        ></el-pagination>

        <!-- 详情对话框 -->
        <el-dialog
            title="会员详情"
            :visible.sync="dialogVisible"
            width="60%"
            @close="resetDialog"
            :before-close="resetDialog"
            class="detail-dialog"
        >
            <div v-html="dialogContent" class="dialog-content"></div>
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetDialog" class="dialog-close-button">关闭</el-button>
            </div>
        </el-dialog>
    </div>

    <div class="image-container" v-else>
        <p>您没有权限访问此页面。</p>
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
            isMobile: false,
            dialogVisible: false,
            dialogContent: '',
            redirectTimer: null,
            total: 0,  // 总记录数
            currentPage: 1,  // 当前页码
            pageSize: 10  // 每页显示的记录条数
        };
    },
    created() {
        this.fetchMembers();
        this.checkIfAdmin();
    },
    mounted() {
        this.checkIfMobile();
        window.addEventListener('resize', this.checkIfMobile);
    },
    beforeDestroy() {
        if (this.redirectTimer) {
            clearTimeout(this.redirectTimer);
        }
        window.removeEventListener('resize', this.checkIfMobile);
    },
    methods: {
        checkIfMobile(){
            this.isMobile = window.innerWidth <= 768;
        },
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
                const response = await axios.get(this.$baseUrl + '/api/unchecked-individuals', {
                    params: {
                        pageNum: this.currentPage,
                        pageSize: this.pageSize
                    }
                });
                this.members = response.data.data;
                this.total = response.data.total;
                // this.$alert(this.members[this.members.length - 1]);
                if (this.members.length === 0) {
                    this.$message.info('暂无数据');
                }
            } catch (error) {
                console.error('获取会员个人数据失败', error);
            }
        },
        confirmRejectMember(member) {
            this.$confirm('您确定要拒绝此会员吗？', '确认拒绝', {
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
                await axios.post(this.$baseUrl + `/api/individual-members/${member.id}/approve`);
                this.$message.success('会员审核通过');
                this.fetchMembers();
            } catch (error) {
                console.error('Failed to approve member:', error);
                this.$message.error('审核失败');
            }
        },
        async rejectMember(member) {
            try {
                await axios.post(this.$baseUrl + `/api/individual-members/${member.id}/reject`);
                this.$message.success('会员已拒绝');
                this.fetchMembers();
            } catch (error) {
                console.error('Failed to reject member:', error);
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
        viewDetails(member) {
            this.dialogContent = `
                <p><strong>姓名:</strong> ${member.name}</p>
                <p><strong>联系方式:</strong> ${member.phone}</p>
                <p><strong>注册日期:</strong> ${this.formatDate(member.created_at)}</p>
                <p><strong>修改日期:</strong> ${this.formatDate(member.updated_at)}</p>
                <p><strong>审核状态:</strong> ${member.status}</p>
            `;
            this.dialogVisible = true;
        },
        resetDialog() {
            this.dialogVisible = false;
            this.dialogContent = '';
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
        goBack(){
            this.$router.go(-1);
        }
    }
};
</script>

<style scoped>
.individual-member-review {
    padding: 20px;
    background-color: #f5f7fa;
    min-height: 100vh;
    width: 100%;
}

.header-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 10px 20px;
    border-bottom: 1px solid #e4e7ed;
    background-color: #ffffff;
    border-radius: 10px 10px 0 0;
}

.header-title {
    font-size: 18px;
    font-weight: bold;
    color: #333;
}

.go-back-button {
    margin-right: 20px;
}

.table-container {
    margin-top: 20px;
    background-color: #ffffff;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.status-tag {
    margin: 0;
}

.pagination {
    margin: 20px 0;
}

.detail-dialog .dialog-content {
    padding: 20px;
}

.dialog-footer {
    text-align: right;
}

.dialog-close-button {
    background-color: #409eff;
    color: #fff;
}
.individual-member-review {
    padding: 20px;
}
.box-card {
    max-width: 1000px;
    margin: 0 auto;
}
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

.image-container img.full-width {
    width: 100%;
}

.image-container img.thumbnail {
    width: auto;
    max-width: 200px;
    height: auto;
}

.approve-button {
    background-color: #4CAF50; /* 绿色背景色 */
    border-color: #4CAF50; /* 同样的边框颜色 */
}

.reject-button {
    background-color: #FF5722; /* 红色背景色 */
    border-color: #FF5722; /* 同样的边框颜色 */
}
/* 为按钮添加 hover 效果 */
.el-button:hover.approve-button {
    background-color: #45a049; /* 绿色背景色 hover 效果 */
    border-color: #45a049; /* 同样的边框颜色 */
}

.el-button:hover.reject-button {
    background-color: #e64a19; /* 红色背景色 hover 效果 */
    border-color: #e64a19; /* 同样的边框颜色 */
}
</style>
