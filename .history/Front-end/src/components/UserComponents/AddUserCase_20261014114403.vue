<script setup>
import { onMounted, ref } from 'vue';
import Header from './Header.vue';
import CaseService from '@/services/CaseService';

const cases = ref(null);
const userId = sessionStorage.getItem("userId");
const selectedCase = ref([]);
const showModal = ref(false);
const loadingCase = ref(false);
const openEditModal = ref(false);
const fileToUpload = ref(null);
const openAccordion = ref(null); // Track which accordion is open

const showCreateCaseModal = ref(false);

const newCase = ref({
  caseName: '',
  caseNumber: '',
  caseType: '',
  description: '',
  stationName: '',
  province: '',
  officerName: '',
  officerContact: '',
  occurrenceDate: ''
});

// Function to toggle accordion
const toggleAccordion = (courtDateId) => {
  openAccordion.value = openAccordion.value === courtDateId ? null : courtDateId;
};

// Function to get notes for a specific court date
const getCourtNotes = (courtDateId) => {
  if (!selectedCase.value || !selectedCase.value.courtNotes) return [];
  return selectedCase.value.courtNotes.filter(note => 
    note.courtDate && note.courtDate.courtDateId === courtDateId
  );
};

// Format date and time
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

// Get appropriate CSS class based on status
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

const fetchUserCases = async () => {
  try {
    const response = await CaseService.getCasesByUserId(userId);
    cases.value = response;
  } catch (error) {
    console.log("error fetching cases", error);
  }
};

const openCreateModalCaseModal = () => {
  newCase.value = {
    caseName: '',
    caseNumber: '',
    caseType: '',
    description: '',
    stationName: '',
    province: '',
    officerName: '',
    officerContact: '',
    occurrenceDate: ''
  };
  showCreateCaseModal.value = true;
};

// Creating a case
const createCase = async () => {
  try {
    const caseData = {
      ...newCase.value,
      userId: userId
    };

    const response = await CaseService.createCase(caseData);

    await fetchUserCases();
    showCreateCaseModal.value = false;
    alert('Case created successfully');
  } catch (error) {
    console.log('Error creating a case', error);
    alert(`Failed to create case: ${error.response?.data || error.message}`);
  }
};

const openModal = async (caseId) => {
  try {
    loadingCase.value = true;
    selectedCase.value = await CaseService.getCaseById(caseId);
    showModal.value = true;
    openAccordion.value = null; // Reset accordion state
  } catch (error) {
    console.log("Error fetching the case details", error);
  } finally {
    loadingCase.value = false;
  }
};

const closeModal = () => {
  showModal.value = false;
  selectedCase.value = null;
};

const closeEditModal = () => {
  openEditModal.value = false;
  selectedCase.value = null;
};

const openFileUploadModal = (caseId) => {
  selectedCase.value = { caseId };
  openEditModal.value = true;
};

const handleFileChange = (event) => {
  fileToUpload.value = event.target.files[0];
};

// UPLOADING A FILE
const uploadFile = async () => {
  if (!fileToUpload.value) {
    alert('Please select a file to upload');
    return;
  }

  try {
    const formData = new FormData();
    formData.append('file', fileToUpload.value);
    formData.append('caseId', selectedCase.value.caseId);

    console.log('Uploading file:', {
      fileName: fileToUpload.value.name,
      caseId: selectedCase.value.caseId
    });

    const response = await CaseService.addFile(selectedCase.value.caseId, fileToUpload.value);

    console.log('Upload response:', response);

    alert('File uploaded successfully');

    // Refresh the case details
    selectedCase.value = await CaseService.getCaseById(selectedCase.value.caseId);

    // Close the upload modal
    openEditModal.value = false;
    fileToUpload.value = null;
  } catch (error) {
    console.error('Full error details:', error);
    console.error('Error response:', error.response);
    alert(`Failed to upload file: ${error.response?.data || error.message}`);
  }
};

