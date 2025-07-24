import axios from "axios";

class CourtDateService {

    static BASE_URL ="http://localhost:8080/";

    static getAuthHeaders(){
        const token = sessionStorage.getItem("token");
        return {
            headers:{
                Authorization: `Bearer ${token}`,
            },
        };
    }

    static async createCourtDate(caseId, scheduledDateTime, courtName, courtRoom, judgeAssigned, hearingType, adminId){
        try{
            const response = await axios.post(
             `${CourtDateService.BASE_URL}api/court-dates/create`,{
                caseId,
                scheduledDateTime,
                courtName,
                courtRoom,
                judgeAssigned,
                hearingType,
                adminId
             },
             this.getAuthHeaders()   
            );
            return response.data;
        } catch (err){
            throw err;
        }
    }
}

export default CourtDateService;