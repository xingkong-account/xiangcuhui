<template>
    <div class="page-container">
        <!-- 电脑端布局 -->
        <div class="desktop-layout" v-show="!isMobile">
            <!-- 原有电脑端布局代码 -->
            <el-container class="total">
                <!-- 头部选项 -->
                <el-header class="header-container">
                    <div class="header-logo">
                        <img src="@/assets/images/new-icon.png" alt="Logo" style="object-fit: contain; height: 75px; scale: 0.9; width: 100%">
                    </div>

                    <el-menu mode="horizontal" class="mobile-header-menu">
                        <el-menu-item index="1" @click="handleHomeClick" style="font-size: 20px; color: black">首页</el-menu-item>
                        <!-- 会员管理下拉菜单，登录才能看到会员菜单 -->
                        <el-submenu class="member-management-menu" v-if="isAdmin || username" :index="'2'" :popper-append-to-body="false" :default-active="activeMenu">
                            <template #title>会员</template>
                            <el-submenu index="2-1">
                                <template #title>个人会员</template>
                                <el-menu-item index="2-1-1" v-if="isAdmin" @click="navigate('individual-members')">个人会员管理</el-menu-item>
                                <el-menu-item index="2-1-1" v-else @click="navigate('individual-members')">查看个人会员</el-menu-item>
                                <el-menu-item v-if="isAdmin" index="2-1-2" @click="navigate('add-personal')">添加个人会员</el-menu-item>
                                <el-menu-item v-if="isAdmin" index="2-1-3" @click="navigate('individual-member-requests')">审核个人会员</el-menu-item>
                            </el-submenu>

                            <el-submenu index="2-2">
                                <template #title>团队会员</template>
                                <el-menu-item index="2-2-1" @click="navigate('group--member-manage')">团队会员管理</el-menu-item>
                                <el-menu-item index="2-2-2" @click="navigate('team-members')">查看所有团队会员</el-menu-item>
                                <el-menu-item v-if="isAdmin" index="2-2-3" @click="navigate('add-team-member')">添加团队会员</el-menu-item>
                                <el-menu-item v-if="isAdmin" index="2-2-4" @click="navigate('review-team-members')">审核团队会员</el-menu-item>
                            </el-submenu>
                        </el-submenu>

                        <!-- 文章管理下拉菜单 -->
                        <el-submenu class="article-management-menu" :index="'3'" :popper-append-to-body="false" :default-active="activeMenu">
                            <template #title>文章</template>
                            <el-menu-item index="3-1" v-if="username" @click="navigate('articles/create')">添加文章</el-menu-item>
                            <el-menu-item index="3-2" v-if="isAdmin" @click="navigate('article-review')">文章审核</el-menu-item>
                            <el-menu-item index="3-3" v-if="isAdmin" @click="navigate('articles')">文章管理</el-menu-item>
                            <el-menu-item index="3-3" v-else-if="username" @click="navigate('articles')">我发表的</el-menu-item>
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

<!--                        <el-menu-item index="5" @click="navigate('about')" style="font-size: 20px; color: black">-->
<!--                            关于我们-->
<!--                        </el-menu-item>-->
                    </el-menu>

                    <!-- 搜索条 -->
                    <div class="search-container" @mouseleave="showSearch = false">
                        <div class="search-icon" @mouseenter="showSearch = true">
                            <i class="el-icon-search"></i>
                            <input
                                v-if="showSearch"
                                type="text"
                                placeholder="搜索..."
                                class="search-input"
                                @keyup.enter="performSearch"
                                @focus="showSearch = true"
                                @click.stop
                            />
                        </div>
                    </div>
                    <!-- 登录注册 -->
                    <div class="auth-buttons">
                        <el-dropdown v-if="!isLoggedIn" trigger="click" @command="handleCommand">
                            <span class="el-dropdown-link" @click="toggleDropdown">
                                <i class="el-icon-user"></i><i class="el-icon-arrow-down"></i>
                            </span>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item command="login">登录</el-dropdown-item>
                                <el-dropdown-item command="register">注册</el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                        <div v-if="username">
                            <el-dropdown @command="handleCommand">
                                <span class="el-dropdown-link">
                                    <i class="el-icon-user"></i><i class="el-icon-arrow-down"></i>
                                </span>
                                <el-dropdown-menu slot="dropdown">
                                    <el-dropdown-item command="changePassword">修改密码</el-dropdown-item>
                                    <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                                </el-dropdown-menu>
                            </el-dropdown>
                        </div>
                    </div>
                </el-header>
                <!-- 主页上面的轮播图 -->
                <el-main>
<!--                    <div class="computer-banner">-->
<!--                        <el-carousel :interval="5000" arrow="always" indicator-position="outside">-->
<!--                            <el-carousel-item v-for="(banner, index) in topBanners" :key="index">-->
<!--                                <router-link :to="'/articles/' + banner.articleId">-->
<!--                                    <img :src="banner.src" alt="Banner" class="banner-image" />-->
<!--                                </router-link>-->
<!--                            </el-carousel-item>-->
<!--                        </el-carousel>-->
<!--                    </div>-->
                    <div class="computer-banner">
                        <el-carousel height="420px" :interval="2000" arrow="always" indicator-position="outside">
                            <el-carousel-item v-for="(image, index) in topBanners" :key="index">
                                <router-link :to="'/articles/' + image.articleId">
                                    <img :src="image.src" :alt="image.alt" class="carousel-image" />
                                </router-link>
                            </el-carousel-item>
                        </el-carousel>
                    </div>

                    <!-- <img src="@/assets/images/banner1.jpg" alt="Banner" class="banner-image">-->
