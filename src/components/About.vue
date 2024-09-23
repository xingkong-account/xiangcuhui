<template>
    <div class="about">
        <div class="box-card">
            <div slot="header" class="clearfix">
                <el-button type="default" @click="goBack" style="margin-right: 20px;">
                    <i class="el-icon-arrow-left"></i> 返回
                </el-button>
                <span>关于我们</span>
            </div>
            <div class="text item">
                <img src="https://www.bing.com/th?id=OIP.dktjOy4n2xlTeUd6kZr0pQHaEK&w=200&h=106&c=8&rs=1&qlt=90&o=6&dpr=2&pid=3.1&rm=2"
                     alt="我们的团队"
                     class="team-image"/>
            </div>
            <div class="text item">
                <h2>我们的使命</h2>
                <p>我们致力于提供优质的服务和解决方案，帮助客户实现他们的目标。我们的团队拥有丰富的经验和专业的知识，致力于在技术创新和服务质量方面保持领先地位。</p>
            </div>
            <div class="text item">
                <h2>我们的团队</h2>
                <p>我们拥有一支多元化的团队，包括经验丰富的开发者、设计师和市场营销专家。我们的团队成员都致力于不断学习和成长，以便为客户提供最好的服务。</p>
            </div>
            <div class="text item">
                <h2>公司历史</h2>
                <p>公司成立于XXXX年，经过多年的发展，我们已经成长为行业领先的企业。我们始终保持对技术的热情，并不断追求卓越。</p>
            </div>
            <div class="text item">
                <h2>联系我们</h2>
                <p>如果您有任何问题或建议，请随时联系我们。我们非常乐意听取您的意见，并提供帮助。</p>
                <p><strong>邮箱:</strong> hei564927@gmail.com</p>
                <p><strong>电话:</strong> (123) 456-7890</p>
                <p><strong>地址:</strong> 1234</p>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'AboutUs',
    methods:{
        goBack(){
            this.$router.go(-1);
        }
    }
};
</script>

<style scoped>
.about {
    padding: 20px;
    background-color: #f5f5f5;
    display: flex;
    justify-content: center;
}

.box-card {
    width: 100%;
    margin: 0 auto;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    justify-content: center;
}

.team-image {
    width: 50%;
    height: auto;
    border-radius: 10px;
    margin-left: 25%;
}

.text.item {
    margin-bottom: 20px;
}

h2 {
    margin-top: 20px;
    color: #409EFF;
    font-size: 1.5rem;
}

p {
    line-height: 1.6;
    font-size: 1rem;
}

@media (max-width: 768px) {
    .box-card {
        width: 90%;
        padding: 10px;
    }

    h2 {
        font-size: 1.25rem;
    }

    p {
        font-size: 0.875rem;
    }
}
</style>

