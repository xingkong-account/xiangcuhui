<template>
    <div class="page-container">
        <!-- 电脑端布局 -->
        <div class="desktop-layout" v-show="!isMobile">
            <!-- 原有电脑端布局代码 -->
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
                        <div class="footer-left">
                            <img src="@/assets/images/icon.jpg" class="foot-img" alt="">
                        </div>
                        <div class="footer-center">
                            <p><a href="#">网站地图</a> | <a href="#">联系方式</a> | <a href="#">使用帮助</a> | <a href="#">隐私声明</a></p>
                            <p>主办单位: 乡促会&nbsp;&nbsp;&nbsp;</p>
                            <p>地址: XX省XX市XXX区XXX</p>
                        </div>
                        <div class="footer-right">
                            <a href="https://www.beian.gov.cn/portal/registerSystemInfo?recordcode=44030702002386" target="_blank" class="record-link">
<!--                                <img src="@/assets/images/security.png" alt="Security" class="security-logo">-->
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
                <!-- 手机端的头部-->
                <template>
                    <div>
                        <div class="mobile-header">
                            <div class="mobile-header-left">
                                <img src="@/assets/images/icon.jpg" style="width: 120px; height: auto;">
                            </div>
                            <div class="mobile-header-right">
                                <i class="el-icon-search">检索</i>
                                <i @click="toggleMenu" class="menu-icon">
                                    {{ menuVisible ? '✖' : '☰' }} <!-- 根据状态切换图标 -->
                                </i>
                            </div>
                        </div>

                        <transition name="slide">
                            <div v-if="menuVisible" class="overlay" @click="toggleMenu">
                                <div class="menu" @click.stop>
                                    <div v-if="username">
                                        <div class="menu-item" @click="changePassword">修改密码</div>
                                        <div class="menu-item" @click="logout">退出</div>
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
                    <div class="mobile-banner">
                        <el-carousel height="300px" :interval="5000" arrow="always" indicator-position="outside">
                            <el-carousel-item v-for="(banner, index) in banners" :key="index" @click="navigateToArticle(banner.articleId)">
                                <img :src="banner.src" alt="Banner" class="mobile-banner-image" />
                            </el-carousel-item>
                        </el-carousel>
                        <div class="mobile-banner-text">
                            <span>Website of the</span><br>
                            <span>Rural Promotion</span><br>
                            <span>Association</span>
                        </div>
                        <div class="mobile-navigation">
                            <el-menu mode="horizontal" class="header-menu">
                                    <!-- 精选视频下拉菜单 -->
<!--                                <el-submenu class="video-menu" :index="'4'" :popper-append-to-body="false">-->
<!--                                    <template #title>视频</template>-->
<!--                                    <el-menu-item index="4-1" @click="navigate('videos')">精选视频</el-menu-item>-->
<!--                                    <el-menu-item v-if="isAdmin" index="4-2" @click="navigate('video-review')">视频审核</el-menu-item>-->
<!--                                    <el-menu-item v-if="isAdmin" index="4-3" @click="navigate('video-manage')">所有视频</el-menu-item>-->
<!--                                    <el-menu-item v-else-if="username" index="4-3" @click="navigate('video-manage')">我发布的</el-menu-item>-->
<!--                                    <el-menu-item v-if="username" index="4-4" @click="navigate('upload-video')">上传视频</el-menu-item>-->
<!--                                </el-submenu>-->

                                    <!-- 会员管理下拉菜单，登录才能看到会员菜单 -->
                                    <el-submenu class="member-management-menu" v-if="isAdmin || username" :index="'2'" :popper-append-to-body="false" :default-active="activeMenu">
                                        <template #title>会员</template>
                                        <!-- 个人会员子菜单 -->
                                        <el-submenu index="2-1">
                                            <template #title>个人会员</template>
                                            <el-menu-item index="2-1-1" v-if="isAdmin" @click="navigate('individual-members')">个人会员管理</el-menu-item>
                                            <el-menu-item index="2-1-1" v-else @click="navigate('individual-members')">查看个人会员</el-menu-item>
                                        </el-submenu>

                                        <!-- 团队会员子菜单 -->
                                        <el-submenu index="2-2">
                                            <template #title>团队会员</template>
                                            <el-menu-item index="2-2-1" @click="navigate('group--member-manage')">团队会员管理</el-menu-item>
                                            <el-menu-item index="2-2-2" @click="navigate('team-members')">查看所有团队会员</el-menu-item>
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

                                    <el-menu-item index="5" @click="navigate('about')" style="font-size: 20px; color: black">关于我们</el-menu-item>
                            </el-menu>
                        </div>
