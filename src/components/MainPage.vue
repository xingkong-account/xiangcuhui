<template>
    <el-container class="total">
        <!-- 头部选项 -->
        <el-header class="header-container">
            <div class="header-logo">
                <img src="@/assets/images/icon.jpg" alt="Logo"
                     style="object-fit: contain;
                     height: 80px; scale: 0.8">
            </div>
            <el-menu mode="horizontal" class="header-menu">
                <el-menu-item index="1" @click="handleHomeClick" style="font-size: 20px; color: black">首页</el-menu-item>

                <!-- 会员管理下拉菜单，登录才能看到会员菜单 -->
                <el-submenu class="member-management-menu" v-if="isAdmin || username" :index="'2'" :popper-append-to-body="false" :default-active="activeMenu">
                    <template #title>会员</template>
                    <!-- 个人会员子菜单 -->
                    <el-submenu index="2-1">
                        <template #title>个人会员</template>
                        <el-menu-item index="2-1-1" v-if="isAdmin" @click="navigate('individual-members')">个人会员管理</el-menu-item>
                        <el-menu-item index="2-1-1" v-else @click="navigate('individual-members')">查看个人会员</el-menu-item>
<!--                        <el-menu-item v-if="isAdmin" index="2-1-2" @click="navigate('add-personal')">添加个人会员</el-menu-item>-->
<!--                        <el-menu-item v-if="isAdmin" index="2-1-3" @click="navigate('individual-member-requests')">审核个人会员</el-menu-item>-->
                    </el-submenu>

                    <!-- 团队会员子菜单 -->
                    <el-submenu index="2-2">
                        <template #title>团队会员</template>
                        <el-menu-item index="2-2-1" @click="navigate('group--member-manage')">团队会员管理</el-menu-item>
                        <el-menu-item index="2-2-2" @click="navigate('team-members')">查看所有团队会员</el-menu-item>
