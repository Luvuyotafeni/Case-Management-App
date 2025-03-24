<script setup>
    import { onMounted, ref } from 'vue';
    import Header from './Header.vue';
    import AdminServices from '@/services/AdminService';

    const cases = ref([]);
    const selectedCase = ref([]);
    const showModal = ref (false);
    const fetchCases = async() => {
        try{
            cases.value = await AdminServices.getAllAdminCases();
        } catch(error){
            console.log("Error fetching cases: ", error);
        }
    };

    const openModal = (caseItem) => {
      selectedCase.value
    }

    onMounted(fetchCases);
</script>
<template>
    <div class="container">
        <Header/>
        <div class="dashboard">
            <div class="header">
                <h2>Cases</h2>
                <button class="add-case">Add Case</button>
            </div>
            <table>
                <thead>
                <tr>
                    <th>Username</th>
                    <th>Case Name</th>
                    <th>Case Type</th>
                    <th>Station Name</th>
                    <th>Province</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="caseItem in cases" :key="caseItem.caseId">
                    <td>{{ caseItem.user.name }}</td>
                    <td>{{ caseItem.caseName }}</td>
                    <td>{{ caseItem.caseType }}</td>
                    <td>{{ caseItem.stationName }}</td>
                    <td>{{ caseItem.province }}</td>
                    <td>
                    <button class="see-button">See</button>
                    </td>
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

  .add-case{
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