<template>
    <div class="main">
        <div class="header">
            <h4 class="category-title">{{category}} | 文章列表</h4>
            <span class="list-subtitle">LIST OF ARTICLES</span>
            <button @click="goBack" class="back-button">返回</button>
        </div>
        <hr class="divider"/>
        <ul class="article-list">
            <li v-for="(article, index) in articles" :key="article.id" class="article-item">
                <router-link :to="'/articles/' + article.id" class="article-link">
                    <div class="article-card">
                        <div class="article-index">{{ formatIndex(index) }}</div>
                        <div class="article-info">
                            <h2 class="article-title">{{ article.title }}</h2>
                            <p class="article-meta">发布于: {{ formatDate(article.created_at) }} 作者: {{ article.author }}</p>
                            <p class="article-snippet">{{ getSnippet(article.content) }}</p>
                        </div>
                    </div>
                </router-link>
            </li>
        </ul>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            articles: [],
            category: this.$route.query.category || '文章管理'
        };
    },
    created() {
        this.fetchArticles();
    },
    methods: {
        async fetchArticles() {
            try {
                const response = await axios.get(this.$baseUrl + '/api/articles', { params: { category: this.category } });
                this.articles = response.data;
                if (this.articles.length > 0) {
                    setTimeout(() => {
                        this.$message.success('加载成功！');
                    }, 1000);
                } else {
                    setTimeout(() => {
                        this.$message.info('该分类文章列表为空');
                        this.$router.push("/");
                    }, 1000);
                }
            } catch (error) {
                console.error('获取文章失败:', error);
                this.$alert('获取文章失败，请稍后再试。');
            }
        },
        formatDate(dateString) {
            const options = { year: 'numeric', month: 'long', day: 'numeric' };
            return new Date(dateString).toLocaleDateString(undefined, options);
        },
        getSnippet(content) {
            return content.length > 20 ? content.slice(0, 20) + '...' : content;
        },
        formatIndex(index) {
            return ('0' + (index + 1)).slice(-2); // 格式化序号，01, 02
        },
        goBack() {
            this.$router.push("/");
        }
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