<!--                    <div class="banner-text">-->
<!--                        <span>Website of the </span>-->
<!--                        <br>-->
<!--                        <span>Rural Promotion</span>-->
<!--                        <br>-->
<!--                        <span>Association</span>-->
<!--                    </div>-->
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
                                                <span>{{ article.category }} - {{ getSnippet(article.title, 25) }}</span>
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
                                    <h2>新闻动态</h2>
                                    <ul>
                                        <li v-for="article in latestArticles" :key="article.id">
                                            <router-link :to="'/articles/' + article.id">{{ article.title }}</router-link>
                                            <span class="published-date">{{ formatDate(article.created_at) }}</span>
                                        </li>
                                    </ul>
                                </div>
                                <div class="section">
                                    <h2>热点资讯</h2>
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
                        <!-- 左侧导航链接 -->
                        <div class="footer-left">
                            <p>
                                <a href="#">网站地图</a> |
                                <a href="#">联系方式</a> |
                                <a href="#">隐私声明</a>
                            </p>
                        </div>

                        <!-- 中间部分：主办单位和地址信息 -->
                        <div class="footer-center">
                            <p>主办单位: 乡促会</p>
                            <p>赞助：玄易文化有限公司</p>
                            <p>地址: 甘肃省兰州市七里河区</p>
                        </div>

                        <!-- 右侧备案信息 -->
                        <div class="footer-right">
                            <a href="https://www.beian.gov.cn/portal/registerSystemInfo?recordcode=44030702002386" target="_blank" class="record-link">
                                <img src="@/assets/images/security.png" class="security-logo">
                                <span>粤公网安备44030702002386号</span>
                            </a>
                            <p>甘A2-20044005号</p>
                        </div>
                    </div>
                </el-footer>

            </el-container>
        </div>

        <!-- 手机端布局 -->
        <div class="mobile-layout" v-show="isMobile">
            <el-container class="mobile-total">
                <template>
                    <div>
                        <!-- 手机端的头部-->
                        <div class="mobile-header">
                            <div class="mobile-header-left">
                                <img src="@/assets/images/icon.jpg" style="width: 120px; height: auto;">
                            </div>
                            <div class="mobile-header-right">
                                <i class="el-icon-search">
                                    检索
                                </i>
                                <!-- 菜单图标 -->
                                <i @click="toggleMenu" class="menu-icon" :class="menuVisible ? 'el-icon-close' : 'el-icon-s-operation'">
                                    操作
                                </i>
                            </div>
                        </div>
                        <!--头部操作选项框-->
                        <transition name="slide">
                            <div v-if="menuVisible" class="overlay" @click="toggleMenu">
                                <div class="menu" @click.stop>
                                    <div class="menu-header">
                                        <h4>{{ username ? '欢迎, ' + username : '请选择登录或注册' }}</h4>
                                        <span class="close-icon" @click="toggleMenu">✖</span>
                                    </div>
                                    <div v-if="username">
                                        <div class="menu-item" @click="changePassword">修改密码</div>
                                        <div class="menu-item" @click="logout">退出登录</div>
                                    </div>
                                    <div v-else>
                                        <div class="menu-item" @click="goToLogin">登录</div>
                                        <div class="menu-item" @click="goToRegister">注册</div>
                                    </div>
                                </div>
                            </div>
                        </transition>
                    </div>
                </template>

                <!-- 手机端主要内容部分 -->
                <el-main class="mobile-main-content">
                    <!--顶部轮播图，点击跳转到文章内容-->
                    <div class="">
                        <el-carousel height="200px" :interval="5000" arrow="always" indicator-position="outside">
                            <el-carousel-item v-for="(banner, index) in topBanners" :key="index">
                                <router-link :to="'/articles/' + banner.articleId">
                                    <img :src="banner.src" alt="Banner" class="carousel-image" />
                                </router-link>
                            </el-carousel-item>
                        </el-carousel>
                    </div>
<!--                        <div class="mobile-banner-text">-->
<!--                            <span>Website of the</span><br>-->
<!--                            <span>Rural Promotion</span><br>-->
<!--                            <span>Association</span>-->
<!--                        </div>-->
                        <!--手机端顶部导航栏-->
                    <div class="mobile-navigation">
                        <div class="menu-icon" @click="toggleMainMenu">
                            <i class="el-icon-s-operation">菜单</i>
                        </div>
                        <transition name="">
                            <div v-if="mainMenuVisible" class="overlay" @click="toggleMainMenu">
                                <div class="menu" @click.stop>
                                    <el-menu mode="vertical" class="mobile-menu">
