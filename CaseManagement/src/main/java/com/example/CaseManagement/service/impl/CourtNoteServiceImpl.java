package com.example.CaseManagement.service.impl;

import com.example.CaseManagement.entity.CaseEntity;
import com.example.CaseManagement.entity.CourtDateEntity;
import com.example.CaseManagement.entity.CourtNoteEntity;
import com.example.CaseManagement.entity.UserBaseEntity;
import com.example.CaseManagement.enumaration.Role;
import com.example.CaseManagement.repository.CaseRepository;
import com.example.CaseManagement.repository.CourtDateRepository;
import com.example.CaseManagement.repository.CourtNoteRepository;
import com.example.CaseManagement.repository.UserBaseRepository;
import com.example.CaseManagement.service.CourtNoteService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CourtNoteServiceImpl implements CourtNoteService {

    @Autowired
    private CourtNoteRepository courtNoteRepository;

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private CourtDateRepository courtDateRepository;

    @Autowired
    private UserBaseRepository userBaseRepository;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional
    public CourtNoteEntity createCourtNote(Long caseId, Long courtDateId, String noteContent, Long createdBy, String createdByRole) {

        CaseEntity caseEntity = caseRepository.findById(caseId)
                .orElseThrow(()-> new RuntimeException("Case not found with id: "+caseId));

        UserBaseEntity user = userBaseRepository.findById(createdBy)
                .orElseThrow(()-> new RuntimeException("User not found with Id: "+ createdBy));

        if (!user.getRole().toString().equals(createdByRole)){
            throw new RuntimeException("User with id: "+ createdBy + "does not have role: "+ createdByRole);
        }

        if (createdByRole.equals("LAWYER")){
            if (caseEntity.getAssignedlawyer() == null||
            !caseEntity.getAssignedlawyer().getUser().getUserId().equals(createdBy)){
                throw new RuntimeException("lawyer witth id :"+ createdBy + "is not assigned to case with id :" + caseId);
            }
        }

        CourtNoteEntity courtNote = new CourtNoteEntity();
        courtNote.setRelatedCase(caseEntity);
        courtNote.setNoteContent(noteContent);
        courtNote.setCreatedByUserId(createdBy);
        courtNote.setCreatedByRole(createdByRole);

        if (courtDateId != null){
            CourtDateEntity courtDate = courtDateRepository.findById(courtDateId)
                    .orElseThrow(()-> new RuntimeException("Court date not found with ID: "+ courtDateId));
            courtNote.setCourtDate(courtDate);
        }

        String now = LocalDateTime.now().format(formatter);
        courtNote.setCreationDate(now);
        courtNote.setLastUpdated(now);
        return  courtNoteRepository.save(courtNote);
    }

    @Override
    public CourtNoteEntity updateCourtNote(Long noteId, String noteContent, Long updatedById) {
        CourtNoteEntity courtNote = courtNoteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Court Note not found with id: " + noteId));

        UserBaseEntity user = userBaseRepository.findById(updatedById)
                .orElseThrow(()-> new RuntimeException("User not found with Id : " + updatedById));

        if (!courtNote.getCreatedByUserId().equals(updatedById) && !user.getRole().equals(Role.ADMIN)){
            throw new RuntimeException("User with Id: " + updatedById + "does not have the permission to update this note");
        }

        courtNote.setNoteContent(noteContent);
        courtNote.setLastUpdated(LocalDateTime.now().format(formatter));

        return courtNoteRepository.save(courtNote);
    }


    @Override
    public void deleteCourtNote(Long noteId, Long adminId) {
        CourtNoteEntity courtNote = courtNoteRepository.findById(noteId)
                .orElseThrow(()-> new RuntimeException("Admin not found with ID: "+ adminId));

        UserBaseEntity admin = userBaseRepository.findById(adminId)
                .orElseThrow(()-> new RuntimeException("Admin not found with Id: "+ adminId));

        if (!admin.getRole().equals(Role.ADMIN) && !courtNote.getCreatedByUserId().equals(adminId)){
            throw new RuntimeException("User with Id: "+ adminId + "does not have the permission to delete this note");
        }

        courtNoteRepository.delete(courtNote);

    }

    @Override
    public CourtNoteEntity getCourtNoteById(Long noteId) {
        return courtNoteRepository.findById(noteId)
                .orElseThrow(()-> new RuntimeException("Court note not found with Id:" +noteId));
    }

    @Override
    public List<CourtNoteEntity> getCourtNotesByCaseId(Long caseId) {
        return  courtNoteRepository.findByRelatedCase_CaseId(caseId);
    }

    @Override
    public List<CourtNoteEntity> getCourtNotesByCourtDateId(Long courtDateId) {
        return courtNoteRepository.findByCourtDate_CourtDateId(courtDateId);
    }

    @Override
    public List<CourtNoteEntity> getCourtNotesByLawyerId(Long lawyerId) {
        return courtNoteRepository.findByRelatedCase_Assignedlawyer_LawyerId(lawyerId);
    }

    @Override
    public List<CourtNoteEntity> getCourtNotesByUserId(Long userId) {
        return courtNoteRepository.findByRelatedCase_User_UserId(userId);
    }
}
