<template>
    <el-container>
        <el-main>
            <div class="article-container">
                <div class="article-header">
                    <div class="header-left">
                        <div class="dots" style="display: flex; gap: 10px;">
                            <div class="dot"></div>
                            <div class="dot"></div>
                            <div class="dot"></div>
                        </div>
                    </div>
                    <div class="header-right">
                        <div class="article-meta">
                            <span class="article-date">{{ formatDate(article.created_at) }}</span>
                            <span class="article-source">信息来源: {{ article.source }}</span>
                            <span class="article-views">浏览次数: {{ article.views }}</span>
                            <span class="article-font-size">
                              字体:
                              [
                              <a href="#" @click.prevent="bigsize">大</a> |
                              <a href="#" @click.prevent="insize">中</a> |
                              <a href="#" @click.prevent="smallsize">小</a>
                              ]
                            </span>
                        </div>
                    </div>
                </div>

                <div class="article-green-line"></div>
                <div class="header-center">
                    <h2 class="article-title">{{ article.title }}</h2>
                </div>
                <div class="">
                    <div v-html="article.content" class="article-content"></div>
                </div>

                <el-button class="back-button" type="primary" @click="goBack">返回</el-button>
            </div>
        </el-main>

        <div class="ncdt-bottom-tool">
            <div class="bottom-tool-content">
                <span class="bottom-tool-item">
                    <i class="el-icon-printer"></i>
                    <a href="javascript:window.print()">打印</a>
                </span>
                <span class="bottom-tool-item">
                    <i class="el-icon-circle-close"></i>
                    <a href="javascript:window.opener=null;window.open('about:blank','_self');window.close();">关闭</a>
                </span>
            </div>
        </div>

        <el-footer class="custom-footer">
            <div class="footer-content">
                <div class="footer-left">
                    <img src="@/assets/images/icon.jpg" style="width: 300px; height: 80px; border-radius: 10px" @click="goMain">
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
    name: 'ArticleDetail',
    data() {
        return {
            article: {
                title: '',
                created_at: '',
                updated_at: '',
                source: '',
                content: ''
            },
            fonstSize: 20,
        };
    },
    methods: {
        fetchArticle() {
            const id = this.$route.params.id;
            axios.get(this.$baseUrl + `/api/articles/${id}`)
                .then(response => {
                    this.article = response.data;
                })
                .catch(error => {
                    this.$message.error('获取文章详情失败: ' + error.message);
                });
        },
        // 文章浏览次数
        incrementViews() {
            const id = this.$route.params.id;
            axios.post(this.$baseUrl + `/api/articles/${id}/increment-views`)
                .catch(error => {
                    this.$message.error('Error incrementing views:'+ error.message);
                });
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
        goBack() {
            this.$router.go(-1);
        },
        goMain(){
            this.$router.push("/");
        },
        bigsize() {
            this.fontSize = 26;
            this.updateGlobalFontSize();
        },
        insize() {
            this.fontSize = 22;
            this.updateGlobalFontSize();
        },
        smallsize() {
            this.fontSize = 18;
            this.updateGlobalFontSize();
        },
        updateGlobalFontSize() {
            document.documentElement.style.fontSize = this.fontSize + 'px';
        },
    },
    created() {
        this.fetchArticle();
        this.incrementViews();
    },
    mounted() {
        this.updateGlobalFontSize();
    },
    beforeDestroy() {
        this.fontSize = 18;
        this.updateGlobalFontSize();
    }
};
</script>

<style scoped>
/*三个实心圆点*/
.dots .dot {
    width: 10px;
    height: 10px;
    background-color: #66a482;
    border-radius: 50%;
}

.article-font-size a {
    text-decoration: none;
    color: #333;
}

.article-font-size a:hover {
    color: #5cb85c;
    font-weight: bold;
}

.article-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    min-height: 100vh;
    width: 100%;
    background-image: url("@/assets/images/articleDetailBg.png");
    background-repeat: no-repeat;
    background-size: cover;
    background-position: center;
}

.article-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    max-width: 90%;
    margin: 0 auto;
    padding: 10px 0;
    color: #666;
}

.header-left .dots i {
    margin-right: 5px;
    font-size: 12px;
    color: #67C23A;
}

.header-center {
    text-align: center;
}

.header-right .article-meta {
    display: flex;
    justify-content: flex-end;
    gap: 70px;
    color: rgb(113,172,143);
    font-size: 14px;
}

.article-title {
    font-size: 26px;
    font-weight: 600;
    color: rgb(85,156,122);
}

.article-green-line {
    height: 2px;
    background-color: rgb(105,182,180);
    width: 90%;
    margin: 10px auto;
}

.article-content {
    font-size: inherit;
    color: #333;
    width: 90%;
    margin: 20px auto;
    line-height: 1.8;
}

.ncdt-bottom-tool {
    background-color: #f0f0f0;
    padding: 10px;
    border-top: 1px solid #dcdcdc;
    width: 96%;
    margin: 0 auto;
}

.bottom-tool-content {
    display: flex;
    gap: 40px;
    justify-content: flex-end;
    margin-right: 20px;
}

.bottom-tool-item a {
    color: #333;
    text-decoration: none;
}
.custom-footer {
    background-image: url("@/assets/images/footer.png");
    color: #ffffff;
    padding: 20px 0;
    text-align: center;
    min-height: 200px;
    height: auto;
    background-size: cover;
    width: 100%;
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
    scale: 0.8;
    width: 500px;
    height: 100px;
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

.back-button {
    background-color: rgb(101,172,138);
    color: #fff;
    border: none;
    border-radius: 5px;
    padding: 10px 20px;
    font-size: 1rem;
    cursor: pointer;
    margin-left: auto;
    margin-right: 60px;
    transition: background-color 0.3s;
    transform: translate(-50%, -50%);
}

.back-button:hover {
    background-color: #4CAF50;
}
</style>
