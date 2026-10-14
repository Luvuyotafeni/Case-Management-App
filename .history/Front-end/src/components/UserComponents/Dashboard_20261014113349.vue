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

/* =========================================
   BASE / DESKTOP
   ========================================= */

.container {
    width: 50%;
    min-height: 100vh;
    display: flex;
    flex-direction: row;
    padding: 0;
    background-color: #F9FAFB;
    box-sizing: border-box;
}


/* Dashboard */

.dashboard {
    display: flex;
    flex-direction: column;

    margin-left: 10px;
    margin-top: 100px;

    width: calc(100% - 10px);

    padding: 0 20px 30px;

    box-sizing: border-box;
}


/* =========================================
   TOP SECTION
   ========================================= */

.top-section {
    display: flex;
    justify-content: space-between;
    gap: 20px;

    margin-bottom: 20px;

    min-height: 250px;
}


/* =========================================
   TOTAL CASES CARD
   ========================================= */

.assigned-cases {
    display: flex;
    align-items: center;

    gap: 15px;

    padding: 20px;

    background: white;

    border-radius: 8px;

    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);

    min-width: 220px;

    box-sizing: border-box;
}


.assigned-cases i {
    font-size: 28px;
    color: #4A90E2;
}


.case-info h3 {
    margin: 0 0 5px;
}


.case-count {
    margin: 0;

    font-size: 32px;
    font-weight: bold;

    color: #000428;
}


/* =========================================
   UPCOMING COURT DATES
   ========================================= */

.upcoming-court-dates {
    background: white;

    padding: 20px;

    border-radius: 8px;

    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);

    flex: 1;

    min-width: 0;

    box-sizing: border-box;
}


.upcoming-court-dates h3 {
    margin-top: 0;
}


.upcoming-court-dates table {
    width: 100%;

    border-collapse: collapse;

    min-width: 750px;
}


/* =========================================
   BOTTOM SECTION
   ========================================= */

.bottom-section {
    margin-top: 20px;

    background: white;

    padding: 20px;

    border-radius: 8px;

    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);

    box-sizing: border-box;
}


.bottom-section h3 {
    margin-top: 0;
}


.bottom-section table {
    width: 100%;

    border-collapse: collapse;

    min-width: 900px;
}


/* =========================================
   TABLES
   ========================================= */

table {
    width: 100%;

    border-collapse: collapse;

    background: white;
}


thead {
    background: #ddd;
}


td,
th {
    padding: 10px;

    border: 1px solid #ccc;

    text-align: left;

    white-space: nowrap;
}


/* =========================================
   TABLE SCROLLING
   ========================================= */

.upcoming-court-dates,
.bottom-section {
    overflow-x: auto;
}


/* =========================================
   STATUS STYLING
   ========================================= */

[class^="status-"] {
    font-weight: bold;
}


/* =========================================
   LARGE DESKTOP
   1440px+
   ========================================= */

@media screen and (min-width: 1440px) {

    .dashboard {
        margin-left: 20px;
        margin-top: 110px;

        padding: 0 30px 40px;
    }


    .top-section {
        gap: 25px;

        min-height: 280px;
    }


    .assigned-cases {
        min-width: 260px;

        padding: 25px;
    }


    .assigned-cases i {
        font-size: 32px;
    }


    .case-count {
        font-size: 38px;
    }


    .upcoming-court-dates {
        padding: 25px;
    }


    .bottom-section {
        padding: 25px;
    }


    td,
    th {
        padding: 12px;
    }
}


/* =========================================
   LAPTOP
   1024px - 1439px
   ========================================= */

@media screen and (min-width: 1024px) and (max-width: 1439px) {

    .dashboard {
        margin-left: 10px;
        margin-top: 90px;

        padding: 0 15px 30px;
    }


    .top-section {
        gap: 15px;

        min-height: 230px;
    }


    .assigned-cases {
        min-width: 200px;

        padding: 18px;
    }


    .case-count {
        font-size: 28px;
    }


    .upcoming-court-dates {
        padding: 18px;
    }


    .bottom-section {
        padding: 18px;
    }


    td,
    th {
        padding: 8px;

        font-size: 14px;
    }
}


/* =========================================
   TABLET
   768px - 1023px
   ========================================= */

@media screen and (min-width: 768px) and (max-width: 1023px) {

    .dashboard {
        margin-left: 10px;
        margin-top: 80px;

        padding: 0 12px 25px;
    }


    .top-section {
        gap: 12px;

        min-height: 210px;
    }


    .assigned-cases {
        min-width: 170px;

        padding: 15px;

        gap: 10px;
    }


    .assigned-cases i {
        font-size: 23px;
    }


    .case-info h3 {
        font-size: 15px;
    }


    .case-count {
        font-size: 25px;
    }


    .upcoming-court-dates {
        padding: 15px;
    }


    .upcoming-court-dates h3 {
        font-size: 17px;
    }


    .bottom-section {
        padding: 15px;
    }


    td,
    th {
        padding: 7px;

        font-size: 13px;
    }
}


