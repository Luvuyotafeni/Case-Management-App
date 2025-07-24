<script setup>
    import { onMounted, ref } from 'vue';
    import Header from './Header.vue';
    import UsersServices from '@/services/UserService';
    import AuthServices from '@/services/AuthService';

    const user = ref(null);
    const userId = sessionStorage.getItem("userId");
    const editUser = ref({});
    const showProfileModal = ref(false);
    const showOtpModal = ref(false);
    const otpCode = ref("");
    const isSendingOtp = ref(false);
    const isVerifyingOtp = ref(false);
    const otpError = ref("");
    const isUpdatingUser = ref(false);
    const updateError = ref("");
    const updateSuccess = ref("");
    const otpSuccess = ref("");
    const isHoveringImage = ref(false);
    const imageInput = ref(null);
    const isUploading = ref(false);
    const uploadError = ref(null);

    const fetchUser = async() => {
        try{
            const response = await UsersServices.getUserById(userId);
            user.value = response;
        } catch (error){
            console.log("Error fetching user", error);
        }
    };
    // Resend verification email
    const resendVerification = async () => {
    try {
        isSendingOtp.value = true;
        await AuthServices.resendVerification({ email: user.value.email });
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
        email: user.value.email,
        otp: otpCode.value,
        });

        if (response.success) {
        otpSuccess.value = "Email successfully verified!";
        user.value.emailVerified = true;
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

    const openProfileModal = async () => {

        editUser.value ={
            name: user.value.name,
            email: user.value.email,
            phone: user.value.phone,
            role:user.value.role,
            twoStepVerification: user.value.twoStepVerification
        };
        updateError.value = "";
        updateSuccess.value = "";
        showProfileModal.value = true;
    };

    const closeProfileModal = () => {
        showProfileModal.value = false;
        editUser.value = {};
        updateError.value= "";
        updateSuccess.value = "";
    };

    // Updating a user
    const updateUser = async () =>{
        try {
            isUpdatingUser.value= true;
            updateError.value= "";

            const updateData = {};
            if (editUser.value.name !== user.value.name) {
                updateData.name = editUser.value.name;
            }
            if (editUser.value.email !== user.value.email) {
                updateData.email = editUser.value.email;
            }
            if (editUser.value.phone !== user.value.phone) {
                updateData.phone = editUser.value.phone;
            }
            if (editUser.value.twoStepVerification !== user.value.twoStepVerification) {
                updateData.twoStepVerification = editUser.value.twoStepVerification;
            }

            if(Object.keys(updateData).length === 0){
                updateError.value = "No changes detected";
                return;
            }

            const response = await UsersServices.updateUser(userId, updateData);

            user.value = response;
            updateSuccess.value = "Profile updated Successfully";

            // If email was changed, show verification message
            if (updateData.email) {
                updateSuccess.value += " Please check your email for verification.";
                user.value.emailVerified = false; // Email needs re-verification
            }

            setTimeout(()=> {
                closeProfileModal();
            }, 2000);
        } catch (error){
            console.error("Error updating User:", error);
            if(error.response && error.response.data && error.response.data.message){
                updateError.value = error.response.data.message;
            } else {
                updateError.value = "Failed to update profile. Please try again"
            }
        } finally{
            isUpdatingUser.value = false;
        }
    }

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
            user.value = response;
            isUploading.value = false;
        } catch (error) {
            console.error('Error uploading profile picture', error);
            uploadError.value = 'Failed to upload image. Please try again.';
            isUploading.value = false;
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
                        <div 
                            class="profile-img-container" 
                            @mouseenter="handleImageHover(true)" 
                            @mouseleave="handleImageHover(false)"
                        >
                            <img
                                :src="user.profilePictureUrl || 'https://example.com/default-avatar.png'"
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
                            <h2>{{ user.name }} <span
                                :class="user.emailVerified ? 'verified' : 'not-verified'"
                                >
                                {{ user.emailVerified ? "Verified" : "Not Verified" }}
                                </span>
                            </h2>
                            <button
                                v-if="!user.emailVerified"
                                @click="resendVerification"
                                class="resend-btn"
                                :disabled="isSendingOtp"
                            >
                                {{ isSendingOtp ? "Sending..." : "Resend Verification" }}
                            </button>
                            <p v-if="uploadError" class="error-message">{{ uploadError }}</p>
                        </div>
                    </div>
                    <hr />
                    <div class="profile-details">
                        <p><strong>Email:</strong> {{ user.email }}</p>
                        <p><strong>Phone:</strong> {{ user.phone }}</p>
                        <p><strong>Role:</strong> {{ user.role }}</p>
                        <p><strong>Two-step Verification:</strong> {{ user.twoStepVerification ? 'Enabled' : 'Disabled' }}</p>
                        <button class="update-btn" @click="openProfileModal">Update User</button>
                    </div>
                </div>
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
                        <input v-model="editUser.name" />
                    </div>
                    <div class="form-group">
                        <label>Email:</label>
                        <input v-model="editUser.email" />
                    </div>
                    <div class="form-group">
                        <label>Phone:</label>
                        <input v-model="editUser.phone" />
                    </div>
                    <div class="form-group">
                        <label>Role:</label>
                        <input v-model="editUser.role" readonly />
                    </div>
                    <div class="form-group">
                        <label>Two step Verification:</label>
                        <input type="checkbox" v-model="editUser.twoStepVerification" />
                    </div>
                    <p v-if="updateError" class="error-text">{{ updateError }}</p>
                    <p v-if="updateSuccess" class="success-text">{{ updateSuccess }}</p>
                    <div class="modal-actions">
                        <button @click="updateUser"  class="submit-btn" :disabled="isUpdatingUser">
                            {{ isUpdatingUser ? "Saving.." : "Save" }}
                        </button>
                        <button @click="closeProfileModal" class="cancel-btn" :disabled="isUpdatingUser">Cancel</button>
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
    background-color: #F9FAFB;
  }

  .dashboard{
    flex: 1;
    display: flex;
    flex-direction: row;
    margin-top: 20px;
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

.profile-info h2 {
    font-size: 20px;
}

.resend-btn {
    margin-top: 5px;
    padding: 5px 10px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 3px;
    font-size: 12px;
    cursor: pointer;
}

.resend-btn:hover {
    background-color: #0056b3;
}

.resend-btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
}

.error-message {
    color: #f44336;
    font-size: 14px;
    margin-top: 5px;
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

.submit-btn:disabled,
.cancel-btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
}
</style>