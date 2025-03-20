<script setup>
  import { onMounted, ref } from 'vue';
  import Header from './Header.vue';
  import LawyersService from '@/services/LawyersService';

  const lawyers = ref([]);

  const fetchAllLawyers = async ()=>{
    try{
      lawyers.value = await LawyersService.getAllLawyers();    
    } catch (error){
      throw error;
    }
  } ;

  onMounted(fetchAllLawyers);
</script>
<template>
    <div class="container">
        <Header/>
        <div class="dashboard">
            <div class="header">
              <h2>Lawyers</h2>
              <button class="add-lawyer">Add A Lawyer</button>
            </div>
            <table>
              <thead>
                <tr>
                  <th>Lawyer Name</th>
                  <th>Specialization</th>
                  <th>Lawyer Phone</th>
                  <th>Lawyer Email</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="lawyer in lawyers" :key="lawyer.lawyerId">
                  <td>{{ lawyer.user.name }}</td>
                  <td>{{ lawyer.specialization }}</td>
                  <td>{{ lawyer.user.phone }}</td>
                  <td>{{ lawyer.user.email }}</td>
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
</style>