import router from "@/router";
import Layout from "@/layout/Layout";

// 注意：这个文件是设置动态路由的
// permissions是一个资源的数组
export function activeRouter(permissions) {
    let root = {
        path: '/',
        name: 'Layout',
        component: Layout,
        redirect: "/login",
        children: []
    }
    permissions.forEach(p => {
        let obj = {
            path: p.permissionPath,
            name: p.permissionName,
            component:() =>import('../views/' + p.permissionName)
        };
        root.children.push(obj)
    })

    if (router) {
        //动态添加路由信息，重复的会被覆盖掉
        router.addRoute(root)
    }
}