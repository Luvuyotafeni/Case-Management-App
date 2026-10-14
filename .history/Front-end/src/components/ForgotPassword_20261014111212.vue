<script setup>
import AuthServices from '@/services/AuthService';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const email = ref('');
const otp = ref('');
const newPassword = ref('');
const loading = ref(false);
const step = ref('requestOtp'); // 'requestOtp' or 'resetPassword'
const router = useRouter();

const handleForgotPassword = async () => {
    if (!email.value) {
        console.log("Please enter your email.");
        return;
    }
    try {
        loading.value = true;
        await AuthServices.forgotPassword({ email: email.value });

        // Switch to OTP verification step
        step.value = 'resetPassword';
    } catch (error) {
        console.log("Could not send OTP", error);
    } finally {
        loading.value = false;
    }
};

const handleResetPassword = async () => {
    if (!otp.value || !newPassword.value) {
        console.log("Please enter OTP and new password.");
        return;
    }
    try {
        loading.value = true;
        await AuthServices.resetPassword({
            email: email.value,
            otp: otp.value,
            newPassword: newPassword.value
        });

        console.log("Password reset successful");
        router.push('/login'); // Redirect to login after successful reset
    } catch (error) {
        console.log("Failed to reset password", error);
    } finally {
        loading.value = false;
    }
};
</script>

<template>
    <div class="forgot-password-page">
        <div class="forgot-container">
            <div class="forgot-left">
                <form class="forgot-password-form" @submit.prevent="step === 'requestOtp' ? handleForgotPassword() : handleResetPassword()">
                    <h2 v-if="step === 'requestOtp'">Forgot Password</h2>
                    <h2 v-else>Reset Password</h2>

                    <label for="email">Email</label>
                    <input type="email" id="email" placeholder="Enter your email" v-model="email" :disabled="step === 'resetPassword'">

                    <div v-if="step === 'resetPassword'">
                        <div>
                            <label for="otp">OTP Pin</label>
                            <input type="text" id="otp" placeholder="Enter OTP" v-model="otp">
                        </div>
                        <label for="newPassword">New Password</label>
                        <input type="password" id="newPassword" placeholder="Enter new password" v-model="newPassword">
                    </div>

                    <button type="submit" :disabled="loading">
                        {{ loading ? "Processing..." : step === 'requestOtp' ? "Request OTP" : "Reset Password" }}
                    </button>

                    <div class="links">
                        <RouterLink to="/login">Have an account? Login</RouterLink>
                    </div>
                </form>
            </div>
            <div class="forgot-right">
                <img src="../assets/LawIcon.jpeg"/>
                <h2>{{ step === 'requestOtp' ? 'Forgot Password' : 'Reset Password' }}</h2>
            </div>
        </div>
    </div>
</template>

<style scoped>
/* =========================================
   BASE / DESKTOP STYLING
   ========================================= */

.forgot-password-page {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    padding: 20px;
    box-sizing: border-box;
}

/* Container */
.forgot-container {
    display: flex;
    width: 600px;
    max-width: 100%;
    height: 500px;
    border: 1px solid #ccc;
    border-radius: 10px;
    overflow: hidden;
}

