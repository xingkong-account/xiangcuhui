<template>
    <div class="page-container">
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
<!--                            <el-menu-item v-if="isAdmin" index="2-1-2" @click="navigate('add-personal')">添加个人会员</el-menu-item>-->
                            <!--<el-menu-item v-if="isAdmin" index="2-1-3" @click="navigate('individual-member-requests')">审核个人会员</el-menu-item>-->
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
                        <el-menu-item index="3-1" v-if="username" @click="navigate('articles/create')">添加文章</el-menu-item>
                        <el-menu-item index="3-2" v-if="isAdmin" @click="navigate('article-review')">文章审核</el-menu-item>
                        <el-menu-item index="3-3" v-if="isAdmin" @click="navigate('articles')">文章管理</el-menu-item>
                        <el-menu-item index="3-3" v-else-if="username" @click="navigate('articles')">我发表的</el-menu-item>
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
                        <el-menu-item v-else-if="username" index="4-3" @click="navigate('video-manage')">我发布的</el-menu-item>
                        <el-menu-item v-if="username" index="4-4" @click="navigate('upload-video')">上传视频</el-menu-item>
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
                                    <div v-if="isAdmin" @click="navigate('articles')">文章管理</div>
                                    <div v-else @click="navigate('articles')">我的文章</div>
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
                            <div class="section">
                                <h2>最新文章</h2>
                                <ul>
                                    <li v-for="article in latestArticles" :key="article.id">
                                        <router-link :to="'/articles/' + article.id">{{ article.title }}</router-link>
                                    </li>
                                </ul>
                            </div>
                            <div class="section">
                                <h2>最热文章</h2>
                                <ul>
                                    <li v-for="article in popularArticles" :key="article.id">
                                        <router-link :to="'/articles/' + article.id">{{ article.title }}</router-link>
                                    </li>
                                </ul>
                            </div>
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
                        <p>地址: XX省XX市XXX区XXX</p>
                    </div>
                    <div class="footer-right">
                        <img src="" alt="Security" class="security-logo">
                        <p>甘公网安备 235487154313号</p>
                        <p>网站标识码：0000000000</p>
                    </div>
                </div>
            </el-footer>
        </el-container>
    </div>
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
    },
    mounted() {
        document.title="甘肃乡村发展促进会";
    }
}
</script>

<style scoped>
html, body {
    margin: 0;
    padding: 0;
    height: 100%;
    overflow: hidden;
}

/* 默认PC端原尺寸，宽度在1200px及以上 */
/*.page-container {*/
/*    transform: scale(1.0);*/
/*    transform-origin: top center;*/
/*    transition: transform 0.3s ease; !* 平滑过渡效果 *!*/
/*    display: flex;*/
/*    flex-direction: column;*/
/*    overflow: hidden; !* 禁用容器滚动条 *!*/
/*}*/

/*!* 大屏平板设备，宽度在992px到1200px之间 *!*/
/*@media (max-width: 1200px) and (min-width: 992px) {*/
/*    .page-container {*/
/*        !* 动态缩放比例 *!*/
/*        transform: scale(calc(0.7 + 0.3 * (100 / 1200)));*/
/*        transform-origin: top center;*/
/*    }*/
/*}*/

/*!* 小屏平板和大屏手机，宽度在768px到992px之间 *!*/
/*@media (max-width: 992px) and (min-width: 768px) {*/
/*    .page-container {*/
/*        transform: scale(calc(0.6 + 0.4 * (100 / 992)));*/
/*        transform-origin: top center;*/
/*    }*/
/*}*/

/*!* 小屏手机，宽度在480px到768px之间 *!*/
/*@media (max-width: 768px) and (min-width: 480px) {*/
/*    .page-container {*/
/*        transform: scale(calc(0.5 + 0.3 * (100 / 768)));*/
/*        transform-origin: top center;*/
/*    }*/
/*}*/