<!--                        <div class="video-container">-->
<!--                            <div v-if="videos.length > 0" class="video-item-large">-->
<!--                                <div class="video-thumbnail" @click="playVideo(videos[0])">-->
<!--                                    <img :src="videos[0].thumbnail" alt="视频缩略图" />-->
<!--                                    <div class="video-play-button">▶</div>-->
<!--                                </div>-->
<!--                            </div>-->

<!--                            <div class="video-row">-->
<!--                                <div-->
<!--                                    v-for="(video) in videos.slice(1)"-->
<!--                                    :key="video.id"-->
<!--                                    class="video-item"-->
<!--                                >-->
<!--                                    <div class="video-thumbnail" @click="playVideo(video)">-->
<!--                                        <img :src="video.thumbnail" alt="视频缩略图" />-->
<!--                                        <div class="video-play-button">▶</div>-->
<!--                                    </div>-->
<!--                                </div>-->
<!--                            </div>-->
<!--                        </div>-->
                        <!-- TODO 添加两行，第一行放两张图片，2:1的比例，第二行也是两张，1:2的比例-->
                        <div class="mobile-image-container">
                            <div class="mobile-image-section">
                                <!-- 第一行 -->
                                <div class="image-row">
                                    <div class="image-wide">
                                        <img src="@/assets/images/industry.jpg" alt="Image 1" />
                                    </div>
                                    <div class="image-narrow">
                                        <img src="@/assets/images/industry.jpg" alt="Image 2" />
                                    </div>
                                </div>

                                <!-- 第二行 -->
                                <div class="image-row">
                                    <div class="image-narrow">
                                        <img src="@/assets/images/industry.jpg" alt="Image 3" />
                                    </div>
                                    <div class="image-wide">
                                        <img src="@/assets/images/industry.jpg" alt="Image 4" />
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
                        <div class="mobile-image-section">
                            <el-carousel height="200px" :interval="5000" arrow="always" indicator-position="outside">
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
                    </div>

                    <!--新闻动态、热点资讯-->
                    <div>
                       <div class="sidebar" style="width: 100%">
                           <div class="section">
                               <h2 style="min-width: 20%; max-width: 25%; text-align: start">新闻动态</h2>
                               <ul>
                                   <li v-for="article in mobileLatestArticles" :key="article.id">
                                       <router-link :to="'/articles/' + article.id">{{ article.title }}</router-link>
                                       <span class="published-date">{{ formatDate(article.created_at) }}</span>
                                   </li>
                               </ul>
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

                    <div>
                        <el-row :gutter="10" class="mobile-content-section">
                            <!-- 我们部分 -->
                            <el-col :span="6">
                                <div class="mobile-section-title">我们</div>
                                <div class="mobile-text-block">
                                    <div>会议会展</div>
                                    <div>乡村规划</div>
                                    <div>学术交流</div>
                                    <div>产业开发</div>
                                </div>
                            </el-col>

                            <!-- 会员部分 -->
                            <el-col :span="6">
                                <div class="mobile-section-title">会员</div>
                                <div class="mobile-text-block">
                                    <div>入会须知</div>
                                    <div>申请提交</div>
                                    <div>审批流程</div>
                                </div>
                            </el-col>

                            <!-- 服务部分 -->
                            <el-col :span="6">
                                <div class="mobile-section-title">服务</div>
                                <div class="mobile-text-block">
                                    <div>协会筒介</div>
                                    <div>宗旨使命</div>
                                    <div>现任领导</div>
                                    <div>服务愿景</div>
                                </div>
                            </el-col>

                            <!-- 乡村部分 -->
                            <el-col :span="6">
                                <div class="mobile-section-title">乡村</div>
                                <div class="mobile-text-block">
                                    <div>新型集体经济</div>
                                    <div>新型工业化</div>
                                    <div>新型城镇化</div>
                                </div>
                            </el-col>
                        </el-row>
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
import changePassword from "@/components/ChangePassword.vue";
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
        };
    },
    computed: {
        changePassword() {
            return changePassword
        },
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
        toggleMenu() {
            this.menuVisible = !this.menuVisible; // 切换菜单的可见性
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
        this.checkIfAdmin();
        this.fetchVideos();
        this.fetchArticleTitles();
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
    }
}
</script>

