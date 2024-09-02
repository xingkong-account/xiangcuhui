<template>
    <el-container>
        <div class="header-image">
            <el-image src="http://localhost:8081/image.png" fit="contain"></el-image>
        </div>
        <el-header>
            <div class="header-container">
                <!-- 导航栏 -->
                <el-menu mode="horizontal" class="header-menu">
                    <el-menu-item index="1" @click="handleHomeClick" style="font-size: 20px; color: black">首页</el-menu-item>

                    <!-- 会员管理下拉菜单 -->
                    <el-submenu class="member-management-menu" v-if="isAdmin || username" :index="'2'" :popper-append-to-body="false" :default-active="activeMenu">
                        <template #title>会员管理</template>
                        <!-- 个人会员子菜单 -->
                        <el-submenu index="2-1">
                            <template #title>个人会员</template>
                            <el-menu-item index="2-1-1" @click="navigate('individual-members')">查看所有个人会员</el-menu-item>
                            <el-menu-item v-if="isAdmin" index="2-1-2" @click="navigate('add-personal')">添加个人会员</el-menu-item>
                            <el-menu-item v-if="isAdmin" index="2-1-3" @click="navigate('individual-member-requests')">审核个人会员</el-menu-item>
                        </el-submenu>

                        <!-- 团队会员子菜单 -->
                        <el-submenu index="2-2">
                            <template #title>团队会员</template>
                            <el-menu-item index="2-2-1" @click="navigate('team-members')">查看所有团队会员</el-menu-item>
                            <el-menu-item v-if="isAdmin" index="2-2-2" @click="navigate('add-team-member')">添加团队会员</el-menu-item>
                            <el-menu-item v-if="isAdmin" index="2-2-3" @click="navigate('review-team-members')">审核团队会员</el-menu-item>
                        </el-submenu>
                    </el-submenu>

                    <!-- 文章管理下拉菜单 -->
                    <el-submenu class="article-management-menu" :index="'3'" :popper-append-to-body="false" :default-active="activeMenu">
                        <template #title>文章</template>
                        <el-menu-item index="3-1" @click="navigate('articles/create')">添加文章</el-menu-item>
                        <el-menu-item index="3-2" @click="navigate('article-review')">文章审核</el-menu-item>
                        <el-menu-item index="3-3" @click="navigate('articles')">新闻动态</el-menu-item>
                        <el-menu-item index="3-4" @click="navigate('category-articles', '农村党建')">农村党建</el-menu-item>
                        <el-menu-item index="3-5" @click="navigate('category-articles', '集体经济')">集体经济</el-menu-item>
                        <el-menu-item index="3-6" @click="navigate('category-articles', '产业发展')">产业发展</el-menu-item>
                        <el-menu-item index="3-7" @click="navigate('category-articles', '乡土文化')">乡土文化</el-menu-item>
                        <el-menu-item index="3-8" @click="navigate('category-articles', '青山绿水')">青山绿水</el-menu-item>
                        <el-menu-item index="3-9" @click="navigate('category-articles', '青年农人')">青年农人</el-menu-item>
                    </el-submenu>
                    <!-- 精选视频下拉菜单 -->
                    <el-submenu class="video-menu" :index="'4'" :popper-append-to-body="false" :default-active="activeMenu">
                        <template #title>视频</template>
                        <el-menu-item index="4-1" @click="navigate('videos')">精选视频</el-menu-item>
                        <el-menu-item v-if="isAdmin" index="4-2" @click="navigate('video-review')">审核视频</el-menu-item>
                        <el-menu-item v-if="isAdmin" index="4-3" @click="navigate('video-manage')">已审核视频</el-menu-item>
                        <el-menu-item index="4-4" @click="navigate('upload-video')">上传视频</el-menu-item>
                    </el-submenu>

                    <el-menu-item index="5" @click="navigate('about')" style="font-size: 20px; color: black">关于我们</el-menu-item>
                </el-menu>

                <!-- 登录注册按钮 -->
                <div class="auth-buttons">
                    <div v-if="username">
                        <el-dropdown @command="handleCommand">
                    <span class="el-dropdown-link">
                        欢迎, {{ username }} <i class="el-icon-arrow-down"></i>
                    </span>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item command="changePassword">修改密码</el-dropdown-item>
                                <el-dropdown-item command="logout">退出</el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </div>
                    <div v-else>
                        <el-button type="primary" class="custom-login-button" @click="goToLogin">登录</el-button>
                        <el-button class="custom-register-button" @click="goToRegister">注册</el-button>
                    </div>
                </div>
            </div>
        </el-header>


        <el-main>
            <el-container class="main-container">
                <!-- 左侧内容 -->
                <el-main class="main-content">
                    <!-- 新闻公告部分 -->
                    <div class="announcement-container">
                        <div class="announcement-title">
                            推荐阅读
                            <el-button type="text" class="more-button" @click="navigateToArticles">更多>></el-button>
                        </div>
                        <div class="announcement-list">
                            <div v-for="(article, index) in articleTitles" :key="index" class="announcement-item">
                                <router-link :to="'/articles/' + article.id" class="announcement-link">
                                    <span>{{ article.category }} - {{ article.title }}</span>
                                    <span class="announcement-date">{{ formatDate(article.updated_at) }}</span>
                                </router-link>
                            </div>
                        </div>
                    </div>


                    <!-- 轮播图部分 -->
                    <el-carousel :interval="5000" arrow="always" class="carousel">
                        <el-carousel-item v-for="(item, index) in carouselItems" :key="index">
                            <img :src="item.src" :alt="item.alt" />
                        </el-carousel-item>
                    </el-carousel>

                    <el-container class="content-container">
                        <router-view />
                    </el-container>
                </el-main>

                <el-aside width="300px" class="right-sidebar">
                    <div class="sidebar-item">
                        <h2 class="sidebar-title">最新文章</h2>
                        <ul class="sidebar-list">
                            <li v-for="article in latestArticles" :key="article.id" class="sidebar-list-item">
                                <router-link :to="'/articles/' + article.id">{{ article.title }}</router-link>
                            </li>
                        </ul>
                    </div>
                    <div class="sidebar-item">
                        <h2 class="sidebar-title">最热文章</h2>
                        <ul class="sidebar-list">
                            <li v-for="article in popularArticles" :key="article.id" class="sidebar-list-item">
                                <router-link :to="'/articles/' + article.id">{{ article.title }}</router-link>
                            </li>
                        </ul>
                    </div>
                    <div class="sidebar-item">
                        <h2>广告</h2>
                        <img src="http://localhost:8081/" alt="广告">
                    </div>
                </el-aside>
            </el-container>
        </el-main>

        <el-footer class="custom-footer">
            <div class="footer-content">
                <div class="footer-left">
                    <img src="http://localhost:8081/image.png" width="200px" height="120px" class="">
                </div>
                <div class="footer-center">
                    <p><a href="#">网站地图</a> | <a href="#">联系方式</a> | <a href="#">使用帮助</a> | <a href="#">隐私声明</a></p>
                    <p>主办单位:  乡促会&nbsp;&nbsp;&nbsp; 备案号：</p>
                    <p>地址: XX省XX市XXXXXX</p>
                </div>
                <div class="footer-right">
                    <img src="" alt="Security" class="security-logo">
                    <p>X公网安备 235487154313号</p>
                    <p>网站标识码：0000000000</p>
                </div>
            </div>
        </el-footer>
    </el-container>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            carouselItems: [
                { src: 'https://images.unsplash.com/photo-1519608487953-e999c86e7455?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80', alt: 'Starry Sky 1' },
                { src: 'https://images.unsplash.com/photo-1444703686981-a3abbc4d4fe3?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80', alt: 'Starry Sky 2' },
                { src: 'https://images.unsplash.com/photo-1517836357463-d25dfeac3438?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80', alt: 'Starry Sky 3' }
            ],
            articleTitles: [],  // 存储文章标题的数据
            username: '', // 存储用户名
            activeMenu: '3',  // 激活的菜单项
            isAdmin: false,
            latestArticles: [],
            popularArticles: [],
            currentPage: 1,
            pageSize: 10
        };
    },
    methods: {
        navigate(page, category) {
            if (category) {
                this.$router.push({ name: page, query: { category } });
            } else {
                this.$router.push(`/${page}`);
                this.activeMenu = '3';
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
        // 获取最新和最热文章
        async fetchArticles() {
            try {
                const [latestResponse, popularResponse] = await Promise.all([
                    axios.get(this.$baseUrl + '/api/articles/latest'),
                    axios.get(this.$baseUrl + '/api/articles/popular')
                ]);
                this.latestArticles = latestResponse.data;
                this.popularArticles = popularResponse.data;
            } catch (error) {
                console.error('Failed to fetch articles:', error);
                this.$message.error('获取文章失败，请稍后再试。');
            }
        },
        handleHomeClick() {
            window.location.reload();
        },
        goToLogin() {
            this.$router.push('/login');
        },
        goToRegister() {
            this.$router.push('/register');
        },
        handleCommand(command) {
            if (command === 'changePassword') {
                this.$router.push({ path: '/change', query: { username: this.username } });
            } else if (command === 'logout') {
                this.logout();
            }
        },
        logout() {
            sessionStorage.removeItem('username');
            this.username = ''; // 清除用户名
            this.$router.push('/login');
        },
        fetchArticleTitles() {
            axios.get(this.$baseUrl +'/api/articles/articleList', {
                params: {
                    pageNum: this.currentPage,
                    pageSize: this.pageSize
                }
            })
                .then(response => {
                    // this.$alert(response.data.data)
                    this.articleTitles = response.data.data;  // 获取当前页的数据
                    this.total = response.data.total;  // 获取总记录数
                })
                .catch(error => {
                    this.$message.error('获取文章列表失败: ' + error.message);
                });
        },
        navigateToArticles() {
            this.$router.push('/articles');
        },
        checkIfAdmin() {
            const username = sessionStorage.getItem('username');
            this.isAdmin = (username === 'admin');
        }
    },
    created() {
        // 获取用户名
        this.username = sessionStorage.getItem('username');
        this.checkIfAdmin();
        this.fetchArticleTitles();
        this.fetchArticles();
    }
}
</script>

<style scoped>
.header-menu {
    border-bottom: none;
    background-color: transparent;
    border-bottom: none;
}

.header-container {
    display: flex;
    justify-content: space-around;
    align-items: center;
    padding: 10px 20px; /* 头部的内边距 */
    /*background-color: #67C23A;*/
    height: 80%;
}

.custom-header {
    background-color: #8BC34A;
    color: white;
    padding: 10px;
}

.site-title {
    color: white;
    margin: 0;
}

.auth-buttons .el-button {
    margin-left: 10px;
}

.main-container {
    background-color: #F5F5F5;
}

.carousel {
    margin-top: 20px;
    border-radius: 8px;
    overflow: hidden;
}

/* 热点新闻容器整体样式 */
.announcement-container {
    background-color: #F9F9F9; /* 浅灰色背景 */
    padding: 15px 20px; /* 内边距 */
    border-radius: 10px; /* 圆角 */
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 增加阴影 */
    margin-bottom: 20px; /* 与其他部分的间距 */
}

/* 热点新闻标题的样式 */
.announcement-title {
    font-size: 24px; /* 增大字体 */
    font-weight: bold; /* 加粗 */
    color: #333; /* 深色标题 */
    margin-bottom: 20px; /* 标题与新闻列表之间的间距 */
    display: flex;
    justify-content: space-between; /* 标题与“更多”按钮对齐 */
    align-items: center;
    border-bottom: 2px solid #8BC34A; /* 标题底部绿色边框 */
    padding-bottom: 10px; /* 标题底部内边距 */
}

/* “更多”按钮的样式 */
.more-button {
    font-size: 14px; /* 字体大小 */
    color: #8BC34A; /* 按钮颜色 */
    cursor: pointer; /* 鼠标变成指针 */
    transition: color 0.3s; /* 渐变效果 */
}

.more-button:hover {
    color: #4CAF50; /* 鼠标悬停时颜色变化 */
}

/* 热点新闻列表的样式 */
.announcement-list {
    list-style: none; /* 去掉默认列表样式 */
    padding: 0;
    margin: 0;
}

/* 单个新闻项的样式 */
.announcement-item {
    padding: 10px 0; /* 上下内边距 */
    border-bottom: 1px dashed #E0E0E0; /* 分割线 */
    transition: background-color 0.3s; /* 背景色渐变 */
}

.announcement-item:hover {
    background-color: #F1F1F1; /* 鼠标悬停时背景色变化 */
}

.announcement-item:last-child {
    border-bottom: none; /* 最后一个项去掉分割线 */
}

/* 新闻链接的样式 */
.announcement-link {
    text-decoration: none; /* 去掉下划线 */
    color: #333; /* 链接颜色 */
    display: flex;
    justify-content: space-between; /* 标题和日期左右对齐 */
    align-items: center; /* 垂直居中 */
    font-size: 16px; /* 增加字体大小 */
    font-weight: 500; /* 适中的字体粗细 */
    transition: color 0.3s; /* 颜色渐变 */
}

.announcement-link:hover {
    color: #4CAF50; /* 鼠标悬停时颜色变化 */
}

/* 新闻日期的样式 */
.announcement-date {
    font-size: 14px; /* 日期字体大小 */
    color: #757575; /* 日期颜色 */
}

/* 右侧边栏整体样式 */
.right-sidebar {
    background-color: #E0F2F1; /* 保持背景色 */
    padding: 20px;
    border-left: 1px solid #BDBDBD;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 添加轻微阴影 */
}

/* 单个侧边栏项目的样式 */
.sidebar-item {
    margin-bottom: 20px; /* 间距 */
    padding: 10px;
    border-radius: 8px; /* 圆角 */
    background-color: #FFFFFF; /* 背景色 */
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05); /* 轻微阴影 */
}