onMounted(fetchUserCases);
</script>
<template>
    <div class="container">
        <Header/>
        <div class="dashboard">
      <div class="header">
        <h2>Cases</h2>
        <button class="add-case" @click="openCreateModalCaseModal">Add Case</button>
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
            <th>Assigned Lawyer</th>
            <th>Actions</th>
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
            <td>{{ caseItem.assignedlawyer?.user?.name || 'No Lawyer Assigned' }}</td>
            <td>
              <i class="bx bx-show" @click="openModal(caseItem.caseId)"></i>
              <i class='bx bx-edit' @click="openFileUploadModal(caseItem.caseId)"></i>
            </td>
          </tr>
        </tbody>
      </table>
    </div>


    
    <!-- Modal for Case Details -->
  <!-- Modal for Case Details -->
  <teleport to="body">
    <div v-if="showModal" class="modal-overlay">
      <div class="modal-content">
        <h2>Case Details</h2>

        <!-- Loading State -->
        <div v-if="loadingCase">Loading case details...</div>

        <div v-else>
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
          <div class="lawyer-info">
            <h3>Assigned Lawyer</h3>
            <div v-if="selectedCase?.assignedlawyer">
              <p><strong>Name: </strong>{{ selectedCase.assignedlawyer.user.name }}</p>
              <p><strong>Email: </strong>{{ selectedCase.assignedlawyer.user.email }}</p>
              <p><strong>Phone: </strong>{{ selectedCase.assignedlawyer.user.phone }}</p>
              <p><strong>Specialization: </strong>{{ selectedCase.assignedlawyer.specialization }}</p>
            </div>
            <div v-else>
              <p>No lawyer assigned to this case.</p>
            </div>
          </div>
          
          <hr />
          
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
              <li v-for="doc in selectedCase?.documents" :key="doc.documentId">
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

  <!-- modal to create a case -->
   <teleport to="body">
    <div v-if="showCreateCaseModal" class="modal-overlay">
      <div class="modal-content create-case-modal">
        <h2>Create New Case</h2>
        <form @submit.prevent="createCase">
          <div class="form-group">
            <label>Case Name</label>
            <input v-model="newCase.caseName" type="text" placeholder="Enter Case Name" required/>
          </div>
          <div class="form-group">
            <label>Case Number</label>
            <input v-model="newCase.caseNumber" type="text" placeholder="Enter Case Number" required/>
          </div>
          <div class="form-group">
            <label>Case Type</label>
            <select v-model="newCase.caseType">
              <option value=""> Select Case</option>
              <option value="Criminal"> Criminal</option>
              <option value="Civil"> Civil</option>
              <option value="Family"> Family</option>
              <option value="Corporate"> Corporate</option>
            </select>
          </div>
          <div class="form-group">
            <label>Case Description</label>
            <textarea v-model="newCase.description" placeholder="Enter Case Description"></textarea>
          </div>
          <div class="form-group">
            <label>Station Name</label>
            <input v-model="newCase.stationName" type="text" placeholder="Enter Station Name" required/>
          </div>
          <div class="form-group">
            <label>Province</label>
            <select v-model="newCase.province">
              <option value=""> Select Province</option>
              <option value="Eastern Cape">Eastern Cape</option>
              <option value="Free State"> Free State</option>
              <option value="Gauteng"> Gauteng</option>
              <option value="KwaZulu-Natal"> KwaZulu-Natal</option>
              <option value="Limpopo"> Limpopo</option>
              <option value="Mpumalanga"> Mpumalanga</option>
              <option value="Northern Cape"> Northern Cape</option>
              <option value="North West"> North West</option>
              <option value="Western Cape"> Western Cape</option>
            </select>
          </div>
          <div class="form-group">
            <label>Officer Name</label>
            <input v-model="newCase.officerName" type="text" placeholder="Enter Officer Name" required/>
          </div>
          <div class="form-group">
            <label>Officer Contacts</label>
            <input v-model="newCase.officerContact" type="tel" placeholder="Enter Officer Contact Number" required/>
          </div>
          <div class="form-group">
            <label>Occurence Date</label>
            <input v-model="newCase.occurrenceDate" type="date" placeholder="Enter Case Name" required/>
          </div>
          <div class="form-actions">
            <button type="submit" class="upload-button"> Create Case</button>
            <button type="button" class="cancel-button" @click="showCreateCaseModal = false">Cancel</button>
          </div>
        </form>
      </div>
    </div>
   </teleport>

  <!-- file upload modal -->
       <teleport to="body">
            <div v-if="openEditModal" class="modal-overlay">
                <div class="modal-content upload-modal">
                    <h2>Upload Document</h2>
                    <div class="file-upload-section">
                        <input 
                            type="file" 
                            @change="handleFileChange"
                            class="file-input"
                        />
                        <div v-if="fileToUpload" class="file-preview">
                            <p>Selected File: {{ fileToUpload.name }}</p>
                        </div>
                        <div class="upload-actions">
                            <button 
                                @click="uploadFile" 
                                class="upload-button"
                                :disabled="!fileToUpload"
                            >
                                Upload File
                            </button>
                            <button 
                                @click="closeEditModal" 
                                class="cancel-button"
                            >
                                Cancel
                            </button>
                        </div>
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
  z-index: 1000;
}