<style scoped>
.mobile-image-container {
    margin: 20px 0; /* 整个区域的上下边距 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
    border-radius: 8px; /* 可选：添加圆角效果 */
}


.mobile-image-section {
    display: flex;
    flex-direction: column; /* 垂直排列两行 */
}

.image-row {
    display: flex; /* 使用 Flexbox 布局 */
    width: 100%; /* 使每行占满宽度 */
}

.image-wide {
    flex: 2.5;
    margin: 10px;
}

.image-narrow {
    flex: 1.3; /* 窄的图片占 1 的比例 */
    margin: 10px; /* 卡片之间的间距 */
}

.image-wide img,
.image-narrow img {
    width: 100%; /* 使图片占满父容器 */
    height: 100px; /* 设置固定高度 */
    object-fit: cover; /* 保持比例并裁剪 */
}

.mobile-title-container {
    text-align: start;
    padding: 20px;
    margin: 20px auto;
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
.mobile-banner {
    position: relative;
    min-height: 200px;
}

.mobile-banner-image {
    width: 100%;
    height: auto;
    display: block;
}

.mobile-banner-text {
    /*position: absolute;*/
    top: 10px;
    left: 100px;
    color: white;
    text-align: center;
    font-size: 18px;
    z-index: 2;
}

.mobile-navigation {
    position: relative;
    width: 100%;
    z-index: 3;
}

.mobile-navigation nav ul {
    display: flex;
    justify-content: space-around; /* 平均分布每个导航项 */
    list-style: none;
    margin: 0;
    padding: 10px 0;
}

.mobile-navigation nav ul li {
    color: white; /* 导航文字颜色 */
    font-size: 16px;
    cursor: pointer;
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
    width: 50px; /* 根据需要调整logo大小 */
    height: auto;
    margin-right: 10px; /* logo与文字之间的间距 */
}

.mobile-header-left span {
    font-size: 16px; /* 字体大小 */
    color: #333; /* 文字颜色 */
}

.mobile-header-right {
    display: flex;
    gap: 15px; /* 图标之间的间距 */
}

.mobile-header-right i {
    font-size: 18px; /* 图标大小 */
    cursor: pointer; /* 鼠标悬停时显示手形光标 */
    color: rgb(137,139,138); /* 图标颜色 */
}

.mobile-menu {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    background-color: #fff;
    border-top: 1px solid #ddd;
    position: relative;
    z-index: 10;
    height: auto;
    width: 100%;
    margin-top: 28%;
    margin-bottom: -30px;
}

.mobile-menu .el-menu-item {
    font-size: 16px;
    color: #333;
    cursor: pointer;
    padding: 0 15px;
}

.mobile-menu .el-menu-item:hover {
    color: #42b983;
}

.mobile-menu .el-menu-item.is-active {
    color: #42b983;
    border-bottom: 2px solid #42b983;
}

.mobile-content-section {
    background-color: rgb(226,229,234);
    padding: 20px;
    margin: 20px auto;
    border-radius: 8px;
    width: 100%;
}

.mobile-section-title {
    font-size: 20px;
    color: green;
    text-align: center;
    margin-bottom: 10px;
}

.mobile-text-block {
    text-align: center;
    margin-right: 20%;
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

.section-title {
    font-size: 16px;
    font-weight: bold;
    text-align: center;
    color: #333;
    padding: 10px 0;
}

.section-title.active {
    color: #42b983;
    border-bottom: 2px solid #42b983;
}


@media screen and (max-width: 768px) {
    .mobile-content-section {
        flex-direction: column;
    }

    .el-row {
        flex-wrap: wrap;
    }

    .el-col {
        margin-bottom: 10px;
        min-width: 25%;
        max-width: 33%;
    }

    .divider {
        display: none;
    }

    .text-block div {
        font-size: 16px;
    }
}

.mobile-footer {
    background: linear-gradient(45deg, rgb(8, 13, 9), rgb(33, 54, 39));
    color: #ffffff;
    padding: 20px 0;
    text-align: center;
    font-size: 14px;
    min-height: 150px;
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
    margin-right: 10px; /* 给文字和图片之间添加一些间距 */
}
.security-logo {
    width: 12px; /* 调整图片大小 */
    height: auto;
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

/*@media screen and (max-width: 480px) {*/
/*    .mobile-footer-content {*/
/*        flex-direction: column;*/
/*        align-items: center;*/
/*    }*/

/*    .mobile-footer p {*/
/*        font-size: 12px;*/
/*    }*/
/*}*/

.mobile-banner-image {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: auto;
    /*margin: 20% auto;*/
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

.mobile-banner-image {
    width: 100%;
    height: auto;
}

.mobile-main-content {
    padding: 15px;
}

.mobile-footer {
    text-align: center;
}

.desktop-layout {

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

/*@media (max-width: 1400px) and (min-width: 1000px) {*/
/*    .image-item {*/
/*        width: 120px;*/
/*        height: 160px;*/
/*        transform: scale(0.85);*/
/*    }*/

/*    .image-item:nth-child(2) {*/
/*        top: 80px;*/
/*        left: 50px;*/
/*    }*/
/*}*/

/*@media (max-width: 1000px) and (min-width: 992px) {*/
/*    .image-item {*/
/*        width: 100px;*/
/*        height: 140px;*/
/*        transform: scale(0.9);*/
/*    }*/

/*    .image-item:nth-child(2) {*/
/*        top: 60px;*/
/*        left: 30px;*/
/*    }*/
/*}*/

/*@media (max-width: 992px) and (min-width: 768px) {*/
/*    .image-item {*/
/*        width: 100px;*/
/*        height: 140px;*/
/*        transform: scale(0.9);*/
/*    }*/

/*    .image-item:nth-child(2) {*/
/*        top: 70px;*/
/*        left: 50px;*/
/*    }*/
/*}*/

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
    font-size: 20px;
    color: black;
    padding: 10px 20px;
    transition: background-color 0.3s, color 0.3s;
}

.main-banner {
    position: relative;
    height: auto;
    overflow: hidden;
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

.mobile-image-section {
    margin: 20px 0; /* 整个区域的上下边距 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
    border-radius: 8px; /* 可选：添加圆角效果 */
}


.el-carousel {
    width: 100%;
    max-width: 1200px;
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
    width: 100%;
    max-height: 130vh;
    justify-content: space-between;
    overflow-y: auto;
}

.published-date {
    display: block; /* 使日期在新行上显示 */
    font-size: 14px; /* 调整字体大小 */
    color: #666; /* 日期颜色 */
    margin-top: 5px; /* 日期与标题的间距 */
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
    margin-bottom: 3%;
}
.sidebar a:hover {
    text-decoration: none;
    color: rgb(87, 157, 123);
}

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

.footer-left {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.footer-right {
    display: flex;
    align-items: center; /* 垂直居中对齐 */
}

.security-logo {
    scale: 0.3;
    width: 50px; /* 设置图标宽度 */
    height: auto; /* 自动调整高度 */
    margin-right: -20px;
    margin-top: 5%;
    border-radius: 50%; /* 圆形边框 */
    /*margin-*/
}

.record-link {
    display: flex; /* 使用 flexbox 以便于图标和文本在同一行 */
    align-items: center; /* 垂直居中对齐 */
    text-decoration: none; /* 去掉下划线 */
    color: inherit; /* 继承父元素的颜色 */
}

.record-link:hover {
    text-decoration: underline; /* 悬停时添加下划线效果 */
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
.record-link {
    color: white;
    text-decoration: none;
    transition: color 0.3s ease;
}

.record-link:hover {
    color: #0056b3;
}
</style>