<!--                        <el-menu-item v-if="isAdmin" index="2-2-3" @click="navigate('add-team-member')">添加团队会员</el-menu-item>-->
<!--                        <el-menu-item v-if="isAdmin" index="2-2-4" @click="navigate('review-team-members')">审核团队会员</el-menu-item>-->
                    </el-submenu>
                </el-submenu>

                <!-- 文章管理下拉菜单 -->
                <el-submenu class="article-management-menu" :index="'3'" :popper-append-to-body="false" :default-active="activeMenu">
                    <template #title>文章</template>
                    <el-menu-item index="3-1" @click="navigate('articles/create')">添加文章</el-menu-item>
                    <el-menu-item index="3-2" v-if="isAdmin" @click="navigate('article-review')">文章审核</el-menu-item>
                    <el-menu-item index="3-3" v-if="isAdmin" @click="navigate('articles')">文章管理</el-menu-item>
                    <el-menu-item index="3-3" v-else @click="navigate('articles')">我发表的</el-menu-item>
                    <!-- 文章部分的子菜单：按分类查看 -->
                    <el-submenu :index="'3-4'">
                        <template #title>按分类查看</template>
                        <el-menu-item index="3-4-1" @click="navigate('category-articles', '农村党建')">农村党建</el-menu-item>
                        <el-menu-item index="3-4-2" @click="navigate('category-articles', '集体经济')">集体经济</el-menu-item>
                        <el-menu-item index="3-4-3" @click="navigate('category-articles', '产业发展')">产业发展</el-menu-item>
                        <el-menu-item index="3-4-4" @click="navigate('category-articles', '乡土文化')">乡土文化</el-menu-item>
                        <el-menu-item index="3-4-5" @click="navigate('category-articles', '青山绿水')">青山绿水</el-menu-item>
                        <el-menu-item index="3-4-6" @click="navigate('category-articles', '青年农人')">青年农人</el-menu-item>
                    </el-submenu>
                </el-submenu>

                <!-- 精选视频下拉菜单 -->
                <el-submenu class="video-menu" :index="'4'" :popper-append-to-body="false" :default-active="activeMenu">
                    <template #title>视频</template>
                    <el-menu-item index="4-1" @click="navigate('videos')">精选视频</el-menu-item>
                    <el-menu-item v-if="isAdmin" index="4-2" @click="navigate('video-review')">视频审核</el-menu-item>
                    <el-menu-item v-if="isAdmin" index="4-3" @click="navigate('video-manage')">所有视频</el-menu-item>
                    <el-menu-item index="4-4" @click="navigate('upload-video')">上传视频</el-menu-item>
                </el-submenu>

                <el-menu-item index="5" @click="navigate('about')" style="font-size: 20px; color: black">关于我们</el-menu-item>
            </el-menu>
            <!-- 登录注册 -->
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
        </el-header>

        <!-- 主页上面的轮播图 -->
        <el-main class="main-banner">
            <img src="@/assets/images/banner1.jpg" alt="Banner" class="banner-image">
            <div class="banner-text">
                <span>Website of the </span>
                <br>
                <span>Rural Promotion</span>
                <br>
                <span>Association</span>
            </div>
        </el-main>

        <!-- 主要内容 -->
        <el-main class="main-content-container">
            <el-row :gutter="20">
                <el-col :span="16">
                    <el-row class="recommend-read">
                        <!-- 左侧图片列 -->
                        <el-col :span="6" class="image-column">
                            <div class="image-container">
                                <img src="@/assets/images/CountrysideCulture.jpg" alt="Image 1" class="image-item">
                                <img src="@/assets/images/stack-image2.jpg" alt="Image 2" class="image-item">
                            </div>
                        </el-col>
                        <!-- 右侧推荐阅读内容列 -->
                        <el-col :span="18">
                            <div class="announcement-title">
                                推荐阅读
                                <el-button type="text" style="color: rgb(87,157,123)" @click="navigateToArticles">更多>></el-button>
                            </div>
                            <div class="announcement-list">
                                <div v-for="(article, index) in articleTitles" :key="index" class="announcement-item">
                                    <router-link :to="'/articles/' + article.id" class="announcement-link">
                                        <span>{{ article.category }} - {{ article.title }}</span>
                                        <span class="announcement-date">{{ formatDate(article.updated_at) }}</span>
                                    </router-link>
                                </div>
                            </div>
                        </el-col>
                    </el-row>

                    <!-- xiangcuhui标题 -->
                    <div class="title-container">
                        <h1 class="main-title">甘肃省乡村发展促进会</h1>
                        <div class="gansu">
                            <span>GANSU ASSOCIATION FOR THE</span><br>
                            <span>PROMOTION OF RURAL</span><br>
                            <span>DEVELOPMENT</span><br>
                        </div>
                    </div>
                    <!-- 文章类别轮播图 -->
                    <div class="image-section">
                        <el-carousel height="300px" :interval="5000" arrow="always" indicator-position="outside">
                            <el-carousel-item v-for="(imageSet, index) in chunkedImages" :key="index">
                                <el-row :gutter="20">
                                    <el-col v-for="(image, idx) in imageSet" :key="idx" :span="8">
                                        <div class="image-card">
                                            <img :src="image.src" :alt="image.alt" class="image">
                                            <div class="image-overlay">
                                <span @click="navigate('category-articles', image.category)">
                                    {{ image.title }}
                                </span>
                                            </div>
                                        </div>
                                    </el-col>
                                </el-row>
                            </el-carousel-item>
                        </el-carousel>
                    </div>

                    <el-row :gutter="20" class="content-section">
                        <el-col :span="2">
                            <div class="section-title active">文章</div>
                        </el-col>
                        <el-col :span="6">
                            <div class="text-block">
                                <div @click="navigate('articles/create')">添加文章</div>
                                <div @click="navigate('article-review')">文章审核</div>
                                <div @click="navigate('articles')">文章管理</div>
                            </div>
                        </el-col>

                        <el-col :span="6">
                            <div class="text-block">
                                <div @click="navigate('category-articles', '农村党建')">农村党建</div>
                                <div @click="navigate('category-articles', '集体经济')">集体经济</div>
                                <div @click="navigate('category-articles', '产业发展')">产业发展</div>
                            </div>
                        </el-col>

                        <el-col :span="6">
                            <div class="text-block">
                                <div @click="navigate('category-articles', '乡土文化')">乡土文化</div>
                                <div @click="navigate('category-articles', '青山绿水')">青山绿水</div>
                                <div @click="navigate('category-articles', '青年农人')">青年农人</div>
                            </div>
                        </el-col>
                        <el-col :span="1">
                            <div class="divider"></div>
                        </el-col>
                        <!-- 视频  -->
                        <el-col :span="2">
                            <div class="section-title active">视频</div>
                        </el-col>
                        <el-col :span="5">
                            <div class="text-block">
                                <div @click="navigate('videos')">精选视频</div>
                                <div @click="navigate('upload-video')">上传视频</div>
                            </div>
                        </el-col>
                    </el-row>
                </el-col>

                <el-col :span="8">
                    <div class="sidebar">
                        <h2>最新文章</h2>
                        <ul>
                            <li v-for="article in latestArticles" :key="article.id">
                                <router-link :to="'/articles/' + article.id">{{ article.title }}</router-link>
                            </li>
                        </ul>
                        <h2>最热文章</h2>
                        <ul>
                            <li v-for="article in popularArticles" :key="article.id">
                                <router-link :to="'/articles/' + article.id">{{ article.title }}</router-link>
                            </li>
                        </ul>
                    </div>
                </el-col>
            </el-row>
        </el-main>

        <!-- 底部 -->
        <el-footer class="custom-footer">
            <div class="footer-content">
                <div class="footer-left">
                    <img src="@/assets/images/icon.jpg" class="foot-img" alt="">
                </div>
                <div class="footer-center">
                    <p><a href="#">网站地图</a> | <a href="#">联系方式</a> | <a href="#">使用帮助</a> | <a href="#">隐私声明</a></p>
                    <p>主办单位: 乡促会&nbsp;&nbsp;&nbsp; 备案号：</p>
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
    data() {
        return {
            // 文章分类的轮播图数据
            carouselImages: [
                {
                    src: require('@/assets/images/RuralParty.jpg'),
                    alt: "农村党建",
                    title: "关于农村党建",
                    category: "农村党建"
                },
                {
                    src: require('@/assets/images/TeamEconomy.jpg'),
                    alt: "集体经济",
                    title: "关于集体经济",
                    category: "集体经济"
                },
                {
                    src: require('@/assets/images/industry.jpg'),
                    alt: "产业发展",
                    title: "关于产业发展",
                    category: "产业发展"
                },
                {
                    src: require('@/assets/images/CountrysideCulture.jpg'),
                    alt: "乡土文化",
                    title: "关于乡土文化",
                    category: "乡土文化"
                },
                {
                    src: require('@/assets/images/GreenMountains.jpg'),
                    alt: "青山绿水",
                    title: "关于青山绿水",
                    category: "青山绿水"
                },
                {
                    src: require('@/assets/images/YoungFarmer.jpg'),
                    alt: "青年农人",
                    title: "关于青年农人",
                    category: "青年农人"
                }
            ],
            articleTitles: [],
            username: '',
            activeMenu: '3',
            isAdmin: false,
            latestArticles: [],
            popularArticles: [],
            currentPage: 1,
            pageSize: 10
        };
    },
    computed: {
        chunkedImages() {
            const chunkSize = 3;
            return this.carouselImages.reduce((resultArray, item, index) => {
                const chunkIndex = Math.floor(index / chunkSize);
                if (!resultArray[chunkIndex]) {
                    resultArray[chunkIndex] = [];
                }
                resultArray[chunkIndex].push(item);
                return resultArray;
            }, []);
        }
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
            this.username = '';
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
                    this.articleTitles = response.data.data;
                    this.total = response.data.total;
                })
                .catch(error => {
                    this.$message.error('获取文章列表失败: ' + error.message);
                });
        },
        navigateToArticles() {
            this.$router.push('/more');
        },
        checkIfAdmin() {
            const usertype = sessionStorage.getItem('usertype');
            this.isAdmin = (usertype === '管理员');
        },
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
.total{
    background-color: rgb(226,229,234);
}
.header-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;
    background-color: white;
}
.header-logo img {
    height: fit-content;
}

