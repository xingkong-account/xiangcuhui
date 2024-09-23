<template>
    <div class="page-container">
        <div class="welcome-message">
            欢迎注册
        </div>
        <!-- 注册表单 -->
        <el-container class="m">
            <el-main>
                <el-form :model="form" ref="form" label-width="140px" :rules="rules">
                    <div :class="{'apply-shake': true, 'shake': shake}">
                        <el-form-item label="用户名" prop="name">
                            <el-input v-model="form.name"></el-input>
                        </el-form-item>
                    </div>
                    <div :class="{'apply-shake': true, 'shake': shake}">
                        <el-form-item label="密码" prop="password">
                            <el-input v-model="form.password" show-password type="password"></el-input>
                        </el-form-item>
                    </div>
                    <div :class="{'apply-shake': true, 'shake': shake}">
                        <el-form-item label="确认密码" prop="confirmPassword">
                            <el-input v-model="form.confirmPassword" show-password type="password"></el-input>
                        </el-form-item>
                    </div>
                    <div :class="{'apply-shake': true, 'shake': shake}">
                        <el-form-item label="请输入邮箱" prop="email">
                            <el-input v-model="form.email"></el-input>
                        </el-form-item>
                    </div>
                    <div :class="{'apply-shake': true, 'shake': shake}">
                        <el-form-item label="手机号" prop="phone">
                            <el-input v-model="form.phone" maxlength="13"></el-input>
                            <el-button @click="sendSmsCode" :disabled="!form.phone">发送验证码</el-button>
                        </el-form-item>
                    </div>
                    <el-form-item label="手机验证码 (可选)" prop="phoneCode">
                        <el-input v-model="form.phoneCode" placeholder="请输入手机验证码" maxlength="6"></el-input>
                    </el-form-item>
                    <div :class="{'apply-shake': true, 'shake': shake}">
                        <el-form-item label="验证码" prop="code">
                            <div class="input-with-code">
                                <el-input v-model="form.code" placeholder="请输入验证码" maxlength="4"></el-input>
                                <valid-code @update:value="getCode" ref="validCode" class="code-container"></valid-code>
                            </div>
                        </el-form-item>
                    </div>
                    <el-form-item label="用户类型" prop="type" class="user-type">
                        <el-radio-group v-model="form.type" class="type-group">
                            <el-radio label="个人会员">个人会员</el-radio>
                            <el-radio label="团体会员">团体会员</el-radio>
                        </el-radio-group>
                    </el-form-item>

                    <!-- 图片上传字段，仅在用户类型为团体会员时显示 -->
                    <el-form-item v-if="form.type === '团体会员'"  label="上传图片" prop="image_url" class="upload-img">
                        <el-upload
                            class="upload-button"
                            :action="$baseUrl + '/api/upload'"
                            list-type="picture"
                            :show-file-list="false"
                            :on-success="handleUploadSuccess"
                            :on-error="handleUploadError"
                            :limit="1"
                            accept="image/*">
                            <el-button size="small" type="primary">点击上传</el-button>
                            <div slot="tip" class="el-upload__tip">只能上传图片文件</div>
                        </el-upload>
                        <el-image v-if="form.image_url" :src="form.image_url" style="width: 100px; margin-top: 10px;"></el-image>
                    </el-form-item>

                    <el-form-item class="button-group">
                        <el-button type="primary" class="custom-register-button" @click="submitForm">注册</el-button>
                        <el-button @click="goToLogin" class="custom-login-button">登录</el-button>
                    </el-form-item>
                </el-form>
            </el-main>
        </el-container>
    </div>
</template>

<script>
import axios from 'axios';
import ValidCode from "@/components/ValidCode.vue";

