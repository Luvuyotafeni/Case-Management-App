import Dashboard from "@/components/UserComponents/Dashboard.vue";
import ForgotPassword from "@/components/ForgotPassword.vue";
import LoginPage from "@/components/LoginPage.vue";
import Register from "@/components/Register.vue";
import Welcome from "@/components/Welcome.vue";
import AdminProfile from "@/components/AdminComponents/Profile.vue";
import AdminDashboard from "@/components/AdminComponents/AdminDashboard.vue";
import { createRouter, createWebHistory } from "vue-router";
import Cases from "@/components/AdminComponents/Cases.vue";
import Documents from "@/components/AdminComponents/Documents.vue";
import AdminUsers from "@/components/AdminComponents/AdminUsers.vue";
import Lawyers from "@/components/AdminComponents/Lawyers.vue";
import Admins from "@/components/AdminComponents/Admins.vue";

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
    ,
    {
        path: "/admin-profile",
        name: "admin-profile",
        component: AdminProfile
    }
    ,
    {
        path: "/admin-dashboard",
        name: "admin-dashboard",
        component: AdminDashboard
    }
    ,
    {
        path: "/admin-cases",
        name: "admin-cases",
        component: Cases
    }
    ,
    {
        path: "/admin-documents",
        name: "admin-documents",
        component: Documents
    }
    ,
    {
        path: "/admin-users",
        name: "admin-users",
        component: AdminUsers
    },
    {
        path: "/lawyers",
        name: "lawyers",
        component: Lawyers
    },
    {
        path: "/admins",
        name: "admins",
        component: Admins
    },
    {
        path: "/admin-users",
        name: "admin-users",
        component: AdminUsers
    },
    {
        path: "/admin-users",
        name: "admin-users",
        component: AdminUsers
    },
    {
        path: "/admin-users",
        name: "admin-users",
        component: AdminUsers
    },
    {
        path: "/admin-users",
        name: "admin-users",
        component: AdminUsers
    },
    {
        path: "/admin-users",
        name: "admin-users",
        component: AdminUsers
    }

];

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes
})
export default router;