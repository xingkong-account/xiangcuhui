<template>
    <el-container>
        <el-header>
            <div style="display: flex; justify-content: space-between">
                <h2>编辑文章</h2>
                <el-button style="border-radius: 20px; width: 10%; height: auto" @click="navigateBack" type="default">返回</el-button>
            </div>
        </el-header>
        <el-main class="custom-main">
            <el-form :model="article" :rules="rules" ref="form">
                <el-form-item label="标题" prop="title">
                    <el-input v-model="article.title"></el-input>
                </el-form-item>
                <el-form-item label="来源" prop="source">
                    <el-input v-model="article.source"></el-input>
                </el-form-item>
                <el-form-item label="浏览次数" prop="views" v-if="isAdmin">
                    <el-input v-model="article.views"></el-input>
                </el-form-item>
                <br>
                <el-form-item label="内容" prop="content">
                    <div ref="editorContainer" class="editor-container"></div>
                </el-form-item>
                <el-form-item label="分类" prop="category">
                    <el-select v-model="article.category" placeholder="请选择分类" popper-class="custom-dropdown">
                        <el-option label="农村党建" value="农村党建"></el-option>
                        <el-option label="集体经济" value="集体经济"></el-option>
                        <el-option label="产业发展" value="产业发展"></el-option>
                        <el-option label="乡土文化" value="乡土文化"></el-option>
                        <el-option label="青山绿水" value="青山绿水"></el-option>
                        <el-option label="青年农人" value="青年农人"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="状态" prop="status" v-if="isAdmin">
                    <el-select v-model="article.status" placeholder="选择审核状态" popper-class="custom-dropdown">
                        <el-option label="待审核" value="待审核"></el-option>
                        <el-option label="通过审核" value="已审核"></el-option>
                        <el-option label="拒绝" value="已拒绝"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item v-if="isAdmin">
                    <el-select v-model="article.isTop" placeholder="选择置顶状态" popper-class="custom-dropdown">
                        <el-option
                            :label="article.isTop === 1 ? '置顶文章' : '取消置顶'"
                            :value="article.isTop">
                        </el-option>
                        <el-option
                            label="置顶文章"
                            value="1"
                            v-if="article.isTop !== 1">
                        </el-option>
                        <el-option
                            label="取消置顶"
                            value="0"
                            v-if="article.isTop === 1">
                        </el-option>
                    </el-select>
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
                views: '',
                content: '',
                category: '',
                status: '',
                isTop: ''
            },
            isAdmin: false,
            author: '',
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
        navigateBack() {
            this.$router.go(-1);
        },
        fetchArticle() {
            const id = this.$route.params.id;
            this.$axios.get(this.$baseUrl + `/api/articles/${id}`)
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
                    this.$axios.put(this.$baseUrl + `/api/articles/${this.article.id}`, this.article)
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
        },
        canEdit(member){
            return this.isAdmin || (this.author === member.name);
        }
    },
    mounted() {
        this.$nextTick(() => {
            this.editor = new E(this.$refs.editorContainer);
            this.editor.highlight = hljs;
            this.editor.config.height = '300px';

            // 配置图片上传
            this.editor.config.uploadImgServer = this.$baseUrl + '/api/articles/uploadImage';
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
            this.editor.config.uploadVideoServer = this.$baseUrl + "/api/articles/uploadImage"; // 修正了URL
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
    },
    created() {
        this.isAdmin = (sessionStorage.getItem("usertype") === "管理员");
    }
};
</script>

<style scoped>
.el-header {
    background-color: #f5f7fa;
    padding: 20px;
    border-bottom: 1px solid #ebeef5;
}

.el-header h2 {
    margin: 0;
    font-size: 24px;
    font-weight: bold;
}

.custom-main {
    padding: 40px;
    background-color: #ffffff;
}

.el-form-item {
    margin-bottom: 20px;
}

.editor-container {
    min-height: 300px;
    border: 1px solid #dcdfe6;
    padding: 10px;
    border-radius: 4px;
    background-color: #fff;
}

.custom-dropdown .el-select-dropdown__item {
    font-size: 14px;
}

.el-button {
    padding: 10px 20px;
}

.el-button[type="primary"] {
    background-color: #409eff;
    border-color: #409eff;
    color: #fff;
}

.el-button[type="default"] {
    background-color: #fff;
    border-color: #dcdfe6;
    color: #606266;
}

.el-select {
    width: 100%;
}

.el-form-item:last-child {
    display: flex;
    justify-content: flex-end;
}

.el-form-item:last-child .el-button {
    margin-left: 10px;
}

</style>



