// main.ts
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import store from "@/store";
import router from "@/router";

//使用font-awesome是一个免费的图标字体库
import 'font-awesome/css/font-awesome.min.css'

//国际化
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import qs from 'qs'


// 报错 ResizeObserver loop limit exceeded 解决
const debounce = (fn, delay) => {
    let timer = null;
    return function () {
        let context = this;
        let args = arguments;
        clearTimeout(timer);
        timer = setTimeout(function () {
            fn.apply(context, args);
        }, delay);
    }
}
const _ResizeObserver = window.ResizeObserver;
window.ResizeObserver = class ResizeObserver extends _ResizeObserver{
    constructor(callback) {
        callback = debounce(callback, 16);
        super(callback);
    }
}
// const _ResizeObserver = window.ResizeObserver;
// window.ResizeObserver = class ResizeObserver extends _ResizeObserver{
//     constructor(callback) {
//         callback = debounce(callback, 16);
//         super(callback);
//     }
// }
// 如果您正在使用CDN引入，请删除下面一行。使用elementplus图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.use(ElementPlus, {
    locale: zhCn,
})
app.use(store)
app.use(router)
app.use(qs)
app.mount('#app')