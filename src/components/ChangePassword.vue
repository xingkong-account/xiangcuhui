<template>
    <el-container>
        <el-main>
            <div class="form-container">
                <el-form
                    :model="form"
                    ref="form"
                    label-width="120px"
                    class="change-password-form"
                    :rules="rules"
                >
                    <el-form-item label="旧密码" prop="oldPassword">
                        <el-input
                            type="password"
                            v-model="form.oldPassword"
                            auto-complete="off"
                            placeholder="请输入旧密码"
                            prefix-icon="el-icon-lock"
                        />
                    </el-form-item>
                    <el-form-item label="新密码" prop="newPassword">
                        <el-input
                            type="password"
                            v-model="form.newPassword"
                            auto-complete="off"
                            placeholder="请输入新密码"
                            prefix-icon="el-icon-lock"
                        />
                    </el-form-item>
                    <el-form-item label="确认密码" prop="confirmPassword">
                        <el-input
                            type="password"
                            v-model="form.confirmPassword"
                            auto-complete="off"
                            placeholder="请确认新密码"
                            prefix-icon="el-icon-lock"
                        />
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="submitForm">提交</el-button>
                        <el-button type="default" @click="cancel">取消</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </el-main>
    </el-container>
</template>


<script>
import axios from 'axios';

export default {
    data() {
        return {
            form: {
                oldPassword: '',
                newPassword: '',
                confirmPassword: ''
            },
            username: this.$route.query.username,
            rules: {
                oldPassword: [
                    { required: true, message: '请输入旧密码', trigger: 'blur' }
                ],
                newPassword: [
                    { required: true, message: '请输入新密码', trigger: 'blur' }
                ],
                confirmPassword: [
                    { required: true, message: '请确认新密码', trigger: 'blur' },
                    { validator: this.validateConfirmPassword, trigger: 'blur' }
                ]
            }
        };
    },
    methods: {
        validateConfirmPassword(rule, value, callback) {
            if (value !== this.form.newPassword) {
                callback(new Error('新密码与确认密码不匹配'));
            } else {
                callback();
            }
        },
        submitForm() {
            this.$refs.form.validate(valid => {
                if (valid) {
                    axios.post(this.$baseUrl + '/api/change-password', {
                        oldPassword: this.form.oldPassword,
                        newPassword: this.form.newPassword,
                        confirmPassword: this.form.confirmPassword
                    }, {
                        withCredentials: true,
                        headers: {
                            'Content-Type': 'application/json',
                            'username': this.username
                        }
                    })
                        .then(() => {
                            this.$message.success('密码修改成功');
                            this.$router.push('/');
                        })
                        .catch(error => {
                            this.$message.error('密码修改失败: ' + (error.response?.data?.message || error.message));
                        });
                }
            });
        },
        cancel() {
            this.$router.push('/');
        }
    }
};
</script>

<style scoped>
.el-button--primary {
    background-color: #4CAF50;
    border-color: #4CAF50;
    color: #fff;
    transition: background-color 0.3s, transform 0.2s;
}

.el-button--primary:hover {
    background-color: #45A049;
    border-color: #45A049;
}

.el-button--primary:active {
    transform: scale(0.95);
}

.el-button--default {
    transition: background-color 0.3s, transform 0.2s;
}

.el-button--default:hover {
    background-color: #f0f0f0;
}

.el-button--default:active {
    transform: scale(0.95);
}

.form-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 80vh;
    width: 100%;
    max-width: 800px;
    margin: 0 auto;
    padding: 40px;
}

.change-password-form {
    width: 100%;
    max-width: 600px;
    background-color: #fff;
    padding: 50px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
}

@media (max-width: 1200px) {
    .change-password-form {
        padding: 30px;
    }
}

@media (max-width: 992px) {
    .form-container {
        padding: 20px;
    }
    .change-password-form {
        padding: 25px;
    }
}

@media (max-width: 768px) {
    .form-container {
        padding: 10px;
    }
    .change-password-form {
        font-size: 14px;
        padding: 15px;
    }
    .el-form-item {
        label-width: 80px;
    }
}

</style>

