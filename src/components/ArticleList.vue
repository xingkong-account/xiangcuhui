<template>
    <el-container>
        <el-header class="header">
            <div class="header-container">
                <h1>文章管理</h1>
            </div>
            <div class="button-group">
                <el-button class="add-article" v-if="check" style="margin-left: 10%" type="primary" @click="navigateToAddArticle">添加文章</el-button>
                <el-button @click="navigateBack" type="default">返回</el-button>
            </div>
        </el-header>

        <el-main>
            <el-container class="content-container">
                <el-row>
                    <el-col :span="24">
                        <el-table :data="articles" stripe>
                            <el-table-column prop="title" label="标题" width="180"></el-table-column>
                            <el-table-column prop="category" label="分类" width="120"></el-table-column>
                            <el-table-column prop="content" label="内容" width="300">
                                <template #default="{ row }">
                                    <div class="article-content">
                                        {{ row.content }}
                                    </div>
                                </template>
                            </el-table-column>
                            <el-table-column prop="created_at" label="创建时间" width="180">
                                <template #default="{ row }">
                                    {{ formatDate(row.created_at) }}
                                </template>
                            </el-table-column>
                            <el-table-column prop="updated_at" label="修改时间" width="180">
                                <template #default="{ row }">
                                    {{ formatDate(row.updated_at) }}
                                </template>
                            </el-table-column>
                            <el-table-column label="操作" width="300">
                                <template #default="{ row }">
                                    <!-- 操作按钮 -->
                                    <el-button
                                        :disabled="!isAdmin"
                                        @click="navigateToEditArticle(row.id)"
                                        type="primary"
                                        size="small"
                                        class="edit-button"
                                    >
                                        编辑
                                    </el-button>
                                    <el-button
                                        :disabled="!isAdmin"
                                        @click="deleteArticle(row.id)"
                                        type="danger"
                                        size="small"
                                        class="delete-button"
                                    >
                                        删除
                                    </el-button>
                                    <el-button
                                        @click="navigateToDetailPage(row.id)"
                                        type="primary"
                                        size="small"
                                        class="detail-button"
                                    >
                                        查看详情
                                    </el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-col>
                </el-row>

            </el-container>
        </el-main>
        <el-row class="filter-row">
            <el-col :span="24" >
                <span style="margin-left: 10%">请选择单页展示的记录条数:</span>
                <el-select style="width: 10%" v-model="pageSize" @change="handlePageSizeChange" class="page-size-selector">
                    <el-option label="10条/页" value="10"></el-option>
                    <el-option label="20条/页" value="20"></el-option>
                    <el-option label="50条/页" value="50"></el-option>
                </el-select>
                <!-- 分页组件 -->
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
        </el-row>

        <!--   底部-->
        <el-footer class="custom-footer">
            <div class="footer-content">
                <div class="footer-left">
                    <img src="http://localhost:8081/image.png" width="200px" height="120px" class="">
                </div>
                <div class="footer-center">
                    <p><a href="#">网站地图</a> | <a href="#">联系方式</a> | <a href="#">使用帮助</a> | <a href="#">隐私声明</a></p>
                    <p>主办单位:  乡促会&nbsp;&nbsp;&nbsp; 备案号：</p>
                    <p>地址: 甘肃省兰州市XXXXXX</p>
                </div>
                <div class="footer-right">
                    <img src="" alt="Security" class="security-logo">
                    <p>甘公网安备 235487154313号</p>
                    <p>网站标识码：0000000000</p>
                </div>
            </div>
        </el-footer>
    </el-container>
</template>

<script>
import axios from 'axios';

