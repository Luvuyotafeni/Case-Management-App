import axios from "axios";

class CourtDateNoteService {

    static BASE_URL ="http://localhost:8080/";

    static getAuthHeaders(){
        const token = sessionStorage.getItem("token");
        return {
            headers:{
                Authorization: `Bearer ${token}`,
            },
        };
    }
    
}

export default CourtDateNoteService;