/* 侧边栏标题的样式 */
.sidebar-title {
    font-size: 18px; /* 更大的字体 */
    font-weight: bold; /* 加粗 */
    color: #333; /* 标题文字颜色 */
    border-bottom: 2px solid #8BC34A; /* 标题底部边框 */
    padding-bottom: 5px; /* 底部内边距 */
    margin-bottom: 10px; /* 标题和内容之间的间距 */
}

/* 侧边栏文章列表的样式 */
.sidebar-list {
    list-style: none; /* 去掉默认列表样式 */
    padding: 0;
    margin: 0;
}

/* 单个列表项的样式 */
.sidebar-list-item {
    margin-bottom: 10px; /* 列表项之间的间距 */
}

/* 列表项链接的样式 */
.sidebar-list-item a {
    text-decoration: none; /* 去掉下划线 */
    color: black; /* 链接颜色 */
    font-size: 14px; /* 字体大小 */
    transition: color 0.3s; /* 颜色渐变过渡 */
}

/* 列表项链接悬停状态的样式 */
.sidebar-list-item a:hover {
    color: #4CAF50; /* 悬停时的颜色 */
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

/*修改密码和退出下拉菜单样式*/
.el-dropdown-link {
    color: black;
    font-size: 16px;
}

/*子菜单鼠标悬停样式*/
.el-menu-item:hover, .el-submenu__title:hover {
    background-color: #8BC34A !important;
}

.header-menu .el-menu-item {
    color: black; /* 设置菜单项的字体颜色 */
}

/*登录注册按钮样式*/
.custom-login-button {
    background-color: hotpink;
    color: #f1f3f5;
    border-radius: 5px;
    padding: 10px 20px;
    font-weight: bold;
    transition: background-color 0.3s ease;
}

.custom-login-button:hover {
    background-color: deeppink;
}

.custom-login-button:active {
    background-color: crimson;
}

.custom-register-button {
    background-color: powderblue;
    color: #fff;
    border-radius: 5px;
    padding: 10px 20px;
    font-weight: bold;
    transition: background-color 0.3s ease;
}

.custom-register-button:hover {
    background-color: darkviolet;
}

.custom-register-button:active {
    background-color: indigo;
}
</style>