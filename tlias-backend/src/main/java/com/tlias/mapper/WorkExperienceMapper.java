package com.tlias.mapper;

import com.tlias.pojo.entity.WorkExperience;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface WorkExperienceMapper {

    @Select("SELECT * FROM work_experience WHERE emp_id = #{empId} ORDER BY start_date DESC")
    List<WorkExperience> selectByEmpId(Integer empId);

    @Insert("INSERT INTO work_experience(emp_id, start_date, end_date, company, position, create_time, update_time) " +
            "VALUES(#{empId}, #{startDate}, #{endDate}, #{company}, #{position}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(WorkExperience we);

    @Delete("DELETE FROM work_experience WHERE emp_id = #{empId}")
    void deleteByEmpId(Integer empId);

    @Insert("<script>" +
            "INSERT INTO work_experience(emp_id, start_date, end_date, company, position, create_time, update_time) VALUES " +
            "<foreach collection='list' item='w' separator=','>" +
            "(#{w.empId}, #{w.startDate}, #{w.endDate}, #{w.company}, #{w.position}, NOW(), NOW())" +
            "</foreach>" +
            "</script>")
    void batchInsert(@Param("list") List<WorkExperience> list);
}
