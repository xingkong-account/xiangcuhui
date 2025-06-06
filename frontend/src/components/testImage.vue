<template>
    <div class="">
        <el-carousel height="400px" :interval="2000" arrow="always" indicator-position="outside">
            <el-carousel-item v-for="(image, index) in carouselImages" :key="index">
                <router-link :to="'/articles/' + image.articleId">
                    <img :src="image.src" :alt="image.alt" class="carousel-image" />
                </router-link>
            </el-carousel-item>
        </el-carousel>
    </div>
</template>

<script>
export default {
    data() {
        return {
            carouselImages: [
                {
                    src: require('@/assets/images/RuralParty.jpg'),
                    alt: "农村党建",
                    title: "关于农村党建",
                    category: "农村党建"
                },
                {
                    src: require('@/assets/images/TeamEconomy.jpg'),
                    alt: "集体经济",
                    title: "关于集体经济",
                    category: "集体经济"
                },
                {
                    src: require('@/assets/images/industry.jpg'),
                    alt: "产业发展",
                    title: "关于产业发展",
                    category: "产业发展"
                },
                {
                    src: require('@/assets/images/CountrysideCulture.jpg'),
                    alt: "乡土文化",
                    title: "关于乡土文化",
                    category: "乡土文化"
                },
                {
                    src: require('@/assets/images/GreenMountains.jpg'),
                    alt: "青山绿水",
                    title: "关于青山绿水",
                    category: "青山绿水"
                },
                {
                    src: require('@/assets/images/YouthFarmer.jpg'),
                    alt: "青年农人",
                    title: "关于青年农人",
                    category: "青年农人"
                }
            ]
        };
    }
}
</script>

<style scoped>
.carousel-image {
    width: 100%; /* 设置图片宽度为100% */
    height: 100%; /* 高度自适应，保持比例 */
    border-radius: 10px; /* 圆角 */
}
</style>
