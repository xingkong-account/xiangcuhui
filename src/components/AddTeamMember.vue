<template>
    <div class="add-team-member" v-if="isAdmin">
        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <el-button type="default" @click="goBack" style="margin-right: 20px;">
                    <i class="el-icon-arrow-left"></i> 返回
                </el-button>
                <span>添加团队会员</span>
            </div>
            <el-form :model="teamMember" :rules="rules" ref="teamMemberForm" label-width="100px">
                <el-form-item label="名称" prop="name">
                    <el-input v-model="teamMember.name"></el-input>
                </el-form-item>
                <el-form-item label="电话" prop="phone">
                    <el-input v-model="teamMember.phone"></el-input>
                </el-form-item>
                <el-form-item label="简介" prop="description">
                    <el-input type="textarea" v-model="teamMember.description"></el-input>
                </el-form-item>
                <el-form-item label="官网" prop="website">
                    <el-input v-model="teamMember.website"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input type="password" v-model="teamMember.password"></el-input>
                </el-form-item>
                <el-form-item label="上传图片" prop="image_url">
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
                    <el-image v-if="teamMember.image_url" :src="teamMember.image_url" style="width: 100px; margin-top: 10px;"></el-image>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="onSubmit" class="submit-buttton">提交</el-button>
                    <el-button @click="onReset" class="reset-button">重置</el-button>
                </el-form-item>
            </el-form>
        </el-card>
    </div>
    <div class="image-container" v-else>
        <img src="@/assets/images/403.png">
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            teamMember: {
                name: '',
                phone: '',
                description: '',
                website: '',
                password: '',
                image_url: ''
            },
            rules: {
                name: [{ required: true, message: '请输入团队名称', trigger: 'blur' }],
                phone: [
                    { required: true, message: '请输入联系方式', trigger: 'blur' },
                    {
                        pattern: /^(1[3-9]\d{9}$)|^(\d{3,4}-\d{7,8})$/,
                        message: '请输入正确的电话号码，格式为11位手机号或区号-座机号（例：0931-88888888）',
                        trigger: 'blur'
                    }
                ],
                description: [{ required: true, message: '请输入团队简介', trigger: 'blur' }],
                website: [{ required: true, message: '请输入官网地址', trigger: 'blur' }],
                password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
                image_url: [{ required: true, message: '请上传图片', trigger: 'change' }]
            },
            isAdmin: false,
        };
    },
    created() {
        this.checkIfAdmin();
    },
    methods: {
        checkIfAdmin() {
            const usertype = sessionStorage.getItem('usertype');
            this.isAdmin = (usertype === '管理员');
            if (!this.isAdmin) {
                this.$message.warning('您没有权限访问此页面');
                setTimeout(() => {
                }, 3000);
            }
        },
        handleUploadSuccess(response) {
            if (response && response.url) {
                this.teamMember.image_url = response.url;
                this.$message.success('图片上传成功');
            } else {
                this.$message.error('上传失败，请稍后重试');
            }
        },
        handleUploadError() {
            this.$message.error('图片上传失败');
        },
        async onSubmit() {
            this.$refs.teamMemberForm.validate(async (valid) => {
                if (valid) {
                    try {
                        await axios.post(this.$baseUrl + '/api/add-team', this.teamMember);
                        this.$message.success('团队会员添加成功');
                        this.onReset();
                    } catch (error) {
                        this.$message.error('添加失败');
                    }
                }
            });
        },
        onReset() {
            this.$refs.teamMemberForm.resetFields();
            this.teamMember.image_url = '';
        },
        goBack(){
            this.$router.go(-1);
        }
    }
};
</script>

<style scoped>
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
.add-team-member {
    padding: 20px;
    max-width: 100%;
    margin: 0 auto;
}
.upload-button{
    margin-bottom: 20px;
}
.no-access {
    text-align: center;
    margin: 20px 0;
}

.submit-buttton {
    background-color: rgb(85,156,122);
    border-color: rgb(85,156,122);
}

.reset-button {
    background-color: rgb(85,156,122);
    border-color: yellow;
    text-decoration:none ;
    color: white;
}

.el-button:hover.submit-buttton {
    background-color: #45a049;
    border-color: #45a049;
}

.el-button:hover.reset-button {
    background-color: #45a049;
    border-color: #45a049;
}
</style>
