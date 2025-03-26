<script setup>
    import { onMounted, ref } from 'vue';
    import Header from './Header.vue';
    import UsersServices from '@/services/UserService';

    const user = ref(null);
    const userId = sessionStorage.getItem("userId");
    const showProfileModal = ref(false);

    const fetchUser = async() => {
        try{
            const response = await UsersServices.getUserById(userId);
            user.value = response;
        } catch (error){
            console.log("Error fetching user", error);
        }
    };
    const openProfileModal = async (userId)=>{
    showProfileModal.value=true
    }
    const closeProfileModal = () => {
    showProfileModal.value = false;
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

        <!-- edit profile modal -->
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