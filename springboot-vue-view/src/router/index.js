// history: 引入-createWebHistory
import {createRouter, createWebHistory} from "vue-router"

// hash: 引入-createWebHistory
// import { createRouter, createWebHashHistory } from 'vue-router';

// const Foo = defineAsyncComponent(() => import('./Foo.vue'))

// import Login from "@/views/Login";
import {defineAsyncComponent} from "vue";
const Layout = defineAsyncComponent(() =>import('../layout/Layout'))

const routes = [
    {
        path: '/',
        name: 'Layout',
        component: Layout,
        redirect: "/login",
        children: [
            {
                path: '/home',
                name: 'Home',
                component: () =>import('../views/Home')
            }
        ]
    },
    {
        path: '/login',
        name: 'Login',
        component: () =>import('../views/Login')
    },
    {
        path: '/register',
        name: 'Register',
        component: () =>import('../views/Register')
    },
    {
        path: '/:path(.*)',
        name: 'NotFound',
        component: () =>import('../views/NotFound')
    }
]


const router = createRouter({
    // history: 引入-createWebHistory
    history: createWebHistory(process.env.BASE_URL),

    // hash: 引入-createWebHashHistory
    // history: createWebHashHistory(),
    routes
});

// 在刷新页面的时候重置当前路由
activeRouter()

function activeRouter() {
    const userStr = sessionStorage.getItem("userInfo")
    if (userStr) {
        const user = JSON.parse(userStr)
        let root = {
            path: '/',
            name: 'Layout',
            component: Layout,
            redirect: "/login",
            children: []
        }
        user.permissions.forEach(p => {
            let obj = {
                path: p.permissionPath,
                name: p.permissionName,
                component: () => import('../views/' + p.permissionName)
            };
            root.children.push(obj)
        })
        if (router) {
            router.addRoute(root)
        }
    }
}

router.beforeEach((to, from, next) => {
    if (to.path === '/login' || to.path === '/register') {
        next()
        return
    }
    let user = sessionStorage.getItem("userInfo") ? JSON.parse(sessionStorage.getItem("userInfo")) : {}
    if (!user.permissions || !user.permissions.length) {
        next('/login')
    } else if (!user.permissions.find(p => p.permissionPath === to.path)) {
        next('/login')
    } else {
        next()
    }
})

export default router