export default {
    name: 'ArticleList',
    data() {
        return {
            articles: [],
            total: 0,  // 总记录数
            currentPage: 1,  // 当前页码
            pageSize: 10,  // 每页显示的记录条数
            isAdmin: false,
            username: ''
        };
    },
    methods: {
        checkUserRole() {
            const username = sessionStorage.getItem('username');
            if (!username) {
                console.log('未登录或无法获取用户名');
                return Promise.resolve('');
            }

            return axios.get(this.$baseUrl + '/api/user/role', { params: { username } })
                .then(response => response.data.role)
                .catch(error => {
                    console.log('获取用户角色失败:', error);
                    throw error;
                });
        },
        check(){
            return this.isAdmin || this.username !== null;
        },
        navigateToAddArticle() {
            this.$router.push('/articles/create');
        },
        navigateToEditArticle(id) {
            this.$router.push(`/articles/edit/${id}`);
        },
        navigateToDetailPage(id) {
            this.$router.push(`/articles/${id}`);
        },
        deleteArticle(id) {
            this.$confirm('确定要删除这篇文章吗?', '警告', {
                confirmButtonText: '删除',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                axios.delete(this.$baseUrl + `/api/articles/${id}`)
                    .then(() => {
                        this.$message.success('删除成功');
                        this.fetchArticles();
                    })
                    .catch(error => {
                        this.$message.error('删除失败: ' + error.message);
                    });
            }).catch(() => {});
        },
        fetchArticles() {
            axios.get(this.$baseUrl + '/api/articles/articleList', {
                params: {
                    pageNum: this.currentPage,
                    pageSize: this.pageSize
                }
            })
                .then(response => {
                    this.articles = response.data.data;  // 获取当前页的数据
                    this.total = response.data.total;  // 获取总记录数
                })
                .catch(error => {
                    this.$message.error('获取文章列表失败: ' + error.message);
                });
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
        navigateBack() {
            this.$router.push("/")
        }
    },
    created() {
        this.username = sessionStorage.getItem('username');
        if (this.username) {
            this.checkUserRole()
                .then(() => {
                    this.isAdmin = this.username === 'admin';
                    this.fetchArticles(); // 只有在用户角色确定后才获取文章列表
                })
                .catch(error => {
                    console.error('获取用户角色失败:', error);
                });
        } else {
            this.fetchArticles(); // 未登录时仍然可以获取文章列表
        }
    }
};
</script>

<style scoped>
/* 让分页组件在底部对齐 */
.pagination {
    padding: 20px;
    text-align: center;
}
.page-size-selector {
    margin-bottom: 20px;
}
.edit-button {
    background-color: #4CAF50; /* 绿色背景色 */
    border-color: #4CAF50; /* 同样的边框颜色 */
}

.delete-button {
    background-color: #FF5722; /* 红色背景色 */
    border-color: #FF5722; /* 同样的边框颜色 */
}

.detail-button {
    background-color: #03A9F4; /* 蓝色背景色 */
    border-color: #03A9F4; /* 同样的边框颜色 */
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

.el-button:hover.detail-button {
    background-color: #0288d1; /* 蓝色背景色 hover 效果 */
    border-color: #0288d1; /* 同样的边框颜色 */
}

.el-button.add-article {
    background-color: #FFC107; /* 设置为你想要的颜色 */
    border-color: #FFC107; /* 设置边框颜色 */
    color: #fff;
}

.el-button.add-article:hover {
    background-color: #FFD54F; /* 悬停时的背景色 */
    border-color: #FFD54F; /* 悬停时的边框颜色 */
}


.header {
    background-color: #f5f5f5;
    padding: 10px;
    border-bottom: 1px solid #ddd;
}

.header-container {
    display: flex;
    justify-content: space-around;
    align-items: center;
}

.header-container h1 {
    margin: 0;
    font-size: 24px;
}

.button-group {
    display: flex;
    gap: 10px;
    margin-left: auto;
    align-content: space-between;
    width: 100%;
    height: 100%;
}

.content-container {
    padding: 20px;
    margin: 0 auto; /* 自动水平居中 */
    max-width: 1200px; /* 设置最大宽度 */
}

.article-content {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.el-table {
    background-color: #fff;
    border: 1px solid #ddd;
    border-radius: 4px;
}

.el-table th,
.el-table td {
    text-align: center;
}

.custom-footer {
    background-color: #67C23A;
    color: #ffffff;
    padding: 20px 0;
    text-align: center;
    min-height: 200px;
}

.footer-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    max-width: 1200px;
    margin: 0 auto;
}

.footer-left, .footer-right {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.footer-center {
    flex: 2;
    text-align: center;
}

.footer-logo, .security-logo {
    width: 80px;
    margin-bottom: 10px;
}

.footer-center p {
    margin: 5px 0;
}

.footer-center a {
    color: #ffffff;
    text-decoration: none;
    margin: 0 10px;
}

.footer-center a:hover {
    text-decoration: underline;
}
</style>
