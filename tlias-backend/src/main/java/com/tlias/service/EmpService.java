package com.tlias.service;

import com.tlias.pojo.PageResult;
import com.tlias.pojo.entity.Emp;
import com.tlias.pojo.entity.WorkExperience;
import java.time.LocalDate;
import java.util.List;

public interface EmpService {
    PageResult<Emp> page(Integer page, Integer pageSize, String name, Short gender, LocalDate begin, LocalDate end);
    void add(Emp emp);
    Emp getById(Integer id);
    void update(Emp emp);
    void delete(Integer id);
    List<WorkExperience> getWorkExperience(Integer empId);
    void saveWorkExperience(Integer empId, List<WorkExperience> list);
}
