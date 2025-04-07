<script setup>
import { onMounted, ref } from 'vue';
import Header from './Header.vue';
import AdminServices from '@/services/AdminService';
import CaseService from '@/services/CaseService';
import LawyersService from '@/services/LawyersService';
import CourtDateService from '@/services/CourtDate';// Fixed import path

const cases = ref([]);
const selectedCase = ref(null);
const showModal = ref(false);
const loadingCase = ref(false);
const lawyers = ref([]);
const showLawyerSelect = ref(false);
const showCourtDateForm = ref(false);
const selectedLawyer = ref(null);
const selectedLawyerId = ref(null);
const assigningLawyer = ref(false);
const openAccordion = ref(null);
const creatingCourtDate = ref(false);
const errorMessage = ref('');

const fetchCases = async () => {
    try {
        cases.value = await AdminServices.getAllAdminCases();
    } catch (error) {
        console.log("Error fetching cases: ", error);
        errorMessage.value = "Failed to fetch cases. Please try again.";
    }
};

const newCourtDate = ref({
  scheduledDateTime: '',
  courtName: '',
  courtRoom: '',
  judgeAssigned: '',
  hearingType: ''
});

const resetCourtDateForm = () => {
  newCourtDate.value = {
    scheduledDateTime: '',
    courtName: '',
    courtRoom: '',
    judgeAssigned: '',
    hearingType: ''
  };
  errorMessage.value = '';
};

const openCourtDateForm = (caseItem) => {
  selectedCase.value = caseItem;
  showCourtDateForm.value = true;
  resetCourtDateForm();
};

const submitCourtDate = async () => {
  if (!selectedCase.value) {
    errorMessage.value = "No case selected. Please select a case first.";
    return;
  }
  
  // Validate form fields
  if (!newCourtDate.value.scheduledDateTime || 
      !newCourtDate.value.courtName || 
      !newCourtDate.value.courtRoom || 
      !newCourtDate.value.judgeAssigned || 
      !newCourtDate.value.hearingType) {
    errorMessage.value = "Please fill in all required fields.";
    return;
  }
  
  try {
    creatingCourtDate.value = true;
    const adminId = sessionStorage.getItem("userId");
    const caseId = selectedCase.value.caseId;
    
    await CourtDateService.createCourtDate(
      caseId,
      newCourtDate.value.scheduledDateTime,
      newCourtDate.value.courtName,
      newCourtDate.value.courtRoom,
      newCourtDate.value.judgeAssigned,
      newCourtDate.value.hearingType,
      adminId
    );
    
    // Close form and refresh case details
    showCourtDateForm.value = false;
    await openModal(caseId);
    
    // Show success notification
    alert("Court date created successfully!");
  } catch (error) {
    console.error('Error creating court date:', error);
    errorMessage.value = "Failed to create court date. Please try again.";
  } finally {
    creatingCourtDate.value = false;
  }
};

const toggleAccordion = (courtDateId) => {
  openAccordion.value = openAccordion.value === courtDateId ? null : courtDateId;
};

const getCourtNotes = (courtDateId) => {
  if (!selectedCase.value || !selectedCase.value.courtNotes) return [];
  return selectedCase.value.courtNotes.filter(note => 
    note.courtDate && note.courtDate.courtDateId === courtDateId
  );
};

const formatDate = (dateString) => {
  if (!dateString) return "N/A";
  const date = new Date(dateString);
  return date.toLocaleDateString();
};

const formatDateTime = (dateTimeString) => {
  if (!dateTimeString) return "N/A";
  const date = new Date(dateTimeString);
  return date.toLocaleString();
};

const getStatusClass = (status) => {
  switch (status?.toLowerCase()) {
    case 'scheduled':
      return 'status-scheduled';
    case 'completed':
      return 'status-completed';
    case 'postponed':
      return 'status-postponed';
    case 'cancelled':
      return 'status-cancelled';
    default:
      return '';
  }
};

const openModal = async (caseId) => {
  try {
    loadingCase.value = true;
    selectedCase.value = await CaseService.getCaseById(caseId);
    showModal.value = true;
  } catch (error) {
    console.log("Error fetching the case details", error);
    alert("Failed to fetch case details. Please try again.");
  } finally {
    loadingCase.value = false;
  }
};

const closeModal = () => {
  showModal.value = false;
  selectedCase.value = null;
  showLawyerSelect.value = false;
};

const closeCourtDateForm = () => {
  showCourtDateForm.value = false;
  errorMessage.value = '';
};

const fetchLawyers = async () => {
  try {
    lawyers.value = await LawyersService.getAllLawyers();
  } catch (error) {
    console.log("Error fetching lawyers:", error);
    alert("Failed to fetch lawyers. Please try again.");
  }
};

const selectLawyer = async (lawyerId) => {
  try {
    const fullLawyerDetails = await LawyersService.getLawyerById(lawyerId);
    selectedLawyer.value = fullLawyerDetails;
    selectedLawyerId.value = lawyerId;
  } catch (error) {
    console.error('Error fetching lawyer details:', error);
    alert('Failed to fetch lawyer details');
    selectedLawyer.value = null;
    selectedLawyerId.value = null;
  }
};

