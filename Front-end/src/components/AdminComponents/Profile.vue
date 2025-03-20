<script setup>
import { ref, onMounted } from "vue";
import AdminServices from "@/services/AdminService";
import Header from "./Header.vue";
import UsersServices from "@/services/UserService";

// Reactive state to store admin details
const admin = ref(null);
const userId = sessionStorage.getItem("userId");

// Fetch admin details on mount
const fetchAdminDetails = async () => {
  try {
    if (userId) {
      const response = await UsersServices.getUserById(userId);
      admin.value = response;
    }
  } catch (error) {
    console.error("Error fetching admin details:", error);
  }
};

onMounted(fetchAdminDetails);
</script>

<template>
  <div class="container">
    <Header />
    <div class="dashboard">
      <main class="dashboard">
        <div class="profile-card" v-if="admin">
          <div class="profile-header">
            <img
              :src="admin.profileImage || 'https://via.placeholder.com/100'"
              alt="User Avatar"
              class="profile-img"
            />
            <div class="profile-info">
              <h2>
                {{ admin.name }}
                <span
                  :class="admin.emailVerified ? 'verified' : 'not-verified'"
                >
                  {{ admin.emailVerified ? "Verified" : "Not Verified" }}
                </span>
              </h2>
            </div>
          </div>
          <hr />
          <div class="profile-details">
            <p><strong>Email:</strong> {{ admin.email }}</p>
            <p><strong>Phone:</strong> {{ admin.phone }}</p>
            <p><strong>Role:</strong> {{ admin.role }}</p>
            <p>
              <strong>Two-step Verification:</strong>
              {{ admin.twoStepVerification ? "Enabled" : "Disabled" }}
            </p>
            <button class="update-btn">Update User</button>
          </div>
        </div>
        <div v-else class="loading">Loading profile...</div>
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
  background-color: #f9fafb;
}

.dashboard {
  flex: 1;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
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
  display: flex;
  align-items: center;
}

.verified {
  color: green;
  font-weight: bold;
  margin-left: 10px;
}

.not-verified {
  color: red;
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

.loading {
  font-size: 18px;
  font-weight: bold;
  text-align: center;
}
</style>
