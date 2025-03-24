<script setup>
    import { onMounted, ref } from 'vue';
    import Header from './Header.vue';
    import CaseService from '@/services/CaseService';


    const cases = ref(null);
    const userId = sessionStorage.getItem("userId");
    const selectedCase = ref([]);
    const showModal = ref (false);
    const loadingCase = ref(false);

    const fetchUserCases = async ()=> {
        try{
            const response = await CaseService.getCasesByUserId(userId);
            cases.value = response;
        } catch(error){
            console.log("error fetching cases", error);
        }
    };

    const openModal = async (caseId) => {
      try{
        loadingCase.value = true;
        selectedCase.value = await CaseService.getCaseById(caseId);
        showModal.value =true;
      } catch (error){
        console.log("Error fetching the case details", error);
      } finally{
        loadingCase.value = false;
      }
    };

    const closeModal = () => {
      showModal.value = false;
      selectedCase.value = null;
    }

    onMounted(fetchUserCases);
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
            <td>Case Number</td>
            <th>Case Name</th>
            <th>Case Type</th>
            <th>Station Name</th>
            <th>Occurrence Date</th>
            <th>Province</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="caseItem in cases" :key="caseItem.caseId">
            <td>{{ caseItem.caseNumber }}</td>
            <td>{{ caseItem.caseName }}</td>
            <td>{{ caseItem.caseType }}</td>
            <td>{{ caseItem.stationName }}</td>
            <td>{{ caseItem.occurrenceDate }}</td>
            <td>{{ caseItem.province }}</td>
            <td>
              <button class="see-button" @click="openModal(caseItem.caseId)">See</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <!-- Modal for Case Details -->
  <teleport to="body">
    <div v-if="showModal" class="modal-overlay">
      <div class="modal-content">
        <h2>Case Details</h2>

        <!-- Loading State -->
        <div v-if="loadingCase">Loading case details...</div>

        <div v-else>
          <!-- User Info Section -->
          <div class="user-info">
            <img
              :src="selectedCase?.user?.profilePictureUrl || 'https://example.com/default-avatar.png'"
              alt="User Profile"
              class="profile-picture"
            />
            <div>
              <h3>{{ selectedCase?.user?.name }}</h3>
              <p><strong>Email:</strong> {{ selectedCase?.user?.email }}</p>
              <p><strong>Phone:</strong> {{ selectedCase?.user?.phone }}</p>
            </div>
          </div>

          <hr />

          <!-- Case Details Section -->
          <div class="case-details">
            <p><strong>Case Name:</strong> {{ selectedCase?.caseName }}</p>
            <p><strong>Case Number:</strong> {{ selectedCase?.caseNumber }}</p>
            <p><strong>Type:</strong> {{ selectedCase?.caseType }}</p>
            <p><strong>Description:</strong> {{ selectedCase?.description || "No description provided" }}</p>
            <p><strong>Occurrence Date:</strong> {{ selectedCase?.occurrenceDate }}</p>
            <p><strong>Station:</strong> {{ selectedCase?.stationName }} ({{ selectedCase?.province }})</p>
            <p><strong>Officer:</strong> {{ selectedCase?.officerName }} ({{ selectedCase?.officerContact }})</p>
          </div>

          <hr />

          <!-- Documents Section -->
          <div class="documents">
            <h3>Documents</h3>
            <p v-if="!selectedCase?.documents?.length">No documents available.</p>
            <ul v-else>
              <li v-for="doc in selectedCase?.documents" :key="doc.id">
                <a :href="doc.fileUrl" target="_blank">
                  <i class="bx bx-file"></i> {{ doc.fileName }}
                </a>
              </li>
            </ul>
          </div>

          <button class="close-button" @click="closeModal">Close</button>
        </div>
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
   /* Modal Overlay */
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
}

/* Modal Content */
.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 50%;
  height: 700px;
  max-width: 600px;
}

/* User Info */
.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.profile-picture {
  width: 70px;
  height: 70px;
  border-radius: 50%;
  border: 2px solid #007bff;
}

/* Case Details */
.case-details {
  margin-top: 10px;
}

/* Documents Section */
.documents {
  margin-top: 15px;
}

.documents ul {
  list-style: none;
  padding: 0;
}

.documents li {
  margin-bottom: 5px;
}

.documents a {
  text-decoration: none;
  color: #007bff;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 5px;
}

.documents a:hover {
  text-decoration: underline;
}

.bx-file {
  font-size: 108px;
}

/* Close Button */
.close-button {
  display: block;
  width: 50%;
  text-align: center;
  margin-top: 15px;
  padding: 10px;
  background: #dc3545;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  align-self: center;
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