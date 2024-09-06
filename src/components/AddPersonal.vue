<template>
    <div class="add-personal" v-if="isAdmin">
        <el-card class="custom-card">
            <div slot="header" class="card-header">
                <el-button class="custom-back-button" @click="goBack" style="margin-right: 20px;">
                    <i class="el-icon-arrow-left"></i> 返回
                </el-button>
                <span class="header-title">新增个人会员</span>
            </div>
            <el-form :model="form" :rules="rules" ref="form" label-width="100px">
                <el-form-item label="姓名" prop="name">
                    <el-input v-model="form.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="电话" prop="phone">
                    <el-input v-model="form.phone" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input type="password" v-model="form.password" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="状态" prop="status">
                    <el-select v-model="form.status" placeholder="请选择状态">
                        <el-option label="待审核" value="待审核"></el-option>
                        <el-option label="已审核" value="已审核"></el-option>
                        <el-option label="已拒绝" value="已拒绝"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button class="custom-submit-button" type="primary" @click="submitForm">提交</el-button>
                    <el-button class="custom-reset-button" @click="resetForm">重置</el-button>
                </el-form-item>
            </el-form>
        </el-card>
    </div>
    <div class="image-container" v-else>
        <p>您没有权限访问此页面。</p>
        <img src="@/assets/images/403.png">
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            form: {
                name: '',
                phone: '',
                password: '',
                status: '待审核',
            },
            isAdmin: false,
            redirectTimer: null,
            rules: {
                name: [
                    { required: true, message: '请输入姓名', trigger: 'blur' }
                ],
                phone: [
                    { required: true, message: '请输入手机号', trigger: 'blur' },
                    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' }
                ]
            }
        };
    },
    methods: {
        async submitForm() {
            try {
                const valid = await this.$refs.form.validate();
                if (valid) {
                    await axios.post(this.$baseUrl + '/api/add-personal', this.form);
                    this.$message.success('新增成功');
                }
            } catch (error) {
                this.$message.error('新增失败');
                console.error(error);
            }
        },
        resetForm() {
            this.$refs.form.resetFields();
        },
        checkIfAdmin() {
            const username = sessionStorage.getItem('username');
            this.isAdmin = (username === 'admin');
            if (!this.isAdmin) {
                this.$message.warning('您没有权限访问此页面');
            }
        },
        goBack(){
            this.$router.go(-1);
        }
    },
    created() {
        this.checkIfAdmin();
    },
    beforeDestroy() {
        if (this.redirectTimer) {
            clearTimeout(this.redirectTimer);
        }
    }
};
</script>

<style scoped>
.add-personal {
    padding: 20px;
}

.custom-card {
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    padding: 20px;
}

.card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.header-title {
    font-size: 18px;
    font-weight: bold;
    color: #333;
}

.custom-back-button {
    background-color: #409EFF;
    color: #fff;
    border: none;
    border-radius: 4px;
    padding: 8px 16px;
    transition: background-color 0.3s ease;
}

.custom-back-button:hover {
    background-color: #66b1ff;
}

.custom-back-button:active {
    background-color: #3a8ee6;
}

.custom-submit-button, .custom-reset-button {
    border-radius: 4px;
    padding: 10px 20px;
    font-weight: bold;
    transition: background-color 0.3s ease;
}

.custom-submit-button {
    background-color: rgb(101,172,140);
    color: #fff;
    border: none;
}

.custom-submit-button:hover {
    background-color: #85ce61;
}

.custom-submit-button:active {
    background-color: #5daf34;
}

.custom-reset-button {
    background-color: #f5f5f5;
    color: #606266;
    border: none;
    margin-left: 10px;
}

.custom-reset-button:hover {
    background-color: #e5e5e5;
}

.custom-reset-button:active {
    background-color: #d4d4d4;
}

.image-container {
    text-align: center;
    padding: 20px;
    font-size: 16px;
    color: #ff4d4f;
}

.image-container {
    text-align: center;
    margin: 20px 0;
}

.image-container img {
    max-width: 100%;
    height: auto;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.image-container img.full-width {
    width: 100%;
}

.image-container img.thumbnail {
    width: auto;
    max-width: 200px;
    height: auto;
}
</style>
