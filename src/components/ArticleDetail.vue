<template>
    <el-container>
        <el-main>
            <div class="article-container">
                <h2 class="article-title">{{ article.title }}</h2>
                <div class="article-blue-line"></div>
                <el-card class="article-card" :body-style="{ padding: '10px 20px' }">
                    <div class="article-header">
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
                    <div class="w-e-text">
                        <!-- 渲染内容 -->
                        <div v-html="article.content" class="article-content"></div>
                    </div>
                    <el-button class="back-button" type="primary" @click="goBack">返回</el-button>
                </el-card>
            </div>
        </el-main>

        <div class="ncdt-bottom-tool">
            <div class="flex">
                <p>
      <span>
        <i class="el-icon-printer"></i>
      </span>&nbsp;&nbsp;
                    <span>
        <a href="javascript:window.print()">打印</a>
      </span>
                    <span>
        <i class="el-icon-circle-close"></i>
        <a href="javascript:window.opener=null;window.open('about:blank','_self');window.close();">关闭</a>
      </span>
                </p>
            </div>
        </div>

        <el-footer class="custom-footer">
            <div class="footer-content">
                <div class="footer-left">
                    <img src="@/assets/images/icon.png" width="200px" height="120px" class="">
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
            }
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
        setFontSize(size) {
            document.querySelectorAll('.ncdt-list-box p').forEach((el) => {
                el.style.fontSize = size;
            });
        },
        bigsize() {
            this.setFontSize("20px");
        },
        insize() {
            this.setFontSize("16px");
        },
        smallsize() {
            this.setFontSize("14px");
        },
    },
    created() {
        this.fetchArticle();
        this.incrementViews();
    }
};
</script>

<style scoped>
.article-container {
    display: flex;
    flex-direction: column; /* 让子元素按列显示 */
    align-items: center;
    justify-content: center;
    min-height: 100vh;
}

.article-content {
    text-align: left;      /* 文章内容左对齐 */
}

.el-card{
    width: 80%;
    max-width: 90%;
    margin-left: 100px;
}

/*底部打印栏背景框样式*/
.ncdt-bottom-tool {
    background-color: #f0f0f0;
    width: 80%;
    height: 50px;
    display: flex;
    margin-bottom: 10px;
    margin-left: 13%;
}

.ncdt-bottom-tool .flex {
    display: flex;
    justify-content: end;
    width: 80%;
    margin-right: 1200px;
}

.ncdt-bottom-tool a {
    text-decoration: none;
    color: #333;
}

.ncdt-bottom-tool i {
    margin-right: 3px;
}

.link a {
    color: #007bff;
    text-decoration: none;
}

img {
    height: 24px;
    vertical-align: middle;
}
.back-button{
    background-color: #67C23A;
    border-color: #67C23A;
}

.back-button :hover{
    background-color: #228B22;
    border-color: #228B22;
}
.article-header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
    font-size: 14px;
    color: #666;
}

.article-meta {
    margin-bottom: 20px;
    font-size: 14px;
    color: #666;
}

.article-title {
    font-size: 20px;
    font-weight: bold;
    margin-bottom: 10px;
    text-align: center;
}

.article-blue-line {
    height: 4px;
    background-color: #A9A9A9;
    margin-bottom: 10px;
    width: 80%;
    margin-left: 100px;
}

.footer-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.footer-left img,
.footer-right img {
    max-width: 100%;
    height: auto;
}

.footer-center {
    text-align: center;
}

.footer-center p {
    margin: 5px 0;
}

.footer-center a {
    color: #3a8ee6; /* 与蓝色横线相同的颜色 */
    text-decoration: none;
}

.footer-center a:hover {
    text-decoration: underline;
}

.article-content {
    margin-top: 20px;
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
</style>