<!--                                        <el-menu-item index="1" @click="navigate('about')">关于我们</el-menu-item>-->
                                        <el-submenu v-if="isAdmin || username" index="2">
                                            <template #title>会员</template>
                                            <el-menu-item index="2-1" v-if="isAdmin" @click="navigate('individual-members')">个人会员管理</el-menu-item>
                                            <el-menu-item index="2-1" v-else @click="navigate('individual-members')">查看个人会员</el-menu-item>
                                            <el-menu-item index="2-2-1" @click="navigate('group--member-manage')">团队会员管理</el-menu-item>
                                            <el-menu-item index="2-2-2" @click="navigate('team-members')">查看所有团队会员</el-menu-item>
                                        </el-submenu>
                                        <el-submenu index="3">
                                            <template #title>文章</template>
                                            <el-submenu :index="'3-4'">
                                                <template #title>按分类查看</template>
                                                <el-menu-item index="3-4-1" @click="navigate('category-articles', '农村党建')">农村党建</el-menu-item>
                                                <el-menu-item index="3-4-2" @click="navigate('category-articles', '集体经济')">集体经济</el-menu-item>
                                                <el-menu-item index="3-4-3" @click="navigate('category-articles', '产业发展')">产业发展</el-menu-item>
                                                <el-menu-item index="3-4-4" @click="navigate('category-articles', '乡土文化')">乡土文化</el-menu-item>
                                                <el-menu-item index="3-4-5" @click="navigate('category-articles', '青山绿水')">青山绿水</el-menu-item>
                                                <el-menu-item index="3-4-6" @click="navigate('category-articles', '青年农人')">青年农人</el-menu-item>
                                            </el-submenu>
                                            <el-menu-item index="3-1" v-if="username" @click="navigate('articles/create')">添加文章</el-menu-item>
                                            <el-menu-item index="3-2" v-if="isAdmin" @click="navigate('article-review')">文章审核</el-menu-item>
                                            <el-menu-item index="3-3" v-if="isAdmin" @click="navigate('articles')">文章管理</el-menu-item>
                                            <el-menu-item index="3-3" v-else-if="username" @click="navigate('articles')">我发表的</el-menu-item>
                                        </el-submenu>
                                        <el-submenu index="4">
                                            <template #title>视频</template>
                                            <el-menu-item index="4-1" @click="navigate('videos')">精选视频</el-menu-item>
                                            <el-menu-item v-if="isAdmin" index="4-2" @click="navigate('video-review')">视频审核</el-menu-item>
                                            <el-menu-item v-if="isAdmin" index="4-3" @click="navigate('video-manage')">所有视频</el-menu-item>
                                            <el-menu-item v-else-if="username" index="4-3" @click="navigate('video-manage')">我发布的</el-menu-item>
                                            <el-menu-item v-if="username" index="4-4" @click="navigate('upload-video')">上传视频</el-menu-item>
                                        </el-submenu>
                                    </el-menu>
                                </div>
                            </div>
                        </transition>
                        </div>
                            <!-- TODO 添加两行，第一行放两张图片，2:1的比例，第二行也是两张，1:2的比例-->
                            <div class="mobile-image-container">
                                <div class="mobile-image-section">
                                    <!-- 第一行 -->
                                    <div class="image-row">
                                        <div class="image-wide">
                                            <router-link :to="'/articles/' + middleBanners[0]?.articleId">
                                                <img :src="middleBanners[0]?.src" alt="Image 1" class="carousel-image"/>
                                            </router-link>
                                        </div>
                                        <div class="image-narrow">
                                            <router-link :to="'/articles/' + middleBanners[1]?.articleId">
                                                <img :src="middleBanners[1]?.src" alt="Image 2" class="carousel-image"/>
                                            </router-link>
                                        </div>
                                    </div>

                                    <!-- 第二行 -->
                                    <div class="image-row">
                                        <div class="image-narrow">
                                            <router-link :to="'/articles/' + middleBanners[2]?.articleId">
                                                <img :src="middleBanners[2]?.src" alt="Image 3" class="carousel-image"/>
                                            </router-link>
                                        </div>
                                        <div class="image-wide">
                                            <router-link :to="'/articles/' + middleBanners[3]?.articleId">
                                                <img :src="middleBanners[3]?.src" alt="Image 4" class="carousel-image"/>
                                            </router-link>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- 协会标题  -->
                            <div class="mobile-title-container">
                                <h1 class="mobile-main-title">甘肃省乡村发展促进会</h1>
                                <div class="mobile-gansu">
                                    <span>GANSU ASSOCIATION FOR THE</span><br>
                                    <span>PROMOTION OF RURAL</span><br>
                                    <span>DEVELOPMENT</span><br>
                                </div>
                            </div>

                            <!-- 文章分类部分轮播图  -->
                            <div class="mobile-image-category">
                                <el-carousel height="150px" :interval="5000" arrow="always" indicator-position="outside">
                                    <el-carousel-item v-for="(imageSet, index) in chunkedImages" :key="index">
                                        <el-row :gutter="10">
                                            <el-col v-for="(image, idx) in imageSet" :key="idx" :span="8"> <!-- 修改这里的 span 值 -->
                                                <div class="mobile-image-card">
                                                    <img :src="image.src" :alt="image.alt" class="mobile-image" />
                                                    <div class="mobile-image-overlay">
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

                        <!--播放视频的对话框-->
