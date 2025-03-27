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
/* Full Page Styling */
.forgot-password-page {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
}

/* Container Styling */
.forgot-container {
    display: flex;
    width: 600px;
    height: 500px;
    border: 1px solid #ccc;
    border-radius: 10px;
}

/* Left Section */
.forgot-left {
    flex: 1;
    background-color: black;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    padding: 20px;
    border-radius: 10px;
}

/* Right Section */
.forgot-right {
    border-radius: 10px;
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
}

/* Form Styling */
.forgot-password-form {
    display: flex;
    flex-direction: column;
    width: 80%;
}

.forgot-password-form label {
    color: white;
    font-weight: bold;
    margin-bottom: 10px;
}

.forgot-password-form input {
    padding: 8px;
    margin-bottom: 20px;
    background-color: #FFFFFF;
    border: none;
    border-radius: 4px;
}

/* Button Styling */
.forgot-password-form button {
    background-color: #FFFFFF;
    border: none;
    padding: 10px;
    font-weight: bold;
    cursor: pointer;
}

.links a {
    margin-top: 10px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    color: white;
}
</style>
