<script setup>
    import { useRouter } from 'vue-router';

    const router = useRouter();

    const navigateTo = (path) => {
        router.push(path);
    };
</script>
<template>
    <div class="header-wrapper">
        <header class="header">
            <nav class="nav">
                <ul class="nav-list">
                    <li class="nav-item" @click="navigateTo('/admin-profile')">
                        <button>
                            Profile
                        </button>
                    </li>
                    <li class="nav-item" @click="navigateTo('/admin-dashboard')">
                        <button>
                            Dashboard
                        </button>
                    </li>
                    <li class="nav-item" @click="navigateTo('/admin-cases')">
                        <button>
                            Cases
                        </button>
                    </li>
                    <li class="nav-item" @click="navigateTo('/admin-documents')">
                        <button>
                            Documents
                        </button>
                    </li>
                    <li class="nav-item" @click="navigateTo('/admin-users')">
                        <button>
                            Users
                        </button>
                    </li>
                    <li class="nav-item" @click="navigateTo('lawyers')">
                        <button>
                            Lawyers
                        </button>
                    </li>
                    <li class="nav-item" @click="navigateTo('/admins')">
                        <button>
                            Admin
                        </button>
                    </li>
                    <li class="nav-item">
                        <button>
                            Logout
                        </button>
                    </li>
                </ul>
            </nav>
        </header>
    </div>
    
</template>
<style scoped>

    .header-wrapper{
        position: relative;
        background-color: white;
    }

    .header{
        display: flex;
        flex-direction: column;
        width: 300px;
        height: 100%;
        background-color: white;
        transition: transform 0.3s ease-in-out;
        transform: translateX(0);
    }
    .nav{
        display: flex;
        flex-direction: column;
        background-color: white;
        padding: 20px;
    }
    .nav-list{
        list-style: none;
        padding: 0;
        margin: 100px 0;
        display: flex;
        flex-direction: column;
        background-color: white;
        gap: 10px;
    }
    .nav-item{
        padding: 5px;
        border-radius: 5px;
        background-color: white;
    }
    .nav-item button {
    background: none;
    border: none;
    color: #000428;
    font-size: 16px;
    cursor: pointer;
    padding: 10px;
    text-align: left;
  }
    
</style>