<!--                        <el-dialog-->
<!--                            v-if="currentVideo"-->
<!--                            :visible.sync="videoDialogVisible"-->
<!--                            width="80%"-->
<!--                            center-->
<!--                            @close="closeVideoDialog"-->
<!--                        >-->
<!--                            <div class="video-player">-->
<!--                                <video-->
<!--                                    ref="videoPlayer"-->
<!--                                    controls-->
<!--                                    autoplay-->
<!--                                    :src="currentVideo.url"-->
<!--                                    style="width: 100%;"-->
<!--                                ></video>-->
<!--                            </div>-->
<!--                        </el-dialog>-->
                    <!--新闻动态、热点资讯-->
                    <div>
                       <div class="mobile-sidebar">
                           <div class="section">
                               <h2 style="min-width: 20%; max-width: 25%; text-align: start">新闻动态</h2>
                               <ul>
                                   <li v-for="article in mobileLatestArticles" :key="article.id">
                                       <router-link :to="'/articles/' + article.id">{{ article.title }}</router-link>
                                       <span class="published-date">{{ formatDate(article.created_at) }}</span>
                                   </li>
                               </ul>
                           </div>
                           <!-- TODO 由管理员点击按钮插入横幅滚动广告，并可以进行删除操作（管理员使用删除按钮）-->
                           <div class="ad-section">
                               <!-- 轮播广告展示 -->
                               <el-carousel
                                   v-show="this.adsBanners.length > 0"
                                   height="120px"
                                   :interval="5000"
                                   arrow="always"
                                   indicator-position="outside"
                               >
                                   <el-carousel-item v-for="(banner, index) in adsBanners" :key="index">
                                       <img :src="banner.imageUrl" alt="广告图片" class="carousel-image">
                                       <!-- 管理员操作 -->
                                       <div v-if="isAdmin" class="banner-actions">
                                           <el-button @click="confirmDelete(banner.id)" type="danger">删除广告</el-button>
                                       </div>
                                   </el-carousel-item>
                               </el-carousel>
                               <!-- 添加广告按钮 -->
                               <div v-if="isAdmin">
                                   <el-upload
                                       :action="$baseUrl + '/api/upload'"
                                       list-type="picture"
                                       :show-file-list="false"
                                       :on-success="handleUploadSuccess"
                                       :on-error="handleUploadError"
                                       :limit="10"
                                       accept="image/*">
                                       <el-button size="small" type="primary" class="upload-button">点击上传</el-button>
                                       <div slot="tip" class="el-upload__tip">只能上传图片文件</div>
                                   </el-upload>
                               </div>
                           </div>
                           <div class="section">
                               <h2 style="min-width: 20%; max-width: 25%; text-align: start">热点资讯</h2>
                               <ul>
                                   <li v-for="article in mobilePopularArticles" :key="article.id">
                                       <router-link :to="'/articles/' + article.id">{{ article.title }}</router-link>
                                       <span class="published-date">{{ formatDate(article.created_at) }}</span>
                                   </li>
                               </ul>
                           </div>
                       </div>
                    </div>

                    <div class="footer-section">
                        <el-row :gutter="10" class="footer-content-section">
                            <!-- 我们部分 -->
                            <el-col :span="6">
                                <div class="footer-section-title">我们</div>
                                <div class="footer-text-block">
                                    <div>协会简介</div>
                                    <div>宗旨使命</div>
                                    <div>现任领导</div>
                                    <div>服务愿景</div>
                                </div>
                            </el-col>

                            <!-- 会员部分 -->
                            <el-col :span="6">
                                <div class="footer-section-title">会员</div>
                                <div class="footer-text-block">
                                    <div>入会须知</div>
                                    <div>申请提交</div>
                                    <div>审批流程</div>
                                </div>
                            </el-col>

                            <!-- 服务部分 -->
                            <el-col :span="6">
                                <div class="footer-section-title">服务</div>
                                <div class="footer-text-block">
                                    <div>会议会展</div>
                                    <div>乡村规划</div>
                                    <div>学术交流</div>
                                    <div>产业开发</div>
                                </div>
                            </el-col>

                            <!-- 乡村部分 -->
                            <el-col :span="6">
                                <div class="footer-section-title">乡村</div>
                                <div class="footer-text-block">
                                    <div>新型集体经济</div>
                                    <div>新型工业化</div>
                                    <div>新型城镇化</div>
                                </div>
                            </el-col>
                        </el-row>
                    </div>
                <!--TODO 居中放置微博、微信、抖音图标，容器宽度50%，space-between方式-->
                    <div class="social-icons-container">
                        <a href="https://weibo.com" target="_blank" class="social-icon">
                            <img src="@/assets/images/weibo-icon.png" alt="Weibo" />
                        </a>
                        <a href="https://weixin.qq.com" target="_blank" class="social-icon">
                            <img src="@/assets/images/wechat-icon.png" alt="WeChat" />
                        </a>
                        <a href="https://www.douyin.com" target="_blank" class="social-icon">
                            <img src="@/assets/images/tictok-icon.png" alt="Douyin" />
                        </a>
                    </div>

                </el-main>

                <!-- 手机端底部 -->
                <el-footer class="mobile-footer">
                    <div class="mobile-footer-content">
                        <div class="mobile-footer-support">
                            技术支持：玄易文化
                        </div>
                        <div class="mobile-footer-copyright">
                            ©2024 甘肃乡村发展促进会
                        </div>
                        <div class="mobile-record-link">
                            <a href="https://www.beian.gov.cn/portal/registerSystemInfo?recordcode=44030702002386" target="_blank" class="record-link">
                                <span>粤公网安备44030702002386号</span>
