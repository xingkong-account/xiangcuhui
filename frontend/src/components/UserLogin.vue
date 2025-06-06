<template>
    <div class="page-container">
        <div class="welcome-message">
            欢迎登录
        </div>
        <div class="login-card">
            <div class="form-container">
                <el-form :model="user" :rules="rules" ref="submitForm" class="form">
                    <el-form-item prop="name" :class="{'shake': shakeName}">
                        <el-input
                            prefix-icon="el-icon-user"
                            size="medium"
                            placeholder="请输入账号"
                            v-model="user.name"
                            @blur="checkName"
                        ></el-input>
                    </el-form-item>

                    <el-form-item prop="password" :class="{'shake': shakePassword}">
                        <el-input
                            prefix-icon="el-icon-lock"
                            size="medium"
                            type="password"
                            show-password
                            placeholder="请输入密码"
                            v-model="user.password"
                            @blur="checkPassword"
                        ></el-input>
                    </el-form-item>

                    <el-form-item prop="code" :class="{'shake': shakeCode}">
                        <div class="code-container">
                            <el-input
                                size="medium"
                                maxlength="4"
                                placeholder="请输入验证码"
                                v-model="user.code"
                                prefix-icon="el-icon-circle-check"
                                style="flex: 1"
                                @blur="checkCode"
                            ></el-input>
                            <div class="code-image">
                                <valid-code @update:value="getCode"></valid-code>
                            </div>
                        </div>
                    </el-form-item>

                    <el-form-item>
                        <el-button type="primary" class="custom-login-button" @click="submitForm">登录</el-button>
                    </el-form-item>

                    <div class="n">
                        <div class="register">
                            还没有账号？<span class="link" @click="goToRegister">注册</span>
                        </div>
                        <div class="forgot-password">
                            <span class="link" @click="showForgotPasswordDialog">忘记密码</span>
                        </div>
                    </div>
                </el-form>
            </div>
        </div>

        <!-- 忘记密码弹窗 -->
        <el-dialog
            title="忘记密码"
            :visible.sync="dialogVisible"
            :modal="false"
            :close-on-click-modal="false"
            class="responsive-dialog"
        >
            <el-form
                :model="forgotPasswordForm"
                :rules="forgotPasswordRules"
                ref="forgotPasswordForm"
                class="responsive-form"
            >
                <el-form-item prop="name">
                    <el-input
                        placeholder="请输入用户名"
                        v-model="forgotPasswordForm.name"
                        class="input-field"
                        prefix-icon="el-icon-user"
                    ></el-input>
                </el-form-item>
                <el-form-item prop="email">
                    <el-input
                        placeholder="请输入邮箱"
                        v-model="forgotPasswordForm.email"
                        maxlength="20"
                        class="input-field"
                        prefix-icon="el-icon-message"
                    ></el-input>
                </el-form-item>
                <el-form-item prop="code">
                    <div class="code-input">
                        <el-input
                            placeholder="请输入验证码"
                            v-model="forgotPasswordForm.code"
                            @input="validateEmailCode"
                            maxlength="4"
                            class="flex-input"
                            prefix-icon="el-icon-message"
                        ></el-input>
                        <el-button
                            type="primary"
                            @click="handleSendCode"
                            class="send-button"
                            :disabled="!canSend()"
                        >
                            发送验证码
                        </el-button>
                    </div>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input
                        placeholder="请输入新密码"
                        :disabled="!codeVerified"
                        v-model="forgotPasswordForm.password"
                        maxlength="20"
                        class="flex-input"
                        prefix-icon="el-icon-lock"
                        show-password
                    ></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
        <el-button @click="handleCancel" class="dialog-cancel">取消</el-button>
        <el-button
            type="primary"
            @click="handleResetPassword"
            class="confirm-button"
            :disabled="false"
        >
            确认
        </el-button>
    </span>
        </el-dialog>
    </div>
</template>

