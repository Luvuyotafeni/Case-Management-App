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
const isHoveringImage = ref(false);
const imageInput = ref(null);
const isUploading = ref(false);
const uploadError = ref(null);

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

const handleImageHover = (isHovering) => {
  isHoveringImage.value = isHovering;
};

const triggerImageUpload = () => {
  // Trigger the hidden file input click
  if (imageInput.value) {
    imageInput.value.click();
  }
};

const handleImageChange = async (event) => {
  const file = event.target.files[0];
  if (!file) return;

  // Check if file is an image
  if (!file.type.match('image.*')) {
    uploadError.value = 'Please select an image file';
    return;
  }

  // Check file size (limit to 5MB)
  if (file.size > 5 * 1024 * 1024) {
    uploadError.value = 'Image size should be less than 5MB';
    return;
  }

  try {
    isUploading.value = true;
    uploadError.value = null;
    const response = await UsersServices.updateProfilePicture(userId, file);
    // Update the user data with the new profile picture
    admin.value = response;
    isUploading.value = false;
  } catch (error) {
    console.error('Error uploading profile picture', error);
    uploadError.value = 'Failed to upload image. Please try again.';
    isUploading.value = false;
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
            <div class="profile-img-container" 
            @mouseenter="handleImageHover(true)" 
            @mouseleave="handleImageHover(false)"
            >
            <img
            :src="admin.profilePictureUrl || 'https://example.com/default-avatar.png'"
            alt="User Profile"
            class="profile-img"
            :class="{ 'img-uploading': isUploading }"
            />
            <div class="profile-img-overlay" v-if="isHoveringImage && !isUploading">
              <button @click="triggerImageUpload" class="change-img-btn">
                Change Image
              </button>
            </div>
            <div class="profile-img-overlay uploading" v-if="isUploading">
              <div class="spinner"></div>
              <span>Uploading...</span>
            </div>
            <input 
            type="file" 
            ref="imageInput" 
            @change="handleImageChange" 
            accept="image/*" 
            class="hidden-input"
            />
          </div>
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

    <!-- edit profile modal -->
    <teleport to="body" v-if="showProfileModal">
      <div class="modal-overlay">
        <div class="modal-content">
          <div class="modal-header">
            <h2>Edit Profile</h2>
          </div>
          <div class="form-group">
            <label>Name:</label>
            <input v-model="admin.name" />
          </div>
          <div class="form-group">
            <label>Email:</label>
            <input v-model="admin.email" />
          </div>
          <div class="form-group">
            <label>Phone:</label>
            <input v-model="admin.phone" />
          </div>
          <div class="form-group">
            <label>Role:</label>
            <input v-model="admin.role" readonly />
          </div>
          <div class="form-group">
            <label>Two step Verification:</label>
            <input type="checkbox" v-model="admin.twoStepVerification" />
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
    width: 500px;
}

.profile-header {
    display: flex;
    align-items: center;
}

.profile-img-container {
    position: relative;
    width: 100px;
    height: 100px;
    border-radius: 50%;
    margin-right: 15px;
    overflow: hidden;
}

.profile-img {
    width: 100%;
    height: 100%;
    border-radius: 50%;
    object-fit: cover;
    transition: filter 0.3s ease;
}

.img-uploading {
    filter: blur(2px);
}

.profile-img-overlay {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    opacity: 0;
    transition: opacity 0.3s ease;
    border-radius: 50%;
}

.profile-img-container:hover .profile-img-overlay {
    opacity: 1;
}

.uploading {
    opacity: 1;
    flex-direction: column;
    color: white;
}
.change-img-btn {
    background-color: transparent;
    border: 1px solid white;
    color: white;
    padding: 5px 10px;
    border-radius: 4px;
    font-size: 12px;
    cursor: pointer;
    transition: background-color 0.3s;
}

.change-img-btn:hover {
    background-color: rgba(255, 255, 255, 0.2);
}

.hidden-input {
    display: none;
}

.spinner {
    border: 3px solid rgba(255, 255, 255, 0.3);
    border-radius: 50%;
    border-top: 3px solid white;
    width: 20px;
    height: 20px;
    animation: spin 1s linear infinite;
    margin-bottom: 5px;
}

@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
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
