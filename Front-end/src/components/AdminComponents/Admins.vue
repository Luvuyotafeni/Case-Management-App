<script setup>
    import { onMounted, ref } from 'vue';
    import Header from './Header.vue';
    import AdminServices from '@/services/AdminService';

    const admins = ref([]);

    const fetchAdmins = async() => {
      try{
        admins.value=  await AdminServices.getALlAdmins();
        console.log(admins)
      } catch (error){
        console.log("error fetching admins", error);
      }
    };

    onMounted(fetchAdmins);
</script>
<template>
     <div class="container">
        <Header/>
        <div class="dashboard">
            <div class="header">
              <h2>Admins</h2>
              <buttonv class="add-admin">Add an Admin</buttonv>
            </div>
            <table>
              <thead>
                <tr>
                  <th>Admin Name</th>
                  <th>Admin Specialization</th>
                  <th>Admin Email</th>
                  <th>Admin Phone</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="admin in admins" :key="admin.adminId">
                  <td>{{ admin.user.name }}</td>
                  <td>{{ admin.adminSpecificColumn }}</td>
                  <td>{{ admin.user.email }}</td>
                  <td>{{ admin.user.phone }}</td>
                </tr>
              </tbody>
            </table>
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
</style>