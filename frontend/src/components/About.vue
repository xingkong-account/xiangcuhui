<template>
    <div class="about-us">
        <h1>关于我们</h1>
        <div class="title-decoration">
            <hr />
            <div class="dots">
                <span class="dot"></span>
                <span class="dot"></span>
                <span class="dot"></span>
            </div>
        </div>
        <img src="@/assets/images/icon.jpg" alt="甘肃乡村发展促进会" class="banner-image"/>
        <p>
            甘肃乡村发展促进会是由甘肃省农业农村厅主管、在甘肃省民政厅登记注册的社会团体组织，致力于聚合社会各界资源，搭建多方沟通平台，助推农村新型集体经济和各项社会事业，促进甘肃乡村全面振兴和城乡融合发展，为中国式现代化贡献智慧与力量！
        </p>
        <h2>我们的服务</h2>
        <ul>
            <li>
                <h3>会议会务服务</h3>
                    <img src="@/assets/images/meeting.png"
                         alt="会议会务服务"
                         class="service-image"/>
                <p>定期主办或协助有关单位举办政府、高校、企业等各界各类交流会，促进农业农村现代化建设经验的总结共享，为甘肃乡村全面振兴提供顶层设计。</p>
            </li>
            <li>
                <h3>智库咨询服务</h3>
                <img src="@/assets/images/consult.png"
                     alt="智库咨询服务" class="service-image"/>
                <p>围绕农业农村现代化建设高端智库，为地方政府和各个乡村、企事业单位等提供精准咨询服务，因地制宜谋划切实可行的发展方案。</p>
            </li>
            <li>
                <h3>公益活动服务</h3>
<!--                <img src="" alt="公益活动服务" class="service-image"/>-->
                <p>着眼甘肃基层党组织建设、村集体经济发展、产业体系构建、社会服务事业等急难险重任务，策划创业大赛、社会实践、志愿服务、移风易俗、科教文卫等各类公益活动，组织动员各方力量助力乡村振兴。</p>
            </li>
            <li>
                <h3>产业孵化服务</h3>
<!--                <img src="" alt="产业孵化服务" class="service-image"/>-->
                <p>通过组织甘肃青年返乡创业相关赛事，遴选经济和社会效益俱佳的优秀产业项目，协调政府资源和社会资本给予支持，全方位全过程提供咨询服务。</p>
            </li>
            <li>
                <h3>集体经济服务</h3>
<!--                <img src="" alt="集体经济服务" class="service-image"/>-->
                <p>依托各界力量，提供集体经济组织体系建构、运营管理指导、产业发展规划、村级资源整合、市场渠道拓展、公私股权合作等各方面服务，按照一村一策的原则，协助基层党组织引领新型农村集体经济发展壮大。</p>
            </li>
            <li>
                <h3>农民教育服务</h3>
<!--                <img src="" alt="农民教育服务" class="service-image"/>-->
                <p>开办新时代“农民讲习所”，宣传党和国家的方针政策，引导青年返乡创业，培训农民积极分子，普及现代科学技术知识，培育农民集体意识、创新意识、共富意识。</p>
            </li>
        </ul>
    </div>
</template>

<script>
export default {
    name: 'AboutUs',
}
</script>

<style scoped>
.title-decoration {
    display: flex;
    align-items: center;
    margin-top: 10px;
}

hr {
    flex-grow: 1;
    border: none;
    height: 2px;
    background: #4CAF50;
    margin: 0 10px;
}

.dots {
    display: flex;
    gap: 5px;
}

.dot {
    width: 10px;
    height: 10px;
    border-radius: 50%;
    background: linear-gradient(135deg, #6fbf6f, #4caf50);
}

.about-us {
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
    font-family: Arial, sans-serif;
    line-height: 1.6;
}

.banner-image {
    width: 100%;
    height: auto;
    border-radius: 10px;
    margin-bottom: 20px;
}

h1 {
    text-align: start;
    color: #4CAF50;
    margin-bottom: 20px;
}

h2 {
    color: #333;
    margin-top: 30px;
    margin-bottom: 15px;
}

ul {
    list-style-type: none;
    padding: 0;
}

li {
    background: #f9f9f9;
    margin-bottom: 15px;
    border: 1px solid #ddd;
    border-radius: 5px;
    padding: 15px;
    transition: background 0.3s;
}

li:hover {
    background: #f1f1f1;
}

h3 {
    color: #4CAF50;
    margin: 0 0 10px;
}

.service-image {
    width: 100%;
    height: auto;
    border-radius: 5px;
    margin-bottom: 10px;
}
</style>

