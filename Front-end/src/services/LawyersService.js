import axios from "axios";

class LawyersService {
    static BASE_URL = "http://localhost:8080/";

    static getAuthHeaders(){
        const token = sessionStorage.getItem("token");
        return {
            headers:{
                Authorization: `Bearer ${token}`,
            },
        };
    }

    static async getAllLawyers(){
        try{
            const response = await axios.get(`${LawyersService.BASE_URL}api/lawyers`, this.getAuthHeaders());
            return response.data;
        } catch (err){
            throw err;
        }
    }

}

export default LawyersService;