<script>
import axios from 'axios';
import ValidCode from "@/components/ValidCode.vue";

export default {
    name: 'UserLogin',
    components: {
        ValidCode
    },
    data() {
        const validateCode = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入验证码'));
            } else if (value.toLowerCase() !== this.verifyCode) {
                callback(new Error("验证码错误"));
            } else {
                callback();
            }
        };
        return {
            user: {
                name: '',
                password: '',
                type: '',
                code: ''  // 图形验证码
            },
            correctMailCode: '',  // 正确的邮箱验证码
            verifyCode: '',  // 验证码组件传过来的 code
            shakeName: false,
            shakePassword: false,
            shakeCode: false,
            dialogVisible: false,
            forgotPasswordForm: {
                name: '',
                email: '',
                code: '',
                password: ''
            },
            codeSent: false,
            codeVerified: false,
            forgotPasswordRules: {
                name: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
                email: [
                    { required: true, message: '请输入邮箱', trigger: 'blur' },
                    { pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
                        message: '邮箱格式不正确', trigger: 'blur' }
                ],
                // code: [
                //     { validator: this.validateEmailCode, trigger: 'blur' }
                // ]
            },
            rules: {
                name: [
                    { required: true, message: '请输入用户名', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', min: 1, max:18, trigger: 'blur' }
                ],
                code: [
                    { validator: validateCode, trigger: 'blur' }
                ]
            }
        };
    },
    methods: {
        canSend(){
            return this.forgotPasswordForm.name !== '' && this.forgotPasswordForm.email !== '';
        },
        // 发送验证码
        handleSendCode() {
            this.$refs.forgotPasswordForm.validate(valid => {
                if (valid) {
                    const requestData = {
                        email: this.forgotPasswordForm.email,
                        name: this.forgotPasswordForm.name
                    };
                    axios.post(this.$baseUrl + '/api/validate-username-email', requestData)
                        .then(response => {
                            if (response.data.data.isMatch) {
                                // 发送验证码
                                return axios.post(this.$baseUrl + '/api/send-email-code', { email: this.forgotPasswordForm.email });
                            } else {
                                this.$message.error("邮箱与用户名不匹配。");
                                throw new Error("邮箱与用户名不匹配");
                            }
                        })
                        .then(() => {
                            this.$message.success("验证码已发送，请注意查看您的邮箱。");
                            setTimeout(() => {
                                this.codeSent = false;
                            }, 300000);
                        })
                        .catch(error => {
                            console.error(error);
                            this.$message.error("发送验证码失败，请重试。");
                        });
                } else {
                    this.$message.error("请检查表单填写是否正确。");
                }
            });
        },
        // 验证输入的邮箱验证码
        validateEmailCode() {
            if (this.forgotPasswordForm.code.length === 4) {
                const requestData = {
                    email: this.forgotPasswordForm.email,
                    code: this.forgotPasswordForm.code
                };
                axios.post(this.$baseUrl + '/api/verify-email-code', requestData)
                    .then((response) => {
                        if (response.data.success) {
                            this.$message.success("验证码正确");
                            this.codeVerified = true;
                        } else {
                            this.$message.error("验证码错误");
                            this.codeVerified = false;
                        }
                    })
                    .catch(error => {
                        console.error(error);
                        this.$message.error("验证码验证失败，请稍后重试。");
                        this.codeVerified = false;
                    });
            } else {
                this.codeVerified = false;
            }
        },
        showForgotPasswordDialog() {
            this.dialogVisible = true;
        },
        handleCancel() {
            this.dialogVisible = false;
            this.forgotPasswordForm = {
                name: '',
                email: '',
                code: '',
                password: ''
            };
            this.codeVerified = false;
            this.codeSent = false;
            this.$refs.forgotPasswordForm.resetFields(); // 重置表单状态
        },
        // 重置密码
        handleResetPassword() {
            // this.$alert`(this.codeVerified);
            if (this.codeVerified) {
                const requestData = {
                    email: this.forgotPasswordForm.email,
                    password: this.forgotPasswordForm.password
                };
                axios.post(this.$baseUrl + '/api/reset-password', requestData)
                    .then(() => {
                        this.$message.success("密码重置成功");
                        this.dialogVisible = false;
                    })
                    .catch(error => {
                        console.error(error);
                        this.$message.error("密码重置失败，请稍后重试。");
                    });
            } else {
                this.$message.error("请先验证验证码！");
            }
        },
        checkName() {
            if (!this.user.name) {
                this.shakeName = true;
                this.resetShake('name');
            }
        },
        checkPassword() {
            if (!this.user.password) {
                this.shakePassword = true;
                this.resetShake('password');
            }
        },
        checkCode() {
            if (!this.user.code) {
                this.shakeCode = true;
                this.resetShake('code');
            }
        },
        resetShake(field) {
            setTimeout(() => {
                if (field === 'name') this.shakeName = false;
                else if (field === 'password') this.shakePassword = false;
                else if (field === 'code') this.shakeCode = false;
            }, 1000);
        },
        getCode(code) {
            this.verifyCode = code.toLowerCase()
        },
        submitForm() {
            this.$refs.submitForm.validate((valid) => {
                if (valid) {
                    sessionStorage.clear();
                    axios.post(this.$baseUrl + '/api/login', {
                        name: this.user.name,
                        password: this.user.password,
                        code: this.user.code
                    })
                        // response是登录成功后存储的信息
                        .then(response => {
                            const { id, usertype } = response.data;
                            if (id && usertype) {
                                sessionStorage.setItem('username', this.user.name);
                                sessionStorage.setItem('userId', id);
                                sessionStorage.setItem('usertype', usertype);  // 保存用户类型
                                // 检查是否为管理员
                                if (usertype === '管理员') {
                                    // this.$message.success('登录成功，管理员用户');
                                    this.$router.push('/');
                                } else {
                                    // this.$message.success('登录成功，普通用户');
                                    this.$router.push('/');
                                }
                            } else {
                                this.$message.error('登录失败，未获取到用户 ID 或用户类型');
                            }
                        })
                        .catch(error => {
                            const errorMessage = error.response && error.response.data
                                ? error.response.data.message
                                : '登录失败';
                            this.$message.error('登录失败：' + errorMessage);
                        });
                } else {
                    this.checkName();
                    this.checkPassword();
                    this.checkCode();
                    this.$message.error('请填写完整信息');
                }
            });
        },
        goToRegister() {
            this.$router.push('/register');
        },
        checkUserRole() {
            const username = sessionStorage.getItem('username');
            return axios.get(this.$baseUrl + '/api/user/type', { params: { username } })
                .then(response => response.data)
                .catch(error => {
                    console.error('获取用户角色失败:', error);
                    throw error;
                });
        }
    }
};
</script>

