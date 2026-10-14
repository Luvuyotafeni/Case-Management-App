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

/* =========================================
   DESKTOP SIDEBAR
   ========================================= */

.header-wrapper {
    position: relative;
    min-height: 100vh;
}


/* Sidebar */

.header {
    position: fixed;
    top: 0;
    left: 0;

    width: 280px;
    height: 100vh;

    background-color: white;

    display: flex;
    flex-direction: column;

    box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);

    z-index: 1000;

    transition: transform 0.3s ease-in-out;
}


/* =========================================
   SIDEBAR HEADER
   ========================================= */

.sidebar-header {
    height: 80px;

    display: flex;
    align-items: center;
    justify-content: space-between;

    padding: 0 20px;

    border-bottom: 1px solid #eeeeee;
}


.logo-section {
    display: flex;
    align-items: center;
    gap: 10px;

    color: #000428;

    font-size: 18px;
    font-weight: bold;
}


.logo-section i {
    font-size: 25px;
}


/* Close button */

.close-button {
    display: none;

    background: none;
    border: none;

    font-size: 28px;

    cursor: pointer;

    color: #000428;
}


/* =========================================
   NAVIGATION
   ========================================= */

.nav {
    display: flex;
    flex-direction: column;

    padding: 20px;
}


.nav-list {
    list-style: none;

    padding: 0;
    margin: 40px 0 0;

    display: flex;
    flex-direction: column;

    gap: 8px;
}


.nav-item {
    width: 100%;

    border-radius: 6px;

    transition: all 0.2s ease;
}


.nav-item button {
    width: 100%;

    display: flex;
    align-items: center;

    gap: 14px;

    background: none;
    border: none;

    color: #000428;

    font-size: 16px;

    cursor: pointer;

    padding: 13px 12px;

    text-align: left;

    border-radius: 6px;
}


.nav-item button i {
    font-size: 21px;
}


.nav-item:hover {
    background-color: #f3f3f3;
}


.nav-item:hover button {
    color: #000000;
}


/* Logout */

.logout-item {
    margin-top: 20px;
}


/* =========================================
   MOBILE HEADER
   ========================================= */

.mobile-header {
    display: none;
}


/* =========================================
   OVERLAY
   ========================================= */

.sidebar-overlay {
    display: none;
}


/* =========================================
   LARGE DESKTOP
   ========================================= */

@media screen and (min-width: 1440px) {

    .header {
        width: 300px;
    }

    .sidebar-header {
        height: 90px;
        padding: 0 25px;
    }

    .logo-section {
        font-size: 20px;
    }

    .logo-section i {
        font-size: 27px;
    }

    .nav {
        padding: 25px;
    }

    .nav-list {
        margin-top: 50px;
        gap: 10px;
    }

    .nav-item button {
        font-size: 17px;
        padding: 14px;
    }

    .nav-item button i {
        font-size: 22px;
    }
}


/* =========================================
   LAPTOP
   1024px - 1439px
   ========================================= */

@media screen and (min-width: 1024px) and (max-width: 1439px) {

    .header {
        width: 270px;
    }

    .sidebar-header {
        height: 75px;
    }

    .nav {
        padding: 18px;
    }

    .nav-list {
        margin-top: 35px;
    }

    .nav-item button {
        font-size: 15px;
        padding: 11px;
    }

    .nav-item button i {
        font-size: 20px;
    }
}


/* =========================================
   TABLET
   768px - 1023px
   ========================================= */

@media screen and (min-width: 768px) and (max-width: 1023px) {

    .header {
        width: 230px;
    }

    .sidebar-header {
        height: 70px;
        padding: 0 15px;
    }

    .logo-section {
        font-size: 15px;
    }

    .logo-section i {
        font-size: 22px;
    }

    .nav {
        padding: 15px;
    }

    .nav-list {
        margin-top: 30px;
    }

    .nav-item button {
        font-size: 14px;
        padding: 10px;
        gap: 10px;
    }

    .nav-item button i {
        font-size: 19px;
    }
}


/* =========================================
   MOBILE
   BELOW 768px
   ========================================= */

@media screen and (max-width: 767px) {

    /* -------------------------
       Mobile top bar
       ------------------------- */

    .mobile-header {
        position: fixed;

        top: 0;
        left: 0;

        width: 100%;
        height: 60px;

        background-color: white;

        display: flex;
        align-items: center;

        padding: 0 15px;

        box-sizing: border-box;

        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.12);

        z-index: 900;
    }


    .hamburger-button {
        width: 42px;
        height: 42px;

        display: flex;
        align-items: center;
        justify-content: center;

        background: none;
        border: none;

        cursor: pointer;

        color: #000428;

        font-size: 28px;

        border-radius: 5px;
    }


    .hamburger-button:hover {
        background-color: #f3f3f3;
    }


    .mobile-title {
        margin-left: 15px;

        font-size: 17px;
        font-weight: bold;

        color: #000428;
    }


    /* -------------------------
       Sidebar
       ------------------------- */

    .header {
        position: fixed;

        top: 0;
        left: 0;

        width: 280px;
        max-width: 85%;

        height: 100vh;

        transform: translateX(-100%);

        box-shadow: 4px 0 15px rgba(0, 0, 0, 0.2);

        z-index: 1100;
    }


    .header.menu-open {
        transform: translateX(0);
    }


    /* -------------------------
       Sidebar header
       ------------------------- */

    .sidebar-header {
        height: 65px;

        padding: 0 15px;
    }


    .logo-section {
        font-size: 16px;
    }


    .logo-section i {
        font-size: 23px;
    }


    .close-button {
        display: flex;

        align-items: center;
        justify-content: center;

        width: 40px;
        height: 40px;
    }


    /* -------------------------
       Navigation
       ------------------------- */

    .nav {
        padding: 15px;
    }


    .nav-list {
        margin: 25px 0 0;

        gap: 5px;
    }


    .nav-item button {
        font-size: 15px;

        padding: 13px 10px;

        gap: 13px;
    }


    .nav-item button i {
        font-size: 21px;
    }


    /* -------------------------
       Overlay
       ------------------------- */

    .sidebar-overlay {
        display: block;

        position: fixed;

        top: 0;
        left: 0;

        width: 100%;
        height: 100vh;

        background-color: rgba(0, 0, 0, 0.45);

        z-index: 1050;
    }
}


/* =========================================
   SMALL PHONES
   BELOW 480px
   ========================================= */

@media screen and (max-width: 480px) {

    .mobile-header {
        height: 56px;

        padding: 0 10px;
    }


    .hamburger-button {
        width: 40px;
        height: 40px;

        font-size: 26px;
    }


    .mobile-title {
        font-size: 16px;
        margin-left: 10px;
    }


    .header {
        width: 270px;
        max-width: 85%;
    }


    .sidebar-header {
        height: 60px;
    }


    .logo-section {
        font-size: 15px;
    }


    .logo-section i {
        font-size: 21px;
    }


    .nav-item button {
        font-size: 14px;
        padding: 12px 9px;
    }


    .nav-item button i {
        font-size: 20px;
    }
}


/* =========================================
   VERY SMALL PHONES
   BELOW 360px
   ========================================= */

@media screen and (max-width: 359px) {

    .mobile-header {
        height: 52px;
    }


    .mobile-title {
        font-size: 14px;
    }


    .header {
        width: 250px;
    }


    .sidebar-header {
        height: 56px;
    }


    .logo-section {
        font-size: 14px;
    }


    .nav {
        padding: 12px;
    }


    .nav-item button {
        font-size: 13px;
        padding: 11px 8px;
    }
}

</style>