.header-menu .el-menu-item {
    font-size: 16px;
    color: black;
    transition: background-color 0.3s, color 0.3s;
}

.header-menu .el-menu-item:hover {
    background-color: rgba(101, 172, 140, 0.1);
    color: rgb(101, 172, 140);
}

.header-menu .el-menu-item.is-active {
    background-color: rgb(101, 172, 140);
    color: white;
}

.header-menu .el-submenu__title {
    font-size: 18px;
    color: black;
    padding: 10px 20px;
    transition: background-color 0.3s, color 0.3s;
}

.header-menu .el-submenu__title:hover {
    background-color: rgba(101, 172, 140, 0.1);
    color: rgb(101, 172, 140);
}


.header-menu .el-menu-item.is-active {
    background-color: rgb(101, 172, 140);
    color: white;
}

.header-menu .el-menu-item {
    font-size: 16px;
    color: black;
    transition: background-color 0.3s, color 0.3s;
}

.header-menu .el-submenu__title {
    font-size: 20px;
    color: black;
    padding: 10px 20px;
    transition: background-color 0.3s, color 0.3s;
}

.main-banner {
    position: relative;
}
.banner-image {
    width: 100%;
    height: auto;
}
.banner-text {
    position: absolute;
    top: 10%;
    left: 10%;
    transform: translate(-20%, -15%);
    color: white;
    text-align: left;
    width: auto;
    font-size: 20px;
}

.title-container {
    text-align: start;
    padding: 27px;
}

.main-title {
    font-size: 32px;
    color: green;
    margin-bottom: 25px;
}