export default {
    name: 'UserRegister',
    components: {
        ValidCode
    },
    data() {
        return {
            form: {
                name: '',
                password: '',
                confirmPassword: '',
                phone: '',
                email: '',
                image_url: '',
                code: '',
                type: '个人会员'
            },
            verifyCode: '',
            shake: false,
            rules: {
                name: [
                    { required: true, message: '请输入用户名', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' }
                ],
                confirmPassword: [
                    { required: true, message: '请确认密码', trigger: 'blur' },
                    { validator: this.validateConfirmPassword, trigger: 'blur' }
                ],
                phone: [
                    { required: true, message: '请输入手机号', trigger: 'blur' },
                    {
                        pattern: /^(1[3-9]\d{9}|\d{3,4}-\d{7,8})$/,
                        message: '手机号或座机号格式不正确，座机号（0931-11111111）',
                        trigger: 'blur'
                    }
                ],
                code: [
                    { required: true, message: '请输入验证码', trigger: 'blur' },
                    { validator: this.validateCode, trigger: 'blur' }
                ],
                email: [
                    { required: true, message: '请输入邮箱', trigger: 'blur' },
                    {
                        pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
                        message: '邮箱格式不正确',
                        trigger: 'blur'
                    }
                ]
            }
        };
    },
    methods: {
        handleUploadSuccess(response) {
            if (response && response.url) {
                this.form.image_url = response.url;
                this.$message.success('图片上传成功');
            } else {
                this.$message.error('上传失败，请稍后重试');
            }
        },
        handleUploadError() {
            this.$message.error('图片上传失败');
        },
        validateConfirmPassword(rule, value, callback) {
            if (value !== this.form.password) {
                callback(new Error('两次输入的密码不一致'));
            } else {
                callback();
            }
        },
        validateCode(rule, value, callback) {
            if (value === '') {
                callback(new Error('请输入验证码'));
            } else if (value.toLowerCase() !== this.verifyCode) {
                callback(new Error('验证码错误'));
                this.refreshCode();
            } else {
                callback();
            }
        },
        getCode(code) {
            this.verifyCode = code.toLowerCase();
        },
        refreshCode() {
            this.$refs.validCode.refreshCode();
        },
        sendSmsCode() {
            axios.post(this.$baseUrl + '/api/sendSms', { phone: this.form.phone })
                .then(() => {
                    this.$message.success('验证码已发送');
                })
                .catch(error => {
                    console.error('发送验证码失败：', error);
                    this.$message.error('发送验证码失败');
                });
        },
        submitForm() {
            this.$refs.form.validate((valid) => {
                if (valid) {
                    axios.post(this.$baseUrl + '/api/add', {
                        name: this.form.name,
                        password: this.form.password,
                        email: this.form.email,
                        phone: this.form.phone,
                        code: this.form.code,
                        type: this.form.type,
                        image_url: this.form.image_url
                    }).then((response) => {
                        console.log(response);
                        this.$message.success('注册成功');
                        this.$router.push('/login');
                    }).catch(error => {
                        console.error('请求错误：', error);
                        const errorMessage = error.response && error.response.data
                            ? error.response.data
                            : '注册失败';
                        this.$message.error('注册失败：' + errorMessage);
                    });
                } else {
                    this.shake = true;
                    setTimeout(() => {
                        this.shake = false;
                    }, 820);
                    this.$message.error('请填写完整信息');
                }
            });
        },
        goToLogin() {
            if (this.form === '') {
                this.shake = true;
                setTimeout(() => {
                    this.shake = false;
                }, 820);
                return;
            }
            this.$router.push('/login');
        }
    }
};
</script>

<style scoped>
/*验证码*/
.input-with-code {
    display: flex;
    align-items: center;
    margin-top: 10px;
    height: 20px;
}
.page-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    background-image: url("@/assets/images/register.jpg");
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    background-attachment: fixed;
    height: 100%;
    width: 100vw;
    overflow: hidden;
    box-sizing: border-box;
}

.welcome-message {
    color: rgb(156,201,180);
    font-size: 28px;
    font-weight: bold;
    text-align: center;
    margin: 20px 0;
    position: relative;
    width: 100%;
}

.welcome-message::after {
    content: '';
    display: block;
    width: 100%;
    height: 2px;
    background-color: rgb(156,201,180);
    margin: 8px auto 0;
}

@media (min-width: 1200px) {
    .page-container {
        padding: 20px;
        justify-content: center;
        align-items: center;
    }
    .welcome-message {
        font-size: 28px;
    }
}

@media (max-width: 1200px) and (min-width: 992px) {
    .page-container {
        padding: 15px;
        justify-content: center;
        align-items: center;
    }
    .welcome-message {
        font-size: 24px;
    }
}

