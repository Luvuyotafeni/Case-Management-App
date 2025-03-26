<script setup>
  import { onMounted, ref } from 'vue';
  import Header from './Header.vue';
  import UsersServices from '@/services/UserService';


  const users = ref(null);
  const fetchUsers = async ()=> {
    try{
      const response = await UsersServices.getUserByRole();
      users.value = response;
    } catch (err){
      console.log("error fetching the Users", err);
    }
  };

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
                  <td><button>See</button></td>
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
</style>