<script setup>

    import AuthServices from '@/services/AuthService';
    import { onMounted, ref } from 'vue';
    import { useRouter } from 'vue-router';
    import { toast } from 'vue3-toastify';

    const router = useRouter();

    const email = ref("");
    const password = ref("");

    const handleLogIn = async () => {
    try {
        const response = await AuthServices.login(email.value, password.value);
        const role = response.role; // Get the role from response

        // Route based on role
        if (role === "ADMIN") {
            router.push('/admin-dashboard');
        } else if (role === "LAWYER") {
            router.push('/lawyer-dashboard');
        } else {
            router.push('/dashboard'); // Default user dashboard
        }
    } catch (error) {
        toast.error("Invalid log in details");
    }
};

    onMounted(()=> {
        sessionStorage.removeItem("token");
      sessionStorage.removeItem("userName");
      sessionStorage.removeItem("role",); // Save role in session
      sessionStorage.removeItem("userId");
      localStorage.removeItem("token")
    });
</script>

<template>
    <div class="logInPage">
        <div class="login-container">
            <!-- Left Section -->
            <div class="login-left">
                <img src="../assets/LawIcon.jpeg"/>
                <h2>Log In</h2>
            </div>

            <!-- Right Section -->
            <div class="login-right">
                <form class="login-form" @submit.prevent="handleLogIn">
                    <label for="email">Email</label>
                    <input type="email" id="email" placeholder="Enter your email" v-model="email">

                    <label for="password">Password</label>
                    <input type="password" id="password" placeholder="Enter your password" v-model="password">

                    <button type="submit">Login</button>
                    <div class="links">
                        <div><RouterLink to="/register">Don't have an account? Register</RouterLink></div>
                        <div><RouterLink to="/forgot-password">Forgot Password?</RouterLink></div>                       
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>

<style scoped>
/* =========================
   BASE / DESKTOP STYLING
   ========================= */

.logInPage {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    padding: 20px;
    box-sizing: border-box;
}

/* Container */
.login-container {
    display: flex;
    width: 600px;
    max-width: 100%;
    height: 500px;
    border: 1px solid #ccc;
    border-radius: 10px;
    overflow: hidden;
}

/* Left Section */
.login-left {
    flex: 1;
    background-color: white;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    font-size: 24px;
    font-weight: bold;
}

.login-left img {
    width: 150px;
    max-width: 80%;
    height: auto;
}

/* Right Section */
.login-right {
    flex: 1;
    background-color: black;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    padding: 20px;
    box-sizing: border-box;
}

/* Form */
.login-form {
    display: flex;
    flex-direction: column;
    width: 80%;
}

.login-form label {
    color: white;
    font-weight: bold;
    margin-bottom: 10px;
}

.login-form input {
    padding: 8px;
    margin-bottom: 20px;
    background-color: #FFFFFF;
    border: none;
    border-radius: 4px;
    box-sizing: border-box;
    width: 100%;
}

/* Button */
.login-form button {
    background-color: #FFFFFF;
    border: none;
    padding: 10px;
    font-weight: bold;
    cursor: pointer;
    border-radius: 4px;
}

/* Links */
.links {
    margin-top: 10px;
    display: flex;
    flex-direction: column;
}

.links a {
    margin-top: 10px;
    color: white;
}


/* =========================================
   LARGE DESKTOPS
   1440px and above
   ========================================= */

@media screen and (min-width: 1440px) {

    .login-container {
        width: 750px;
        height: 550px;
    }

    .login-left {
        font-size: 28px;
    }

    .login-left img {
        width: 180px;
    }

    .login-right {
        padding: 30px;
    }

    .login-form {
        width: 85%;
    }

    .login-form label {
        font-size: 17px;
    }

    .login-form input {
        padding: 12px;
        font-size: 16px;
    }

    .login-form button {
        padding: 12px;
        font-size: 16px;
    }

    .links a {
        font-size: 15px;
    }
}


