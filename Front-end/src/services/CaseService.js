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
}

export default CaseService;