<!--                                <img src="@/assets/images/security.png" alt="Security" class="mobile-security-logo">-->
                            </a>
                            <p>甘A2-20044005号</p>
                        </div>
                    </div>
                </el-footer>
            </el-container>
        </div>
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
                    src: require('@/assets/images/YouthFarmer.jpg'),
                    alt: "青年农人",
                    title: "关于青年农人",
                    category: "青年农人"
                }
            ],
            // 顶部最重要的文章的轮播图数据
            topBanners: [],
            // 下面四张图片对应的文章轮播图数据
            middleBanners: [],
            // 广告轮播图数据
            adsBanners: [],
            articleTitles: [],
            isMobile: false,
            username: '',
            activeMenu: '3',
            isAdmin: false,
            latestArticles: [],
            popularArticles: [],
            mobileLatestArticles: [],
            mobilePopularArticles: [],
            currentPage: 1,
            pageSize: 10,
            chunkSize: 3,  // 轮播图单行数量,
            videos: [],
            videoDialogVisible: false,
            currentVideo: null,
            menuVisible: false, // 控制菜单的显示与隐藏
            mainMenuVisible: false,
            image_url: '', // 广告图片链接,
            isDropdownVisible: false, // 控制下拉菜单的显示与隐藏
            isLoggedIn: false, // 模拟用户登录状态，true表示已登录
            showSearch: false, // 控制搜索框的显示
        };
    },
    computed: {
        chunkedImages() {
            const chunkSize = this.chunkSize;
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
        getSnippet(text, maxLength){
            return text.length <= maxLength ? text : text.slice(0, maxLength) + '...';
        },
        performSearch() {
            console.log("执行搜索", this.searchQuery);
            this.showSearch = false;
        },
        // 切换搜索框的显示状态
        toggleSearch() {
            this.showSearch = !this.showSearch;
        },
        toggleDropdown() {
            this.isDropdownVisible = !this.isDropdownVisible; // 切换下拉菜单的显示状态
        },
        // 确认删除广告
        confirmDelete(adId) {
            this.$confirm('确定要删除这个广告吗?', '警告', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                axios.delete(this.$baseUrl + `/api/advertisement/${adId}`)
                    .then(() => {
                        this.adsBanners = this.adsBanners.filter(banner => banner.id !== adId);
                        this.$message.success('广告已删除');
                    })
                    .catch(error => {
                        this.$message.error('删除失败');
                        console.error(error);
                    });
            }).catch(() => {
                this.$message.info('已取消删除');
            });
        },
        insertImageUrlToDatabase(imageUrl) {
            axios.post(this.$baseUrl + '/api/advertisement/insert', {
                image_url: imageUrl
            })
                .then(response => {
                    if (response.data.success) {
                        this.fetchAdvertisements();
                    } else {
                        this.$message.error('图片插入数据库失败');
                    }
                })
                .catch(error => {
                    this.$message.error('图片插入数据库失败，请稍后重试', error);
                });
        },
        fetchAdvertisements() {
            axios.get(this.$baseUrl + '/api/advertisement/all')
                .then(response => {
                    this.adsBanners = response.data;
                    // this.$alert(this.adsBanners);
                })
                .catch(error => {
                    console.error('获取广告数据失败:', error);
                    this.$message.error('获取广告数据失败，请稍后重试');
                });
        },
        handleUploadSuccess(response) {
            if (response && response.url) {
                this.$message.success("图片上传成功");
                this.insertImageUrlToDatabase(response.url);
            } else {
                this.$message.error('上传失败，请稍后重试');
            }
        },
        handleUploadError() {
            this.$message.error('图片上传失败');
        },
        changePassword() {
            this.$router.push({ name: 'ChangePassword' });
        },
        toggleMenu() {
            this.menuVisible = !this.menuVisible; // 切换菜单的可见性（手机端）
        },
        toggleMainMenu() {
            this.mainMenuVisible = !this.mainMenuVisible; // 切换菜单的可见性
        },
        navigateToArticle(articleId) {
            this.$router.push(`/articles/${articleId}`);
        },
        async fetchVideos() {
            try {
                const response = await axios.get(this.$baseUrl + '/api/videos/all-checked');
                this.videos = response.data.splice(0, 4);
            } catch (error) {
                console.error('获取视频失败:', error);
                this.$message.error('获取视频失败，请稍后再试。');
            }
        },
        playVideo(video) {
            this.currentVideo = video;
            this.videoDialogVisible = true;
            console.log('播放视频:', video.url);
        },
        closeVideoDialog() {
            const videoElement = this.$refs.videoPlayer;
            if (videoElement) {
                videoElement.pause();
                videoElement.currentTime = 0;
            }
            this.videoDialogVisible = false;
            this.currentVideo = null;
        },
        // 控制推荐阅读的文章标题字数
        truncateTitle(title) {
            const maxLength = 12;
            if (title.length > maxLength) {
                return title.slice(0, maxLength) + '...';
            }
            return title;
        },
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
            // const hours = String(date.getHours()).padStart(2, '0');
            // const minutes = String(date.getMinutes()).padStart(2, '0');
            // const seconds = String(date.getSeconds()).padStart(2, '0');
            return `${year}年${month}月${day}日`;
        },
        // 获取最新和最热文章
        async fetchArticles() {
            try {
                const [latestResponse, popularResponse] = await Promise.all([
                    axios.get(this.$baseUrl + '/api/articles/latest'),
                    axios.get(this.$baseUrl + '/api/articles/popular')
                ]);
                this.latestArticles = latestResponse.data;
                this.mobileLatestArticles = latestResponse.data.splice(0,2);
                this.popularArticles = popularResponse.data;
                this.mobilePopularArticles = popularResponse.data.splice(0, 2);
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
                this.isDropdownVisible = false;
            } else if (command === 'login') {
                this.goToLogin();
            } else if (command === 'register') {
                this.goToRegister();
            }
        },

        logout() {
            sessionStorage.clear();
            this.username = '';
            this.$router.push('/login');
        },
        // 获取推荐阅读的文章（已审核状态）
        fetchArticleTitles() {
            axios.get(this.$baseUrl + '/api/articles/articleList', {
                params: {
                    pageNum: this.currentPage,
                    pageSize: this.pageSize,
                },
            })
                .then(response => {
                    this.articleTitles = response.data.data.map(article => {
                        return {
                            ...article,
                            image: this.extractImageSources(article.content), // 提取第一张图片链接
                            isTop: article.isTop || 0,
                        };
                    });

                    // 更新 topBanners 数组，仅选择置顶文章
                    this.topBanners = this.articleTitles
                        .filter(article => article.isTop)
                        .map(article => ({
                            src: article.image,
                            articleId: article.id,
                        }))
                        .filter(banner => banner.src)
                        .slice(0, 4);

                    // 更新 middleBanners 数组，取views高的前四个
                    this.middleBanners = this.articleTitles
                        .map(article => ({
                            src: article.image,
                            articleId: article.id,
                            views: article.views,
                        }))
                        .filter(banner => banner.src) // 过滤掉没有图片的文章
                        .sort((a, b) => b.views - a.views)
                        .slice(0, 4);
                    this.total = response.data.total;
                })
                .catch(error => {
                    this.$message.error('获取文章列表失败: ' + error.message);
                });
        },
        extractImageSources(content) {
            const div = document.createElement('div');
            div.innerHTML = content;
            const imgElements = div.getElementsByTagName('img');
            if (imgElements.length > 0) {
                return imgElements[0].src; // 返回第一张图片的 src
            }
            return '';
        },
        navigateToArticles() {
            this.$router.push('/more');
        },
        checkIfAdmin() {
            const usertype = sessionStorage.getItem('usertype');
            this.isAdmin = (usertype === '管理员');
        },
        checkIfMobile() {
            this.isMobile = window.innerWidth <= 768;
        },
        changeChunkSize(){
            if (window.innerWidth < 480) {
                this.chunkSize = 3;
            } else if (window.innerWidth < 768) {
                this.chunkSize = 3;
            } else {
                this.chunkSize = 3;
            }
        },
    },
    created() {
        // 获取用户名
        this.username = sessionStorage.getItem('username');
        if (this.username){
            this.isLoggedIn = true;
        }
        this.checkIfAdmin();
        this.fetchVideos();
        this.fetchArticleTitles();
        this.fetchAdvertisements();
        this.fetchArticles();
    },
    mounted() {
        this.checkIfMobile();
        this.changeChunkSize();
        window.addEventListener('resize', this.changeChunkSize);
        window.addEventListener('resize', this.checkIfMobile);
        document.title="甘肃乡村发展促进会";
    },
    beforeDestroy() {
        window.removeEventListener('resize', this.checkIfMobile);
        window.removeEventListener('resize', this.changeChunkSize);
    }
}
</script>

