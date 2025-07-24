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

    static async getUserByRole(){
        try{
            const response = await axios.get(`${UsersServices.BASE_URL}api/users/role/USER`, this.getAuthHeaders());
            return response.data;
        } catch (err){
            throw err;
        }
    }

    static async updateProfilePicture(userId, file){
        try{
            const formData = new FormData();
            formData.append('file', file);

            const response = await axios.post(`${UsersServices.BASE_URL}api/users/${userId}/profile-picture`, formData, {...this.getAuthHeaders()});
            return response.data;
        } catch (err){
            throw err;
        }
    }

    static async updateUser( userId, userData){
        try{
            const response = await axios.put(`${UsersServices.BASE_URL}api/users/${userId}`, userData, this.getAuthHeaders());
            return response.data;
        } catch(err){
            throw err;
        }
    }

}

export default UsersServices;