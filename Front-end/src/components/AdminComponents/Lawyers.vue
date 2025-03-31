<script setup>
  import { onMounted, ref } from 'vue';
  import Header from './Header.vue';
  import LawyersService from '@/services/LawyersService';
import AdminServices from '@/services/AdminService';

  const lawyers = ref([]);
  const showAddLawyerModal = ref (false);
  const isloading = ref(false);
  const lawyerData = ref({
    name: '',
    email: '',
    phone: '',
    specialization: '',
    profilePictureUrl: '',
    password: ''
  });

  const fetchAllLawyers = async ()=>{
    try{
      lawyers.value = await LawyersService.getAllLawyers();    
    } catch (error){
      console.log("error fetching lawyers", error);
    }
  } ;

  const openAddLawyerModal = ()=> {
    showAddLawyerModal.value= true;
  }

  const closeAddLawyerModal = () =>{
    showAddLawyerModal.value = false;
    //resetting the form values
    lawyerData.value = {
      name: '',
      email: '',
      phone: '',
      specialization: '',
      profilePictureUrl: '',
      password: ''
    }
  };

  const createLawyer = async () =>{
    try{
      isloading.value= true;
      const newLawyer = await AdminServices.createLawyer(lawyerData.value);
      lawyers.value.push(newLawyer);
      closeAddLawyerModal();

    } catch (error){
      console.log("Error creating lawyer:", error);
    } finally{
      isloading.value= false;
    }
  }

  onMounted(fetchAllLawyers);
</script>
<template>
    <div class="container">
        <Header/>
        <div class="dashboard">
            <div class="header">
              <h2>Lawyers</h2>
              <button class="add-lawyer" @click="openAddLawyerModal">Add A Lawyer</button>
            </div>
            <table>
              <thead>
                <tr>
                  <th>Lawyer Name</th>
                  <th>Specialization</th>
                  <th>Lawyer Phone</th>
                  <th>Lawyer Email</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="lawyer in lawyers" :key="lawyer.lawyerId">
                  <td>{{ lawyer.user.name }}</td>
                  <td>{{ lawyer.specialization }}</td>
                  <td>{{ lawyer.user.phone }}</td>
                  <td>{{ lawyer.user.email }}</td>
                  <td>
                    <i class="bx bx-show" ></i>
                  </td>
                </tr>
              </tbody>
            </table>
        </div>


        <!-- create lawyer modal -->
        <teleport to="body">
          <div v-if="showAddLawyerModal" class="modal-overlay">
            <div class="modal-content">
              <div class="modal-header">
                <h2> Add new Lawyer</h2>
              </div>
              <form @submit.prevent="createLawyer">
                <div class="form-group">
                  <label for="name">Name</label>
                  <input
                    v-model="lawyerData.name"
                    type="text"
                    required
                    placeholder="Enter lawyer's full name"
                  />
                </div>
                <div class="form-group">
                  <label for="email">Email</label>
                  <input
                    v-model="lawyerData.email"
                    type="email"
                    required
                    placeholder="Enter lawyer's email"
                  />
                  
                </div>
                <div class="form-group">
                  <label for="phone">Phone</label>
                  <input
                    v-model="lawyerData.phone"
                    type="text"
                    required
                    placeholder="Enter lawyer's Phone number"
                  />
                  
                </div>
                <div class="form-group">
                  <label for="specialization">Specialization</label>
                  <select
                    v-model="lawyerData.specialization"
                    required
                  >
                    <option value="" disabled>Select Specialization</option>
                    <option value="Criminal Law" >Criminal Law</option>
                    <option value="Corporate Law" >Corporate Law</option>
                    <option value="Family Law" >Family Law</option>
                    <option value="Intellectual Law">Intellectual Law</option>
                    <option value="Civil right" >Civil right</option>
                  </select>
                </div>
                <div class="form-group">
                  <label>Password</label>
                  <input
                    v-model="lawyerData.password"
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
                    {{ isloading? 'Creating...':'Create Lawyer' }}
                  </button>
                  <button
                    type="button"
                    class="cancel-btn"
                    @click="closeAddLawyerModal"
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

  .header{
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }

  .add-lawyer{
    padding: 10px 15px;
    background-color: #ddd;
    color: black;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }

  .dashboard{
    display: flex;
    flex-direction: column;
    margin-left: 20px;
    margin-top: 100px;
    width: 100%;
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



.submit-btn:disabled,
.cancel-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>