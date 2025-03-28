<script setup>
  import { ref, onMounted } from "vue";
  import Header from "./Header.vue";
  import AdminServices from "@/services/AdminService";
  import LawyersService from "@/services/LawyersService";

  // Reactive state variables
  const totalCases = ref(0);
  const totalLawyers = ref(0);
  const allCases = ref([]);
  const allLawyers = ref([]);

  // Fetching total cases
  const fetchTotalCases = async () => {
    try {
      allCases.value = await AdminServices.getAllAdminCases();
      totalCases.value = allCases.value.length;
    } catch (err) {
      console.error("Error fetching cases:", err);
    }
  };

  // Fetching total lawyers
  const fetchTotalLawyers = async () => {
    try {
      allLawyers.value = await LawyersService.getAllLawyers();
      totalLawyers.value = allLawyers.value.length;
    } catch (err) {
      console.error("Error fetching lawyers:", err);
    }
  };

  // Fetch all data when the component is mounted
  onMounted(() => {
    fetchTotalCases();
    fetchTotalLawyers();
  });
</script>
  <template>
      <div class="container">
          <Header/>
          <div class="dashboard">
        <!-- Top Section -->
        <div class="top-section">
          <div class="statistics">
            <!-- Total Cases -->
            <div class="stat-box">
              <i class="bx bxs-briefcase"></i>
              <div>
                <h3>Total Cases</h3>
                <p>{{ totalCases }}</p>
              </div>
            </div>

            <!-- Total Lawyers -->
            <div class="stat-box">
              <i class="bx bx-briefcase-alt-2"></i>
              <div>
                <h3>Total Lawyers</h3>
                <p>{{ totalLawyers }}</p>
              </div>
            </div>
          </div>
          <!-- Upcoming Court Dates Table -->
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
                <tr v-for="(courtCase, index) in upcomingCourtDates" :key="index">
                  <td>{{ courtCase.courtDate }}</td>
                  <td>{{ courtCase.caseName }}</td>
                  <td>{{ courtCase.caseUser }}</td>
                  <td>{{ courtCase.courtVenue }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

      

        <!-- All Cases Table -->
        <div class="all-cases">
          <h3>All Cases</h3>
          <table>
            <thead>
              <tr>
                <th>Case ID</th>
                <th>Case Name</th>
                <th>Client</th>
                <th>Assigned Lawyer</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="caseItem in allCases" :key="caseItem.caseId">
                <td>{{ caseItem.caseNumber }}</td>
                <td>{{ caseItem.caseType }}</td>
                <td>{{ caseItem.user.name }}</td>
                <td>{{ caseItem.assignedlawyer?.user?.name || "Not Assigned" }}</td>
                
                <td>{{ caseItem.status }}</td>
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
      margin-left: 10px;
      margin-top: 100px;
      width: 100%;
    }
    .top-section {
      display: flex;
      flex-direction: row;
      margin-bottom: 20px;
      height: 30%;
    }

    .statistics{
      display: flex;
      flex-direction: column ;
      justify-content: space-between;
      width:300px;
    }

    .stat-box {
      display: flex;
      align-items: center;
      background: #fff;
      padding: 15px;
      border-radius: 10px;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
      width: 250px;
      height: 30%;
    }

    .stat-box i {
      font-size: 30px;
      margin-right: 10px;
      color: #4a90e2;
    }

    .stat-box h3 {
      margin: 0;
      font-size: 16px;
    }

    .stat-box p {
      margin: 0;
      font-size: 20px;
      font-weight: bold;
    }

    .upcoming-court-dates,
    .all-cases {
      background: #fff;
      padding: 15px;
      border-radius: 10px;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
      
    }

    .upcoming-court-dates{
      width: 65%;
    }

    .all-cases{
      margin-top: 20px;
    }
    .upcoming-court-dates h3,
    .all-cases h3 {
      margin-bottom: 10px;
    }

    table {
      width: 100%;
      border-collapse: collapse;
    }

    th, td {
      padding: 10px;
      border: 1px solid #ddd;
      text-align: left;
    }

    th {
      background-color: #f4f4f4;
    }

  </style>