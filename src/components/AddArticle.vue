<template>
    <el-container>
        <el-header style="background-color: rgb(101,172,140); color: white; text-align: center; padding: 13px;">
            <span style="font-size: 26px">添加文章</span>
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
<!--                <el-form-item label="分类" prop="category">-->
<!--                    <el-dropdown @command="handleCommand" ref="categoryDropdown" trigger="click">-->
<!--                        <el-button type="primary">-->
<!--                            {{ selectedCategory ? selectedCategory : '请选择分类' }}<i class="el-icon-arrow-down el-icon&#45;&#45;right"></i>-->
<!--                        </el-button>-->
<!--                        <template #dropdown>-->
<!--                            <el-dropdown-menu>-->
<!--                                <el-dropdown-item command="农村党建">农村党建</el-dropdown-item>-->
<!--                                <el-dropdown-item command="集体经济">集体经济</el-dropdown-item>-->
<!--                                <el-dropdown-item command="产业发展">产业发展</el-dropdown-item>-->
<!--                                <el-dropdown-item command="乡土文化">乡土文化</el-dropdown-item>-->
<!--                                <el-dropdown-item command="青山绿水">青山绿水</el-dropdown-item>-->
<!--                                <el-dropdown-item command="青年农人">青年农人</el-dropdown-item>-->
<!--                            </el-dropdown-menu>-->
<!--                        </template>-->
<!--                    </el-dropdown>-->
<!--                </el-form-item>-->

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
                <el-form-item>
                    <el-button class="submit-button" type="primary" @click="submitForm">提交</el-button>
                    <el-button @click="resetForm" class="reset-button" style="margin-left: 10px;">重置</el-button>
                </el-form-item>
            </el-form>
        </el-main>

        <el-footer class="custom-footer" v-if="!isMobile">
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

        <el-footer class="mobile-footer" v-show="isMobile">
            <div class="mobile-footer-content">
                <div class="">
<!--                    <img src="@/assets/images/icon.jpg" class="foot-img" alt="">-->
                </div>
                <div>
                    <p><a href="#">网站地图</a> | <a href="#">联系方式</a> | <a href="#">使用帮助</a> | <a href="#">隐私声明</a></p>
                    <p>主办单位: 乡促会&nbsp;&nbsp;&nbsp; </p>
                    <p>地址:甘肃省兰州市七里河区 </p>
                </div>
                <div class="mobile-record-link">
                    <a href="https://www.beian.gov.cn/portal/registerSystemInfo?recordcode=44030702002386" target="_blank" class="record-link">
                        <span>粤公网安备44030702002386号</span>
                    </a>
                    <p>甘A2-20044005号</p>
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
            selectedCategory: null, // 保存选择的分类
            isAdmin: false,
            isMobile: false,
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
                    { required: true, message: '请选择分类', trigger: 'blur' }
                ]
            },
            editor: null
        };
    },
    methods: {
        handleCommand(command) {
            this.selectedCategory = command; // 更新选中的分类
            this.article.category = this.selectedCategory;
        },
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
        },
        checkIfMobile() {
            this.isMobile = window.innerWidth <= 768;
        },
        changeChunkSize(){
            if (window.innerWidth < 480) {
                this.chunkSize = 1;
            } else if (window.innerWidth < 768) {
                this.chunkSize = 1;
            } else {
                this.chunkSize = 3;
            }
        },
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
        this.checkIfMobile();
        this.changeChunkSize();
        window.addEventListener('resize', this.changeChunkSize);
        window.addEventListener('resize', this.checkIfMobile);
    },
    created() {
        this.article.author = sessionStorage.getItem("username");
    },
    beforeDestroy() {
        if (this.editor) {
            this.editor.destroy();
        }
        window.removeEventListener('resize', this.checkIfMobile);
        window.removeEventListener('resize', this.changeChunkSize);
    }
};
</script>

<!-- 全局样式，不使用 scoped -->
<style>
.el-dropdown-menu {
    max-height: 200px;
    overflow-y: auto;
    z-index: 9999;
    position: relative;
}
</style>

<style scoped>
.record-link {
    display: flex;
    align-items: center;
    text-decoration: none;
    color: inherit;
}
.mobile-record-link {
    display: flex;
    align-items: center;
    justify-content: center;
    flex-wrap: wrap;
}
.mobile-footer {
    background: linear-gradient(45deg, rgb(8, 13, 9), rgb(33, 54, 39));
    background-size: cover;
    color: #ffffff;
    padding: 20px 0;
    text-align: center;
    min-height: 200px;
}

.mobile-footer-content {
    display: flex;
    flex-direction: column;
    align-items: center;
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

.security-logo {
    width: 50px;
    margin-bottom: 5px;
}

@media screen and (max-width: 480px) {
    .mobile-footer-content {
        flex-direction: column;
        align-items: center;
    }

    .mobile-footer p {
        font-size: 12px;
    }
}

body {
    margin: 0;
    font-family: Arial, sans-serif;
}

.el-container {
    min-height: 100%;
    display: flex;
    flex-direction: column;
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

.security-logo {
    width: 20px;
    height: auto;
    border-radius: 50%;
    margin-right: 10px;
}

@media (max-width: 992px) and (min-width: 768px) {
    .custom-footer{
        min-height: 240px;
    }
    .footer-content {
        flex-direction: column;
        height: 100px;
    }
    .footer-left, .footer-right {
        margin-bottom: 20px;
    }
    .foot-img {
        width: 200px;
        height: 50px;
    }
}
</style>