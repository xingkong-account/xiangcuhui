<template>
    <div class="page-container">
        <div class="welcome-message">
            欢迎登录
        </div>
        <div class="login-card">
            <div class="form-container" :class="{'shake': shake}">
                <el-form :model="user" :rules="rules" ref="submitForm" class="form">
                    <el-form-item prop="name">
                        <el-input prefix-icon="el-icon-user" size="medium" placeholder="请输入账号" v-model="user.name"></el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input prefix-icon="el-icon-lock" size="medium" type="password" show-password placeholder="请输入密码" v-model="user.password"></el-input>
                    </el-form-item>
                    <el-form-item prop="code">
                        <div class="code-container">
                            <el-input size="medium" maxlength="4" placeholder="请输入验证码" v-model="user.code" prefix-icon="el-icon-circle-check" style="flex: 1"></el-input>
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
                            <span class="link">忘记密码</span>
                        </div>
                    </div>
                </el-form>
            </div>
        </div>
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
                // 刷新验证码
                this.$refs.validCode.refreshCode();
            } else {
                callback();
            }
        };
        return {
            user: {
                name: '',
                password: '',
                code: ''
            },
            verifyCode: '',  // 验证码组件传过来的 code
            shake: false,
            rules: {
                name: [
                    { required: true, message: '请输入用户名', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' }
                ],
                code: [
                    { validator: validateCode, trigger: 'blur' }
                ]
            }
        };
    },
    methods: {
        getCode(code) {
            this.verifyCode = code.toLowerCase()
        },
        submitForm() {
            this.$refs.submitForm.validate((valid) => {
                if (valid) {
                    axios.post(this.$baseUrl + '/api/login', {
                        name: this.user.name,
                        password: this.user.password,
                        code: this.user.code
                    })
                        .then(response => {
                            const { id } = response.data;
                            if (id) {
                                sessionStorage.setItem('username', this.user.name);
                                sessionStorage.setItem('userId', id);
                                // 检查用户角色
                                this.checkUserRole()
                                    .then(isAdmin => {
                                        if (isAdmin) {
                                            this.$message.success('登录成功，管理员用户');
                                            this.$router.push('/');
                                        } else {
                                            this.$message.success('登录成功：普通用户');
                                            this.$router.push('/');
                                        }
                                    })
                                    .catch(error => {
                                        console.error('检查用户角色失败:', error);
                                        this.$message.error('登录成功，但角色检查失败');
                                    });
                            } else {
                                this.$message.error('登录失败，未获取到用户 ID');
                            }
                        })
                        .catch(error => {
                            const errorMessage = error.response && error.response.data
                                ? error.response.data.message
                                : '登录失败';
                            this.$message.error('登录失败：' + errorMessage);
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
        goToRegister() {
            this.$router.push('/register');
        },
        checkUserRole() {
            const username = sessionStorage.getItem('username');
            return axios.get(this.$baseUrl + '/api/user/role', { params: { username } })
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
/* 页面容器样式 */
.page-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    background-image: url("@/assets/images/login.jpg");
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    background-attachment: fixed;
    height: 100vh;
    width: 100vw;
    overflow: hidden;
}

/* 欢迎登录消息的样式 */
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
    background-color: rgb(156,201,180); /* 下划线颜色 */
    margin: 8px auto 0;
}

/* 登录卡片样式 */
.login-card {
    display: flex;
    flex-direction: column;
    width: 30%; /* 控制登录卡片的宽度 */
    border-radius: 5px;
    overflow: hidden;
    padding: 200px;
    margin-top: 20px;
}

/* 表单标题 */
.form-title {
    font-size: 24px;
    font-weight: bold;
    margin-bottom: 20px;
    color: #67C23A; /* Green color */
}

/* 代码输入框容器 */
.code-container {
    display: flex;
    align-items: center;
}

/* 登录按钮 */
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

/* 链接样式 */
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

