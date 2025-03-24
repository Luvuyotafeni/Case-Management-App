<script setup>
    import { onMounted, ref } from 'vue';
    import Header from './Header.vue';
    import UsersServices from '@/services/UserService';

    const lawyer = ref(null);
    const userId = sessionStorage.getItem("userId")

    const fetchUser = async ()=> {
        try{
            if (userId){
                const response = await UsersServices.getUserById(userId);
                console.log(response)
                lawyer.value = response;
            }
        } catch( error){
            console.log("Error fetching Lawyer", error);
        }
    }

    onMounted(fetchUser);
</script>

<template>
     <div class="container">
        <Header/>
        <div class="dashboard">
            <main class="dashboard">
                <div class="profile-card" v-if="lawyer">
                    <div class="profile-header">
                    <img src="" alt="User Avatar" class="profile-img" />
                    <h2>
                        {{ lawyer.name }}
                        <span
                        :class="lawyer.emailVerified ? 'verified' : 'not-verified'"
                        >
                        {{ lawyer.emailVerified ? "Verified" : "Not Verified" }}
                        </span>
                    </h2>
                    </div>
                    <hr />
                    <div class="profile-details">
                    <p><strong>Email:</strong> {{ lawyer.email }}</p>
                    <p><strong>Phone:</strong> {{ lawyer.phone }}</p>
                    <p><strong>Role:</strong> {{ lawyer.role }}</p>
                    <p><strong>Role:</strong> {{ lawyer.role }}</p>
                    <p>
                    <strong>Two-step Verification:</strong>
                    {{ lawyer.twoStepVerification ? "Enabled" : "Disabled" }}
                    </p>
                    <button class="update-btn">Update User</button>
                    </div>
                </div>
            </main>
        </div>
    </div>
</template>
<style scoped>
.container {
    max-width: 100%;
    height: 100vh;
    display: flex;
    flex-direction: row;
    padding: 0%;
    background-color: #F9FAFB;
  }

  .dashboard{
    flex: 1;
    display: flex;
    flex-direction: row;
    margin-top: 20px;
    justify-content: center;
    width: 100%;
    height: 80vh;
  }

    /* Profile Card */
    .profile-card {
        background: #ffffff;
        padding: 20px;
        border-radius: 8px;
        box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
        width: 700px;
    }

    .profile-header {
        display: flex;
        align-items: center;
    }

    .profile-img {
        width: 100px;
        height: 100px;
        border-radius: 50%;
        margin-right: 15px;
    }

    .profile-info h2 {
        font-size: 20px;
    }

    .verified {
        color: green;
        font-weight: bold;
        margin-left: 10px;
    }

    .profile-details p {
        margin: 10px 0;
    }

    .update-btn {
        margin-top: 15px;
        padding: 10px 15px;
        background-color: #d1d5db;
        border: none;
        border-radius: 5px;
        cursor: pointer;
    }

    .update-btn:hover {
        background-color: #b0b3b8;
    }
</style>