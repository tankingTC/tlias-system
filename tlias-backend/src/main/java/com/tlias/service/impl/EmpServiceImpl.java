package com.tlias.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tlias.exception.BusinessException;
import com.tlias.mapper.EmpMapper;
import com.tlias.mapper.WorkExperienceMapper;
import com.tlias.pojo.PageResult;
import com.tlias.pojo.entity.Emp;
import com.tlias.pojo.entity.WorkExperience;
import com.tlias.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 员工管理业务实现类
 * 处理员工 CRUD、密码加密、唯一性校验、工作经历管理
 */
@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private WorkExperienceMapper workExperienceMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 分页查询员工列表
     * PageHelper 插件自动在下一条 SQL 上追加分页逻辑
     */
    @Override
    public PageResult<Emp> page(Integer page, Integer pageSize, String name, Short gender, LocalDate begin, LocalDate end) {
        PageHelper.startPage(page, pageSize); // 设置分页参数
        List<Emp> empList = empMapper.selectByCondition(name, gender, begin, end);
        Page<Emp> p = (Page<Emp>) empList; // PageHelper 返回 Page 对象（含 total）
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    /**
     * 新增员工
     * 校验：用户名唯一、手机号唯一
     * 处理：密码 BCrypt 加密、默认密码 123456
     */
    @Override
    @Transactional // 事务管理：校验+插入在同一事务中
    public void add(Emp emp) {
        // 检查用户名是否已存在
        Emp existing = empMapper.selectByUsername(emp.getUsername());
        if (existing != null) {
            throw new BusinessException("用户名已存在");
        }
        // 检查手机号是否已存在
        if (emp.getPhone() != null) {
            Emp phoneExist = empMapper.selectByPhone(emp.getPhone());
            if (phoneExist != null) {
                throw new BusinessException("手机号已存在");
            }
        }
        // 密码加密：未传密码则默认 123456
        if (emp.getPassword() == null || emp.getPassword().isEmpty()) {
            emp.setPassword(passwordEncoder.encode("123456"));
        } else {
            emp.setPassword(passwordEncoder.encode(emp.getPassword()));
        }
        empMapper.insert(emp);
    }

    /**
     * 查询员工详情
     * 密码字段置空，防止泄露给前端
     */
    @Override
    public Emp getById(Integer id) {
        Emp emp = empMapper.selectById(id);
        if (emp != null) {
            emp.setPassword(null); // 安全：隐藏密码
        }
        return emp;
    }

    /**
     * 修改员工信息
     * 强制忽略 username 和 password 字段，不允许通过此接口修改
     */
    @Override
    public void update(Emp emp) {
        emp.setUsername(null); // 禁止修改用户名
        emp.setPassword(null); // 禁止修改密码
        empMapper.update(emp);
    }

    /** 删除员工 */
    @Override
    public void delete(Integer id) {
        empMapper.deleteById(id);
    }

    /** 查询员工工作经历 */
    @Override
    public List<WorkExperience> getWorkExperience(Integer empId) {
        return workExperienceMapper.selectByEmpId(empId);
    }

    /**
     * 保存员工工作经历（先删后增策略）
     * 先删除该员工所有旧记录，再批量插入新记录
     */
    @Override
    @Transactional
    public void saveWorkExperience(Integer empId, List<WorkExperience> list) {
        workExperienceMapper.deleteByEmpId(empId); // 删除旧记录
        if (list != null && !list.isEmpty()) {
            list.forEach(w -> w.setEmpId(empId)); // 设置关联的员工ID
            workExperienceMapper.batchInsert(list); // 批量插入
        }
    }
}