/* Modal Content */
.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 50%;
  height: 700px;
  overflow-y: auto;
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
  font-size: 20px;
}

/* Close Button */
.close-button {
  display: block;
  width: 50%;
  text-align: center;
  margin: 15px auto 0;
  padding: 10px;
  background: #dc3545;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.add-case {
  padding: 10px 15px;
  background-color: #ddd;
  color: black;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.dashboard {
  display: flex;
  flex-direction: column;
  margin-left: 20px;
  margin-top: 100px;
  width: 100%;
}

table {
  width: 100%;
  border-collapse: collapse;
  background: white;
}

thead {
  background: #ddd;
}

td, th {
  padding: 10px;
  border: 1px solid #ccc;
  text-align: left;
}

.file-upload-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  padding: 20px;
}

.file-input {
  width: 90%;
  padding: 10px;
  margin: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.file-preview {
  margin: 10px 0;
  padding: 10px;
  background-color: #f4f4f4;
  border-radius: 5px;
}

.upload-actions {
  display: flex;
  gap: 10px;
  width: 100%;
  justify-content: center;
}

.upload-button {
  background-color: #28a745;
  color: white;
  border: none;
  padding: 10px 15px;
  border-radius: 5px;
  cursor: pointer;
}

.upload-modal {
  height: 300px;
}

.upload-button:disabled {
  background-color: #6c757d;
  cursor: not-allowed;
}

.cancel-button {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 10px 15px;
  border-radius: 5px;
  cursor: pointer;
}

/* ADDing case styles */
.create-case-modal {
  height: 80vh;
  width: 50%;
  overflow-y: auto;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-group input, select, textarea {
  width: 80%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.form-group textarea {
  height: 100px;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

/* Court Dates Accordion Styling */
.court-dates-section {
  margin-top: 15px;
}

.accordion {
  margin-top: 10px;
  width: 100%;
}

.accordion-item {
  border: 1px solid #ccc;
  border-radius: 5px;
  margin-bottom: 10px;
  overflow: hidden;
}

.accordion-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 15px;
  background-color: #f4f4f4;
  cursor: pointer;
  transition: background-color 0.2s;
}

.accordion-header:hover {
  background-color: #e4e4e4;
}

.accordion-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
}

.accordion-content {
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.3s ease-out;
  background-color: #ffffff;
}

.accordion-content.active {
  max-height: 500px;
  padding: 15px;
  border-top: 1px solid #ccc;
}

/* Court Date Details */
.court-date-details {
  margin-bottom: 15px;
}

/* Court Notes Styling */
.court-notes {
  border-top: 1px dashed #ccc;
  padding-top: 15px;
  margin-top: 15px;
}

.court-notes h4 {
  margin-top: 0;
  margin-bottom: 10px;
  font-size: 16px;
  font-weight: bold;
}

.note-item {
  background-color: #f9f9f9;
  border-left: 3px solid #007bff;
  padding: 10px;
  margin-bottom: 10px;
  border-radius: 0 4px 4px 0;
}

.note-item p {
  margin: 0 0 5px 0;
}

.note-item small {
  color: #666;
  font-style: italic;
  font-size: 12px;
}

/* Status Badge Styling */
.status-badge {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  margin-left: 10px;
}

.status-scheduled {
  background-color: #cce5ff;
  color: #004085;
}

.status-completed {
  background-color: #d4edda;
  color: #155724;
}

.status-postponed {
  background-color: #fff3cd;
  color: #856404;
}

.status-cancelled {
  background-color: #f8d7da;
  color: #721c24;
}

hr {
  margin: 15px 0;
  border: 0;
  border-top: 1px solid #ccc;
}
</style>