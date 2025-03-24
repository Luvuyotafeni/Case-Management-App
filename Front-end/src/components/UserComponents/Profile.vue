<script setup>
    import { onMounted, ref } from 'vue';
    import Header from './Header.vue';
    import UsersServices from '@/services/UserService';

    const user = ref(null);
    const userId = sessionStorage.getItem("userId");

    const fetchUser = async() => {
        try{
            const response = await UsersServices.getUserById(userId);
            user.value = response;
        } catch (error){
            console.log("Error fetching user", error);
        }
    };

    onMounted(fetchUser);

</script>
<template>
    <div class="container">
        <Header/>
        <div class="dashboard">
            <main class="dashboard">
                <div class="profile-card" v-if="user">
                    <div class="profile-header">
                        <img
                        :src="user.profilePictureUrl|| 'https://example.com/default-avatar.png'"
                        alt="User Profile"
                        class="profile-img"
                        />
                    <div class="profile-info">
                        <h2>{{ user.name }} <span class="verified">Verified</span></h2>
                    </div>
                    </div>
                    <hr />
                    <div class="profile-details">
                    <p><strong>Email:</strong>{{ user.email }}</p>
                    <p><strong>Phone:</strong>{{ user.phone }}</p>
                    <p><strong>Role:</strong> {{ user.role }}</p>
                    <p><strong>Two-step Verification:</strong> Enabled</p>
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
    flex-direction: column;
    /* margin-top: 20px; */
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 50vh;
  }

    /* Profile Card */
    .profile-card {
        background: #ffffff;
        padding: 20px;
        border-radius: 8px;
        box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
        width: 500px;
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