<template>
    <el-container class="layout-wrapper" v-if="!isMobile">
        <!-- 左侧导航栏 -->
        <el-aside width="240px" class="sidebar">
            <el-menu
                :default-active="activeMenu"
                class="el-menu-vertical"
                background-color="#1d1f22"
                text-color="#fff"
                active-text-color="#67c23a"
                @select="handleMenuSelect"
            >
                <el-menu-item index="default">
                    <i class="el-icon-user"></i>
                    <span slot="title">团体会员管理</span>
                </el-menu-item>
                <el-submenu index="2">
                    <template slot="title">
                        <i class="el-icon-plus"></i>
                        <span>新增团体会员</span>
                    </template>
                    <el-menu-item index="2-1">团体会员</el-menu-item>
                </el-submenu>
                <el-submenu index="3">
                    <template slot="title">
                        <i class="el-icon-document"></i>
                        <span>团体会员审核</span>
                    </template>
                    <el-menu-item index="3-1">待审核</el-menu-item>
                    <el-menu-item index="3-2">已审核</el-menu-item>
                </el-submenu>
                <el-menu-item index="4">
                    <i class="el-icon-message"></i>
                    <span slot="title">消息中心</span>
                </el-menu-item>
            </el-menu>
        </el-aside>

        <!-- 右侧内容 -->
        <el-main>
            <div class="content-wrapper">
                <!-- 搜索-->
                <div style="display: flex; width: 100%; background-color: rgb(227,230,235); justify-content: space-between; align-items: center;" v-if="this.showSearchSection">
                    <div style="display: flex; align-items: center">
                        <h2 style="margin: 5px 20px 10px 10px">团体会员管理中心</h2>
                        <el-select v-model="searchType" placeholder="选择搜索条件" style="margin-right: 10px;">
                            <el-option label="姓名" value="name"></el-option>
                            <el-option label="电话" value="phone"></el-option>
                            <el-option label="状态" value="status"></el-option>
                        </el-select>
                        <el-input
                            v-model="searchQuery"
                            placeholder="输入搜索内容"
                            prefix-icon="el-icon-search"
                            style="width: 200px; border-radius: 8px; height: 32px;"
                        ></el-input>
                        <el-button type="primary" @click="handleSearch" style="margin-top: 10px; margin-left: 10px;">
                            搜索
                        </el-button>
                    </div>
                </div>
                <!--右侧显示的内容-->
                <add-team-member v-if="activeMenu === '2-1'" />
                <review-team-members v-if="activeMenu === '3-1'" />
                <checked-team v-if="activeMenu === '3-2'" />
                <el-card v-if="activeMenu === 'default'" class="box-card">
                    <!-- 表格内容 -->
                    <div class="table-header">
                        <el-checkbox v-model="selectAll" @change="handleSelectAll">全选</el-checkbox>
                        <el-button type="danger" @click="confirmDeleteSelected" :disabled="selectedMembers.length === 0 || !isAdmin">
                            删除选中
                        </el-button>
                    </div>
                    <el-table
                        :data="members"
                        stripe
                        style="width: 100%"
                        @selection-change="handleSelectionChange"
                        ref="table"
                    >
                        <el-table-column type="selection" width="55"></el-table-column>
                        <el-table-column prop="id" label="ID" width="80"></el-table-column>
                        <el-table-column prop="name" label="团体名称" width="120"></el-table-column>
                        <el-table-column prop="image_url" label="团体图片" width="120">
                            <template v-slot="scope">
                                <div class="image-container">
                                    <img :src="scope.row.image_url" alt="团体图片" class="team-image">
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column prop="phone" label="联系方式" width="180"></el-table-column>
                        <el-table-column prop="status" label="状态" width="100"></el-table-column>
                        <el-table-column prop="created_at" label="创建时间" width="180">
                            <template #default="{ row }">
                                {{ formatDate(row.created_at) }}
                            </template>
                        </el-table-column>
                        <el-table-column prop="updated_at" label="修改时间" width="180">
                            <template #default="{ row }">
                                {{ formatDate(row.updated_at) }}
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" width="180">
                            <template slot-scope="scope">
                                <el-button
                                    size="mini"
                                    type="text"
                                    class="edit-btn"
                                    :disabled="!canEdit(scope.row)"
                                    @click="openEditDialog(scope.row)"
                                >编辑
                                </el-button>
                                <el-button
                                    size="mini"
                                    type="text"
                                    class="delete-btn"
                                    :disabled="!isAdmin"
                                    @click="deleteMember(scope.row.id)"
                                >删除
                                </el-button>
                            </template>

                        </el-table-column>
                    </el-table>
                </el-card>
                <div v-if="activeMenu === 'default'" class="pagination">
                    <el-pagination
                        @size-change="handlePageSizeChange"
                        @current-change="handlePageChange"
                        :current-page="currentPage"
                        :page-size="pageSize"
                        :total="total"
                        layout="total, sizes, prev, pager, next, jumper"
                        class="pagination"
                    ></el-pagination>
                </div>
                <!-- 编辑团队会员的弹窗 -->
                <el-dialog
                    title="编辑团体会员"
                    :visible.sync="dialogVisible"
                    width="50%"
                    @close="resetDialog"
                >
                    <el-form :model="currentMember" ref="editForm" label-width="100px" class="edit-form">
                        <el-form-item label="团体名称">
                            <el-input v-model="currentMember.name" placeholder="请输入团体名称"></el-input>
                        </el-form-item>
                        <el-form-item label="联系方式">
                            <el-input v-model="currentMember.phone" placeholder="请输入联系方式"></el-input>
                        </el-form-item>
                        <el-form-item label="邮箱">
                            <el-input v-model="currentMember.email" placeholder="请输入邮箱地址"></el-input>
                        </el-form-item>
                        <el-form-item label="简介">
                            <el-input type="textarea" v-model="currentMember.description" placeholder="请输入团队简介"></el-input>
                        </el-form-item>
                        <el-form-item label="官网">
                            <el-input v-model="currentMember.website" placeholder="请输入团队官网地址"></el-input>
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
                            <el-image v-if="currentMember.image_url" :src="currentMember.image_url" style="width: 100px; margin-top: 10px;"></el-image>
                        </el-form-item>
                        <el-form-item label="状态" v-if="isAdmin">
                            <el-select v-model="currentMember.status" placeholder="请选择状态">
                                <el-option label="待审核" value="待审核"></el-option>
                                <el-option label="已审核" value="已审核"></el-option>
                                <el-option label="已拒绝" value="已拒绝"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="submitEditForm">提交</el-button>
                            <el-button @click="resetDialog">取消</el-button>
                        </el-form-item>
                    </el-form>
                </el-dialog>
            </div>
        </el-main>
    </el-container>
    <el-container class="mobile-layout-wrapper" v-else>
        <!-- 顶部导航 -->
        <header class="mobile-header">
            <h1>团体会员管理</h1>
            <el-button class="back-button" @click="goBack">返回</el-button>
        </header>
        <nav class="mobile-menu">
            <el-menu
                :default-active="activeMenu"
                class="el-menu-vertical"
                background-color="#fff"
                text-color="#000"
                active-text-color="#67c23a"
                @select="handleMenuSelect"
            >
                <el-menu-item index="default">
                    <i class="el-icon-user"></i>
                    <span>团体会员管理</span>
                </el-menu-item>
                <el-submenu index="2">
                    <template #title>
                        <i class="el-icon-plus"></i>
                        <span>新增团体会员</span>
                    </template>
                    <el-menu-item index="2-1">团体会员</el-menu-item>
                </el-submenu>
                <el-submenu index="3">
                    <template #title>
                        <i class="el-icon-document"></i>
                        <span>团体会员审核</span>
                    </template>
                    <el-menu-item index="3-1">待审核</el-menu-item>
                    <el-menu-item index="3-2">已审核</el-menu-item>
                </el-submenu>
                <el-menu-item index="4">
                    <i class="el-icon-message"></i>
                    <span>消息中心</span>
                </el-menu-item>
            </el-menu>
        </nav>

        <!-- 搜索区域 -->
        <div class="mobile-search-section">
            <el-select v-model="searchType" placeholder="搜索条件">
                <el-option label="团体名称" value="name"></el-option>
                <el-option label="电话" value="phone"></el-option>
                <el-option label="状态" value="status"></el-option>
            </el-select>
            <el-input
                v-model="searchQuery"
                placeholder="搜索内容"
                prefix-icon="el-icon-search"
                class="mobile-search-input"
            ></el-input>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
        </div>

        <add-team-member v-if="activeMenu === '2-1'" />
        <review-team-members v-if="activeMenu === '3-1'" />
        <checked-team v-if="activeMenu === '3-2'" />

        <!-- 团体会员列表 -->
        <div class="">
            <el-card v-for="member in members" :key="member.id" class="mobile-member-card">
                <div class="member-info">
                    <p>团体名称: {{ member.name }}</p>
                    <p>电话: {{ member.phone }}</p>
                    <p>状态: {{ member.status }}</p>
                </div>
                <div class="member-actions">
                    <el-button @click="openEditDialog(member)">编辑</el-button>
                    <el-button type="danger" @click="deleteMember(member.id)">删除</el-button>
                </div>
            </el-card>
        </div>

        <!-- 分页 -->
        <div class="mobile-pagination">
            <el-pagination
                @size-change="handlePageSizeChange"
                @current-change="handlePageChange"
                :current-page="currentPage"
                :page-size="pageSize"
                :total="total"
                layout="total, sizes, prev, pager, next, jumper"
            ></el-pagination>
        </div>

        <!-- 编辑团体会员对话框 -->
        <el-dialog title="编辑团体会员" :visible.sync="dialogVisible" width="90%">
            <el-form :model="currentMember" ref="editForm" label-width="80px">
                <el-form-item label="团体名称">
                    <el-input v-model="currentMember.name" placeholder="请输入团体名称"></el-input>
                </el-form-item>
                <el-form-item label="电话">
                    <el-input v-model="currentMember.phone" placeholder="请输入电话"></el-input>
                </el-form-item>
                <el-form-item label="状态">
                    <el-select v-model="currentMember.status" placeholder="请选择状态">
                        <el-option label="待审核" value="待审核"></el-option>
                        <el-option label="已审核" value="已审核"></el-option>
                        <el-option label="已拒绝" value="已拒绝"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitEditForm">提交</el-button>
                    <el-button @click="resetDialog">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </el-container>