<style scoped>
.search-container {
    position: relative; /* 使子元素的绝对定位相对于此容器 */
}

.search-icon {
    display: flex;
    align-items: center;
    cursor: pointer;
}

.search-input {
    margin-left: 8px;
    padding: 5px;
    border: 1px solid #ccc;
    border-radius: 50px;
    transition: width 0.3s ease;
}

.search-input:focus {
    border-color: #3a8ee6;
    outline: none;
}

.ad-section {
    width: 100%;
    height: 50%;
    max-height: 220px;
    position: relative;
}
.upload-button {
    margin-bottom: 20px;
    background-color: rgb(101,172,140);
    color: white;
}

.upload-button :hover{
    background-color: rgb(85, 145, 118);
    border-color: rgb(85, 145, 118);
}

.banner-actions {
    position: absolute;
    left: 10px;
    top: 50%;
    border-radius: 4px;
    padding: 5px;
    z-index: 1;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.5);
    transition: background-color 0.3s ease;
}

.mobile-ads-image {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 100%;
    height: auto;
    min-height: 100%;
    min-width: 100%;
    transform: translate(-50%, -50%);
    border-radius: 8px;
}

/*手机端顶部操作的样式*/
.overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.7);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}

.menu {
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
    width: 80%;
    max-width: 400px;
    padding: 20px;
    text-align: center;
}

.menu-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

.close-icon {
    cursor: pointer;
    font-size: 24px;
    color: #888;
}

.menu-item {
    padding: 10px;
    border-radius: 5px;
    margin: 5px 0;
    cursor: pointer;
    transition: background 0.2s;
}

.menu-item:hover {
    background: #f0f0f0;
}
/*微博、抖音、微信图标容器*/
.social-icons-container {
    display: flex;
    justify-content: space-between;
    width: 50%;
    margin: 0 auto;
    align-items: center;
}

.social-icon img {
    width: 40px;
    height: auto;
}

.footer-section {
    padding: 20px;
    background-color: rgb(225,228,232);
}

.footer-content-section {
    display: flex;
    justify-content: space-between;
}

.footer-section-title {
    font-weight: 450;
    color: rgb(106,157,128);
    margin-bottom: 10px;
}

.footer-text-block div {
    margin-bottom: 5px;
    font-size: 12px;
    font-weight: 520;
    color: rgb(55,62,57);
}

.footer-text-block div :hover{
    background-color: #f0f0f0;
    border-color: #42b983;
    color: #42b983;
}

