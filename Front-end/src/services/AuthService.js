import axios from "axios";

class AuthServices{

    static BASE_URL ="http://localhost:8080/";

    static async login(email, password){
        try{
            const response = await axios.post(`${AuthServices.BASE_URL}api/auth/login`,{email, password});
            localStorage.setItem("token", response.data.token);
            localStorage.setItem("userName", response.data.name);
            localStorage.setItem("userId", response.data.userId);
            return response.data;
        } catch (err){
            throw err;
        }
    }
}

export default AuthServices;