const assignLawyer = async () => {
  if (!selectedLawyer.value || !selectedCase.value) {
    alert('Please select a lawyer and case');
    return;
  }

  try {
    assigningLawyer.value = true;
    const response = await CaseService.assignLawyer(
      selectedCase.value.caseId,
      selectedLawyer.value.lawyerId
    );

    selectedCase.value.assignedlawyer = selectedLawyer.value;
    showLawyerSelect.value = false;
    selectedLawyer.value = null;
    selectedLawyerId.value = null;

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
            <th>Assigned Lawyer</th>
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
            <td>{{ caseItem.assignedlawyer?.user?.name }}</td>
            <td>{{ caseItem.stationName }}</td>
            <td>{{ caseItem.province}}</td>
            <td>
              <i class="bx bx-show" @click="openModal(caseItem.caseId)"></i>
              <i class='bx bx-calendar-plus' @click="openCourtDateForm(caseItem)"></i>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    
    <!-- Court Date Creation Modal -->
    <teleport to="body">
      <div v-if="showCourtDateForm" class="modal-overlay">
        <div class="modal-content">
          <h2>Create Court Date</h2>
          <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
          
          <form @submit.prevent="submitCourtDate">
            <div class="form-group">
              <label>Date & Time: <span class="required">*</span></label>
              <input 
                type="datetime-local" 
                v-model="newCourtDate.scheduledDateTime" 
                required
              />
            </div>
            <div class="form-group">
              <label>Court Name: <span class="required">*</span></label>
              <input 
                v-model="newCourtDate.courtName" 
                required
                placeholder="Enter court name"
              />
            </div>
            <div class="form-group">
              <label>Court Room: <span class="required">*</span></label>
              <input 
                v-model="newCourtDate.courtRoom" 
                required
                placeholder="Enter court room"
              />
            </div>
            <div class="form-group">
              <label>Judge Assigned: <span class="required">*</span></label>
              <input 
                v-model="newCourtDate.judgeAssigned" 
                required
                placeholder="Enter judge name"
              />
            </div>
            <div class="form-group">
              <label>Hearing Type: <span class="required">*</span></label>
              <input 
                v-model="newCourtDate.hearingType" 
                required
                placeholder="Enter hearing type"
              />
            </div>
            <div class="form-actions">
              <button 
                type="submit"
                :disabled="creatingCourtDate"
              >
                {{ creatingCourtDate ? 'Creating...' : 'Create Court Date' }}
              </button>
              <button 
                type="button"
                @click="closeCourtDateForm"
                :disabled="creatingCourtDate"
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
    </teleport>

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
              <h3>Assigned Lawyer</h3>

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

            <hr/>

            <!-- Court Dates Accordion Section -->
            <div class="court-dates-section">
              <h3>Court Dates</h3>
              
              
              <p v-if="!selectedCase?.courtDates?.length">No court dates scheduled.</p>
              <div v-else class="accordion">
                <div 
                  v-for="courtDate in selectedCase.courtDates" 
                  :key="courtDate.courtDateId" 
                  class="accordion-item"
                >
                  <div 
                    class="accordion-header" 
                    @click="toggleAccordion(courtDate.courtDateId)"
                  >
                    <div class="accordion-title">
                      <i class="bx bx-calendar"></i>
                      {{ formatDate(courtDate.scheduledDateTime) }} - {{ courtDate.courtName }}
                      <span class="status-badge" :class="getStatusClass(courtDate.status)">
                        {{ courtDate.status }}
                      </span>
                    </div>
                    <i 
                      class="bx" 
                      :class="openAccordion === courtDate.courtDateId ? 'bx-chevron-up' : 'bx-chevron-down'"
                    ></i>
                  </div>
                  <div 
                    class="accordion-content" 
                    :class="{ 'active': openAccordion === courtDate.courtDateId }"
                  >
                    <div class="court-date-details">
                      <p><strong>Date & Time:</strong> {{ formatDateTime(courtDate.scheduledDateTime) }}</p>
                      <p><strong>Court:</strong> {{ courtDate.courtName }}</p>
                      <p v-if="courtDate.courtRoom"><strong>Court Room:</strong> {{ courtDate.courtRoom }}</p>
                      <p><strong>Judge:</strong> {{ courtDate.judgeAssigned }}</p>
                      <p><strong>Hearing Type:</strong> {{ courtDate.hearingType }}</p>
                      <p><strong>Status:</strong> {{ courtDate.status }}</p>
                    </div>
                    
                    <div class="court-notes">
                      <h4>Court Notes</h4>
                      <div v-if="getCourtNotes(courtDate.courtDateId).length > 0">
                        <div 
                          v-for="note in getCourtNotes(courtDate.courtDateId)" 
                          :key="note.noteId"
                          class="note-item"
                        >
                          <p>{{ note.noteContent }}</p>
                          <small>Added by: {{ note.createdByRole }} on {{ formatDate(note.creationDate) }}</small>
                        </div>
                      </div>
                      <p v-else>No notes available for this court date.</p>
                    </div>
                  </div>
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

  .error-message {
  color: red;
  margin-bottom: 15px;
  font-weight: bold;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-group input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.form-actions {
  margin-top: 20px;
  display: flex;
  gap: 10px;
}

.required {
  color: red;
}

.add-court-date-btn {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 5px;
  margin-bottom: 15px;
}

.add-court-date-btn:hover {
  background-color: #45a049;
}

.status-scheduled {
  background-color: #3498db;
  color: white;
}

.status-completed {
  background-color: #2ecc71;
  color: white;
}

.status-postponed {
  background-color: #f39c12;
  color: white;
}

.status-cancelled {
  background-color: #e74c3c;
  color: white;
}

.status-badge {
  padding: 3px 8px;
  border-radius: 12px;
  font-size: 0.8em;
  font-weight: bold;
  margin-left: 10px;
}
</style>