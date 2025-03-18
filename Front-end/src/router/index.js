import Dashboard from "@/components/Dashboard.vue";
import ForgotPassword from "@/components/ForgotPassword.vue";
import LoginPage from "@/components/LoginPage.vue";
import Register from "@/components/Register.vue";
import Welcome from "@/components/Welcome.vue";
import { createRouter, createWebHistory } from "vue-router";

const routes = [
    {
        path: "/",
        name: "welcome",
        component: Welcome
    },
    {
        path: "/login",
        name: "login",
        component: LoginPage
    },
    {
        path: "/register",
        name: "register",
        component: Register
    },
    {
        path: "/forgot-password",
        name: "forgot-password",
        component: ForgotPassword
    },
    {
        path: "/dashboard",
        name: "dashboard",
        component: Dashboard
    }
];

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes
})
export default router;