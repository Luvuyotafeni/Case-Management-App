<script setup>
    import AuthServices from '@/services/AuthService';
    import { onMounted, ref } from 'vue';
    import { useRouter } from 'vue-router';

    const router = useRouter();

    const email = ref("");
    const password = ref("");
    const toast = ref({
        show: false,
        message: '',
        type: 'error' // 'success' or 'error'
    });

    const showToast = (message, type = 'error') => {
        toast.value = {
            show: true,
            message,
            type
        };
        
        // Auto hide toast after 3 seconds
        setTimeout(() => {
            toast.value.show = false;
        }, 3000);
    };

    const hideToast = () => {
        toast.value.show = false;
    };

    const handleLogIn = async () => {
        try {
            // Basic validation
            if (!email.value || !password.value) {
                showToast('Please fill in all fields');
                return;
            }

            const response = await AuthServices.login(email.value, password.value);
            const role = response.role; // Get the role from response

            // Show success toast
            showToast('Login successful!', 'success');

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
            
            // Handle different error scenarios
            let errorMessage = 'Login failed. Please try again.';
            
            if (error.response) {
                // Server responded with error status
                switch (error.response.status) {
                    case 401:
                        errorMessage = 'Invalid email or password';
                        break;
                    case 404:
                        errorMessage = 'Account not found';
                        break;
                    case 500:
                        errorMessage = 'Server error. Please try again later';
                        break;
                    default:
                        errorMessage = error.response.data?.message || 'Login failed';
                }
            } else if (error.request) {
                // Network error
                errorMessage = 'Network error. Please check your connection';
            }
            
            showToast(errorMessage);
        }
    };

    onMounted(()=> {
        sessionStorage.removeItem("token");
        sessionStorage.removeItem("userName");
        sessionStorage.removeItem("role"); // Save role in session
        sessionStorage.removeItem("userId");
        localStorage.removeItem("token")
    });
</script>

<template>
    <div class="logInPage">
        <!-- Toast Notification -->
        <div v-if="toast.show" :class="['toast', toast.type]" @click="hideToast">
            <div class="toast-content">
                <span class="toast-icon">
                    <i v-if="toast.type === 'success'">✓</i>
                    <i v-else>✕</i>
                </span>
                <span class="toast-message">{{ toast.message }}</span>
                <button class="toast-close" @click="hideToast">×</button>
            </div>
        </div>

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
    position: relative;
}

/* Toast Notification Styles */
.toast {
    position: fixed;
    top: 20px;
    right: 20px;
    min-width: 300px;
    padding: 16px;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    z-index: 1000;
    cursor: pointer;
    animation: slideIn 0.3s ease-out;
}

.toast.success {
    background-color: #10b981;
    color: white;
}

.toast.error {
    background-color: #ef4444;
    color: white;
}

.toast-content {
    display: flex;
    align-items: center;
    gap: 12px;
}

.toast-icon {
    font-weight: bold;
    font-size: 16px;
}

.toast-message {
    flex: 1;
    font-weight: 500;
}

.toast-close {
    background: none;
    border: none;
    color: white;
    font-size: 20px;
    cursor: pointer;
    padding: 0;
    margin-left: 8px;
    opacity: 0.8;
}

.toast-close:hover {
    opacity: 1;
}

@keyframes slideIn {
    from {
        transform: translateX(100%);
        opacity: 0;
    }
    to {
        transform: translateX(0);
        opacity: 1;
    }
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