@media (max-width: 768px) {
    .footer-content-section {
        flex-wrap: nowrap;
    }

    .footer-text-block div {
        white-space: nowrap;
    }
}
/*中间的四张图片的样式*/
.mobile-image-container {
    margin: 20px 0; /* 整个区域的上下边距 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
}

.image-row {
    display: flex;
    width: 100%;
}

.image-wide {
    flex: 2.5;
    margin: 10px;
}

.image-narrow {
    flex: 1.3;
    margin: 10px;
}

.image-wide img,
.image-narrow img {
    width: 100%;
    height: 120px;
}

/*中间四张图片的父容器*/
.mobile-image-section {
    margin: 20px 0; /* 整个区域的上下边距 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    display: flex;
    flex-direction: column;
}

/*手机端协会标题*/
.mobile-title-container {
    text-align: start;
    padding: 5px;
    margin: 20px auto;
    border-radius: 8px;
}
.mobile-gansu {
    font-size: 14px;
    color: #333;
    line-height: 1.5;
}
.mobile-main-title {
    font-size: 20px;
    color: green;
    margin-bottom: 10px;
}
.mobile-total {
    width: 100%;
    height: 100%;
    background-color: rgb(226, 229, 234);
    box-sizing: border-box;
}
/*手机端顶部轮播图*/
.mobile-banner {
    position: relative;
    height: 100%;
    width: 100%;
}
.mobile-banner-image {
    width: 100%;
    max-height: 100%;
    border-radius: 8px;
    transition: transform 0.3s ease;
    object-fit: cover;
    position: absolute;
    top: 0;
    left: 0;
    height: auto;
}

.mobile-banner-image:hover {
    transform: scale(1);
}

.el-carousel__arrow {
    color: #fff;
    background-color: rgba(0, 0, 0, 0.5);
    border-radius: 50%;
    width: 30px;
    height: 30px;
    line-height: 30px;
}

.el-carousel__arrow:hover {
    background-color: rgba(0, 0, 0, 0.8);
}

.el-carousel__indicator {
    bottom: 10px;
    text-align: center;
}

.el-carousel__indicator.is-active {
    background-color: #00bcd4;
}

.el-carousel__indicator {
    background-color: rgba(255, 255, 255, 0.7);
    border-radius: 50%;
    width: 10px;
    height: 10px;
    display: inline-block;
    margin: 0 5px;
}

.carousel-image {
    width: 100%;
    height: 100%;
    border-radius: 10px;
}

.banner-image {
    width: 100%;
    height: 100%;
    /*background-size: cover; !* 确保背景图片覆盖整个容器 *!*/
    background-position: center; /* 居中显示背景 */
    border-radius: 10px; /* 保持圆角 */
}

.mobile-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px; /* 添加内边距 */
    background-color: white; /* 背景颜色 */
}

.mobile-header-left {
    display: flex;
    align-items: center;
}

.mobile-header-left img {
    width: 50px;
    height: auto;
    margin-right: 10px;
}

.mobile-header-left span {
    font-size: 16px;
    color: #333;
}

.mobile-header-right {
    display: flex;
    gap: 15px;
}

.mobile-header-right i {
    font-size: 18px; /* 图标大小 */
    cursor: pointer;
    color: rgb(137,139,138);
}

.mobile-text-block div {
    min-width: 50%;
    padding: 8px;
    font-size: 14px;
    color: #555;
    text-align: center;
    line-height: 1;
    cursor: pointer;
    transition: all 0.3s ease;
}

.mobile-text-block div:hover {
    background-color: #f0f0f0;
    border-color: #42b983;
    color: #42b983;
}

.divider {
    width: 1px;
    height: 100%;
    background-color: #ddd;
    margin: auto;
}

.mobile-footer {
    background: linear-gradient(45deg, rgb(8, 13, 9), rgb(33, 54, 39));
    color: #ffffff;
    padding: 20px 0;
    text-align: center;
    font-size: 14px;
    min-height: 100px;
}

.mobile-footer-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
}

.mobile-record-link {
    display: flex;
    align-items: center;
    justify-content: center;
}
.record-link span {
    margin-right: 10px;
}
.security-logo {
    width: 20px;
    height: auto;
    border-radius: 50%;
    margin-right: 10px;
}
.foot-img {
    width: 80px;
    margin-bottom: 10px;
}

.mobile-footer a {
    color: white;
    text-decoration: none;
    margin: 0 5px;
}

.mobile-footer a:hover {
    text-decoration: underline;
}

.mobile-footer p {
    margin: 5px 0;
    font-size: 14px;
    color: white;
}

.mobile-banner-text {
    position: absolute;
    top: 10px;
    font-size: 8px;
    left: 10px;
    color: white;
    margin: 20% auto;
    z-index: 1;
}

.mobile-layout {

}


.mobile-main-content {
    padding: 15px;
}

.mobile-footer {
    text-align: center;
}

.desktop-layout {
    width: 100%;
}

/*@media (max-width: 768px) {*/
/*    .desktop-layout {*/
/*        display: none;*/
/*    }*/
/*}*/

/*@media (min-width: 768px) {*/
/*    .mobile-layout {*/
/*        display: none;*/
/*    }*/
/*}*/

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

@media (max-width: 1400px) and (min-width: 1000px) {
    .image-item {
        width: 120px;
        height: 160px;
        transform: scale(0.85);
    }

    .image-item:nth-child(2) {
        top: 80px;
        left: 50px;
    }
}

@media (max-width: 1000px) and (min-width: 992px) {
    .image-item {
        width: 100px;
        height: 140px;
        transform: scale(0.9);
    }

    .image-item:nth-child(2) {
        top: 60px;
        left: 30px;
    }
}

@media (max-width: 992px) and (min-width: 768px) {
    .image-item {
        width: 100px;
        height: 140px;
        transform: scale(0.9);
    }

    .image-item:nth-child(2) {
        top: 70px;
        left: 50px;
    }
}

.total {
    width: 100%;
    height: 100%;
    background-color: rgb(226, 229, 234);
    box-sizing: border-box;
}
.header-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;
    background-color: white;
    flex-shrink: 0;
    position: relative;
}

.header-logo img {
    height: fit-content;
    object-fit: contain;
    max-height: 80px;
}

