<template>
    <el-container>
        <div class="desktop-layout" v-show="!isMobile">
            <el-header class="header">
                <div class="header-container">
                    <h1>{{isAdmin ? "文章管理" : "我发表的文章"}}</h1>
                </div>
                <div class="button-group">
                    <el-button class="add-article" v-if="check" style="margin-left: 10%" type="primary" @click="navigateToAddArticle">添加文章</el-button>
                    <el-button @click="navigateBack" type="default">返回</el-button>
                </div>
            </el-header>
            <el-main style="width: 100%; margin: 10px auto">
                <el-container class="content-container">
                    <el-row>
                        <el-col :span="24">
                            <!--管理员展示全部文章-->
                            <el-table :data="articles" stripe v-if="isAdmin" style="width: 1500px">
                                <el-table-column prop="title" label="标题" width="180">
                                    <template #default="{ row }">
                                        {{ truncateText(row.title, 30) }}
                                    </template>
                                </el-table-column>
                                <el-table-column prop="category" label="分类" width="100"></el-table-column>
                                <el-table-column prop="author" label="作者" width="100"></el-table-column>
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
                                <el-table-column label="操作" width="260">
                                    <template #default="{ row }">
                                        <el-button
                                            :disabled="!canEdit(row.author)"
                                            @click="navigateToEditArticle(row.id)"
                                            type="primary"
                                            size="small"
                                            class="edit-button"
                                        >
                                            编辑
                                        </el-button>
                                        <el-button
                                            :disabled="!canEdit(row.author)"
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
                            <!-- 根据文章作者展示文章列表 -->
                            <el-table :data="myArticles" stripe v-else-if="username">
                                <el-table-column prop="title" label="标题" width="180"></el-table-column>
                                <el-table-column prop="author" label="作者" width="100"></el-table-column>
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
                                        <el-button
                                            :disabled="!canEdit(row.author)"
                                            @click="navigateToEditArticle(row.id)"
                                            type="primary"
                                            size="small"
                                            class="edit-button"
                                        >
                                            编辑
                                        </el-button>
                                        <el-button
                                            :disabled="!canEdit(row.author)"
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
        </div>
        <!-- 手机端布局 -->
        <div class="mobile-layout" v-show="isMobile">
            <el-header class="mobile-header">
                <div class="mobile-header-container">
                    <h1>{{isAdmin ? "文章管理" : "我的文章"}}</h1>
                    <el-button v-if="check" type="primary" @click="navigateToAddArticle">添加文章</el-button>
                    <el-button type="default" @click="navigateBack">返回</el-button>
                </div>
            </el-header>

            <el-main>
                <el-container class="mobile-content-container">
                    <el-row>
                        <el-col :span="24">
                            <!-- 管理员展示全部文章 -->
                            <div v-if="isAdmin">
                                <el-card v-for="article in articles" :key="article.id" class="mobile-article-card">
                                    <h3>{{ article.title }}</h3>
                                    <p><strong>来源:</strong> {{ article.source }}</p>
                                    <p><strong>浏览次数:</strong> {{ article.views + '次' }}</p>
                                    <p><strong>作者:</strong> {{ article.author }}</p>
                                    <p><strong>状态:</strong> {{ article.status }}</p>
                                    <p><strong>分类:</strong> {{ article.category }}</p>
                                    <p><strong>置顶状态:</strong> {{ article.isTop ? '已置顶' : '未置顶' }}</p>
                                    <p class="mobile-article-content">{{ truncateText(article.content, 100) }}</p>
                                    <p><strong>创建时间:</strong> {{ formatDate(article.created_at) }}</p>
                                    <p><strong>修改时间:</strong> {{ formatDate(article.updated_at) }}</p>
                                    <div class="article-actions">
                                        <el-button size="mini" @click="navigateToDetailPage(article.id)">详情</el-button>
                                        <el-button size="mini" :disabled="!canEdit(article.author)" @click="navigateToEditArticle(article.id)">编辑</el-button>
                                        <el-button size="mini" :disabled="!canEdit(article.author)" type="danger" @click="deleteArticle(article.id)">删除</el-button>
                                    </div>
                                </el-card>
                            </div>

                            <!-- 用户文章展示 -->
                            <div v-else-if="username">
                                <el-card v-for="article in myArticles" :key="article.id" class="mobile-article-card">
                                    <h3>{{ article.title }}</h3>
                                    <p><strong>作者:</strong> {{ article.author }}</p>
                                    <p><strong>分类:</strong> {{ article.category }}</p>
                                    <p class="article-content">{{ article.content }}</p>
                                    <p><strong>创建时间:</strong> {{ formatDate(article.created_at) }}</p>
                                    <p><strong>修改时间:</strong> {{ formatDate(article.updated_at) }}</p>
                                    <div class="article-actions">
                                        <el-button size="mini" @click="navigateToDetailPage(article.id)">详情</el-button>
                                        <el-button size="mini" :disabled="!canEdit(article.author)" @click="navigateToEditArticle(article.id)">编辑</el-button>
                                        <el-button size="mini" :disabled="!canEdit(article.author)" type="danger" @click="deleteArticle(article.id)">删除</el-button>
                                    </div>
                                </el-card>
                            </div>
                        </el-col>
                    </el-row>
                </el-container>
            </el-main>

            <el-row class="mobile-filter-row">
                <el-col :span="24">
                    <span>每页展示条数:</span>
                    <el-select style="width: 30%" v-model="pageSize" @change="handlePageSizeChange">
                        <el-option label="10条/页" value="10"></el-option>
                        <el-option label="20条/页" value="20"></el-option>
                        <el-option label="50条/页" value="50"></el-option>
                    </el-select>
                    <el-pagination
                        background
                        layout="prev, pager, next, total"
                        :total="total"
                        :page-size="pageSize"
                        :current-page="currentPage"
                        @current-change="handlePageChange"
                    ></el-pagination>
                </el-col>
            </el-row>
        </div>
    </el-container>