.gansu {
    font-size: 18px;
    color: #333;
    line-height: 0.5;
}

.custom-login-button {
    background-color: rgb(101, 172, 140) !important;
    color: white !important;
    border-color: rgb(101, 172, 140) !important;
    padding: 10px 20px;
    font-size: 16px;
    border-radius: 4px;
    transition: background-color 0.3s ease;
}

.custom-login-button:hover {
    background-color: rgb(91, 155, 126) !important;
    border-color: rgb(91, 155, 126) !important;
}

.custom-register-button {
    background-color: transparent !important;
    color: rgb(101, 172, 140) !important;
    border: 1px solid rgb(101, 172, 140) !important;
    padding: 10px 20px;
    font-size: 16px;
    border-radius: 4px;
    transition: background-color 0.3s ease, color 0.3s ease;
}

.custom-register-button:hover {
    background-color: rgb(101, 172, 140) !important;
    color: white !important;
}

.gansu span {
    display: block;
    margin-bottom: 5px;
}
.el-dropdown-link {
    background-color: rgb(101,172,140);
    color: white;
    padding: 8px 12px;
    border-radius: 4px;
    display: inline-flex;
    align-items: center;
    font-size: 16px;
    cursor: pointer;
    text-decoration: none;
}

.el-dropdown-link i {
    margin-left: 8px;
    font-size: 14px;
}

.image-column {
    position: relative;
    padding: 1px;
    margin-top: 10%;
}

.image-container {
    position: relative;
    height: auto;
}

.image-item {
    position: absolute;
    width: 150px;
    height: 200px;
    object-fit: cover;
    border: 2px solid white;
    scale: 0.8;
}

/* 图片1 */
.image-item:nth-child(1) {
    top: 0;
    left: 0;
}

/* 图片2 */
.image-item:nth-child(2) {
    top: 100px;
    left: 100px;
}

.content-section {
    height: fit-content;
    margin-top: 20px;
    display: flex;
    align-items: center;
    background-color: rgb(227,230,235);
    padding: 27px;
    width: auto;
}

.text-block {
    font-size: 16px;
    line-height: 2;
}

.text-block :hover{
    color: rgb(87,157,123);
    cursor: pointer;
}

.section-title {
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 10px;
}

.section-title.active {
    color: #4CAF50;
}

.divider {
    width: 2px;
    height: 80px;
    background-color: rgb(170,175,169);
    margin-right: auto;
}

.news-item {
    display: flex;
    justify-content: space-between;
}

.recommend-read{
    background-color: rgb(243,244,246);
    border-radius: 20px;
    width: auto;
}

.announcement-title {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 20px;
    padding: 10px;
}

.announcement-list {
    margin-bottom: 40px;
    padding: 10px;
}

.announcement-item {
    margin-bottom: 10px;
}

.announcement-link {
    display: flex;
    justify-content: space-between;
    text-decoration: none;
    color: #333;
}

.announcement-date {
    color: #999;
    font-size: 14px;
}

.image-section {
    width: 100%;
    margin: 10px 0;
}

.image-card {
    position: relative;
    overflow: hidden;
    height: 300px;
    width: 100%;
    border-radius: 8px;
    scale: 0.8;
}

.image {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.image-overlay {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: rgb(196,200,203);
    color: #171B1B;
    text-align: center;
    font-size: 26px;
    padding: 10px 0;
    cursor: pointer;
    transition: background-color 0.1s ease;
}

.image-overlay:hover {
    background-color: rgb(100,171,141);
    color: white;
}

.sidebar {
    width: 400px;
    height: auto;
    padding: 20px;
    background-color: rgb(226,229,234);
    border: 1px solid #ddd;
    box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
    display: flex;
    flex-direction: column;
}

.sidebar h2 {
    font-size: 20px;
    color: green;
    margin: 0;
    padding: 10px 15px;
    display: flex;
    align-items: center;
    border-left: 6px solid green;
    background-color: #C0C0C0;
    border-radius: 4px;
    margin-bottom: 20px;
    margin-top: 10px;
    width: 30%;
}

.sidebar ul {
    padding: 0;
    margin: 0;
    flex: 1;
}

.sidebar li {
    margin-bottom: 10px;
}

.sidebar a {
    text-decoration: none;
    color: black;
    font-size: 16px;
    line-height: 2.5;
}

.sidebar a:hover {
    text-decoration: none;
    color: rgb(87,157,123);
}

.custom-footer {
    background-image: url("@/assets/images/footer.png");
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

.foot-img{
    width: 300px;
    height: 80px;
    border-radius: 10px;
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
    text-decoration: none;
    cursor: pointer;
}
</style>
