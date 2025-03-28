<script setup>
    import { onMounted, ref } from 'vue';
    import Header from './Header.vue';
    import AdminServices from '@/services/AdminService';

    const admins = ref([]);
    const showAddAdminModal = ref(false);
    const isloading = ref(false);
    const adminData = ref({
      name: '',
      email: '',
      phone: '',
      adminSpecificColumn: '',
      profilePictureUrl: '',
      password: ''
    });

    const fetchAdmins = async() => {
      try{
        admins.value=  await AdminServices.getALlAdmins();
        console.log(admins)
      } catch (error){
        console.log("error fetching admins", error);
      }
    };

    const openAddAdminModel = () => {
      showAddAdminModal.value = true;
    };

    const closeAddAdminModal = () => {
      showAddAdminModal.value = false;
      //resetting the form values
      adminData.value = {
        name: '',
        email: '',
        phone: '',
        adminSpecificColumn: '',
        profilePictureUrl: '',
        password: ''
      }
    };
    const createAdmin = async () =>{
      try{
        isloading.value= true;
        const newAdmin = await AdminServices.createAdmin(adminData.value);
        admins.value.push(newAdmin);
        closeAddAdminModal();

      } catch (error){
        console.log("Error creating lawyer:", error);
      } finally{
        isloading.value= false;
      }
    }

    onMounted(fetchAdmins);
</script>
<template>
     <div class="container">
        <Header/>
        <div class="dashboard">
            <div class="header">
              <h2>Admins</h2>
              <button class="add-admin" @click="openAddAdminModel">Add an Admin</button>
            </div>
            <table>
              <thead>
                <tr>
                  <th>Admin Name</th>
                  <th>Admin Specialization</th>
                  <th>Admin Email</th>
                  <th>Admin Phone</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="admin in admins" :key="admin.adminId">
                  <td>{{ admin.user.name }}</td>
                  <td>{{ admin.adminSpecificColumn }}</td>
                  <td>{{ admin.user.email }}</td>
                  <td>{{ admin.user.phone }}</td>
                  <td>
                    <i class="bx bx-show"></i>
                  </td>
                </tr>
              </tbody>
            </table>
        </div>

        <!-- create lawyer modal -->
        <teleport to="body">
          <div v-if="showAddAdminModal" class="modal-overlay">
            <div class="modal-content">
              <div class="modal-header">
                <h2> Add new Admin</h2>
              </div>
              <form @submit.prevent="createAdmin">
                <div class="form-group">
                  <label for="name">Name</label>
                  <input
                    v-model="adminData.name"
                    type="text"
                    required
                    placeholder="Enter Admins full name"
                  />
                </div>
                <div class="form-group">
                  <label for="email">Email</label>
                  <input
                    v-model="adminData.email"
                    type="email"
                    required
                    placeholder="Enter Admin's email"
                  />
                  
                </div>
                <div class="form-group">
                  <label for="phone">Phone</label>
                  <input
                    v-model="adminData.phone"
                    type="text"
                    required
                    placeholder="Enter Admin's Phone number"
                  />
                  
                </div>
                <div class="form-group">
                  <label for="specialization">Specialization</label>
                  <select
                    v-model="adminData.adminSpecificColumn"
                    required
                  >
                    <option value="" disabled>Select Specialization</option>
                    <option value="System Administrator" >System Administrator</option>
                  </select>
                </div>
                <div class="form-group">
                  <label>Password</label>
                  <input
                    v-model="adminData.password"
                    type="password"
                    required
                    placeholder="set initial password"
                  />
                </div>
                <div class="modal-actions">
                  <button
                    type="submit"
                    class="submit-btn"
                    :disabled="isloading"
                  >
                    {{ isloading? 'Creating...':'Create Admin' }}
                  </button>
                  <button
                    type="button"
                    class="cancel-btn"
                    @click="closeAddAdminModal"
                    :disabled="isloading"
                  > 
                  Cancel
                  </button>
                </div>
              </form>
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
  .add-admin{
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