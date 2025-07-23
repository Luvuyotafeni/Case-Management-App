<script setup>
    import { onMounted, ref } from 'vue';
    import Header from './Header.vue';
    import UsersServices from '@/services/UserService';

    const user = ref(null);
    const userId = sessionStorage.getItem("userId");
    const showProfileModal = ref(false);
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

    const openProfileModal = async () => {
        showProfileModal.value = true;
    };

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
                        <input v-model="user.name" />
                    </div>
                    <div class="form-group">
                        <label>Email:</label>
                        <input v-model="user.email" />
                    </div>
                    <div class="form-group">
                        <label>Phone:</label>
                        <input v-model="user.phone" />
                    </div>
                    <div class="form-group">
                        <label>Role:</label>
                        <input v-model="user.role" readonly />
                    </div>
                    <div class="form-group">
                        <label>Two step Verification:</label>
                        <input type="checkbox" v-model="user.twoStepVerification" />
                    </div>
                    <div class="modal-actions">
                        <button type="submit" class="submit-btn">Save</button>
                        <button @click="closeProfileModal" class="cancel-btn">Cancel</button>
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