/*!* 超小屏手机，宽度在480px以下 *!*/
/*@media (max-width: 480px) {*/
/*    .page-container {*/
/*        transform: scale(calc(0.4 + 0.2 * (100 / 480)));*/
/*        transform-origin: top center;*/
/*    }*/
/*}*/

/* 全局默认样式 */
/* 全局样式 */
.total {
    width: 100%;
    height: 100%;
    background-color: rgb(226, 229, 234);
    box-sizing: border-box;
}

/* Header 容器 */
.header-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;
    background-color: white;
    flex-shrink: 0;
    position: relative; /* 确保子菜单栏的定位相对于这个容器 */
}

/* Logo 样式 */
.header-logo img {
    height: fit-content;
    object-fit: contain;
    max-height: 80px; /* 限制 Logo 最大高度 */
}

/* 菜单项样式 */
.header-menu .el-menu-item {
    font-size: 16px;
    color: black;
    transition: background-color 0.3s, color 0.3s;
}

/* 菜单项悬停样式 */
.header-menu .el-menu-item:hover {
    background-color: rgba(101, 172, 140, 0.1);
    color: rgb(101, 172, 140);
}

/* 活动菜单项样式 */
.header-menu .el-menu-item.is-active {
    background-color: rgb(101, 172, 140);
    color: white;
}

/* 子菜单标题样式 */
.header-menu .el-submenu__title {
    font-size: 20px;
    color: black;
    padding: 10px 20px;
    transition: background-color 0.3s, color 0.3s;
}

/* 响应式设计 */

/* 大屏平板设备，宽度在992px到1200px之间 */
@media (max-width: 1200px) and (min-width: 992px) {
    .header-container {
        flex-direction: column; /* 确保 logo 和菜单垂直排列 */
        align-items: center; /* 居中 logo 和菜单 */
        padding: 0 15px;
    }
    .header-logo {
        margin-bottom: 10px; /* logo 和菜单之间的间距 */
    }
    .header-menu {
        width: 100%; /* 使菜单宽度填满父容器 */
        margin-top: 10px; /* 菜单和 logo 之间的间距 */
        z-index: 10; /* 确保菜单在轮播图之上 */
    }
    .header-menu .el-menu-item {
        font-size: 15px;
    }
    .header-menu .el-submenu__title {
        font-size: 18px;
    }
}

/* 小屏平板和大屏手机，宽度在768px到992px之间 */
@media (max-width: 992px) and (min-width: 768px) {
    .header-container {
        flex-direction: column; /* 垂直排列 logo 和菜单 */
        align-items: center; /* 居中 logo 和菜单 */
        padding: 10px;
    }
    .header-logo {
        margin-bottom: 15px; /* logo 和菜单之间的间距 */
    }
    .header-menu {
        width: 100%; /* 使菜单宽度填满父容器 */
        margin-top: 10px; /* 菜单和 logo 之间的间距 */
        z-index: 10; /* 确保菜单在轮播图之上 */
    }
    .header-menu .el-menu-item {
        font-size: 14px;
    }
    .header-menu .el-submenu__title {
        font-size: 17px;
    }
}

/* 小屏手机，宽度在480px到768px之间 */
@media (max-width: 768px) and (min-width: 480px) {
    .header-container {
        flex-direction: column; /* 垂直排列 logo 和菜单 */
        align-items: center; /* 居中 logo 和菜单 */
        padding: 10px;
    }
    .header-logo {
        margin-bottom: 15px; /* logo 和菜单之间的间距 */
    }
    .header-menu {
        width: 100%; /* 使菜单宽度填满父容器 */
        margin-top: 10px; /* 菜单和 logo 之间的间距 */
        z-index: 10; /* 确保菜单在轮播图之上 */
    }
    .header-menu .el-menu-item {
        font-size: 13px;
    }
    .header-menu .el-submenu__title {
        font-size: 16px;
    }
}