/* Left Section - Form */
.forgot-left {
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
.forgot-right {
    flex: 1;
    flex-direction: column;
    background-color: white;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 24px;
    font-weight: bold;
}

.forgot-right img {
    width: 150px;
    max-width: 80%;
    height: auto;
}

/* Form */
.forgot-password-form {
    display: flex;
    flex-direction: column;
    width: 80%;
}

.forgot-password-form h2 {
    color: white;
    margin-top: 0;
    margin-bottom: 25px;
}

.forgot-password-form label {
    display: block;
    color: white;
    font-weight: bold;
    margin-bottom: 10px;
}

.forgot-password-form input {
    width: 100%;
    padding: 8px;
    margin-bottom: 20px;
    background-color: #FFFFFF;
    border: none;
    border-radius: 4px;
    box-sizing: border-box;
}

/* Disabled Email Input */
.forgot-password-form input:disabled {
    background-color: #e5e5e5;
    cursor: not-allowed;
}

/* Button */
.forgot-password-form button {
    background-color: #FFFFFF;
    border: none;
    padding: 10px;
    font-weight: bold;
    cursor: pointer;
    border-radius: 4px;
}

.forgot-password-form button:disabled {
    cursor: not-allowed;
    opacity: 0.7;
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

    .forgot-container {
        width: 750px;
        height: 550px;
    }

    .forgot-right {
        font-size: 28px;
    }

    .forgot-right img {
        width: 180px;
    }

    .forgot-left {
        padding: 30px;
    }

    .forgot-password-form {
        width: 85%;
    }

    .forgot-password-form h2 {
        font-size: 27px;
    }

    .forgot-password-form label {
        font-size: 17px;
    }

    .forgot-password-form input {
        padding: 12px;
        font-size: 16px;
    }

    .forgot-password-form button {
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

    .forgot-container {
        width: 650px;
        height: 500px;
    }

    .forgot-right {
        font-size: 24px;
    }

    .forgot-right img {
        width: 150px;
    }

    .forgot-password-form {
        width: 85%;
    }
}


/* =========================================
   TABLETS
   768px - 1023px
   ========================================= */

@media screen and (min-width: 768px) and (max-width: 1023px) {

    .forgot-container {
        width: 90%;
        max-width: 650px;
        height: 500px;
    }

    .forgot-right {
        font-size: 22px;
    }

    .forgot-right img {
        width: 130px;
    }

    .forgot-left {
        padding: 18px;
    }

    .forgot-password-form {
        width: 90%;
    }

    .forgot-password-form h2 {
        font-size: 22px;
        margin-bottom: 20px;
    }

    .forgot-password-form label {
        font-size: 14px;
        margin-bottom: 8px;
    }

    .forgot-password-form input {
        padding: 9px;
        margin-bottom: 18px;
    }

    .forgot-password-form button {
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

    .forgot-password-page {
        padding: 20px;
    }

    .forgot-container {
        flex-direction: column-reverse;
        width: 90%;
        max-width: 500px;
        height: auto;
        min-height: 600px;
    }

    /* Logo section moves to top */
    .forgot-right {
        flex: none;
        width: 100%;
        height: 200px;
        padding: 20px;
        font-size: 22px;
        box-sizing: border-box;
    }

    .forgot-right img {
        width: 100px;
    }

    .forgot-right h2 {
        margin: 10px 0 0;
    }

    /* Form underneath */
    .forgot-left {
        flex: 1;
        width: 100%;
        padding: 30px 20px;
        box-sizing: border-box;
    }

    .forgot-password-form {
        width: 85%;
    }

    .forgot-password-form h2 {
        font-size: 22px;
        margin-bottom: 20px;
    }

    .forgot-password-form label {
        margin-bottom: 7px;
    }

    .forgot-password-form input {
        padding: 10px;
        margin-bottom: 16px;
    }

    .forgot-password-form button {
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

    .forgot-password-page {
        min-height: 100vh;
        height: auto;
        padding: 15px;
    }

    .forgot-container {
        flex-direction: column-reverse;
        width: 100%;
        height: auto;
        min-height: 570px;
    }

    /* Logo */
    .forgot-right {
        flex: none;
        width: 100%;
        height: 170px;
        padding: 15px;
        box-sizing: border-box;
    }

    .forgot-right img {
        width: 80px;
    }

    .forgot-right h2 {
        font-size: 22px;
        margin: 7px 0 0;
    }

    /* Form */
    .forgot-left {
        flex: 1;
        width: 100%;
        padding: 25px 15px;
        box-sizing: border-box;
    }

    .forgot-password-form {
        width: 90%;
    }

    .forgot-password-form h2 {
        font-size: 20px;
        margin-bottom: 18px;
    }

    .forgot-password-form label {
        font-size: 14px;
        margin-bottom: 6px;
    }

    .forgot-password-form input {
        padding: 10px;
        margin-bottom: 14px;
        font-size: 14px;
    }

    .forgot-password-form button {
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

    .forgot-password-page {
        min-height: 100vh;
        height: auto;
        padding: 10px;
    }

    .forgot-container {
        flex-direction: column-reverse;
        width: 100%;
        height: auto;
        min-height: 540px;
    }

    /* Logo */
    .forgot-right {
        flex: none;
        width: 100%;
        height: 150px;
        padding: 10px;
        box-sizing: border-box;
    }

    .forgot-right img {
        width: 70px;
    }

    .forgot-right h2 {
        font-size: 20px;
        margin: 5px 0 0;
    }

    /* Form */
    .forgot-left {
        flex: 1;
        width: 100%;
        padding: 20px 10px;
        box-sizing: border-box;
    }

    .forgot-password-form {
        width: 92%;
    }

    .forgot-password-form h2 {
        font-size: 19px;
        margin-bottom: 16px;
    }

    .forgot-password-form label {
        font-size: 13px;
        margin-bottom: 5px;
    }

    .forgot-password-form input {
        padding: 9px;
        margin-bottom: 12px;
        font-size: 13px;
    }

    .forgot-password-form button {
        padding: 9px;
        font-size: 13px;
    }

    .links a {
        font-size: 12px;
    }
}
</style>