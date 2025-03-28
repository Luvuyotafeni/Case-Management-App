<script setup>
import { ref, onMounted } from "vue";
import Header from "./Header.vue";
import UsersServices from "@/services/UserService";

import AuthServices from "@/services/AuthService";

// Reactive state to store admin details
const admin = ref(null);
const userId = sessionStorage.getItem("userId");
const showProfileModal = ref(false);
const showOtpModal = ref(false);
const otpCode = ref("");
const isSendingOtp = ref(false);
const isVerifyingOtp = ref(false);
const otpError = ref("");
const otpSuccess = ref("");

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

// Resend verification email
const resendVerification = async () => {
  try {
    isSendingOtp.value = true;
    await AuthServices.resendVerification({ email: admin.value.email });
    showOtpModal.value = true;
  } catch (error) {
    console.error("Error resending verification email:", error);
  } finally {
    isSendingOtp.value = false;
  }
};

// Verify OTP
const verifyOtp = async () => {
  try {
    isVerifyingOtp.value = true;
    const response = await AuthServices.verifyOtp({
      email: admin.value.email,
      otp: otpCode.value,
    });

    if (response.success) {
      otpSuccess.value = "Email successfully verified!";
      admin.value.emailVerified = true;
      setTimeout(() => {
        showOtpModal.value = false;
        otpSuccess.value = "";
      }, 2000);
    } else {
      otpError.value = "Invalid OTP. Please try again.";
    }
  } catch (error) {
    otpError.value = "Error verifying OTP.";
    console.error("OTP Verification Error:", error);
  } finally {
    isVerifyingOtp.value = false;
  }
};
// Close OTP Modal
const closeOtpModal = () => {
  showOtpModal.value = false;
  otpCode.value = "";
  otpError.value = "";
};

const openProfileModal = async (userId)=>{
  showProfileModal.value=true
}
const closeProfileModal = () => {
  showProfileModal.value = false;
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
              <button
                v-if="!admin.emailVerified"
                @click="resendVerification"
                class="resend-btn"
                :disabled="isSendingOtp"
              >
                {{ isSendingOtp ? "Sending..." : "Resend Verification" }}
              </button>
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
            <button class="update-btn" @click="openProfileModal">Update User</button>
          </div>
        </div>
        <div v-else class="loading">Loading profile...</div>
      </main>
    </div>

    <!-- profile modal -->
    <teleport to="body" v-if="showProfileModal">
      <div class="modal-overlay">
        <div class="modal-content">
          <div class="modal-header">
            <h2>Edit Admin</h2>
          </div>
          <div class="form-group">
            <label>Name:</label>
            <input
              
            />
          </div>
          <div class="form-group">
            <label>Email:</label>
            <input
              
            />
          </div>
          <div class="form-group">
            <label>Phone:</label>
            <input
              
            />
          </div>
          <div class="form-group">
            <label>Role:</label>
            <input
              
            />
          </div>
          <div class="form-group">
            <label>Two step Verification:</label>
            
          </div>
          <div class="modal-actions">
            <button type="submit" class="submit-btn">Save</button>
            <button @click="closeProfileModal" class="cancel-btn">Cancel</button>
          </div>
        </div>
      </div>
    </teleport>

     <!-- OTP Verification Modal -->
     <teleport to="body" v-if="showOtpModal">
      <div class="modal-overlay">
        <div class="modal-content">
          <div class="modal-header">
            <h2>Enter OTP</h2>
          </div>
          <p class="modal-description">
            Please enter the OTP sent to your email to verify your account.
          </p>
          <div class="form-group">
            <input
              v-model="otpCode"
              type="text"
              placeholder="Enter OTP"
              class="otp-input"
              maxlength="6"
            />
          </div>
          <p v-if="otpError" class="error-text">{{ otpError }}</p>
          <p v-if="otpSuccess" class="success-text">{{ otpSuccess }}</p>
          <div class="modal-actions">
            <button @click="verifyOtp" class="submit-btn" :disabled="isVerifyingOtp">
              {{ isVerifyingOtp ? "Verifying..." : "Verify" }}
            </button>
            <button @click="closeOtpModal" class="cancel-btn">Cancel</button>
          </div>
        </div>
      </div>
    </teleport>
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
 /* Modal Styles */
 .modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 30px;
  border-radius: 10px;
  width: 500px;
  max-width: 90%;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.modal-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.submit-btn {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
}

.cancel-btn {
  background-color: #f44336;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
}

.submit-btn:disabled,
.cancel-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
