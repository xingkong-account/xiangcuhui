<template>
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
                    <el-form-item label="手机号" prop="phone">
                        <el-input v-model="form.phone" maxlength="11"></el-input>
                        <el-button @click="sendSmsCode" :disabled="!form.phone">发送验证码</el-button>
                    </el-form-item>
                </div>
                <el-form-item label="手机验证码 (可选)" prop="phoneCode">
                    <el-input v-model="form.phoneCode" placeholder="请输入手机验证码"></el-input>
                </el-form-item>
                <div :class="{'apply-shake': true, 'shake': shake}">
                    <el-form-item label="验证码" prop="code">
                        <el-input v-model="form.code" placeholder="请输入验证码"></el-input>
                        <valid-code @update:value="getCode" ref="validCode"></valid-code>
                    </el-form-item>
                </div>
                <el-form-item label="用户类型" prop="type">
                    <el-radio-group v-model="form.type">
                        <el-radio label="个人会员">个人会员</el-radio>
                        <el-radio label="团体会员">团体会员</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm">注册</el-button>
                    <el-button @click="goToLogin">登录</el-button>
                </el-form-item>
            </el-form>
        </el-main>
    </el-container>
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
                    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
                ],
                code: [
                    { required: true, message: '请输入验证码', trigger: 'blur' },
                    { validator: this.validateCode, trigger: 'blur' }
                ]
            }
        };
    },
    methods: {
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
                    axios.post(this.$baseUrl +'/api/add', {
                        name: this.form.name,
                        password: this.form.password,
                        phone: this.form.phone,
                        code: this.form.code,
                        type: this.form.type
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
.el-form {
    max-width: 400px;
    margin: 0 auto;
}

.m {
    background-image: url("@/assets/images/register.jpg");
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    height: 100vh;
    padding: 50px;
    align-content: center;
    justify-content: center;
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
</style>


