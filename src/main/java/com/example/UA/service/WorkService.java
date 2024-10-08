package com.example.UA.service;

import com.example.UA.controller.form.WorkForm;
import com.example.UA.repository.WorkRepository;
import com.example.UA.repository.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkService {

    @Autowired
    WorkRepository workRepository;

    public void saveWork(WorkForm workForm) {
        Work work = setWork(workForm);
    }

    private Work setWork(WorkForm workForm) {
        Work work = new Work();
        work.setWorkStart(workForm.getWorkStart());
        work.setWorkEnd(workForm.getWorkEnd());
        return work;
    }
}
