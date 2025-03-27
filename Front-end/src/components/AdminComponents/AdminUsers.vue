<script setup>
  import { onMounted, ref } from 'vue';
  import Header from './Header.vue';
  import UsersServices from '@/services/UserService';
  import AdminServices from '@/services/AdminService';


  const users = ref(null);
  const showModal = ref (false);
  const loadingUser= ref(false);
  const selectedUser = ref([]);


  const fetchUsers = async ()=> {
    try{
      const response = await UsersServices.getUserByRole();
      users.value = response;
    } catch (err){
      console.log("error fetching the Users", err);
    }
  };

  const openModal = async (userId) => {
      try{
        loadingUser.value = true;
        selectedUser.value = await AdminServices.getUserByIdAdmin(userId);
        showModal.value =true;
      } catch (error){
        console.log("Error fetching the user details", error);
      } finally{
        loadingUser .value = false;
      }
    };

    const closeModal = () => {
      showModal.value = false;
      selectedUser.value = null;
    }

  onMounted(fetchUsers);
</script>
<template>
    <div class="container">
        <Header/>
        <div class="dashboard">
            <div class="header">
              <h2>Users</h2>
            </div>
            <table>
              <thead>
                <tr>
                  <th>User Name</th>
                  <th>User Phone</th>
                  <th>User Email</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="user in users" :key="user.userId">
                  <td>{{ user.name }}</td>
                  <td>{{ user.phone }}</td>
                  <td>{{ user.email }}</td>
                  <td><button @click="openModal(user.userId)">See</button></td>
                </tr>
              </tbody>
            </table>
        </div>
        <teleport to="body">
          <div v-if="showModal" class="modal-overlay">
            <div class="modal-content">
              <h2>Case Details</h2>
              <!-- Loading State -->
              <div v-if="loadingUser">Loading case details...</div>

              <div v-else>
                <!-- User Info Section -->
                <div class="user-info">
                  <img
                    :src="selectedUser?.profilePictureUrl || 'https://example.com/default-avatar.png'"
                    alt="User Profile"
                    class="profile-picture"
                  />
                  <div>
                    <h3>{{ selectedUser?.name }}</h3>
                    <p><strong>Email:</strong> {{ selectedUser?.email }}</p>
                    <p><strong>Phone:</strong> {{ selectedUser?.phone }}</p>
                    <p><strong>Role:</strong> {{ selectedUser.role }}</p>
                  </div>
                </div>
                </div>
                <button class="close-button" @click="closeModal">Close</button>
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
    display: flex;
    flex-direction: column;
    margin-left: 20px;
    margin-top: 100px;
    width: 100%;
  }

  .header{
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 20px;
  }
  .add-User{
    padding: 10px 15px;
    background-color: #ddd;
    color: black;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  table{
    width: 100%;
    border-collapse: collapse;
    background: white;
  }

  thead{
    background: #ddd;
  }
  td, th{
    padding: 10px;
    border: 1px solid #ccc;
    text-align: left;
  }

   /* Modal Overlay */
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
  }
  /* Close Button */
.close-button {
  display: block;
  width: 50%;
  text-align: center;
  margin-top: 15px;
  padding: 10px;
  background: #dc3545;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  align-self: center;
}

  /* Modal Content */
  .modal-content {
    background: white;
    padding: 20px;
    border-radius: 8px;
    width: 50%;
    height: 400px;
    max-width: 600px;
    overflow-y: auto;
  }

  /* User Info */
  .user-info {
    display: flex;
    align-items: center;
    gap: 15px;
  }

  .profile-picture {
    width: 200px;
    height: 200px;
    border-radius: 5px;
    border: 2px solid #007bff;
  }

</style>