package com.tlias.mapper;

import com.tlias.pojo.entity.Emp;
import org.apache.ibatis.annotations.*;
import java.time.LocalDate;
import java.util.List;

public interface EmpMapper {

    @Select("<script>" +
            "SELECT e.*, d.name as deptName FROM emp e " +
            "LEFT JOIN dept d ON e.dept_id = d.id " +
            "WHERE 1=1 " +
            "<if test='name != null and name != \"\"'>" +
            "AND e.name LIKE CONCAT('%', #{name}, '%') " +
            "</if>" +
            "<if test='gender != null'>" +
            "AND e.gender = #{gender} " +
            "</if>" +
            "<if test='begin != null'>" +
            "AND e.entry_date &gt;= #{begin} " +
            "</if>" +
            "<if test='end != null'>" +
            "AND e.entry_date &lt;= #{end} " +
            "</if>" +
            "ORDER BY e.update_time DESC" +
            "</script>")
    List<Emp> selectByCondition(@Param("name") String name,
                                 @Param("gender") Short gender,
                                 @Param("begin") LocalDate begin,
                                 @Param("end") LocalDate end);

    @Select("SELECT * FROM emp WHERE id = #{id}")
    Emp selectById(Integer id);

    @Insert("INSERT INTO emp(username, password, name, gender, phone, job, salary, dept_id, image, entry_date, create_time, update_time) " +
            "VALUES(#{username}, #{password}, #{name}, #{gender}, #{phone}, #{job}, #{salary}, #{deptId}, #{image}, #{entryDate}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Emp emp);

    @Update("<script>" +
            "UPDATE emp SET update_time = NOW() " +
            "<if test='username != null'>, username = #{username}</if>" +
            "<if test='password != null'>, password = #{password}</if>" +
            "<if test='name != null'>, name = #{name}</if>" +
            "<if test='gender != null'>, gender = #{gender}</if>" +
            "<if test='phone != null'>, phone = #{phone}</if>" +
            "<if test='job != null'>, job = #{job}</if>" +
            "<if test='salary != null'>, salary = #{salary}</if>" +
            "<if test='deptId != null'>, dept_id = #{deptId}</if>" +
            "<if test='image != null'>, image = #{image}</if>" +
            "<if test='entryDate != null'>, entry_date = #{entryDate}</if>" +
            "WHERE id = #{id}" +
            "</script>")
    void update(Emp emp);

    @Delete("DELETE FROM emp WHERE id = #{id}")
    void deleteById(Integer id);

    @Select("SELECT * FROM emp WHERE username = #{username}")
    Emp selectByUsername(String username);

    @Select("SELECT * FROM emp WHERE phone = #{phone}")
    Emp selectByPhone(String phone);

    @Select("SELECT COUNT(*) FROM emp")
    int count();
}
