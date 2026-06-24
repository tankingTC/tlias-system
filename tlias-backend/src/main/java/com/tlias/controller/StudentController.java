package com.tlias.controller;

import com.alibaba.excel.EasyExcel;
import com.tlias.pojo.PageResult;
import com.tlias.pojo.Result;
import com.tlias.pojo.entity.Student;
import com.tlias.service.StudentService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 学员管理控制器
 * 处理学员相关的CRUD操作、导入导出功能，服务于学员管理模块
 */
@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 分页查询学员列表，支持按姓名、班级ID和学历筛选
     * @param page 当前页码（默认1）
     * @param pageSize 每页记录数（默认10）
     * @param name 学员姓名（可选，模糊查询）
     * @param classId 班级ID（可选，精确匹配）
     * @param education 学历（可选，精确匹配）
     * @return 分页后的学员列表
     */
    @GetMapping
    public Result<PageResult<Student>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer classId,
            @RequestParam(required = false) String education) {
        return Result.success(studentService.page(page, pageSize, name, classId, education));
    }

    /**
     * 新增学员
     * @param student 学员信息（从请求体获取）
     * @return 操作结果
     */
    @PostMapping
    public Result<Void> add(@RequestBody Student student) {
        studentService.add(student);
        return Result.success();
    }

    /**
     * 根据ID查询学员详情
     * @param id 学员ID（从路径参数获取）
     * @return 学员详细信息
     */
    @GetMapping("/{id}")
    public Result<Student> getById(@PathVariable Integer id) {
        return Result.success(studentService.getById(id));
    }

    /**
     * 更新学员信息
     * @param student 学员信息（从请求体获取，需包含id）
     * @return 操作结果
     */
    @PutMapping
    public Result<Void> update(@RequestBody Student student) {
        studentService.update(student);
        return Result.success();
    }

    /**
     * 根据ID删除学员
     * @param id 学员ID（从路径参数获取）
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        studentService.delete(id);
        return Result.success();
    }

    /**
     * 为学员添加违规记录
     * @param id 学员ID（从路径参数获取）
     * @param body 请求体，包含扣分分数score（默认为0）
     * @return 操作结果
     */
    @PostMapping("/{id}/violation")
    public Result<Void> addViolation(@PathVariable Integer id, @RequestBody java.util.Map<String, Integer> body) {
        // 获取扣分分数，如果未提供则默认为0
        Integer score = body.getOrDefault("score", 0);
        studentService.addViolation(id, score);
        return Result.success();
    }

    /**
     * 批量导入学员（从Excel文件）
     * @param file Excel文件（ multipart/form-data格式）
     * @return 操作结果
     * @throws IOException 文件读取异常
     */
    @PostMapping("/import")
    public Result<Void> importStudents(@RequestParam("file") MultipartFile file) throws IOException {
        // 使用EasyExcel解析Excel文件为学员列表
        List<Student> students = EasyExcel.read(file.getInputStream())
                .head(Student.class)
                .sheet()
                .doReadSync();
        // 批量保存学员数据
        studentService.batchImport(students);
        return Result.success();
    }

    /**
     * 导出所有学员数据为Excel文件
     * @param response HTTP响应对象，用于设置响应头和输出流
     * @throws IOException 文件写入异常
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        // 设置响应头为Excel格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 对文件名进行URL编码，处理中文和空格
        String fileName = URLEncoder.encode("学员数据", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        // 查询所有学员数据
        List<Student> students = studentService.exportAll();
        // 使用EasyExcel写入Excel文件并输出到响应流
        EasyExcel.write(response.getOutputStream(), Student.class).sheet("学员数据").doWrite(students);
    }
}
