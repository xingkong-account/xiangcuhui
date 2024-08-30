<template>
    <el-container>
        <el-header>
            <h2>编辑文章</h2>
        </el-header>
        <el-main>
            <el-form :model="article" :rules="rules" ref="form">
                <el-form-item label="标题" prop="title">
                    <el-input v-model="article.title"></el-input>
                </el-form-item>
                <el-form-item label="内容" prop="content">
                    <div ref="editorContainer" class="editor-container"></div>
                </el-form-item>
                <el-form-item label="分类" prop="category">
                    <el-input v-model="article.category"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm">提交</el-button>
                    <el-button @click="resetForm">重置</el-button>
                </el-form-item>
            </el-form>
        </el-main>
    </el-container>
</template>

<script>
import E from 'wangeditor';
import hljs from 'highlight.js';

export default {
    data() {
        return {
            article: {
                id: null,
                title: '',
                content: '',
                category: ''
            },
            rules: {
                title: [
                    { required: true, message: '请输入标题', trigger: 'blur' }
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
        fetchArticle() {
            const id = this.$route.params.id;
            this.$axios.get(`http://localhost:8081/api/articles/${id}`)
                .then(response => {
                    this.article = response.data;
                    this.editor.txt.html(this.article.content); // 预加载内容
                })
                .catch(() => {
                    this.$message.error('加载文章失败');
                });
        },
        submitForm() {
            this.article.content = this.editor.txt.html();

            this.$refs.form.validate(valid => {
                if (valid) {
                    this.$axios.put(`http://localhost:8081/api/articles/${this.article.id}`, this.article)
                        .then(() => {
                            this.$message.success('文章更新成功');
                            this.$router.push('/articles');
                        })
                        .catch(() => {
                            this.$message.error('更新文章失败');
                        });
                } else {
                    this.$message.error('请检查表单输入');
                }
            });
        },
        resetForm() {
            this.$refs.form.resetFields();
            this.editor.txt.clear();
        }
    },
    mounted() {
        this.$nextTick(() => {
            this.editor = new E(this.$refs.editorContainer);
            this.editor.highlight = hljs;

            // 配置图片上传
            this.editor.config.uploadImgServer = 'http://localhost:8081/api/articles/uploadImage';
            this.editor.config.uploadFileName = 'file';
            this.editor.config.uploadImgParams = {
                type: "img"
            }
            // 设置上传图片的回调函数
            this.editor.config.uploadImgHooks = {
                customInsert: function (insertImgFn, result) {
                    if (result.errno === 0) {
                        insertImgFn(result.data.url);
                    } else {
                        console.error('图片上传失败', result);
                    }
                },
                onError: function (xhr) {
                    console.error('图片上传出错', xhr);
                }
            };

            // 配置视频上传
            this.editor.config.uploadVideoServer = "http://localhost:8081/api/articles/uploadImage"; // 修正了URL
            this.editor.config.uploadVideoName = "file";
            this.editor.config.uploadVideoParams = {
                type: "video"
            };

            // 设置上传视频的回调函数
            this.editor.config.uploadVideoHooks = {
                customInsert: function (insertVideoFn, result) {
                    if (result.errno === 0) {
                        insertVideoFn(result.data.url);
                    } else {
                        console.error('视频上传失败', result);
                    }
                },
                onError: function (xhr) {
                    console.error('视频上传出错', xhr);
                }
            };

            this.editor.create();
            this.fetchArticle();
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
.custom-footer {
    background-color: #1b75bb;
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
.el-form-item {
    margin-bottom: 20px;
}
.editor-container {
    border: 1px solid #ddd;
    min-height: 200px;
    padding: 10px;
}
</style>
