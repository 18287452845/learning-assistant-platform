package com.learning.infra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learning.domain.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程Mapper接口
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    /**
     * 分页查询课程
     */
    IPage<Course> selectCoursePage(Page<Course> page, @Param("status") Integer status);

    /**
     * 根据教师ID查询课程
     */
    List<Course> selectByTeacherId(@Param("teacherId") Long teacherId);

    /**
     * 根据分类ID查询课程
     */
    List<Course> selectByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 更新学生数量
     */
    int updateStudentCount(@Param("courseId") Long courseId);

    /**
     * 更新资源数量
     */
    int updateResourceCount(@Param("courseId") Long courseId);
}