<style scoped>
.responsive-dialog {
    max-width: 100%;
    width: 1400px;
    height: auto;
    border-radius: 15px;
}

.input-field {
    width: 100%;
    margin-bottom: 10px;
}

.code-input {
    display: flex;
    align-items: center;
    width: 100%;
}

.flex-input {
    flex: 1;
    margin-right: 20px;
}

.send-button,
.confirm-button,
.dialog-cancel{
    transition: background-color 0.3s, transform 0.3s;
    border-radius: 10px;
    padding: 10px 15px;
}

.send-button {
    background-color: rgb(101,172,140);
    border-color: rgb(101,172,140);
    color: white;
}

.send-button:hover {
    background-color: rgb(85, 145, 118);
    border-color: rgb(85, 145, 118);
}

.send-button:active {
    transform: scale(0.95);
}

.confirm-button {
    background-color: rgb(101,172,140);
    color: white;
}

.confirm-button:hover {
    background-color: rgb(85, 145, 118);
    border-color: rgb(85, 145, 118);
}

.confirm-button:active {
    transform: scale(0.95);
}
.dialog-cancel{
    background-color: rgb(101,172,140);
    border-color: rgb(101,172,140);
    color: white;
}
.dialog-cancel :hover{
    background-color: rgb(85, 145, 118);
    border-color: rgb(85, 145, 118);
}
@media (max-width: 520px) {
    .responsive-dialog {
        width: 100%;
        height: auto;
    }
    .code-input {
        flex-direction: column;
    }
    .flex-input {
        margin-right: 0;
        margin-bottom: 10px;
    }
    .send-button {
        width: 100%;
    }
}