/* =========================================
   LAPTOPS
   1024px - 1439px
   ========================================= */

@media screen and (min-width: 1024px) and (max-width: 1439px) {

    .login-container {
        width: 650px;
        height: 500px;
    }

    .login-left img {
        width: 150px;
    }

    .login-left {
        font-size: 24px;
    }

    .login-form {
        width: 85%;
    }
}


/* =========================================
   TABLETS
   768px - 1023px
   ========================================= */

@media screen and (min-width: 768px) and (max-width: 1023px) {

    .login-container {
        width: 90%;
        max-width: 650px;
        height: 480px;
    }

    .login-left {
        font-size: 22px;
    }

    .login-left img {
        width: 130px;
    }

    .login-right {
        padding: 18px;
    }

    .login-form {
        width: 90%;
    }

    .login-form label {
        font-size: 14px;
    }

    .login-form input {
        padding: 9px;
        margin-bottom: 18px;
    }

    .login-form button {
        padding: 10px;
    }

    .links a {
        font-size: 13px;
    }
}


/* =========================================
   LARGE PHONES / SMALL TABLETS
   481px - 767px
   ========================================= */

@media screen and (min-width: 481px) and (max-width: 767px) {

    .logInPage {
        padding: 20px;
        align-items: center;
    }

    .login-container {
        flex-direction: column;
        width: 90%;
        max-width: 500px;
        height: auto;
        min-height: 600px;
    }

    .login-left {
        flex: none;
        height: 220px;
        padding: 20px;
        font-size: 22px;
        box-sizing: border-box;
    }

    .login-left img {
        width: 110px;
    }

    .login-left h2 {
        margin: 10px 0 0;
    }

    .login-right {
        flex: 1;
        width: 100%;
        padding: 35px 20px;
        box-sizing: border-box;
    }

    .login-form {
        width: 85%;
    }

    .login-form input {
        padding: 11px;
    }

    .login-form button {
        padding: 11px;
    }

    .links a {
        font-size: 14px;
    }
}


/* =========================================
   MOBILE PHONES
   320px - 480px
   ========================================= */

@media screen and (max-width: 480px) {

    .logInPage {
        min-height: 100vh;
        height: auto;
        padding: 15px;
    }

    .login-container {
        flex-direction: column;
        width: 100%;
        height: auto;
        min-height: 570px;
    }

    .login-left {
        flex: none;
        height: 190px;
        padding: 15px;
        font-size: 20px;
        box-sizing: border-box;
    }

    .login-left img {
        width: 90px;
    }

    .login-left h2 {
        font-size: 24px;
        margin: 8px 0 0;
    }

    .login-right {
        flex: 1;
        width: 100%;
        padding: 30px 15px;
        box-sizing: border-box;
    }

    .login-form {
        width: 90%;
    }

    .login-form label {
        font-size: 14px;
        margin-bottom: 7px;
    }

    .login-form input {
        padding: 10px;
        margin-bottom: 16px;
        font-size: 14px;
    }

    .login-form button {
        padding: 10px;
        font-size: 14px;
    }

    .links {
        margin-top: 8px;
    }

    .links a {
        margin-top: 8px;
        font-size: 13px;
    }
}


/* =========================================
   VERY SMALL PHONES
   Below 360px
   ========================================= */

@media screen and (max-width: 359px) {

    .logInPage {
        padding: 10px;
    }

    .login-container {
        min-height: 520px
    }

    .login-left {
        height: 200px
    }

    .login-left img {
        width: 75px;
    }

    .login-left h2 {
        font-size: 20px;
    }

    .login-right {
        padding: 20px 10px;
    }

    .login-form {
        width: 92%;
    }

    .login-form label {
        font-size: 13px;
    }

    .login-form input {
        padding: 9px;
        font-size: 13px;
    }

    .login-form button {
        padding: 9px;
        font-size: 13px;
    }

    .links a {
        font-size: 12px;
    }
}
</style>
