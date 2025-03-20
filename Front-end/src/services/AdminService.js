import axios from "axios";

class AdminServices {
    static BASE_URL ="http://localhost:8080/";

    static getAuthHeaders(){
        const token = localStorage.getItem("token");
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
}

export default AdminServices;