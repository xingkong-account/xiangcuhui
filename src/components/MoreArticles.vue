<template>
    <el-container>
        <el-header class="header">
            <div class="header-container">
                <h1>文章列表</h1>
                <el-button @click="navigateBack" type="default">返回</el-button>
            </div>
        </el-header>
        <el-main>
            <el-container class="content-container">
                <el-row>
                    <el-col :span="24">
                        <el-table :data="articles" stripe>
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
                            <el-table-column label="操作" width="120">
                                <template #default="{ row }">
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
    </el-container>
</template>

<script>
import axios from 'axios';

export default {
    name: 'ArticleList',
    data() {
        return {
            articles: [],
            total: 0,
            currentPage: 1,
            pageSize: 10,
            isAdmin: false,
            username: '',
            usertype: '',
            author: ''
        };
    },
    methods: {
        navigateToDetailPage(id) {
            this.$router.push(`/articles/${id}`);
        },
        fetchArticles() {
            axios.get(this.$baseUrl + '/api/articles/articleList', {
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
        handlePageChange(page) {
            this.currentPage = page;
            this.fetchArticles();
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
        this.fetchArticles();
    }
};
</script>

<style scoped>
.pagination {
    padding: 20px;
    text-align: center;
}
.page-size-selector {
    margin-bottom: 20px;
}

.detail-button {
    background-color: #03A9F4;
    border-color: #03A9F4;
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

.content-container {
    padding: 20px;
    margin: 0 auto; /* 自动水平居中 */
    max-width: 1400px;
    justify-content: center;
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
</style>
