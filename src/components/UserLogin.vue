<template>
    <div class="m" style="height: 100vh; display: flex; align-items: center; justify-content: center;">
        <div class="login-card">
            <div class="image-container">
                <img src="@/assets/images/login.png" alt="" style="width: 100%">
            </div>
            <div class="form-container" :class="{'shake': shake}">
                <el-form :model="user" :rules="rules" ref="submitForm" style="width: 80%">
                    <div class="form-title">欢迎登录</div>
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
                        <el-button type="primary" @click="submitForm" style="width: 100%">登录</el-button>
                    </el-form-item>
                    <div class="footer">
                        <div class="register">
                            还没有账号？<span style="color: #67C23A; cursor: pointer" @click="goToRegister">注册</span>
                        </div>
                        <div class="forgot-password">
                            <span style="color: #67C23A; cursor: pointer">忘记密码</span>
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
.m {
    background-image: url("@/assets/images/backgroundImg.png");
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    height: 100vh;
}

.login-card {
    display: flex;
    background-color: white;
    width: 50%;
    border-radius: 5px;
    overflow: hidden;
}

.image-container {
    flex: 1;
}

.form-container {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
}

.form-title {
    font-size: 20px;
    font-weight: bold;
    text-align: center;
    margin-bottom: 20px;
}

.code-container {
    display: flex;
}

.code-image {
    flex: 1;
    height: 36px;
    display: flex;
    align-items: center;
}

.footer {
    display: flex;
}

.register {
    flex: 1;
}

.forgot-password {
    flex: 1;
    text-align: right;
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
