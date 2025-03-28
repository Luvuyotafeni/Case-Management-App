<script setup>
    import { onMounted, ref } from 'vue';
    import Header from './Header.vue';
    import AdminServices from '@/services/AdminService';
    import CaseService from '@/services/CaseService';
    import LawyersService from '@/services/LawyersService';

    const cases = ref([]);
    const selectedCase = ref(null);
    const showModal = ref(false);
    const loadingCase = ref(false);
    const lawyers = ref([]);
    const showLawyerSelect = ref(false);
    
    const selectedLawyer = ref(null);
    const selectedLawyerId = ref(null);
    const assigningLawyer = ref(false);

    const fetchCases = async () => {
        try {
            cases.value = await AdminServices.getAllAdminCases();
        } catch (error) {
            console.log("Error fetching cases: ", error);
        }
    };

    const openModal = async (caseId) => {
        try {
            loadingCase.value = true;
            selectedCase.value = await CaseService.getCaseById(caseId);
            showModal.value = true;
            console.log(selectedCase.value.case)
        } catch (error) {
            console.log("Error fetching the case details", error);
        } finally {
            loadingCase.value = false;
        }
    };

    const closeModal = () => {
        showModal.value = false;
        selectedCase.value = null;
        showLawyerSelect.value = false;
    };

    const fetchLawyers = async () => {
        try {
            lawyers.value = await LawyersService.getAllLawyers();
        } catch (error) {
            console.log("Error fetching lawyers:", error);
        }
    };
    const selectLawyer = async (lawyerId) => {
      try {
          // Fetch full lawyer details by ID
          const fullLawyerDetails = await LawyersService.getLawyerById(lawyerId);
          
          // Update both selectedLawyer and selectedLawyerId
          selectedLawyer.value = fullLawyerDetails;
          selectedLawyerId.value = lawyerId;
          
          console.log('Selected Lawyer Details:', fullLawyerDetails);
      } catch (error) {
          console.error('Error fetching lawyer details:', error);
          alert('Failed to fetch lawyer details');
          selectedLawyer.value = null;
          selectedLawyerId.value = null;
      }
    };

    const assignLawyer = async () => {
        // Validate both case and lawyer selection
        if (!selectedLawyer.value || !selectedCase.value) {
            console.error('No lawyer or case selected');
            alert('Please select a lawyer and case');
            return;
        }

        try {
            assigningLawyer.value = true;
            
            // Use the confirmed lawyer ID and case ID
            const response = await CaseService.assignLawyer(
                selectedCase.value.caseId,  // Ensure this is the correct case ID
                selectedLawyer.value.lawyerId  // Ensure this is the correct lawyer ID
            );

            console.log('Lawyer assignment response:', response);

            // Update the case with the assigned lawyer
            selectedCase.value.assignedlawyer = selectedLawyer.value;
            
            // Reset selection states
            showLawyerSelect.value = false;
            selectedLawyer.value = null;
            selectedLawyerId.value = null;

            // Optional: Refresh cases to reflect the update
            await fetchCases();
        } catch (error) {
            console.error('Error assigning lawyer:', error);
            alert('Failed to assign lawyer');
        } finally {
            assigningLawyer.value = false;
        }
      };
    

    onMounted(fetchCases);

</script>
<template>
  <div class="container">
    <Header />
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
              <i class="bx bx-show" @click="openModal(caseItem.caseId)"></i>
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
          <!-- Assigned Lawyer Section -->
          <div class="lawyer-info">
              <h3> Assigned Lawyer</h3>

              <div v-if="selectedCase?.assignedlawyer">
                  <p><strong>Name: </strong>{{ selectedCase.assignedlawyer.user.name }}</p>
                  <p><strong>Email: </strong>{{ selectedCase.assignedlawyer.user.email }}</p>
                  <p><strong>Phone: </strong>{{ selectedCase.assignedlawyer.user.phone }}</p>
                  <p><strong>Specialization: </strong>{{ selectedCase.assignedlawyer.specialization }}</p>
              </div>

              <div v-else>
                  <button class="assign-lawyer-button" @click="fetchLawyers(); showLawyerSelect = true">
                      Assign Lawyer
                  </button>
                  
                  <div v-if="showLawyerSelect">
                    <select 
                        v-model="selectedLawyerId" 
                        @change="selectLawyer(selectedLawyerId)"
                    >
                        <option value="" disabled>Select a lawyer</option>
                        <option 
                            v-for="lawyer in lawyers" 
                            :key="lawyer.lawyerId" 
                            :value="lawyer.lawyerId"
                        >
                            {{ lawyer.user.name }} - {{ lawyer.specialization }}
                        </option>
                    </select>
                    <button 
                        @click="assignLawyer" 
                        :disabled="assigningLawyer || !selectedLawyer"
                    >
                        {{ assigningLawyer ? 'Assigning...' : 'Confirm' }}
                    </button>
                </div>
                              </div>
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
  overflow-y: auto;
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