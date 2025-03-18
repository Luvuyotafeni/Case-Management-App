<script setup>
</script>

<template>
    <div class="logInPage">
        <div class="login-container">
            <!-- Left Section -->
            <div class="login-left">
                <h1>Log In</h1>
            </div>

            <!-- Right Section -->
            <div class="login-right">
                <form class="login-form">
                    <label for="email">Email</label>
                    <input type="email" id="email" placeholder="Enter your email">

                    <label for="password">Password</label>
                    <input type="password" id="password" placeholder="Enter your password">

                    <button type="submit">Login</button>
                    <div class="links">
                        <div><RouterLink to="/register">Don't have an account? Register</RouterLink></div>
                        <div><RouterLink to="/forgot-paswword">Forgot Password?</RouterLink></div>                       
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
}

/* Left Section */
.login-left {
    flex: 1;
    background-color: white;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 24px;
    font-weight: bold;
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
</style>
