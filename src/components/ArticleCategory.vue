<template>
    <div class="article-category">
        <div class="header">
            <button @click="goBack" class="back-button">返回</button>
            <h1 class="category-title">{{ category }} 文章列表</h1>
        </div>
        <ul class="article-list">
            <li v-for="article in articles" :key="article.id" class="article-item">
                <router-link :to="'/articles/' + article.id" class="article-link">
                    <div class="article-card">
                        <h2 class="article-title">{{ article.title }}</h2>
                        <p class="article-meta">发布于: {{ formatDate(article.created_at) }} | 作者: {{ article.author }}</p>
                        <p class="article-snippet">{{ getSnippet(article.content) }}</p>
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
            category: this.$route.query.category || '新闻动态'
        };
    },
    created() {
        this.fetchArticles();
    },
    methods: {
        async fetchArticles() {
            try {
                const response = await axios.get('http://localhost:8081/api/articles', { params: { category: this.category } });
                this.articles = response.data;
                if (this.articles.length > 0) {
                    this.$message.success('加载成功！');
                } else {
                    this.$message.info('没有找到文章。');
                    setTimeout(() => {
                        this.$router.push("/");
                    }, 1000)
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
            return content.length > 100 ? content.slice(0, 100) + '...' : content;
        },
        goBack() {
            this.$router.go(-1);
        }
    }
};
</script>

<style scoped>
.article-category {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
    font-family: Arial, sans-serif;
}

.header {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
}

.back-button {
    background-color: #67C23A;
    color: #fff;
    border: none;
    border-radius: 5px;
    padding: 10px 20px;
    font-size: 1rem;
    cursor: pointer;
    margin-right: 20px;
    transition: background-color 0.3s;
}

.back-button:hover {
    background-color: #4CAF50;
}

.category-title {
    font-size: 2rem;
    margin: 0;
    color: #333;
}

.article-list {
    list-style: none;
    padding: 0;
    margin: 0;
}

.article-item {
    margin-bottom: 20px;
}

.article-link {
    text-decoration: none;
    color: inherit;
}

.article-card {
    border: 1px solid #ddd;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    padding: 20px;
    background: #fff;
    transition: box-shadow 0.3s;
}

.article-card:hover {
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.article-title {
    font-size: 1.5rem;
    margin: 0 0 10px;
    color: #333333;
}

.article-meta {
    font-size: 0.875rem;
    color: #666;
    margin-bottom: 10px;
}

.article-snippet {
    font-size: 1rem;
    color: #333;
}
</style>
