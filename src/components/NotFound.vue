<template>
    <el-container class="not-found-container">
        <div class="not-found-content">
            <el-image
                class="not-found-image"
                src="https://www.artzstudio.com/content/images/wordpress/2020/05/404-error-not-found-page-lost.png"
                fit="contain"
            ></el-image>
            <h1>Oops! 404 Not Found</h1>
            <p>页面好像开小差了。</p>
            <el-button type="primary" @click="goHome">返回首页</el-button>
            <el-button type="primary" @click="goBack">返回上一页</el-button>
        </div>
    </el-container>
</template>

<script>
export default {
    methods: {
        goHome() {
            this.$router.push('/');
        },
        goBack() {
            this.$router.go(-1);
        },
    },
};
</script>

<style scoped>
.not-found-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    text-align: center;
    background: linear-gradient(to right, #ece9e6, #ffffff);
}

.not-found-content {
    max-width: 600px;
    padding: 20px;
    background-color: #fff;
    box-shadow: 0px 0px 20px rgba(0, 0, 0, 0.1);
    border-radius: 10px;
    animation: fadeIn 1s ease-in-out;
}

.not-found-image {
    margin-bottom: 20px;
}

h1 {
    font-size: 36px;
    color: #333;
    margin: 20px 0;
}

p {
    font-size: 18px;
    color: #666;
    margin-bottom: 30px;
}

.el-button {
    margin: 5px;
    padding: 10px 20px;
    font-size: 16px;
}

@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(-10px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}
</style>
