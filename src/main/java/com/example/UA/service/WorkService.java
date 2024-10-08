package com.example.UA.service;

import com.example.UA.controller.form.WorkForm;
import com.example.UA.repository.WorkRepository;
import com.example.UA.repository.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkService {
    @Autowired
    WorkRepository workRepository;
    /*
     * 勤怠記録全取得
     */
    public List<WorkForm> findAllWorks() {
        List<Work> results = new ArrayList<>();
        results = workRepository.findAll();
        List<WorkForm> works = setWorkForm(results);
        return works;
    }

    private List<WorkForm> setWorkForm(List<Work> results) {
        List<WorkForm> works = new ArrayList<>();
        for (Work work : results) {
            WorkForm workForm = new WorkForm();
            workForm.setId(work.getId());
            workForm.setWorkStart(work.getWorkStart());
            workForm.setWorkEnd(work.getWorkEnd());
            workForm.setRest(work.getRest());
            workForm.setDate(work.getDate());
            workForm.setDate(work.getRest());
            workForm.setMemo(work.getMemo());
            workForm.setStatus(work.getStatus());
            workForm.setGroupId(work.getGroupId());
            workForm.setAccountId(work.getAccountId());
            works.add(workForm);
        }
        return works;
    }
}
