<template>
    <div class="main">
        <div class="header">
            <h4 class="category-title">文章列表</h4>
            <span class="list-subtitle">LIST OF ARTICLES</span>
            <button @click="navigateBack" class="back-button">返回</button>
        </div>
        <hr class="divider"/>
        <div v-if="articles.length === 0" class="no-articles">暂无文章</div>
        <ul class="article-list">
            <li v-for="(article, index) in articles" :key="article.id" class="article-item">
                <router-link :to="'/articles/' + article.id" class="article-link">
                    <div class="article-card">
                        <div class="article-index">{{ formatIndex(index) }}</div>
                        <div class="article-info">
                            <h2 class="article-title">{{ article.title }}</h2>
                            <p class="article-meta">发布于: {{ formatDate(article.created_at) }} 作者: {{ article.author }}</p>
<!--                            <p class="article-snippet">{{ getSnippet(article.content) }}</p>-->
                        </div>
                    </div>
                </router-link>
            </li>
        </ul>

        <el-row class="filter-row">
            <el-col :span="24">
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
        };
    },
    methods: {
        navigateToDetailPage(id) {
            this.$router.push(`/articles/${id}`);
        },
        async fetchArticles() {
            axios.get(this.$baseUrl + '/api/articles/articleList', {
                params: {
                    pageNum: this.currentPage,
                    pageSize: this.pageSize
                }
            })
                .then(response => {
                    this.articles = response.data.data;
                    this.total = response.data.total;
                    // this.$alert(this.articles[this.articles.length - 1]);
                    // this.$alert(this.total);
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
        // 截取内容，多余内容省略号表示
        getSnippet(content) {
            return content.length > 20 ? content.slice(0, 20) + '...' : content;
        },
        navigateBack() {
            this.$router.push("/")
        },
        formatIndex(index) {
            return ('0' + (index + 1)).slice(-2);
        }
    },
    created() {
        this.fetchArticles();
    }
};
</script>

<style scoped>
.main {
    max-width: 1000px;
    margin: 0 auto;
    padding: 20px;
    font-family: 'Arial', sans-serif;
}

.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.category-title {
    font-size: 2.5rem;
    font-weight: 300;
    color: rgb(101,172,138);
}

.list-subtitle {
    font-size: 1rem;
    color: rgb(101,172,138);
    margin-left: 20px;
}

.back-button {
    background-color: rgb(101,172,138);
    color: #fff;
    border: none;
    border-radius: 5px;
    padding: 10px 20px;
    font-size: 1rem;
    cursor: pointer;
    margin-left: auto;
    transition: background-color 0.3s;
}

.back-button:hover {
    background-color: #4CAF50;
}

.divider {
    margin-top: 20px;
    margin-bottom: 30px;
    border: 3px solid rgb(101,172,138);
}

.article-list {
    list-style: none;
    padding: 0;
    margin: 0;
}

.article-item {
    margin-bottom: 20px;
    display: flex;
    align-items: center;
}

.article-link {
    text-decoration: none;
    color: inherit;
    display: flex;
    flex-grow: 1;
}

.article-card {
    display: flex;
    align-items: center;
    width: 100%;
    transition: box-shadow 0.3s;
    margin-top: 20px;
}

.article-card:hover {
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.article-index {
    background-color: rgb(101,172,138);
    color: white;
    font-size: 1.5rem;
    width: 50px;
    height: 50px;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 8px;
    margin-right: 20px;
}

.article-info {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #9ec8a5;
    width: 100%;
    padding-bottom: 10px;
}

.article-title {
    font-size: 1.25rem;
    margin: 0;
    color: rgb(111,111,111);
}

.article-meta {
    font-size: 0.875rem;
    color: #666;
    margin-right: 20px;
    max-width: 80%;
}

.article-snippet {
    font-size: 1rem;
    color: #333;
    margin-top: 5px;
    max-width: 50%;
}
</style>
