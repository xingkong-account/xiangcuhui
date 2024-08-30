import Vue from 'vue';
import App from './App.vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import router from '@/router';
import axios from 'axios';
import 'highlight.js/styles/1c-light.css'
Vue.config.productionTip = false;

Vue.use(ElementUI);
// const instance = axios.create({
//   baseURL: 'http://localhost:8081/api',
//   timeout: 10000,
// });

const instance = axios.create({
  baseURL: 'http://124.70.48.240:8081/api', // 使用你的 ECS 公网 IP
  timeout: 10000,
});

Vue.prototype.$axios = instance;
// 将 WangEditor 挂载到 window 对象上
import E from 'wangeditor';
window.wangEditor = E;

new Vue({
  router,
  render: h => h(App),
}).$mount('#app');
