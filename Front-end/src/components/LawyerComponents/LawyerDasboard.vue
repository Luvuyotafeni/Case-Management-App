<script setup>
import { ref, onMounted } from 'vue';
import Header from './Header.vue';
import CaseService from '@/services/CaseService';

const assignedCases = ref([]);
const upcomingCourtDates = ref([]);
const totalAssignedCases = ref(0);
const userId = sessionStorage.getItem("userId");

const fetchLawyerCases = async () => {
    try {
        assignedCases.value = await CaseService.getCaseByAssignedLaywyerUserId(userId);
        totalAssignedCases.value = assignedCases.value.length;
        upcomingCourtDates.value = assignedCases.value
            .filter(caseItem => caseItem.courtDate)
            .slice(0, 5);
    } catch (error) {
        console.error("Error fetching lawyer cases:", error);
    }
};

onMounted(fetchLawyerCases);
</script>
<template>
     <div class="container">
        <Header/>
        <div class="dashboard">
          <div class="top-section">
                <div class="assigned-cases">
                    <i class='bx bxs-briefcase'></i>
                    <h3>Total Assigned Cases</h3>
                    <p>{{ totalAssignedCases }}</p>
                </div>
                <div class="upcoming-court-dates">
                    <h3>Upcoming Court Dates</h3>
                    <table>
                        <thead>
                            <tr>
                                <th>Court Date</th>
                                <th>Case Name</th>
                                <th>Case User</th>
                                <th>Court Venue</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="courtCase in upcomingCourtDates" :key="courtCase.caseId">
                                <td>{{ courtCase.courtDate }}</td>
                                <td>{{ courtCase.caseName }}</td>
                                <td>{{ courtCase.caseUser.name }}</td>
                                <td>{{ courtCase.courtVenue }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <hr />
            <div class="bottom-section">
                <h3>Assigned Cases</h3>
                <table>
                  <thead>
                    <tr>
                      <th>Username</th>
                      <th>Case Name</th>
                      <th>Case Type</th>
                      <th>Station Name</th>
                      <th>Province</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="caseItem in assignedCases" :key="caseItem.caseId">
                      <td>{{ caseItem.user.name }}</td>
                      <td>{{ caseItem.caseName }}</td>
                      <td>{{ caseItem.caseType }}</td>
                      <td>{{ caseItem.stationName }}</td>
                      <td>{{ caseItem.province }}</td>
                    </tr>
                  </tbody>
                </table>
            </div>
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
    margin-left: 100px;
    margin-top: 100px;
    width: 100%;
  }
  .top-section {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
    height: 50%;
}

.assigned-cases {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 20px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    height: 50%;
}

.assigned-cases i {
    font-size: 24px;
    color: #4A90E2;
}

.upcoming-court-dates {
    background: white;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    flex-grow: 1;
    margin-left: 20px;
}

.upcoming-court-dates table {
    width: 100%;
    border-collapse: collapse;
}

.upcoming-court-dates th, .upcoming-court-dates td {
    border: 1px solid #ddd;
    padding: 8px;
    text-align: left;
}

.bottom-section {
    margin-top: 20px;
    background: white;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.bottom-section ul {
    list-style-type: none;
    padding: 0;
}

.bottom-section li {
    padding: 10px;
    border-bottom: 1px solid #ddd;
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