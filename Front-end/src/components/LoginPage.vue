<script setup>

    import AuthServices from '@/services/AuthService';
    import { onMounted, ref } from 'vue';
    import { useRouter } from 'vue-router';

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
        console.log(error);
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
/* Full Page Styling */
.logInPage {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    
}

/* Container Styling */
.login-container {
    display: flex;
    width: 600px;
    height: 500px;
    border: 1px solid #ccc;
    border-radius: 10px;
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
    border-radius: 10px;
}

.login-left img{
    width: 150px;
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
    border-radius: 10px;
}

/* Form Styling */
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
}

/* Button Styling */
.login-form button {
    background-color: #FFFFFF;
    border: none;
    padding: 10px;
    font-weight: bold;
    cursor: pointer;
}

.links{
    margin-top: 10px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    
}
.links a{
    margin-top: 10px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    color: white;
}
</style>