</template>

<script>
import axios from 'axios';

export default {
    name: 'ArticleList',
    data() {
        return {
            articles: [],
            myArticles: [],
            total: 0,
            currentPage: 1,
            pageSize: 10,
            isAdmin: false,
            isMobile: false,
            username: '',
            usertype: '',
            author: ''
        };
    },
    methods: {
        checkIfMobile() {
            this.isMobile = window.innerWidth <= 768;
        },
        checkUserRole() {
            const username = sessionStorage.getItem('username');
            if (!username) {
                console.log('未登录或无法获取用户名');
                return Promise.resolve('');
            }
            return axios.get(this.$baseUrl + '/api/user/type', { params: { username } })
                .then(response => response.data.role)
                .catch(error => {
                    console.log('获取用户角色失败:', error);
                    throw error;
                });
        },
        canEdit(articleAuthor) {
            return this.isAdmin || this.username === articleAuthor;
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
            axios.get(this.$baseUrl + '/api/articles/allArticles', {
                params: {
                    pageNum: this.currentPage,
                    pageSize: this.pageSize
                }
            })
                .then(response => {
                    this.articles = response.data.data;
                    this.total = response.data.total;
                })
                .catch(error => {
                    this.$message.error('获取文章列表失败: ' + error.message);
                });
        },
        fetchArticlesByUsername() {
            axios.get(this.$baseUrl + '/api/articles/user-articles', {
                params: {
                    author: this.author,
                    pageNum: this.currentPage,
                    pageSize: this.pageSize
                }
            })
                .then(response => {
                    this.myArticles = response.data.data;
                    this.total = response.data.total;
                })
                .catch(error => {
                    this.$message.error('获取文章列表失败: ' + error.message);
                });
        },
        handlePageChange(page) {
            this.currentPage = page;
            if (this.isAdmin){
                this.fetchArticles();
            }
            else{
                this.fetchArticlesByUsername();
            }
        },
        handlePageSizeChange(size){
            this.pageSize = size;
            this.currentPage = 1;
            if (this.isAdmin){
                this.fetchArticles();
            }
            else{
                this.fetchArticlesByUsername();
            }
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
        },
        truncateText(text, maxLength) {
            if (text.length > maxLength) {
                return text.substring(0, maxLength) + '...';
            }
            return text;
        },
    },
    created() {
        this.usertype = sessionStorage.getItem('usertype');
        this.username = sessionStorage.getItem('username');
        if (this.username) {
            this.author = this.username;
            this.checkUserRole()
                .then(() => {
                    this.isAdmin = (this.usertype === '管理员');
                    if (this.isAdmin) {
                        this.fetchArticles();
                    }
                    else {
                        this.fetchArticlesByUsername();
                    }
                })
                .catch(error => {
                    console.error('获取用户角色失败:', error);
                });
        } else {
            // this.fetchArticles();
            this.total = 0;
        }
    },
    mounted() {
        this.checkIfMobile();
        window.addEventListener('resize', this.checkIfMobile);
    },
    beforeDestroy() {
        window.removeEventListener('resize', this.checkIfMobile);
    }
};
</script>