/* 平板设备的适配 */
@media screen and (min-width: 768px) and (max-width: 1024px) {
    .header-container {
        padding: 10px;
    }

    .header-logo img {
        max-height: 60px;
        scale: 0.8;
    }

    .el-menu-item,
    .el-submenu__title {
        font-size: 15px;
    }

    .auth-buttons .custom-login-button,
    .auth-buttons .custom-register-button {
        font-size: 14px;
        padding: 8px;
    }
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

.el-dropdown-link {
    background-color: rgb(101, 172, 140);
    color: white;
    padding: 8px 12px;
    border-radius: 4px;
    display: inline-flex;
    align-items: center;
    font-size: 12px;
    cursor: pointer;
    text-decoration: none;
}

.el-dropdown-link i {
    margin-left: 8px;
    font-size: 14px;
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

.text-block div :hover {
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
    width: 50px;
}

.divider {
    width: 2px;
    height: 80px;
    background-color: rgb(170, 175, 169);
    margin-right: auto;
}

.news-item {
    display: flex;
    justify-content: space-between;
    padding: 15px;
    background-color: #f9f9f9;
    border-bottom: 1px solid #ddd;
}

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

.announcement-link {
    color: inherit;
    text-decoration: none;
    transition: color 0.3s ease;
}

.announcement-link:hover {
    color: rgb(87, 157, 123);
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
    transform: scale(1.0);
    transform-origin: top left;
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

.el-carousel {
    border: 1px solid #eaeaea;
    border-radius: 8px;
    width: 100%;
    max-width: 3000px;
    margin: 0 auto;
}

.el-carousel-item {
    display: flex;
    justify-content: space-between;
}

.mobile-image-card {
    position: relative;
    overflow: hidden;
    height: 140px;
    width: 100%;
    border-radius: 8px;
    transform: scale(1.0);
    transform-origin: top left;
}

.mobile-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.mobile-image-overlay {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: rgba(196, 200, 203, 0.8);
    color: #171B1B;
    text-align: center;
    font-size: 12px;
    padding: 10px 0;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.mobile-image-overlay:hover {
    background-color: rgba(100, 171, 141, 0.9);
    color: white;
}

.sidebar {
    display: flex;
    flex-direction: column;
    height: 100%;
    background-color: rgb(226, 229, 234);
    border: 1px solid #ddd;
    box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
    gap: 20px;
    width: 80%;
    margin: 0 auto;
    max-height: 150vh;
    justify-content: space-between;
    border-radius: 8px;
    overflow-y: auto;
}

.mobile-sidebar {
    display: flex;
    flex-direction: column;
    height: 100%;
    background-color: rgb(226, 229, 234);
    border: 1px solid #ddd;
    box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
    gap: 20px;
    width: 100%;
    margin: 0 auto;
    max-height: 150vh;
    justify-content: space-between;
    border-radius: 8px;
    overflow-y: auto;
}


.published-date {
    display: block;
    font-size: 14px;
    color: #666;
    margin-top: 5px;
}

.sidebar > *:not(h2) {
    flex: 1;
    margin: 10px 0;
}

.section {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 50%;
    overflow: hidden;
}

h2 {
    font-size: 20px;
    padding: 10px 15px;
    color: white;
    background-color: rgb(136,138,136);
    border-radius: 4px;
    margin: 0 0 10px;
    font-weight: 500;
    text-align: center;
}

.sidebar ul {
    list-style: none;
    padding: 0;
    margin: 0;
    overflow-y: auto;
    scrollbar-width: none;
    -ms-overflow-style: none;
}

.mobile-sidebar ul {
    list-style: none;
    padding: 0;
    margin: 0;
    overflow-y: auto;
    scrollbar-width: none;
    -ms-overflow-style: none;
}


.router-link {
    text-decoration: none;
    color: inherit;
}
.sidebar a {
    text-decoration: none;
    color: black;
    font-size: 16px;
    line-height: 2.5;
}
.sidebar li {
    margin-bottom: 4%;
}
.sidebar a:hover {
    text-decoration: none;
    color: rgb(87, 157, 123);
}

.mobile-sidebar a {
    text-decoration: none;
    color: black;
    font-size: 16px;
    line-height: 2.5;
}
.mobile-sidebar li {
    margin-bottom: 4%;
}
.mobile-sidebar a:hover {
    text-decoration: none;
    color: rgb(87, 157, 123);
}

.custom-footer {
    background-image: url("@/assets/images/footer.png");
    background-size: cover;
    color: #ffffff;
    padding: 20px 0;
    text-align: center;
    min-height: 150px;
    font-size: 14px;
}

.footer-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    max-width: 1200px;
    margin: 0 auto;
}

.footer-left {
    flex: 1;
    text-align: left;
}

.footer-center {
    flex: 2;
    text-align: center;
}

.footer-right {
    flex: 1;
    text-align: center;
}

.footer-left a,
.footer-right a {
    color: #ffffff;
    text-decoration: none;
    padding: 0 10px;
    transition: color 0.3s ease;
}

.footer-left a:hover,
.footer-right a:hover {
    color: rgb(88,157,126); /* 悬停效果 */
    text-decoration: none;
}

.record-link i {
    margin-right: 5px;
}


.footer-right a {
    display: flex;
    align-items: center;
}


/* 平板设备的适配（底部版权信息） */
@media screen and (max-width: 1024px) {
    .custom-footer {
        min-height: 270px;
        padding: 20px 10px;
    }

    .footer-content {
        flex-direction: column;
        align-items: center;
        gap: 20px;
    }

    .footer-center,
    .footer-right {
        flex: none;
        width: 100%;
        margin-bottom: 15px;
    }

    .footer-left img {
        max-width: 40%;
    }

    .footer-center p {
        font-size: 14px;
        margin: 5px 0;
    }

    .footer-right {
        display: flex;
        flex-direction: column;
        align-items: center;
        text-align: center;
    }

    .footer-right img {
        margin-bottom: 5px;
    }

    .record-link span {
        display: block;
        margin-top: 5px;
        font-size: 12px;
    }
}


.record-link {
    display: flex;
    align-items: center;
    text-decoration: none;
    color: inherit;
}

.record-link:hover {
    text-decoration: underline;
}

.foot-img {
    width: 300px;
    height: 80px;
    border-radius: 10px;
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
.record-link {
    color: white;
    text-decoration: none;
    transition: color 0.3s ease;
}

.record-link:hover {
    color: #0056b3;
}
</style>
