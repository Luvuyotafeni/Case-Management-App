import axios from "axios";

class AuthServices {
  static BASE_URL = "http://localhost:8080/";

  static getAuthHeaders() {
    const token = sessionStorage.getItem("token");
    return {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    };
  }

  static async login(email, password) {
    
    try {
      const response = await axios.post(`${AuthServices.BASE_URL}api/auth/login`, { email, password });

      // Store token
      sessionStorage.setItem("token", response.data.token);
      // sessionStorage.setItem("userName", response.data.name);
      sessionStorage.setItem("role", response.data.role); // Save role in session
      sessionStorage.setItem("userId", response.data.userId)
      return response.data;
    } catch (err) {
      throw err;
    }
  }

  static async registerUser(user) {
    try {
      const response = await axios.post(`${AuthServices.BASE_URL}api/users/create`, user);
      return response.data;
    } catch (err) {
      throw err;
    }
  }

  static async forgotPassword(emailData){
    try{
      const response = await axios.post(`${AuthServices.BASE_URL}api/auth/forgot-password`,emailData);
      return response.data;
    } catch(err){
      throw err;
    }
  }

  static async resetPassword(data){
    try{
      const response = await axios.post(`${AuthServices.BASE_URL}api/auth/reset-password`, data);
      return response.data;
    } catch(err){
      throw err;
    }
  }

  static async resendVerification(email){
    try{
      const response = await axios.post(`${AuthServices.BASE_URL}api/auth/resend`, email, this.getAuthHeaders());
      return response.data;
    } catch (err){
      throw err;
    }
  }

  static async verifyOtp(data){
    try{
      const response = await axios.post(`${AuthServices.BASE_URL}api/auth/verify`, data, this.getAuthHeaders());
      return response.data;
    } catch (err){
      throw err;
    }
  }

  
}

export default AuthServices;
