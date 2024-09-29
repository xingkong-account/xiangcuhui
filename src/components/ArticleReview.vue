<template>
    <div class="article-review" v-if="isAdmin">
        <el-card class="box-card" style="width: 100%; overflow: auto;">
            <div slot="header" class="header clearfix">
                <el-button type="default" @click="goBack" class="back-button">
                    <i class="el-icon-arrow-left"></i> 返回
                </el-button>
                <span class="header-title">文章审核</span>
            </div>
            <el-table :data="articles" class="article-table">
                <el-table-column prop="title" label="标题" min-width="220"></el-table-column>
                <el-table-column prop="category" label="分类" min-width="150"></el-table-column>
                <el-table-column prop="author" label="作者" min-width="150"></el-table-column>
                <el-table-column prop="source" label="来源" min-width="150"></el-table-column>
                <el-table-column
                    prop="created_at"
                    label="创建日期"
                    min-width="180"
                    :formatter="(row, column, cellValue) => formatDate(cellValue)"
                ></el-table-column>
                <el-table-column
                    prop="updated_at"
                    label="修改日期"
                    min-width="180"
                    :formatter="(row, column, cellValue) => formatDate(cellValue)"
                ></el-table-column>
                <el-table-column prop="status" label="审核状态" min-width="120">
                    <template slot-scope="scope">
                        <el-tag :type="getStatusTagType(scope.row.status)">
                            {{ scope.row.status }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" min-width="250">
                    <template slot-scope="scope">
                        <el-button
                            v-if="scope.row.status === '待审核'"
                            size="small"
                            type="primary"
                            @click="approveArticle(scope.row)"
                            class="approve-button"
                        >
                            通过
                        </el-button>
                        <el-button
                            v-if="scope.row.status === '待审核'"
                            size="small"
                            type="danger"
                            @click="confirmDeleteArticle(scope.row)"
                            class="reject-button"
                        >
                            拒绝
                        </el-button>
                        <el-button
                            v-if="scope.row.status === '待审核'"
                            size="small"
                            @click="viewDetails(scope.row)"
                        >
                            查看详情
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
            <div class="pagination-container">
                <el-col>
                    <span class="page-size-label">请选择单页展示的记录条数:</span>
                    <el-select style="width: 10%" v-model="pageSize" @change="handlePageSizeChange" class="page-size-selector">
                        <el-option label="10条/页" value="10"></el-option>
                        <el-option label="20条/页" value="20"></el-option>
                        <el-option label="50条/页" value="50"></el-option>
                    </el-select>
                    <el-pagination
                        class="pagination"
                        background
                        layout="prev, pager, next, total"
                        :total="total"
                        :page-size="pageSize"
                        :current-page="currentPage"
                        @current-change="handlePageChange"
                    ></el-pagination>
                </el-col>
            </div>
        </el-card>
        <el-dialog
            title="文章详情"
            :visible.sync="dialogVisible"
            :width="dialogWidth"
            @close="resetDialog"
            :before-close="resetDialog"
            class="dialog-container"
        >
            <div v-html="dialogContent" class="dialog-content"></div>
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetDialog">关闭</el-button>
            </div>
        </el-dialog>
    </div>
    <div class="no-access-container" v-else>
        <el-image src="https://th.bing.com/th/id/R.43154c2eed5a578eb233439593ab8bb7?rik=Dpp%2fywekYckTzQ&pid=ImgRaw&r=0&sres=1&sresct=1"></el-image>

        <p>您没有权限访问此页面。</p>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            isAdmin: false,
            articles: [],
            dialogVisible: false,
            dialogContent: '',
            dialogWidth: '75%',
            pageSize: 10,
            currentPage: 1,
            total: 0
        };
    },
    created() {
        this.fetchArticles();
        this.checkIfAdmin();
    },
    beforeDestroy() {
        if (this.redirectTimer) {
            clearTimeout(this.redirectTimer);
        }
    },
    watch: {
        dialogVisible(newVal) {
            if (newVal) {
                this.$nextTick(() => {
                    this.calculateDialogWidth();
                });
            }
        }
    },
    methods: {
        goBack() {
            this.$router.go(-1);
        },
        handlePageChange(page) {
            this.currentPage = page;
            this.fetchArticles();  // 当页码变化时，重新获取数据
        },
        handlePageSizeChange(size){
            this.pageSize = size;
            this.currentPage = 1;
            this.fetchArticles();
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
        async fetchArticles() {
            try {
                const response = await axios.get(this.$baseUrl + '/api/articles/articleList',
                    {
                        params: {
                            pageNum: this.currentPage,
                            pageSize: this.pageSize
                        }
                    });
                this.articles = response.data.data;
                this.total = response.data.total;
                if (this.articles.length === 0) {
                    this.$message.info('暂无数据');
                }
            } catch (error) {
                console.error('Failed to fetch articles:', error);
            }
        },
        async approveArticle(article) {
            try {
                await axios.post(this.$baseUrl + `/api/articles/${article.id}/approve`);
                this.$message.success('文章审核通过');
                this.fetchArticles();
            } catch (error) {
                console.error('Failed to approve article:', error);
                this.$message.error('审核失败');
            }
        },
        async rejectArticle(article) {
            try {
                await axios.post(this.$baseUrl + `/api/articles/${article.id}/reject`);
                this.$message.success('文章已拒绝');
                this.fetchArticles();
            } catch (error) {
                console.error('Failed to reject article:', error);
                this.$message.error('拒绝失败');
            }
        },
        confirmDeleteArticle(article) {
            // 在拒绝前显示确认对话框
            this.$confirm('您确定要拒绝此文章吗？', '确认拒绝', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.rejectArticle(article);
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消操作'
                });
            });
        },
        viewDetails(article) {
            this.dialogContent = `
                <p><strong>标题:</strong> ${article.title}</p>
                <p><strong>作者:</strong> ${article.author}</p>
                <p><strong>内容:</strong> ${article.content}</p>
                <p><strong>创建日期:</strong> ${this.formatDate(article.created_at)}</p>
                <p><strong>修改日期:</strong> ${this.formatDate(article.updated_at)}</p>
            `;
            this.dialogVisible = true;
        },
        resetDialog() {
            this.dialogVisible = false;
            this.dialogContent = '';
            this.dialogWidth = '75%'; // Reset to default width
        },
        calculateDialogWidth() {
            this.$nextTick(() => {
                const dialogContentElement = this.$el.querySelector('.dialog-content');
                if (dialogContentElement) {
                    const contentWidth = dialogContentElement.scrollWidth;
                    const maxWidth = window.innerWidth * 0.75; // Maximum width (e.g., 75% of window width)
                    this.dialogWidth = Math.min(contentWidth, maxWidth) + 'px';
                }
            });
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
        }
    }
};
</script>

<style scoped>
.article-review {
    padding: 10px;
    width: 100%;
}

.box-card {
    max-width: 100%;
    margin: 0 auto;
}

.no-access-container {
    text-align: center;
    margin-top: 20px;
}

.pagination {
    padding: 20px;
    text-align: center;
}

.dialog-content {
    word-wrap: break-word;
    overflow-wrap: break-word;
}

.el-dialog {
    max-width: 80%;
}

@media (max-width: 768px) {
    .el-dialog {
        width: 90%;
    }
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

.dialog-content p img {
    max-width: 100%;
    height: auto;
    display: block;
    margin: 0 auto;
}
</style>

