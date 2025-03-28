<script setup>
    import { onMounted, ref } from 'vue';
    import Header from './Header.vue';
    import CaseService from '@/services/CaseService';


    const cases = ref(null);
    const userId = sessionStorage.getItem("userId");
    const selectedCase = ref([]);
    const showModal = ref (false);
    const loadingCase = ref(false);
    const openEditModal = ref(false);
    const fileToUpload = ref(null);

    const showCreateCaseModal = ref(false);

    const newCase = ref({
      caseName:'',
      caseNumber:'',
      caseType:'',
      description:'',
      stationName:'',
      province:'',
      officerName:'',
      officerContact:'',
      occurenceDate:''

    });

    const fetchUserCases = async ()=> {
        try{
            const response = await CaseService.getCasesByUserId(userId);
            cases.value = response;
        } catch(error){
            console.log("error fetching cases", error);
        }
    };

    const openCreateModalCaseModal = () =>{
      newCase.value ={
        caseName:'',
        caseNumber:'',
        caseType:'',
        description:'',
        stationName:'',
        province:'',
        officerName:'',
        officerContact:'',
        occurenceDate:''
      };
      showCreateCaseModal.value =true;
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
    };
    

    const closeEditModal = () => {
      openEditModal.value = false;
      selectedCase.value = null;
    };

    const closeAddCaseModal =()=>{
      openCreateModalCaseModal = false;
    }


    const openFileUploadModal = (caseId) => {
      selectedCase.value = {caseId};
      openEditModal.value = true;
    };

    const handleFileChange = (event) => {
      fileToUpload.value = event.target.files[0];
    };

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
  <teleport to="body">
    <div v-if="showModal" class="modal-overlay">
      <div class="modal-content">
        <h2>Case Details</h2>

        <!-- Loading State -->
        <div v-if="loadingCase">Loading case details...</div>

        <div v-else>
          <!-- User Info Section -->
          <!-- <div class="user-info">
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
          </div> -->

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
          <div class="lawyer-info">
            <h3> Assigned Lawyer</h3>
            <div v-if="selectedCase?.assignedlawyer">
                <p><strong>Name: </strong>{{ selectedCase.assignedlawyer.user.name  }}</p>
                <p><strong>Email: </strong>{{ selectedCase.assignedlawyer.user.email  }}</p>
                <p><strong>Phone: </strong>{{ selectedCase.assignedlawyer.user.phone  }}</p>
                <p><strong>Specialization: </strong>{{ selectedCase.assignedlawyer.specialization }}</p>
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

  <!-- modal to create a case -->
   <teleport to="body">
    <div v-if="showCreateCaseModal" class="modal-overlay">
      <div class="modal-content create-case-modal">
        <h2>Create New Case</h2>
        <form>
          <div class="form-group">
            <label>Case Name</label>
            <input type="text" placeholder="Enter Case Name" required/>
          </div>
          <div class="form-group">
            <label>Case Number</label>
            <input type="text" placeholder="Enter Case Number" required/>
          </div>
          <div class="form-group">
            <label>Case Type</label>
            <select>
              <option value=""> Select Case</option>
              <option value="Criminal"> Criminal</option>
              <option value="Civil"> Civil</option>
              <option value="Family"> Family</option>
              <option value="Corporate"> Corporate</option>
            </select>
          </div>
          <div class="form-group">
            <label>Case Description</label>
            <textarea placeholder="Enter Case Description"></textarea>
          </div>
          <div class="form-group">
            <label>Station Name</label>
            <input type="text" placeholder="Enter Station Name" required/>
          </div>
          <div class="form-group">
            <label>Province</label>
            <select>
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
            <input type="text" placeholder="Enter Officer Name" required/>
          </div>
          <div class="form-group">
            <label>Officer Contacts</label>
            <input type="tel" placeholder="Enter Officer Contact Number" required/>
          </div>
          <div class="form-group">
            <label>Occurence Date</label>
            <input type="date" placeholder="Enter Case Name" required/>
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
                <div class="modal-content">
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
    margin:10px;
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
.create-case-modal{
  height:80vh;
  width:50%;
  overflow-y: auto;
}

.form-group{
  margin-bottom: 15px;
}
.form-group label{
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}
.form-group input, select, textarea{
  width: 80%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.form-group text{
  height: 100px;
}

.form-actions{
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}
</style>