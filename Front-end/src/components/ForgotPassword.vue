<script setup>
import AuthServices from '@/services/AuthService';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
const email = ref();
const loading = ref(false);
const router = useRouter();

const handleForgotPassword = async ()=>{
    if (!email.value){
        console.log("please enter email");
        return;
    }
    try{
        loading.value = true;
        const response = await AuthServices.forgotPassword({email:email.value});
        
    } catch (error){
        console.log("could not progress", error);
    } finally{
        loading.value= false;
    }
};

</script>
<template>
    <div class="forgot-password-page">
        <div class="forgot-container">
            <!-- Right Section -->
            <div class="forgot-left">
                <form class="forgot-password-form" @submit.prevent="handleForgotPassword">

                    <label for="email">Email</label>
                    <input type="email" id="email" placeholder="Enter your email" v-model="email">

                    <button type="submit">Forgot Password</button>
                    <div class="links">
                        <div><RouterLink to="/login">Have an account? Login</RouterLink></div>                      
                    </div>
                </form>
            </div>
            <!-- Left Section -->
            <div class="forgot-right">
                <img src="../assets/LawIcon.jpeg"/>
                <h2>Forgot Password</h2>
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

.forgot-right img{
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

.links a{
    margin-top: 10px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    color: white;
}
</style>