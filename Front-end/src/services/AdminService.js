import axios from "axios";

class AdminServices {
    static BASE_URL ="http://localhost:8080/";

    static getAuthHeaders(){
        const token = sessionStorage.getItem("token");
        return {
            headers:{
                Authorization: `Bearer ${token}`,
            },
        };
    }

    static async getAdminById(adminId){
        try{
            const response= await axios.get(`${AdminServices.BASE_URL}api/admins/${adminId}`, this.getAuthHeaders());
            return response.data;
        } catch (err){
            throw err;
        }
    }

    static async getALlAdmins(){
        try{
            const response= await axios.get(`${AdminServices.BASE_URL}api/admins`, this.getAuthHeaders());
            return response.data;
        } catch ( err){
            throw err;
        }
    }

    
    static async getAllAdminCases(){
        try{
            const response = await axios.get(`${AdminServices.BASE_URL}api/cases`, this.getAuthHeaders());
            return response.data;
        } catch(err){
            throw err;
        }
    }

    static async createLawyer(lawyerData){
        try{
            const response = await axios.post(`${AdminServices.BASE_URL}api/lawyers/create`,lawyerData, this.getAuthHeaders());
            return response.data;
        } catch (err){
            throw err;
        }
    }

    static async getLawyerById(lawyerId){
        try{
            const response = await axios.get(`${AdminServices.BASE_URL}api/lawyers/${lawyerId}`, this.getAuthHeaders());
            return response.data;
        } catch(err){
            throw err
        }
    }
}

export default AdminServices;