.page-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    background-image: url("@/assets/images/login.jpg");
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    background-attachment: fixed;
    height: 100%;
    width: 100vw;
    position: relative;
    z-index: 1;
}

@media (min-width: 992px) {
    .page-container {
        background-size: cover;
        height: 100%;
    }
}

@media (max-width: 992px) and (min-width: 768px) {
    .page-container {
        background-size: cover;
        padding: 20px;
        height: 100%;
    }
}

@media (max-width: 768px) and (min-width: 480px) {
    .page-container {
        background-size: cover;
        padding: 15px;
        height: 100%;
    }
}

@media (max-width: 480px) {
    .page-container {
        background-size: contain;
        padding: 10px;
        height: 100%;
    }
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

@media (max-width: 992px) and (min-width: 768px) {
    .welcome-message {
        font-size: 24px;
        margin: 18px 0;
    }
    .welcome-message::after {
        width: 80%;
    }
}

@media (max-width: 768px) and (min-width: 480px) {
    .welcome-message {
        font-size: 22px;
        margin: 16px 0;
    }
    .welcome-message::after {
        width: 70%;
    }
}

@media (max-width: 480px) {
    .welcome-message {
        font-size: 20px;
        margin: 14px 0;
    }
    .welcome-message::after {
        width: 60%;
    }
}

.login-card {
    display: flex;
    flex-direction: column;
    width: 30%;
    border-radius: 5px;
    /*background-color: #A9A9A9;*/
    min-height: 300px;
    max-height: 450px;
    padding: 200px;
    margin-top: 20px;
    overflow: hidden;
}

@media (min-width: 1200px) {
    .login-card {
        width: 30%;
    }
}

@media (max-width: 1200px) and (min-width: 992px) {
    .login-card {
        width: 40%;
        padding: 80px;
    }
}

@media (max-width: 992px) and (min-width: 768px) {
    .login-card {
        width: 50%;
        padding: 100px;
    }
}

@media (max-width: 768px) {
    .login-card {
        width: 80%;
        padding: 100px;
    }
}

.form-title {
    font-size: 24px;
    font-weight: bold;
    margin-bottom: 20px;
    color: #67C23A;
}

.code-container {
    display: flex;
    align-items: center;
}


.custom-login-button {
    background-color: rgb(101, 172, 140);
    border-color: rgb(101, 172, 140);
    color: #fff;
    transition: background-color 0.3s, border-color 0.3s, color 0.3s;
    width: 100%;
}

.custom-login-button:hover {
    background-color: rgb(85, 145, 118);
    border-color: rgb(85, 145, 118);
}

.custom-login-button:active {
    background-color: rgb(75, 130, 106);
    border-color: rgb(75, 130, 106);
}

.link {
    color: #67C23A;
    cursor: pointer;
}

.n {
    display: flex;
    justify-content: space-between;
    width: 100%;
}

@keyframes shake {
    0% { transform: translateX(0); }
    20% { transform: translateX(-10px); }
    40% { transform: translateX(10px); }
    60% { transform: translateX(-10px); }
    80% { transform: translateX(10px); }
    100% { transform: translateX(0); }
}

.shake {
    animation: shake 0.82s cubic-bezier(.36,.07,.19,.97) both;
    transform: translate3d(0, 0, 0);
    backface-visibility: hidden;
    perspective: 1000px;
}
</style>