/* =========================================
   LARGE MOBILE / SMALL TABLET
   481px - 767px
   ========================================= */

@media screen and (min-width: 481px) and (max-width: 767px) {

    .container {
        display: block;

        width: 100%;

        min-height: 100vh;
    }


    .dashboard {
        width: 100%;

        margin-left: 0;

        /*
         * Header is 60px on mobile.
         * Give dashboard space underneath it.
         */
        margin-top: 80px;

        padding: 0 15px 30px;

        box-sizing: border-box;
    }


    /* Stack top cards */

    .top-section {
        flex-direction: column;

        gap: 15px;

        height: auto;

        min-height: 0;

        margin-bottom: 15px;
    }


    /* Total cases */

    .assigned-cases {
        width: 100%;

        min-width: 0;

        height: auto;

        min-height: 110px;

        padding: 20px;
    }


    .assigned-cases i {
        font-size: 28px;
    }


    .case-count {
        font-size: 30px;
    }


    /* Court dates */

    .upcoming-court-dates {
        width: 100%;

        margin-left: 0;

        height: auto;

        padding: 15px;

        box-sizing: border-box;
    }


    .upcoming-court-dates h3 {
        font-size: 18px;
    }


    /* Bottom cases */

    .bottom-section {
        width: 100%;

        margin-top: 15px;

        padding: 15px;

        box-sizing: border-box;
    }


    .bottom-section h3 {
        font-size: 18px;
    }


    /* Tables can scroll horizontally */

    .upcoming-court-dates table {
        min-width: 750px;
    }


    .bottom-section table {
        min-width: 900px;
    }


    td,
    th {
        padding: 8px;

        font-size: 13px;
    }
}


/* =========================================
   MOBILE
   360px - 480px
   ========================================= */

@media screen and (min-width: 360px) and (max-width: 480px) {

    .container {
        display: block;

        width: 100%;

        min-height: 100vh;
    }


    .dashboard {
        width: 100%;

        margin-left: 0;

        /*
         * Mobile Header = 56px
         */
        margin-top: 72px;

        padding: 0 10px 25px;

        box-sizing: border-box;
    }


    /* Stack everything */

    .top-section {
        display: flex;

        flex-direction: column;

        gap: 12px;

        height: auto;

        min-height: 0;

        margin-bottom: 12px;
    }


    /* Total Cases */

    .assigned-cases {
        width: 100%;

        min-width: 0;

        min-height: 100px;

        height: auto;

        padding: 15px;
    }


    .assigned-cases i {
        font-size: 25px;
    }


    .case-info h3 {
        font-size: 16px;
    }


    .case-count {
        font-size: 28px;
    }


    /* Court Dates */

    .upcoming-court-dates {
        width: 100%;

        margin-left: 0;

        height: auto;

        padding: 12px;

        box-sizing: border-box;
    }


    .upcoming-court-dates h3 {
        font-size: 16px;

        margin-bottom: 12px;
    }


    /* Cases */

    .bottom-section {
        width: 100%;

        margin-top: 12px;

        padding: 12px;

        box-sizing: border-box;
    }


    .bottom-section h3 {
        font-size: 16px;
    }


    /* Horizontal scrolling tables */

    .upcoming-court-dates table {
        min-width: 750px;
    }


    .bottom-section table {
        min-width: 900px;
    }


    td,
    th {
        padding: 7px;

        font-size: 12px;
    }
}


/* =========================================
   VERY SMALL PHONES
   BELOW 360px
   ========================================= */

@media screen and (max-width: 359px) {

    .container {
        display: block;

        width: 100%;
    }


    .dashboard {
        width: 100%;

        margin-left: 0;

        /*
         * Mobile header = 52px
         */
        margin-top: 65px;

        padding: 0 8px 20px;
    }


    .top-section {
        flex-direction: column;

        gap: 10px;

        height: auto;

        min-height: 0;
    }


    .assigned-cases {
        width: 100%;

        min-width: 0;

        min-height: 90px;

        padding: 12px;
    }


    .assigned-cases i {
        font-size: 22px;
    }


    .case-info h3 {
        font-size: 14px;
    }


    .case-count {
        font-size: 25px;
    }


    .upcoming-court-dates {
        width: 100%;

        margin-left: 0;

        padding: 10px;
    }


    .upcoming-court-dates h3 {
        font-size: 15px;
    }


    .bottom-section {
        width: 100%;

        margin-top: 10px;

        padding: 10px;
    }


    .bottom-section h3 {
        font-size: 15px;
    }


    .upcoming-court-dates table {
        min-width: 700px;
    }


    .bottom-section table {
        min-width: 850px;
    }


    td,
    th {
        padding: 6px;

        font-size: 11px;
    }
}

</style>