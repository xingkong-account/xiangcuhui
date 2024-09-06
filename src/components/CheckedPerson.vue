<template>
    <div class="individual-member-review" v-if="isAdmin">
        <div class="header-container">
            <el-button type="default" @click="goBack" class="go-back-button">
                <i class="el-icon-arrow-left"></i> 返回
            </el-button>
            <span class="header-title">已审核个人会员</span>
        </div>

<!--        <div>-->
<!--            <h3>测试输出:</h3>-->
<!--            <pre>{{ members }}</pre>-->
<!--        </div>-->
        <div class="table-container">
            <el-table :data="members" style="width: 100%;" border>
                <el-table-column prop="name" label="姓名" width="150"></el-table-column>
                <el-table-column
                    prop="type"
                    label="会员类型"
                    width="180"
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
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button
                            size="small"
                            @click="viewDetails(scope.row)"
                        >
                            查看详情
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
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
                const response = await axios.get(this.$baseUrl + '/api/processed-members', {
                    params: {
                        pageNum: this.currentPage,
                        pageSize: this.pageSize
                    }
                });
                this.members = response.data.data;
                this.total = response.data.total;
                if (this.members.length === 0) {
                    this.$message.info('暂无数据');
                }
            } catch (error) {
                console.error('获取会员个人数据失败', error);
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
        checkIfAdmin() {
            const username = sessionStorage.getItem('username');
            this.isAdmin = (username === 'admin');
            if (!this.isAdmin) {
                this.$message.warning('您没有权限访问此页面');
                this.redirectTimer = setTimeout(() => {
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
.processed-member-review {
    padding: 20px;
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
.header-row {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
}
.table-container {
    margin-top: 20px;
    background-color: #ffffff;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.back-button {
    margin-right: 20px;
}

.page-title {
    font-size: 18px;
    font-weight: bold;
}

.el-table {
    border: 1px solid #ebeef5;
}

.el-pagination {
    margin-top: 20px;
}

.dialog-footer {
    text-align: right;
}
</style>
