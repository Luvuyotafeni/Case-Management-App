import axios from "axios";

class UsersServices {
    static BASE_URL ="http://localhost:8080/";

    static getAuthHeaders(){
        const token = sessionStorage.getItem("token");
        return {
            headers:{
                Authorization: `Bearer ${token}`,
            },
        };
    }

    static async getUserById(userId){
        try{
            const response = await axios.get(`${UsersServices.BASE_URL}api/users/${userId}`, this.getAuthHeaders());
            return response.data;
        } catch (err){
            throw err;
        }
    }

}

export default UsersServices;