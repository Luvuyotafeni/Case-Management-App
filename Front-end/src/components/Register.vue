<script setup>

    import AuthServices from '@/services/AuthService';
    import { ref } from 'vue';

    const formData = ref({
        name: "",
        email: "",
        phone: "",
        password: "",
    });

    const user = {
        name: formData.name,
        email: formData.email,
        phone: formData.phone,
        password: formData.password
    }


    const handleRegister = async ()=> {
        try{
            const response = await AuthServices.registerUser(user);
            return response;
        } catch(error){
            console.log(error)
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
/* Full Page Styling */
.registerPage {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
}

/* Container Styling */
.register-container {
    display: flex;
    width: 600px;
    height: 500px;
    border: 1px solid #ccc;
    border-radius: 10px;
}

/* Left Section */
.register-left {
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
.register-right {
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

.register-right img{
    width: 150px;
}

/* Form Styling */
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
    padding: 8px;
    margin-bottom: 20px;
    background-color: #FFFFFF;
    border: none;
    border-radius: 4px;
}

/* Button Styling */
.register-form button {
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
