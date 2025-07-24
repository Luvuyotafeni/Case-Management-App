<script setup>
import { ref, onMounted } from 'vue';
import Header from './Header.vue';
import CaseService from '@/services/CaseService';
import CourtDateService from '@/services/CourtDate';

const cases = ref([]);
const courtDates = ref([]);
const userId = sessionStorage.getItem("userId");
const upcomingCourtDates = ref([]);
const totalCases = ref(0);
const loading = ref(true);
const error = ref(null);

// Helper function to format date
const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    try {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    } catch (e) {
        return dateString;
    }
};

// Helper function to get case name by case ID
const getCaseNameById = (caseId) => {
    const foundCase = cases.value.find(c => c.caseId === caseId);
    return foundCase ? foundCase.caseName : 'Unknown Case';
};

// Helper function to get case user name by case ID
const getCaseUserNameById = (caseId) => {
    const foundCase = cases.value.find(c => c.caseId === caseId);
    return foundCase && foundCase.user ? foundCase.user.name : 'Unknown User';
};

const fetchUserCases = async () => {
    try {
        const response = await CaseService.getCasesByUserId(userId);
        cases.value = response;
        totalCases.value = cases.value.length;
    } catch (error) {
        console.error("Error fetching cases:", error);
        error.value = "Failed to fetch cases";
    }
};

const fetchUserCourtDates = async () => {
    try {
        const response = await CourtDateService.getCourtDatesByUserId(userId);
        courtDates.value = response;
        
        // Filter upcoming court dates (future dates only) and sort by date
        const now = new Date();
        upcomingCourtDates.value = courtDates.value
            .filter(courtDate => {
                const courtDateTime = new Date(courtDate.scheduledDateTime);
                return courtDateTime > now;
            })
            .sort((a, b) => new Date(a.scheduledDateTime) - new Date(b.scheduledDateTime))
            .slice(0, 5); // Get only first 5 upcoming dates
            
    } catch (error) {
        console.error("Error fetching court dates:", error);
        error.value = "Failed to fetch court dates";
    }
};

const fetchData = async () => {
    loading.value = true;
    error.value = null;
    
    try {
        await Promise.all([
            fetchUserCases(),
            fetchUserCourtDates()
        ]);
    } catch (err) {
        error.value = "Failed to load dashboard data";
        console.error("Error loading dashboard data:", err);
    } finally {
        loading.value = false;
    }
};

onMounted(fetchData);
</script>

<template>
    <div class="container">
        <Header />
        <div class="dashboard">
            <!-- Loading State -->
            <div v-if="loading" class="loading-state">
                <p>Loading dashboard data...</p>
            </div>
            
            <!-- Error State -->
            <div v-else-if="error" class="error-state">
                <p>{{ error }}</p>
                <button @click="fetchData" class="retry-btn">Retry</button>
            </div>
            
            <!-- Dashboard Content -->
            <template v-else>
                <div class="top-section">
                    <div class="assigned-cases">
                        <i class='bx bxs-briefcase'></i>
                        <div class="case-info">
                            <h3>Total Cases</h3>
                            <p class="case-count">{{ totalCases }}</p>
                        </div>
                    </div>
                    
                    <div class="upcoming-court-dates">
                        <h3>Upcoming Court Dates</h3>
                        <div v-if="upcomingCourtDates.length === 0" class="no-data">
                            <p>No upcoming court dates</p>
                        </div>
                        <table v-else>
                            <thead>
                                <tr>
                                    <th>Court Date</th>
                                    <th>Case Name</th>
                                    <th>Court Name</th>
                                    <th>Court Room</th>
                                    <th>Hearing Type</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="courtDate in upcomingCourtDates" :key="courtDate.courtDateId">
                                    <td>{{ formatDate(courtDate.scheduledDateTime) }}</td>
                                    <td>{{ getCaseNameById(courtDate.relatedCase?.caseId) }}</td>
                                    <td>{{ courtDate.courtName }}</td>
                                    <td>{{ courtDate.courtRoom }}</td>
                                    <td>{{ courtDate.hearingType }}</td>
                                    <td>
                                        <span :class="'status-' + courtDate.status.toLowerCase().replace(' ', '-')">
                                            {{ courtDate.status }}
                                        </span>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                
                <hr />
                
                <div class="bottom-section">
                    <h3>My Cases</h3>
                    <div v-if="cases.length === 0" class="no-data">
                        <p>No cases found</p>
                    </div>
                    <table v-else>
                        <thead>
                            <tr>
                                <th>Case Name</th>
                                <th>Case Number</th>
                                <th>Case Type</th>
                                <th>Assigned Lawyer</th>
                                <th>Lawyer Contact</th>
                                <th>Station Name</th>
                                <th>Province</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="caseItem in cases" :key="caseItem.caseId">
                                <td>{{ caseItem.caseName }}</td>
                                <td>{{ caseItem.caseNumber || 'N/A' }}</td>
                                <td>{{ caseItem.caseType }}</td>
                                <td>{{ caseItem.assignedlawyer?.user?.name || 'No Lawyer Assigned' }}</td>
                                <td>{{ caseItem.assignedlawyer?.user?.phone || 'N/A' }}</td>
                                <td>{{ caseItem.stationName }}</td>
                                <td>{{ caseItem.province }}</td>
                                <td>
                                    <span :class="'status-' + (caseItem.status || 'unknown').toLowerCase().replace(' ', '-')">
                                        {{ caseItem.status || 'Unknown' }}
                                    </span>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                
                
            
            </template>
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
    margin-left: 10px;
    margin-top: 100px;
    width: 100%;
  }
  .top-section {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
    height: 35%;
}

.assigned-cases {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 20px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    height: 90%;
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
    height: 90%;
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