<style scoped>
.mobile-layout {
    padding: 10px;
}

.mobile-header {
    background-color: #f5f5f5;
    padding: 10px;
    border-bottom: 1px solid #e0e0e0;
}

.mobile-header-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.mobile-header h1 {
    margin: 0;
    font-size: 20px;
}

.mobile-content-container {
    margin-top: 10px;
}

.mobile-article-card {
    margin-bottom: 15px;
    padding: 10px;
    border: 1px solid #dcdcdc;
    border-radius: 4px;
    background-color: #ffffff;
}

.mobile-article-card h3 {
    margin: 0 0 10px;
    font-size: 18px;
}

.mobile-article-card p {
    margin: 5px 0;
    font-size: 14px;
}

.mobile-article-content {
    color: #666;
}

.article-actions {
    margin-top: 10px;
}

.mobile-filter-row {
    margin-top: 15px;
    text-align: center;
}

.mobile-filter-row span {
    margin-right: 10px;
}

.mobile-filter-row .el-select {
    display: inline-block;
    margin-right: 10px;
}

.el-pagination {
    display: inline-block;
    margin-top: 10px;
}

.mobile-layout {
    padding: 20px;
    background-color: #f5f5f5;
}

.page-size-selector {
    margin-bottom: 20px;
}
.edit-button {
    background-color: #4CAF50;
    border-color: #4CAF50;
}

.delete-button {
    background-color: #FF5722;
    border-color: #FF5722;
}

.detail-button {
    background-color: #03A9F4;
    border-color: #03A9F4;
}

.el-button:hover.edit-button {
    background-color: #45a049;
    border-color: #45a049;
}

.el-button:hover.delete-button {
    background-color: #e64a19;
    border-color: #e64a19;
}

.el-button:hover.detail-button {
    background-color: #0288d1;
    border-color: #0288d1;
}

.el-button.add-article {
    background-color: rgb(101,172,140);
    border-color: rgb(101,172,140);
    color: #fff;
}

.el-button.add-article:hover {
    background-color: rgb(91, 155, 126) !important;
    border-color: rgb(91, 155, 126) !important;
    cursor: pointer;
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
    color: rgb(85,97,83);
}

.header-container h1 {
    margin: 0;
    font-size: 28px;
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
    margin: 10px auto;
    width: 100%;
    justify-content: center;
}

.article-content {
    white-space: nowrap;
    text-overflow: ellipsis;
}

.el-table {
    background-color: #fff;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 16px;
}

.el-table th,
.el-table td {
    text-align: center;
}
</style>
