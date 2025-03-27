import axios from "axios";

class CaseService {
    static BASE_URL ="http://localhost:8080/";
   
    static getAuthHeader(){
        const token = sessionStorage.getItem("token");
        return {
            headers:{
                Authorization: `Bearer ${token}`,
            },
        };
    }

    static async getCasesByUserId(userId){
        try{
            const response = await axios.get(`${CaseService.BASE_URL}api/cases/user/${userId}`, this.getAuthHeader());
            return response.data;
        } catch (err){
            throw err;
        }
    }
    
    static async getCaseById(caseId){
        try{
            const response = await axios.get(`${CaseService.BASE_URL}api/cases/${caseId}`, this.getAuthHeader());
            return response.data;
        } catch(err){
            throw err;
        }
    }

    static async getCaseByAssignedLaywyerUserId(userId){
        try{
            const response = await axios.get(`${CaseService.BASE_URL}api/cases/lawyer-user/${userId}`, this.getAuthHeader());
            return response.data;
        } catch (err){
            throw err;
        }
    }
    static async addFile(caseId, file){
        try {
            const formData = new FormData();
            formData.append('file', file);
            formData.append('caseId', caseId);
    
            const response = await axios.post(`${CaseService.BASE_URL}api/documents/upload`, formData, {
                ...this.getAuthHeader(),
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
            return response.data;
        } catch (err){
            console.error('File upload error:', err.response ? err.response.data : err.message);
            throw err;
        }
    }
}

export default CaseService;