/* 超小屏手机，宽度在480px以下 */
@media (max-width: 480px) {
    .header-container {
        flex-direction: column; /* 垂直排列 logo 和菜单 */
        align-items: center; /* 居中 logo 和菜单 */
        padding: 10px;
    }
    .header-logo {
        margin-bottom: 10px; /* logo 和菜单之间的间距 */
    }
    .header-menu {
        width: 100%; /* 使菜单宽度填满父容器 */
        margin-top: 10px; /* 菜单和 logo 之间的间距 */
        z-index: 10; /* 确保菜单在轮播图之上 */
    }
    .header-menu .el-menu-item {
        font-size: 12px;
    }
    .header-menu .el-submenu__title {
        font-size: 14px;
    }
}



/* 默认样式 */
.main-banner {
    position: relative;
    height: auto;
    overflow: hidden; /* 确保图片不会导致滚动条 */
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
/* 响应式媒体查询 */

/* 大屏平板设备，宽度在992px到1200px之间 */
@media (max-width: 1200px) and (min-width: 992px) {
    .banner-text {
        font-size: 18px;
        top: 12%;
        left: 10%;
        transform: translate(-15%, -12%);
    }
}

/* 小屏平板和大屏手机，宽度在768px到992px之间 */
@media (max-width: 992px) and (min-width: 768px) {
    .banner-text {
        font-size: 16px;
        top: 15%;
        left: 8%;
        transform: translate(-10%, -10%);
    }
}

/* 小屏手机，宽度在480px到768px之间 */
@media (max-width: 768px) and (min-width: 480px) {
    .banner-text {
        font-size: 14px;
        top: 18%;
        left: 6%;
        transform: translate(-5%, -5%);
    }
}

/* 超小屏手机，宽度在480px以下 */
@media (max-width: 480px) {
    .banner-text {
        font-size: 12px;
        top: 20%;
        left: 5%;
        transform: translate(0, 0);
    }
}

/* 默认样式 */
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

/* 响应式媒体查询 */

/* 大屏平板设备，宽度在992px到1200px之间 */
@media (max-width: 1200px) and (min-width: 992px) {
    .title-container {
        padding: 20px;
    }
    .main-title {
        font-size: 28px;
        margin-bottom: 20px;
    }
    .gansu {
        font-size: 16px;
        line-height: 0.7;
    }
}

/* 小屏平板和大屏手机，宽度在768px到992px之间 */
@media (max-width: 992px) and (min-width: 768px) {
    .title-container {
        padding: 15px;
    }
    .main-title {
        font-size: 24px;
        margin-bottom: 18px;
    }
    .gansu {
        font-size: 14px;
        line-height: 0.75;
    }
}

/* 小屏手机，宽度在480px到768px之间 */
@media (max-width: 768px) and (min-width: 480px) {
    .title-container {
        padding: 12px;
    }
    .main-title {
        font-size: 22px;
        margin-bottom: 15px;
    }
    .gansu {
        font-size: 12px;
        line-height: 0.8;
    }
}

/* 超小屏手机，宽度在480px以下 */
@media (max-width: 480px) {
    .title-container {
        padding: 10px;
    }
    .main-title {
        font-size: 20px;
        margin-bottom: 10px;
    }
    .gansu {
        font-size: 10px;
        line-height: 1;
    }
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

/* 响应式媒体查询 */

/* 大屏平板设备，宽度在992px到1200px之间 */
@media (max-width: 1200px) and (min-width: 992px) {
    .custom-login-button,
    .custom-register-button {
        padding: 12px 24px;
        font-size: 14px;
    }
}

/* 小屏平板和大屏手机，宽度在768px到992px之间 */
@media (max-width: 992px) and (min-width: 768px) {
    .custom-login-button,
    .custom-register-button {
        padding: 10px 20px;
        font-size: 14px;
    }
}

/* 小屏手机，宽度在480px到768px之间 */
@media (max-width: 768px) and (min-width: 480px) {
    .custom-login-button,
    .custom-register-button {
        padding: 8px 16px;
        font-size: 12px;
    }
}

/* 超小屏手机，宽度在480px以下 */
@media (max-width: 480px) {
    .custom-login-button,
    .custom-register-button {
        padding: 6px 12px;
        font-size: 12px;
    }
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

/* 默认样式 */
.el-dropdown-link {
    background-color: rgb(101, 172, 140);
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

/* 响应式媒体查询 */

/* 大屏平板设备，宽度在992px到1200px之间 */
@media (max-width: 1200px) and (min-width: 992px) {
    .el-dropdown-link {
        padding: 10px 14px;
        font-size: 15px;
    }

    .el-dropdown-link i {
        margin-left: 10px;
        font-size: 15px;
    }
}

/* 小屏平板和大屏手机，宽度在768px到992px之间 */
@media (max-width: 992px) and (min-width: 768px) {
    .el-dropdown-link {
        padding: 9px 13px;
        font-size: 14px;
    }

    .el-dropdown-link i {
        margin-left: 9px;
        font-size: 13px;
    }
}

/* 小屏手机，宽度在480px到768px之间 */
@media (max-width: 768px) and (min-width: 480px) {
    .el-dropdown-link {
        padding: 8px 12px;
        font-size: 13px;
    }

    .el-dropdown-link i {
        margin-left: 8px;
        font-size: 12px;
    }
}

/* 超小屏手机，宽度在480px以下 */
@media (max-width: 480px) {
    .el-dropdown-link {
        padding: 6px 10px;
        font-size: 12px;
    }

    .el-dropdown-link i {
        margin-left: 6px;
        font-size: 11px;
    }
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

/* 响应式媒体查询 */

/* 大屏平板设备，宽度在992px到1200px之间 */
@media (max-width: 1200px) and (min-width: 992px) {
    .image-item {
        width: 120px;
        height: 160px;
        transform: scale(0.85);
    }

    .image-item:nth-child(2) {
        top: 80px;
        left: 80px;
    }
}

/* 小屏平板和大屏手机，宽度在768px到992px之间 */
@media (max-width: 992px) and (min-width: 768px) {
    .image-item {
        width: 100px;
        height: 140px;
        transform: scale(0.9);
    }

    .image-item:nth-child(2) {
        top: 60px;
        left: 60px;
    }
}

/* 小屏手机，宽度在480px到768px之间 */
@media (max-width: 768px) and (min-width: 480px) {
    .image-item {
        width: 80px;
        height: 120px;
        transform: scale(0.95);
    }

    .image-item:nth-child(2) {
        top: 40px;
        left: 40px;
    }
}

/* 超小屏手机，宽度在480px以下 */
@media (max-width: 480px) {
    .image-item {
        width: 60px;
        height: 100px;
        transform: scale(1);
    }

    .image-item:nth-child(2) {
        top: 20px;
        left: 20px;
    }
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

/* 默认样式 */
.content-section {
    height: fit-content;
    margin-top: 20px;
    display: flex;
    align-items: center;
    background-color: rgb(227, 230, 235);
    padding: 27px;
    width: auto;
}

.text-block {
    font-size: 16px;
    line-height: 2;
}

.text-block:hover {
    color: rgb(87, 157, 123);
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

/* 响应式媒体查询 */

/* 大屏平板设备，宽度在992px到1200px之间 */
@media (max-width: 1200px) and (min-width: 992px) {
    .content-section {
        padding: 20px;
    }

    .text-block {
        font-size: 15px;
        line-height: 1.8;
    }

    .section-title {
        font-size: 17px;
    }
}

/* 小屏平板和大屏手机，宽度在768px到992px之间 */
@media (max-width: 992px) and (min-width: 768px) {
    .content-section {
        padding: 15px;
    }

    .text-block {
        font-size: 14px;
        line-height: 1.6;
    }

    .section-title {
        font-size: 16px;
    }
}

/* 小屏手机，宽度在480px到768px之间 */
@media (max-width: 768px) and (min-width: 480px) {
    .content-section {
        padding: 12px;
    }

    .text-block {
        font-size: 13px;
        line-height: 1.5;
    }

    .section-title {
        font-size: 15px;
    }
}

/* 超小屏手机，宽度在480px以下 */
@media (max-width: 480px) {
    .content-section {
        padding: 10px;
    }

    .text-block {
        font-size: 12px;
        line-height: 1.4;
    }

    .section-title {
        font-size: 14px;
    }
}


/* 默认样式 */
.divider {
    width: 2px;
    height: 80px;
    background-color: rgb(170, 175, 169);
    margin-right: auto;
}

/* 响应式媒体查询 */

/* 大屏平板设备，宽度在992px到1200px之间 */
@media (max-width: 1200px) and (min-width: 992px) {
    .divider {
        height: 70px; /* 适度调整高度 */
    }
}

/* 小屏平板和大屏手机，宽度在768px到992px之间 */
@media (max-width: 992px) and (min-width: 768px) {
    .divider {
        height: 60px; /* 进一步调整高度 */
    }
}

/* 小屏手机，宽度在480px到768px之间 */
@media (max-width: 768px) and (min-width: 480px) {
    .divider {
        height: 50px; /* 继续调整高度 */
    }
}

/* 超小屏手机，宽度在480px以下 */
@media (max-width: 480px) {
    .divider {
        height: 40px; /* 最小高度 */
    }
}


/* 默认样式 */
.news-item {
    display: flex;
    justify-content: space-between;
    padding: 15px;
    background-color: #f9f9f9;
    border-bottom: 1px solid #ddd;
}

/* 响应式媒体查询 */

/* 大屏平板设备，宽度在992px到1200px之间 */
@media (max-width: 1200px) and (min-width: 992px) {
    .news-item {
        padding: 12px;
        flex-direction: row; /* 保持水平排列 */
    }
}

/* 小屏平板和大屏手机，宽度在768px到992px之间 */
@media (max-width: 992px) and (min-width: 768px) {
    .news-item {
        padding: 10px;
        flex-direction: row; /* 保持水平排列 */
    }
}

/* 小屏手机，宽度在480px到768px之间 */
@media (max-width: 768px) and (min-width: 480px) {
    .news-item {
        padding: 8px;
        flex-direction: column; /* 切换为垂直排列 */
        align-items: flex-start;
    }
}

/* 超小屏手机，宽度在480px以下 */
@media (max-width: 480px) {
    .news-item {
        padding: 6px;
        flex-direction: column; /* 保持垂直排列 */
        align-items: flex-start;
    }
}


/* 默认样式 */
.recommend-read {
    background-color: rgb(243, 244, 246);
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

/* 响应式媒体查询 */

/* 大屏平板设备，宽度在992px到1200px之间 */
@media (max-width: 1200px) and (min-width: 992px) {
    .announcement-title {
        font-size: 17px;
        padding: 12px;
    }

    .announcement-list {
        padding: 15px;
    }

    .announcement-link {
        font-size: 15px;
    }

    .announcement-date {
        font-size: 13px;
    }
}

/* 小屏平板和大屏手机，宽度在768px到992px之间 */
@media (max-width: 992px) and (min-width: 768px) {
    .announcement-title {
        font-size: 16px;
        padding: 12px;
    }

    .announcement-list {
        padding: 12px;
    }

    .announcement-link {
        font-size: 14px;
    }

    .announcement-date {
        font-size: 12px;
    }
}

/* 小屏手机，宽度在480px到768px之间 */
@media (max-width: 768px) and (min-width: 480px) {
    .announcement-title {
        font-size: 15px;
        padding: 10px;
    }

    .announcement-list {
        padding: 10px;
    }

    .announcement-link {
        font-size: 13px;
    }

    .announcement-date {
        font-size: 11px;
    }
}

/* 超小屏手机，宽度在480px以下 */
@media (max-width: 480px) {
    .recommend-read {
        border-radius: 15px; /* 调整边框半径 */
    }

    .announcement-title {
        font-size: 14px;
        padding: 8px;
    }

    .announcement-list {
        padding: 8px;
    }

    .announcement-link {
        font-size: 12px;
    }

    .announcement-date {
        font-size: 10px;
    }
}

/*文章类别轮播图，每行展示三张 */
/* 默认样式 */
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
    transform: scale(1.0); /* 初始缩放比例 */
    transform-origin: top left; /* 缩放原点设置在左上角 */
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
    background-color: rgb(196, 200, 203);
    color: #171B1B;
    text-align: center;
    font-size: 26px;
    padding: 10px 0;
    cursor: pointer;
    transition: background-color 0.1s ease;
}

.image-overlay:hover {
    background-color: rgb(100, 171, 141);
    color: white;
}

/* 响应式媒体查询 */

/* 大屏平板设备，宽度在992px到1200px之间 */
@media (max-width: 1200px) and (min-width: 992px) {
    .image-card {
        height: 250px; /* 调整高度 */
        width: 90%; /* 调整宽度 */
        transform: scale(0.75); /* 按比例缩小 */
    }

    .image-overlay {
        font-size: 22px; /* 调整文字大小 */
        padding: 8px 0;
    }
}

/* 小屏平板和大屏手机，宽度在768px到992px之间 */
@media (max-width: 992px) and (min-width: 768px) {
    .image-card {
        height: 200px; /* 进一步调整高度 */
        width: 85%; /* 调整宽度 */
        transform: scale(0.7); /* 按比例缩小 */
    }

    .image-overlay {
        font-size: 18px; /* 调整文字大小 */
        padding: 6px 0;
    }
}

/* 小屏手机，宽度在480px到768px之间 */
@media (max-width: 768px) and (min-width: 480px) {
    .image-card {
        height: 160px; /* 调整高度 */
        width: 80%; /* 调整宽度 */
        transform: scale(0.65); /* 按比例缩小 */
    }

    .image-overlay {
        font-size: 16px; /* 调整文字大小 */
        padding: 5px 0;
    }
}

/* 超小屏手机，宽度在480px以下 */
@media (max-width: 480px) {
    .image-card {
        height: 130px; /* 最小高度 */
        width: 75%; /* 最小宽度 */
        transform: scale(0.6); /* 按比例缩小 */
    }

    .image-overlay {
        font-size: 14px; /* 调整文字大小 */
        padding: 4px 0;
    }
}

/* 默认样式 */
.sidebar {
    display: flex;
    flex-direction: column;
    height: 100%;
    padding: 20px;
    background-color: rgb(226, 229, 234);
    border: 1px solid #ddd;
    box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
    gap: 20px;
    width: 400px;
    max-height: 130vh;
    justify-content: space-between;
    overflow-y: auto;
}

.sidebar > *:not(h2) {
    flex: 1;
    margin: 10px 0;
}

.section {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 50%; /* 每个 section 占据父容器的一半高度 */
    overflow: hidden;
}

h2 {
    font-size: 20px;
    color: rgb(101, 172, 140);
    padding: 10px 15px;
    background-color: rgb(197, 198, 200);
    border-left: 8px solid rgb(101, 172, 140);
    border-radius: 4px;
    margin: 0 0 10px;
}

.sidebar ul {
    list-style: none;
    padding: 0;
    margin: 0;
    overflow-y: auto;
    scrollbar-width: none;
    -ms-overflow-style: none;
}

/* 响应式媒体查询 */

/* 大屏平板设备，宽度在992px到1200px之间 */
@media (max-width: 1200px) and (min-width: 992px) {
    .sidebar {
        width: 350px; /* 调整宽度 */
        padding: 15px;
        gap: 15px;
    }

    h2 {
        font-size: 18px; /* 调整标题字体大小 */
    }
}

/* 小屏平板和大屏手机，宽度在768px到992px之间 */
@media (max-width: 992px) and (min-width: 768px) {
    .sidebar {
        width: 300px; /* 调整宽度 */
        padding: 10px;
        gap: 10px;
    }

    h2 {
        font-size: 16px; /* 调整标题字体大小 */
    }
}

/* 小屏手机，宽度在480px到768px之间 */
@media (max-width: 768px) and (min-width: 480px) {
    .sidebar {
        width: 250px; /* 调整宽度 */
        padding: 8px;
        gap: 8px;
    }

    h2 {
        font-size: 14px; /* 调整标题字体大小 */
    }
}

/* 超小屏手机，宽度在480px以下 */
@media (max-width: 480px) {
    .sidebar {
        width: 200px; /* 调整宽度 */
        padding: 5px;
        gap: 5px;
    }

    h2 {
        font-size: 12px; /* 调整标题字体大小 */
    }

    .sidebar ul {
        scrollbar-width: thin;
        -ms-overflow-style: scrollbar;
    }
}

ul::-webkit-scrollbar {
    display: none; /* 隐藏 WebKit 浏览器中的滚动条 */
}

.router-link {
    text-decoration: none;
    color: inherit;
}


/* 默认样式 */
.sidebar a {
    text-decoration: none;
    color: black;
    font-size: 16px;
    line-height: 2.5;
}

.sidebar a:hover {
    text-decoration: none;
    color: rgb(87, 157, 123);
}

/* 响应式媒体查询 */

/* 大屏平板设备，宽度在992px到1200px之间 */
@media (max-width: 1200px) and (min-width: 992px) {
    .sidebar a {
        font-size: 15px; /* 调整字体大小 */
        line-height: 2.4; /* 调整行高 */
    }
}

/* 小屏平板和大屏手机，宽度在768px到992px之间 */
@media (max-width: 992px) and (min-width: 768px) {
    .sidebar a {
        font-size: 14px; /* 调整字体大小 */
        line-height: 2.3; /* 调整行高 */
    }
}

/* 小屏手机，宽度在480px到768px之间 */
@media (max-width: 768px) and (min-width: 480px) {
    .sidebar a {
        font-size: 13px; /* 调整字体大小 */
        line-height: 2.2; /* 调整行高 */
    }
}

/* 超小屏手机，宽度在480px以下 */
@media (max-width: 480px) {
    .sidebar a {
        font-size: 12px; /* 调整字体大小 */
        line-height: 2; /* 调整行高 */
    }
}


/* 底部版权等信息 */
.custom-footer {
    background-image: url("@/assets/images/footer.png");
    background-size: cover;
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

.foot-img {
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

/* 响应式媒体查询 */

/* 大屏平板设备，宽度在992px到1200px之间 */
@media (max-width: 1200px) and (min-width: 992px) {
    .footer-content {
        flex-direction: row; /* 保持三列布局 */
    }

    .footer-left, .footer-right {
        align-items: center;
    }

    .footer-center {
        flex: 2;
    }

    .foot-img {
        width: 250px; /* 调整图片宽度 */
        height: 60px;  /* 调整图片高度 */
    }
}

/* 小屏平板和大屏手机，宽度在768px到992px之间 */
@media (max-width: 992px) and (min-width: 768px) {
    .footer-content {
        flex-direction: column; /* 变为竖直布局 */
    }

    .footer-left, .footer-right {
        margin-bottom: 20px; /* 增加底部间距 */
    }

    .foot-img {
        width: 200px; /* 调整图片宽度 */
        height: 50px;  /* 调整图片高度 */
    }
}

/* 小屏手机，宽度在480px到768px之间 */
@media (max-width: 768px) and (min-width: 480px) {
    .footer-content {
        flex-direction: column; /* 保持竖直布局 */
    }

    .footer-left, .footer-right {
        margin-bottom: 15px; /* 调整底部间距 */
    }

    .foot-img {
        width: 150px; /* 调整图片宽度 */
        height: 40px;  /* 调整图片高度 */
    }
}

/* 超小屏手机，宽度在480px以下 */
@media (max-width: 480px) {
    .footer-content {
        flex-direction: column; /* 保持竖直布局 */
    }

    .footer-left, .footer-right {
        margin-bottom: 10px; /* 调整底部间距 */
    }

    .foot-img {
        width: 120px; /* 调整图片宽度 */
        height: 30px;  /* 调整图片高度 */
    }
}
</style>
