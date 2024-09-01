<template>
    <el-container style="height: 100vh;">
        <el-header style="background-color: #67C23A; color: white; text-align: center; padding: 10px;">
            <span style="margin: 30px;">添加文章</span>
            <el-button type="default" @click="goBack" style="margin-right: 30px; background-color: #67C23A">
                <i class="el-icon-arrow-left"></i>
                <span style="color: white; font-size: 16px; font-weight: bold">
                    返回
                </span>
            </el-button>
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
                    <el-input v-model="article.category" placeholder="请输入分类"></el-input>
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
                    <img src="http://localhost:8081/image.png" width="200px" height="120px" class="">
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
    }
};
</script>


<style scoped>
.article-form {
    background-color: #f4f4f5;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.editor-container {
    border: 1px solid #ddd;
    border-radius: 4px;
    min-height: 300px;
    padding: 10px;
    background-color: #ffffff;
}

.el-form-item {
    margin-bottom: 20px;
}

.submit-buttton {
    background-color: #4CAF50; /* 绿色背景色 */
    border-color: #4CAF50; /* 同样的边框颜色 */
}

.reset-button {
    background-color: #C0C0C0; /* 红色背景色 */
    border-color: #C0C0C0; /* 同样的边框颜色 */
    text-decoration:none ;
    color: black;
}
/* 为按钮添加 hover 效果 */
.el-button:hover.submit-buttton {
    background-color: #45a049; /* 绿色背景色 hover 效果 */
    border-color: #45a049; /* 同样的边框颜色 */
}

.el-button:hover.reset-button {
    background-color: #A9A9A9;
    border-color: #A9A9A9;
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