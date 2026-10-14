<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const isMenuOpen = ref(false);

const navigateTo = (path) => {
    router.push(path);
    isMenuOpen.value = false;
};

const toggleMenu = () => {
    isMenuOpen.value = !isMenuOpen.value;
};

const closeMenu = () => {
    isMenuOpen.value = false;
};

const logOut = () => {
    sessionStorage.removeItem("userId");
    sessionStorage.removeItem("token");
    sessionStorage.removeItem("role");

    router.push("/login");
};
</script>
<template>
    <div class="header-wrapper">

        <!-- Mobile Header -->
        <div class="mobile-header">
            <button
                class="hamburger-button"
                @click="toggleMenu"
                aria-label="Open navigation menu"
            >
                <i class='bx' :class="isMenuOpen ? 'bx-x' : 'bx-menu'"></i>
            </button>

            <span class="mobile-title">
                Legal Portal
            </span>
        </div>


        <!-- Dark Overlay -->
        <div
            v-if="isMenuOpen"
            class="sidebar-overlay"
            @click="closeMenu"
        ></div>


        <!-- Sidebar -->
        <header
            class="header"
            :class="{ 'menu-open': isMenuOpen }"
        >

            <!-- Sidebar Header -->
            <div class="sidebar-header">

                <div class="logo-section">
                    <i class='bx bx-briefcase-alt-2'></i>
                    <span>Legal Portal</span>
                </div>

                <!-- Mobile Close Button -->
                <button
                    class="close-button"
                    @click="closeMenu"
                    aria-label="Close navigation menu"
                >
                    <i class='bx bx-x'></i>
                </button>

            </div>


            <!-- Navigation -->
            <nav class="nav">

                <ul class="nav-list">

                    <li
                        class="nav-item"
                        @click="navigateTo('/user-profile')"
                    >
                        <button>
                            <i class='bx bx-user'></i>
                            <span>Profile</span>
                        </button>
                    </li>


                    <li
                        class="nav-item"
                        @click="navigateTo('/dashboard')"
                    >
                        <button>
                            <i class='bx bx-grid-alt'></i>
                            <span>Dashboard</span>
                        </button>
                    </li>


                    <li
                        class="nav-item"
                        @click="navigateTo('/user-cases')"
                    >
                        <button>
                            <i class='bx bx-briefcase'></i>
                            <span>Cases</span>
                        </button>
                    </li>


                    <!-- Documents -->
                    <!--
                    <li
                        class="nav-item"
                        @click="navigateTo('/user-documents')"
                    >
                        <button>
                            <i class='bx bx-file'></i>
                            <span>Documents</span>
                        </button>
                    </li>
                    -->


                    <!-- Consultations -->
                    <!--
                    <li
                        class="nav-item"
                        @click="navigateTo('/user-consultations')"
                    >
                        <button>
                            <i class='bx bx-calendar'></i>
                            <span>Consultations</span>
                        </button>
                    </li>
                    -->


                    <li
                        class="nav-item logout-item"
                        @click="logOut"
                    >
                        <button>
                            <i class='bx bx-log-out'></i>
                            <span>Logout</span>
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
        box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
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
    .nav-item:hover {
        box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.3);
        transition: box-shadow 0.2s ease-in-out;
    }
    
</style>