@media (max-width: 992px) and (min-width: 768px) {
    .page-container {
        padding: 10px;
        justify-content: center;
        align-items: center;
    }
    .welcome-message {
        font-size: 22px;
    }
}

@media (max-width: 768px) {
    .page-container {
        padding: 5px;
        justify-content: center;
        align-items: center;
    }
    .welcome-message {
        font-size: 20px;
    }
}

/* 给表单设置最大宽度和居中对齐 */
.el-form {
    max-width: 800px;
    margin: 10px 10px auto;
    border-radius: 8px;
}

.el-form-item {
    margin-bottom: 20px;
}

.el-form-item:nth-of-type(1) {
    margin-bottom: 30px;
    margin-left: -50px;
}

.el-form-item:nth-of-type(2) {
    margin-bottom: 20px;
    margin-left: -50px;
}

.el-form-item:nth-of-type(3) {
    margin-bottom: 15px;
    margin-left: -50px;
}

.el-form-item:nth-of-type(4) {
    margin-bottom: 25px;
    margin-left: -50px;
}

.apply-shake.shake {
    animation: shake 0.82s cubic-bezier(0.36, 0.07, 0.19, 0.97) both;
}

@keyframes shake {
    18%, 98% {
        transform: translate3d(-1px, 0, 0);
    }
    28%, 88% {
        transform: translate3d(2px, 0, 0);
    }
    38%, 58%, 78% {
        transform: translate3d(-4px, 0, 0);
    }
    40%, 68% {
        transform: translate3d(4px, 0, 0);
    }
}
.upload-img{
    margin-left: -45px;
}
.user-type {
    flex-direction: column;
    align-items: flex-start;
    display: flex;
    margin-left: -40px;
}
.user-type .el-radio {
    margin-right: 30px;
    margin-bottom: 10px;
}
.user-type .el-radio:last-child {
    margin-bottom: 0;
}
.type-group{
    margin-top: -71px;
    padding: 10px;
    /*background-color: #A9A9A9;*/
    margin-left: 30px;
}

.user-type .el-radio:last-child {
    margin-right: 0;
}

@media (max-width: 768px) {
    .user-type {
        flex-direction: column;
        align-items: flex-start;
        margin-left: -30px;
    }
    .user-type .el-form-item__label {
        margin-right: 10px;
        margin-bottom: 10px;
    }
    .user-type .el-radio {
        margin-right: 0;
        margin-bottom: 10px;
    }
    .user-type .el-radio:last-child {
        margin-bottom: 0;
    }
}


/* 注册按钮的样式 */
.custom-register-button {
    background-color: rgb(101, 172, 140);
    border-color: rgb(101, 172, 140);
    color: #fff;
    transition: background-color 0.3s, border-color 0.3s, color 0.3s;
}

.custom-register-button:hover {
    background-color: rgb(85, 145, 118);
    border-color: rgb(85, 145, 118);
}

.custom-register-button:active {
    background-color: rgb(75, 130, 106);
    border-color: rgb(75, 130, 106);
}

.user-type .el-radio.is-checked .el-radio__input.is-checked .el-radio__inner {
    border-color: #67C23A;
    background-color: #67C23A;
}

.user-type .el-radio.is-checked .el-radio__label {
    color: #67C23A;
}

.custom-register-button {
    background-color: rgb(101, 172, 140);
    border-color: rgb(101, 172, 140);
    color: #fff;
    transition: background-color 0.3s, border-color 0.3s, color 0.3s;
}

.custom-register-button:hover {
    background-color: rgb(85, 145, 118);
    border-color: rgb(85, 145, 118);
}

.custom-register-button:active {
    background-color: rgb(75, 130, 106);
    border-color: rgb(75, 130, 106);
}
/* 登录按钮的样式 */
.custom-login-button {
    background-color: rgb(101, 172, 140);
    border-color: rgb(101, 172, 140);
    color: #fff;
    transition: background-color 0.3s, border-color 0.3s, color 0.3s;
}

.custom-login-button:hover {
    background-color: rgb(85, 145, 118);
    border-color: rgb(85, 145, 118);
}

.custom-login-button:active {
    background-color: rgb(75, 130, 106);
    border-color: rgb(75, 130, 106);
}
</style>