</template>

<script>
import axios from 'axios';
import AddTeamMember from "@/components/AddTeamMember.vue";
import CheckedTeam from "@/components/CheckedTeam.vue";
import ReviewTeamMembers from "@/components/ReviewTeamMembers.vue";

export default {
    components: {
        AddTeamMember,
        CheckedTeam,
        ReviewTeamMembers
    },
    data() {
        return {
            members: [],
            isMobile: false,
            showSidebar: false,
            dialogVisible: false,
            // 当前编辑的会员信息
            currentMember: {
                id: null,
                name: '',
                phone: '',
                status: '待审核',
                description: '',
                email: '',
                website: '',
                image_url: ''
            },
            showSearchSection: false, // 控制搜索部分的显示
            searchQuery: '', // 搜索框输入的内容
            searchType: '',  // 选择的查询类型
            select: '',  // 选择的搜索条件
            isAdmin: false,
            currentUserId: '',
            activeMenu: "default",
            total: 0,
            currentPage: 1,
            pageSize: 10,
            selectedMembers: [],
            selectAll: false,
        };
    },
    methods: {
        toggleSidebar() {
            this.showSidebar = !this.showSidebar;
        },
        checkIfMobile() {
            this.isMobile = window.innerWidth < 700;
        },
        handleUploadSuccess(response) {
            if (response && response.url) {
                this.currentMember.image_url = response.url;
                this.$message.success('图片上传成功');
            } else {
                this.$message.error('上传失败，请稍后重试');
            }
        },
        handleUploadError() {
            this.$message.error('图片上传失败');
        },
        async openEditDialog(member) {
            try {
                // 当前编辑的会员数据
                this.currentMember = { ...member };
                this.dialogVisible = true;
            } catch (error) {
                console.error('打开编辑对话框失败:', error);
                this.$message.error('无法打开编辑对话框');
            }
        },
        async submitEditForm() {
            try {
                await axios.post(this.$baseUrl + `/api/update-team/${this.currentMember.id}`, this.currentMember);
                this.$message.success('团体会员信息更新成功');
                this.dialogVisible = false;
                await this.fetchMembers();
            } catch (error) {
                console.error('团体会员信息更新失败:', error);
                this.$message.error('团体会员信息更新失败');
            }
        },
        async handleSearch() {
            if (!this.searchType) {
                this.$message.error('请选择搜索条件');
                return;
            }
            try {
                const response = await axios.get(this.$baseUrl + '/api/all-teams', {
                    params: {
                        query: this.searchQuery,
                        select: this.searchType
                    }
                });
                this.members = response.data;
                if  (this.members.length === 0) {
                    this.$message.info("搜索内容为空")
                }
            } catch (error) {
                console.error('获取数据失败:', error);
                this.$message.error('无法获取数据');
            }
        },
        resetDialog() {
            this.currentMember = {};
            this.dialogVisible = false;
        },
        async deleteMember(id) {
            this.$confirm('确定要删除这个会员吗?', '警告', {
                confirmButtonText: '删除',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                axios.delete(this.$baseUrl + `/api/delete-single-team/${id}`)
                    .then(() => {
                        this.$message.success('删除成功');
                        this.fetchMembers();
                    })
                    .catch(error => {
                        this.$message.error('删除失败: ' + error.message);
                    });
            }).catch(() => {});
        },
        handlePageChange(page) {
            this.currentPage = page;
            this.fetchMembers();
        },
        handlePageSizeChange(size){
            this.pageSize = size;
            this.currentPage = 1;
            this.fetchMembers();
        },
        async fetchMembers() {
            try {
                const response = await axios.get(this.$baseUrl + '/api/team-members', {
                    params: {
                        pageNum: this.currentPage,
                        pageSize: this.pageSize
                    }
                });
                this.members = response.data.data;
                this.total = response.data.total;
                // await this.$alert(this.members);
                if (this.members.length === 0) {
                    this.$message.info('暂无数据');
                }
            } catch (error) {
                console.error('获取团体会员数据失败', error);
            }
        },
        confirmDeleteSelected() {
            this.$confirm('确定要删除选中的会员吗？', '确认删除', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.deleteSelected();
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        handleMenuSelect(index) {
            this.activeMenu = index;
            this.showSearchSection = this.activeMenu === 'default';
        },
        handleSelectionChange(selection) {
            this.selectedMembers = selection;
            this.selectAll = selection.length === this.members.length;
        },
        handleSelectAll() {
            const table = this.$refs.table;
            if (this.selectAll) {
                table.toggleAllSelection();
                this.selectedMembers = this.members;
            } else {
                table.clearSelection();
                this.selectedMembers = [];
            }
        },
        async deleteSelected() {
            const ids = this.selectedMembers.map(member => member.id);
            try {
                await axios.post(this.$baseUrl + '/api/delete-members', ids);
                this.$message.success('已成功删除选中的会员');
                await this.fetchMembers();
                this.selectedMembers = [];
                this.selectAll = false;
            } catch (error) {
                console.error('删除选中会员失败', error);
            }
        },
        navigateToAddPage() {
            this.$router.push('/add-personal');
        },
        editMember(member) {
            this.$router.push({ path: `/edit-personal/${member.id}` });
        },
        checkIfAdmin() {
            const userType = sessionStorage.getItem('usertype');
            this.isAdmin = (userType === '管理员');
        },
        getCurrentUserId() {
            this.currentUserId = sessionStorage.getItem('userId');
        },
        canEdit(member) {
            return this.isAdmin || this.currentUserId === member.id.toString();
        },
        formatDate(dateString) {
            if (!dateString) return '';
            const date = new Date(dateString);
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            const hours = String(date.getHours()).padStart(2, '0');
            const minutes = String(date.getMinutes()).padStart(2, '0');
            const seconds = String(date.getSeconds()).padStart(2, '0');
            return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
        },
        goBack() {
            this.$router.go(-1)
        }
    },
    created() {
        this.fetchMembers();
        this.checkIfAdmin();
        this.getCurrentUserId();
        this.handleMenuSelect('default');
    },
    mounted() {
        this.checkIfMobile();
        window.addEventListener('resize', this.checkIfMobile);
    },
    beforeDestroy() {
        window.removeEventListener('resize', this.checkIfMobile);
    }
};
</script>

<style scoped>
.mobile-layout-wrapper {
    display: flex;
    flex-direction: column;
    height: 100vh;
    background-color: #f9f9f9;
}

.mobile-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 15px;
    background-color: #67c23a;
    color: white;
}

.mobile-menu {
    background-color: #fff;
    border-bottom: 1px solid #e0e0e0;
}

.mobile-search-section {
    display: flex;
    flex-direction: column;
    padding: 10px;
    background-color: #f2f2f2;
}

.mobile-search-select,
.mobile-search-input {
    margin-bottom: 10px;
    width: 100%;
}

.mobile-search-input {
    flex: 1;
}

.mobile-member-list {
    flex: 1;
    padding: 10px;
    overflow-y: auto;
}

.mobile-member-card {
    margin-bottom: 10px;
    padding: 15px;
    background-color: #fff;
    border-radius: 5px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.member-info {
    margin-bottom: 10px;
}

.member-actions {
    display: flex;
    justify-content: space-between;
}

.mobile-pagination {
    padding: 10px;
    background-color: #fff;
}

.back-button {
    background-color: transparent;
    color: white;
    border: none;
    cursor: pointer;
}

.el-dialog {
    max-width: 90%;
}
.mobile-member-list {
    flex: 1;
    padding: 10px;
    overflow-y: auto;
    max-height: calc(100vh - 200px);
}

.image-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
}

.team-image {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    scale: 0.8;
    justify-content: center;
    align-items: center;
    margin-right: 40px;
}

.back-button{
    background-color: rgb(101, 172, 140);
    border-color: rgb(101, 172, 140);
    color: #fff;
    font-size: 18px;
}
.back-button :hover{
    background-color: rgb(85, 145, 118);
    border-color: rgb(85, 145, 118);
}
.back-button:active {
    background-color: rgb(75, 130, 106);
    border-color: rgb(75, 130, 106);
}
.input-with-select {
    background-color: #fff;
    width: 400px;
}
.layout-wrapper {
    display: flex;
    height: 100vh;
}

.sidebar {
    background-color: #1d1f22;
    color: #fff;
}
.box-card {
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 20px;
}

.table-header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
}

.el-table {
    border-radius: 8px;
    overflow: hidden;
}

.edit-btn, .delete-btn {
    color: #67c23a;
    margin-right: 10px;
}

.pagination {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
}

.pagination-bar {
    background-color: transparent;
}
</style>
