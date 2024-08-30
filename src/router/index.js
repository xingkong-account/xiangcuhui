import Vue from 'vue';
import Router from 'vue-router';
import Login from "@/components/UserLogin.vue";
import Register from "@/components/UserRegister.vue";
import ArticleList from "@/components/ArticleList.vue";
import AddArticle from "@/components/AddArticle.vue";
import EditArticle from "@/components/EditArticle.vue";
import ArticleDetail from "@/components/ArticleDetail.vue";
import ChangePassword from "@/components/ChangePassword.vue";
import MainPage from "@/components/MainPage.vue";
import NotFound from "@/components/NotFound.vue";
import IndividualMembers from "@/components/IndividualMembers.vue";
import TeamMembers from "@/components/TeamMembers.vue";
import EditPersonal from "@/components/EditPersonal.vue";
import AddPersonal from "@/components/AddPersonal.vue";
import About from "@/components/About.vue";
import AddTeamMember from "@/components/AddTeamMember.vue";
import ReviewTeamMembers from "@/components/ReviewTeamMembers.vue";
import IndividualMemberReview from "@/components/IndividualMemberReview.vue";
import ArticleReview from "@/components/ArticleReview.vue";
import ArticleCategory from "@/components/ArticleCategory.vue";
import VideoPage from "@/components/VideoPage.vue";
import VideoReviewPage from "@/components/VideoReviewPage.vue";
import UploadVideo from "@/components/UploadVideo.vue";
import VideoManage from "@/components/VideoManage.vue";

Vue.use(Router);

export default new Router({
    mode: 'history',
    routes: [
        {
            path: '/login',
            name: 'Login',
            component: Login,
            meta: { requiresAuth: false }
        },
        {
            path: '/change',
            name: 'ChangePassword',
            component: ChangePassword,
            meta: { requiresAuth: false }
        },
        {
            path: '/',
            name: 'MainPage',
            component: MainPage
        },
        {
            path: '/register',
            name: 'Register',
            component: Register
        },
        {
            path: '/articles',
            name: 'ArticleList',
            component: ArticleList
        },
        {
            path: '/articles/create',
            name: 'AddArticle',
            component: AddArticle
        },
        {
            path: '/articles/edit/:id',
            name: 'EditArticle',
            component: EditArticle,
            props: true // 使路由参数作为组件的 props
        },
        {
            path: '/articles/:id',
            name: 'ArticleDetail',
            component: ArticleDetail,
            props: true
        },
        {
            path: '/individual-members',
            name: 'IndividualMembers',
            component: IndividualMembers,
            meta: { requiresAuth: false }
        },
        {
            path: '/team-members',
            name: 'TeamMembers',
            component: TeamMembers,
            meta: { requiresAuth: false }
        },
        {
            path: '*',
            name: 'NotFound',
            component: NotFound
        },
        {
            path: '/edit-personal/:id', // :id 是路由参数
            name: 'EditPersonal',
            component: EditPersonal
        },
        {
            path: '/add-personal',
            name: 'AddPersonal',
            component: AddPersonal
        },
        {
            path: '/about',
            name: 'About',
            component: About
        },
        {
            path: '/add-team-member',
            name: 'add-team-member',
            component: AddTeamMember
        },
        {
            path: '/review-team-members',
            name: 'review-team-members',
            component: ReviewTeamMembers
        },
        {
            path: '/individual-member-requests',
            name: 'IndividualMemberReview',
            component: IndividualMemberReview
        },
        {
            path: '/article-review',
            name: 'ArticleReview',
            component: ArticleReview
        },
        {
            path: '/articles/category',
            name: 'category-articles',
            component: ArticleCategory
        },
        {
            path: '/videos',
            name: 'VideoPage',
            component: VideoPage
        },
        {
            path: '/video-review',
            name: 'VideoReviewPage',
            component: VideoReviewPage
        },
        {
            path: '/upload-video',
            name: 'UploadVideoPage',
            component: UploadVideo
        },
        {
            path: '/video-manage',
            name: 'VideoManage',
            component: VideoManage
        }
    ]
});
