<script setup>
import AuthServices from '@/services/AuthService';
import { ref } from 'vue';
import { toast } from 'vue3-toastify';

const formData = ref({
    name: "",
    email: "",
    phone: "",
    password: "",
});

const handleRegister = async () => {
    try {
        // Create the user object inside the function to capture current form values
        const user = {
            name: formData.value.name,
            email: formData.value.email,
            phone: formData.value.phone,
            password: formData.value.password
        };

        const response = await AuthServices.registerUser(user);
        toast.success("Registration successful!");
        return response;
    } catch (error) {
        toast.error(error.response?.data?.message || "Registration failed");
        console.error(error);
    }
};
</script>

<template>
    <div class="registerPage ">
        <div class="register-container">
            <!-- Right Section -->
            <div class="register-left">
                <form class="register-form" @submit.prevent="handleRegister">
                    <label for="name">Full Name</label>
                    <input type="text" id="name" placeholder="Enter your Full name" v-model="formData.name">

                    <label for="email">Email</label>
                    <input type="email" id="email" placeholder="Enter your email" v-model="formData.email">

                    <label for="phone">Phone</label>
                    <input type="text" id="phone" placeholder="Enter your contact number" v-model="formData.phone">

                    <label for="password">Create password</label>
                    <input type="password" id="password" placeholder="Enter your password" v-model="formData.password">

                    <button type="submit">Register</button>
                    <div class="links">
                        <div><RouterLink to="/login">Have an account? Login</RouterLink></div>                      
                    </div>
                </form>
            </div>
            <!-- Left Section -->
            <div class="register-right">
                <img src="../assets/LawIcon.jpeg"/>
                <h2>Register</h2>
            </div>

            
        </div>
    </div>
</template>

<style scoped>
/* =========================================
   BASE / DESKTOP STYLING
   ========================================= */

.registerPage {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    padding: 20px;
    box-sizing: border-box;
}

/* Container */
.register-container {
    display: flex;
    width: 600px;
    max-width: 100%;
    height: 500px;
    border: 1px solid #ccc;
    border-radius: 10px;
    overflow: hidden;
}

/* Left Section - Form */
.register-left {
    flex: 1;
    background-color: black;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    padding: 20px;
    box-sizing: border-box;
}

/* Right Section - Logo */
.register-right {
    flex: 1;
    flex-direction: column;
    background-color: white;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 24px;
    font-weight: bold;
}

.register-right img {
    width: 150px;
    max-width: 80%;
    height: auto;
}

/* Form */
.register-form {
    display: flex;
    flex-direction: column;
    width: 80%;
}

.register-form label {
    color: white;
    font-weight: bold;
    margin-bottom: 10px;
}

.register-form input {
    width: 100%;
    padding: 8px;
    margin-bottom: 20px;
    background-color: #FFFFFF;
    border: none;
    border-radius: 4px;
    box-sizing: border-box;
}

/* Button */
.register-form button {
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
}

.links a {
    margin-top: 10px;
    display: flex;
    flex-direction: column;
    color: white;
}


/* =========================================
   LARGE DESKTOPS
   1440px and above
   ========================================= */

@media screen and (min-width: 1440px) {

    .register-container {
        width: 750px;
        height: 550px;
    }

    .register-right {
        font-size: 28px;
    }

    .register-right img {
        width: 180px;
    }

    .register-left {
        padding: 30px;
    }

    .register-form {
        width: 85%;
    }

    .register-form label {
        font-size: 17px;
    }

    .register-form input {
        padding: 12px;
        font-size: 16px;
    }

    .register-form button {
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

    .register-container {
        width: 650px;
        height: 500px;
    }

    .register-right {
        font-size: 24px;
    }

    .register-right img {
        width: 150px;
    }

    .register-form {
        width: 85%;
    }
}


/* =========================================
   TABLETS
   768px - 1023px
   ========================================= */

@media screen and (min-width: 768px) and (max-width: 1023px) {

    .register-container {
        width: 90%;
        max-width: 650px;
        height: 500px;
    }

    .register-right {
        font-size: 22px;
    }

    .register-right img {
        width: 130px;
    }

    .register-left {
        padding: 18px;
    }

    .register-form {
        width: 90%;
    }

    .register-form label {
        font-size: 14px;
        margin-bottom: 8px;
    }

    .register-form input {
        padding: 9px;
        margin-bottom: 18px;
    }

    .register-form button {
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

    .registerPage {
        padding: 20px;
    }

    .register-container {
        flex-direction: column-reverse;
        width: 90%;
        max-width: 500px;
        height: auto;
        min-height: 650px;
    }

    /* Logo appears on top */
    .register-right {
        flex: none;
        width: 100%;
        height: 200px;
        padding: 20px;
        font-size: 22px;
        box-sizing: border-box;
    }

    .register-right img {
        width: 100px;
    }

    .register-right h2 {
        margin: 10px 0 0;
    }

    /* Form appears underneath */
    .register-left {
        flex: 1;
        width: 100%;
        padding: 30px 20px;
        box-sizing: border-box;
    }

    .register-form {
        width: 85%;
    }

    .register-form label {
        margin-bottom: 7px;
    }

    .register-form input {
        padding: 10px;
        margin-bottom: 16px;
    }

    .register-form button {
        padding: 11px;
    }

    .links a {
        font-size: 14px;
    }
}


/* =========================================
   MOBILE PHONES
   360px - 480px
   ========================================= */

@media screen and (min-width: 360px) and (max-width: 480px) {

    .registerPage {
        min-height: 100vh;
        height: auto;
        padding: 15px;
    }

    .register-container {
        flex-direction: column-reverse;
        width: 100%;
        height: auto;
        min-height: 620px;
    }

    .register-right {
        flex: none;
        width: 100%;
        height: 170px;
        padding: 15px;
        box-sizing: border-box;
    }

    .register-right img {
        width: 80px;
    }

    .register-right h2 {
        font-size: 22px;
        margin: 7px 0 0;
    }

    .register-left {
        flex: 1;
        width: 100%;
        padding: 25px 15px;
        box-sizing: border-box;
    }

    .register-form {
        width: 90%;
    }

    .register-form label {
        font-size: 14px;
        margin-bottom: 6px;
    }

    .register-form input {
        padding: 10px;
        margin-bottom: 14px;
        font-size: 14px;
    }

    .register-form button {
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

    .registerPage {
        min-height: 100vh;
        height: auto;
        padding: 10px;
    }

    .register-container {
        flex-direction: column-reverse;
        width: 100%;
        height: auto;
        min-height: 580px;
    }

    .register-right {
        flex: none;
        width: 100%;
        height: 150px;
        padding: 10px;
        box-sizing: border-box;
    }

    .register-right img {
        width: 70px;
    }

    .register-right h2 {
        font-size: 20px;
        margin: 5px 0 0;
    }

    .register-left {
        flex: 1;
        width: 100%;
        padding: 20px 10px;
        box-sizing: border-box;
    }

    .register-form {
        width: 92%;
    }

    .register-form label {
        font-size: 13px;
        margin-bottom: 5px;
    }

    .register-form input {
        padding: 9px;
        margin-bottom: 12px;
        font-size: 13px;
    }

    .register-form button {
        padding: 9px;
        font-size: 13px;
    }

    .links a {
        font-size: 12px;
    }
}
</style>