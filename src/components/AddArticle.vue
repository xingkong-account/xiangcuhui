<template>
    <el-container>
        <el-header style="background-color: rgb(101,172,140); color: white; text-align: center; padding: 13px;">
            <span style="margin-top: 30px; font-size: 26px">添加文章</span>
        </el-header>
        <el-main style="padding: 20px;">
            <el-form :model="article" :rules="rules" ref="form" label-width="100px" class="article-form">
                <el-form-item label="标题" prop="title">
                    <el-input v-model="article.title" placeholder="请输入标题"></el-input>
                </el-form-item>
                <el-form-item label="作者" prop="author">
                    <el-input v-model="article.author" placeholder="请输入作者"></el-input>
                </el-form-item>
                <el-form-item label="来源" prop="source">
                    <el-input v-model="article.source" placeholder="请输入来源"></el-input>
                </el-form-item>
                <el-form-item label="内容" prop="content">
                    <div ref="editorContainer" class="editor-container"></div>
                </el-form-item>
                <el-form-item label="分类" prop="category">
                    <el-select v-model="article.category" placeholder="请选择分类">
                        <el-option label="农村党建" value="农村党建"></el-option>
                        <el-option label="集体经济" value="集体经济"></el-option>
                        <el-option label="产业发展" value="产业发展"></el-option>
                        <el-option label="乡土文化" value="乡土文化"></el-option>
                        <el-option label="青山绿水" value="青山绿水"></el-option>
                        <el-option label="青年农人" value="青年农人"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item>
                    <el-button class="submit-buttton" type="primary" @click="submitForm">提交</el-button>
                    <el-button @click="resetForm" class="reset-button" style="margin-left: 10px;">重置</el-button>
                </el-form-item>
            </el-form>
        </el-main>

        <el-footer class="custom-footer">
            <div class="footer-content">
                <div class="footer-left">
                    <img src="@/assets/images/icon.jpg" class="foot-img">
                </div>
                <div class="footer-center">
                    <p><a href="#">网站地图</a> | <a href="#">联系方式</a> | <a href="#">使用帮助</a> | <a href="#">隐私声明</a></p>
                    <p>主办单位:  乡促会&nbsp;&nbsp;&nbsp; 备案号：</p>
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
import E from 'wangeditor';
import hljs from 'highlight.js';

export default {
    data() {
        return {
            article: {
                title: '',
                author: '',
                source: '',
                content: '',
                category: '',
                views: ''
            },
            isAdmin: false,
            rules: {
                title: [
                    { required: true, message: '请输入标题', trigger: 'blur' }
                ],
                author: [
                    { required: true, message: '请输入作者', trigger: 'blur' }
                ],
                source: [
                    { required: true, message: '请输入文章来源', trigger: 'blur' }
                ],
                content: [
                    { required: true, message: '请输入内容', trigger: 'blur' }
                ],
                category: [
                    { required: true, message: '请输入分类', trigger: 'blur' }
                ]
            },
            editor: null
        };
    },
    methods: {
        submitForm() {
            this.article.content = this.editor.txt.html();
            this.$refs.form.validate(valid => {
                if (valid) {
                    this.$axios.post(this.$baseUrl + '/api/articles/add', this.article)
                        .then(() => {
                            this.$message.success('文章添加成功');
                            this.$router.push('/articles');
                        })
                        .catch((msg) => {
                            this.$message.error('添加文章失败', msg);
                        });
                } else {
                    this.$message.error('请检查表单输入');
                }
            });
        },
        resetForm() {
            this.$refs.form.resetFields();
            this.editor.txt.clear();
        },
        goBack(){
            this.$router.go(-1);
        },
        canEdit(member){
            this.isAdmin = (sessionStorage.getItem("usertype") === "管理员");
            return this.isAdmin || this.article.author === member.name;
        }
    },
    mounted() {
        this.$nextTick(() => {
            this.editor = new E(this.$refs.editorContainer);
            this.editor.highlight = hljs;

            // 配置图片上传
            this.editor.config.uploadImgServer = this.$baseUrl +'/api/articles/uploadImage';
            this.editor.config.uploadFileName = 'file';
            this.editor.config.uploadImgParams = {
                type: "img"
            };
            this.editor.config.uploadImgHooks = {
                customInsert: (insertImgFn, result) => {
                    if (result.errno === 0) {
                        insertImgFn(result.data.url);
                    } else {
                        console.error('图片上传失败', result);
                    }
                },
                onError: (xhr) => {
                    console.error('图片上传出错', xhr);
                }
            };

            // 配置视频上传
            this.editor.config.uploadVideoServer = this.$baseUrl + "/api/articles/uploadImage"; // 注意使用合适的API路径
            this.editor.config.uploadVideoName = "file";
            this.editor.config.uploadVideoParams = {
                type: "video"
            };
            this.editor.config.uploadVideoHooks = {
                customInsert: (insertVideoFn, result) => {
                    if (result.errno === 0) {
                        insertVideoFn(result.data.url);
                    } else {
                        console.error('视频上传失败', result);
                    }
                },
                onError: (xhr) => {
                    console.error('视频上传出错', xhr);
                }
            };

            this.editor.create();
        });
    },
    beforeDestroy() {
        if (this.editor) {
            this.editor.destroy();
        }
    },
    created() {
        this.article.author = sessionStorage.getItem("username");
    }
};
</script>


<style scoped>
/* 基本样式设置 */
body {
    margin: 0;
    font-family: Arial, sans-serif;
}

/* 全局容器样式 */
.el-container {
    min-height: 100%;
    display: flex;
    flex-direction: column;
}

/* 大屏平板设备，宽度在992px到1200px之间 */
@media (max-width: 1200px) and (min-width: 992px) {
    .container {
        padding: 20px;
    }

    .header, .footer {
        flex-direction: row; /* 保持水平布局 */
    }

    .main-content {
        padding: 20px;
    }
}

/* 小屏平板和大屏手机，宽度在768px到992px之间 */
@media (max-width: 992px) and (min-width: 768px) {
    .container {
        padding: 15px;
    }

    .header, .footer {
        flex-direction: column; /* 垂直布局 */
    }

    .main-content {
        padding: 15px;
    }
}

/* 小屏手机，宽度在480px到768px之间 */
@media (max-width: 768px) and (min-width: 480px) {
    .container {
        padding: 10px;
    }

    .header, .footer {
        flex-direction: column; /* 垂直布局 */
    }

    .main-content {
        padding: 10px;
    }
}

/* 超小屏手机，宽度在480px以下 */
@media (max-width: 480px) {
    .container {
        padding: 5px;
    }

    .header, .footer {
        flex-direction: column; /* 垂直布局 */
    }

    .main-content {
        padding: 5px;
    }
}

.submit-buttton {
    background-color: rgb(101,172,140);
    border-color: rgb(101,172,140);
}

.reset-button {
    background-color: #C0C0C0;
    border-color: #C0C0C0;
    text-decoration:none ;
    color: black;
}

.el-button:hover.submit-buttton {
    background-color: #45a049;
    border-color: #45a049;
}

.el-button:hover.reset-button {
    background-color: #A9A9A9;
    border-color: #A9A9A9;
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