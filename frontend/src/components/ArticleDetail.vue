<template>
    <el-container>
        <el-main>
            <div class="article-container">
                <div class="header-center">
                    <h2 class="article-title">{{ article.title }}</h2>
                </div>
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
                <div class="article-content">
                    <div v-html="article.content" class="article-details"></div>
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
<!--            <div class="footer-content">-->
<!--                <div class="footer-left">-->
<!--                    <img src="@/assets/images/icon.jpg" style="width: 300px; height: 80px; border-radius: 10px" @click="goMain">-->
<!--                </div>-->
<!--                <div class="footer-center">-->
<!--                    <p><a href="#">网站地图</a> | <a href="#">联系方式</a> | <a href="#">使用帮助</a> | <a href="#">隐私声明</a></p>-->
<!--                    <p>主办单位: 乡促会&nbsp;&nbsp;&nbsp; 备案号：</p>-->
<!--                    <p>地址: 甘肃省兰州市XXXXXX</p>-->
<!--                </div>-->
<!--                <div class="footer-right">-->
<!--                    <img src="" alt="Security" class="security-logo">-->
<!--                    <p>甘公网安备 235487154313号</p>-->
<!--                    <p>网站标识码：0000000000</p>-->
<!--                </div>-->
<!--            </div>-->
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
.article-meta span {
    display: inline;
}
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
    margin: 20px auto;
    /*background-image: url("@/assets/images/articleDetailBg.png");*/
    background-repeat: no-repeat;
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
    color: rgb(85, 156, 122);
    max-width: 60ch;
    white-space: normal;
    overflow-wrap: break-word;
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
    max-width: 90%;
    margin: 20px auto;
    line-height: 1.8;
    /*background-color: #A9A9A9;*/
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

/* 大屏平板设备，宽度在992px到1200px之间 */
@media (max-width: 1200px) and (min-width: 992px) {
    .article-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: flex-start;
        min-height: 100vh;
        width: 100%;
    }
    .article-content {
        flex-grow: 1;
        width: 100%;
    }
    .article-header {
        padding: 20px 0;
    }

    .header-right .article-meta {
        gap: 50px;
    }

    .ncdt-bottom-tool {
        padding: 15px;
    }

    .bottom-tool-content {
        gap: 30px;
    }
}

/* 小屏平板和大屏手机，宽度在768px到992px之间 */
@media (max-width: 992px) and (min-width: 768px) {
    .article-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: flex-start; /* 改为 flex-start */
        min-height: 100vh;
        width: 100%;
    }
    .article-content {
        overflow: visible;
        flex-grow: 1;
        width: 100%;
    }
    .article-header {
        padding: 20px 0;
    }

    .header-right .article-meta {
        flex-direction: row;
        gap: 20px;
        font-size: 15px;
    }

    .ncdt-bottom-tool {
        padding: 15px;
    }

    .bottom-tool-content {
        flex-direction: row;
        align-items: center;
    }
}

/* 小屏手机，宽度在480px到768px之间 */
@media (max-width: 768px) and (min-width: 480px) {
    .article-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        margin: 20px 0;
        justify-content: flex-start;
        min-height: auto;
        width: 100%;
    }
    .article-content {
        flex-grow: 1;
        width: 100%;
    }
    .article-content p img{
        width: 10px;
        height: 10px;
        margin: 10px 0;
    }
    .article-header {
        flex-direction: row;
        align-items: center;
    }

    .article-meta span:not(.article-date):not(.article-source) {
        display: none;
    }

    /* 控制间距和字体大小 */
    .article-meta {
        gap: 10px;
        font-size: 14px;
    }
    .header-right .article-meta {
        flex-direction: row;
        gap: 15px;
        font-size: 14px;
    }

    .ncdt-bottom-tool {
        padding: 10px;
    }

    .bottom-tool-content {
        flex-direction: row;
        align-items: center;
    }
}

/* 手机端，宽度在480px以下 */
@media (max-width: 480px) {
    .article-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: flex-start;
        min-height: 100vh;
        width: 100%;
    }
    .article-content {
        width: 100%;
        flex-grow: 1;
    }
    .article-content img {
        width: 100px;
        height: auto;
    }
    .article-header {
        flex-direction: row;
        align-items: center;
    }
    .article-meta span:not(.article-date):not(.article-source) {
        display: none;
    }

    /* 控制间距和字体大小 */
    .article-meta {
        gap: 10px;
        font-size: 12px;
    }

    .header-left, .header-right {
        margin-bottom: 10px;
    }

    .header-right .article-meta {
        flex-direction: row;
        gap: 10px;
        font-size: 10px;
        text-align: center; /* 日期和信息来源居中 */
    }

    .ncdt-bottom-tool {
        padding: 5px;
    }

    .bottom-tool-content {
        flex-direction: row;
        align-items: center;
        gap: 10px;
    }
}
</style>
