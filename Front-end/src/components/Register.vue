<script setup>
</script>

<template>
    <div class="registerPage ">
        <div class="register-container">
            <!-- Right Section -->
            <div class="register-left">
                <form class="register-form">
                    <label for="name">Full Name</label>
                    <input type="text" id="name" placeholder="Enter your Full name">

                    <label for="email">Email</label>
                    <input type="email" id="email" placeholder="Enter your email">

                    <label for="phone">Phone</label>
                    <input type="text" id="phone" placeholder="Enter your contact number">

                    <label for="password">Create password</label>
                    <input type="password" id="password" placeholder="Enter your password">

                    <button type="submit">Register</button>
                    <div class="links">
                        <div><RouterLink to="/login">Have an account? Login</RouterLink></div>                      
                    </div>
                </form>
            </div>
            <!-- Left Section -->
            <div class="register-right">
                <h1>Register</h1>
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

}

/* Right Section */
.register-right {
    
    flex: 1;
    background-color: white;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 24px;
    font-weight: bold;
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

.links{
    margin-top: 10px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    
}
</style>
