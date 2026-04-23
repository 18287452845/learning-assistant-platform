<template>
  <div class="admin-courses">
    <el-card shadow="hover">
      <template #header>
        <div class="page-header">
          <span><el-icon><Reading /></el-icon> 课程管理</span>
          <el-button type="primary" size="small">
            <el-icon><Plus /></el-icon> 添加课程
          </el-button>
        </div>
      </template>
      
      <el-table :data="courses" stripe>
        <el-table-column prop="name" label="课程名称" min-width="150" />
        <el-table-column prop="teacher" label="授课教师" width="120" align="center" />
        <el-table-column prop="students" label="学生数" width="100" align="center" />
        <el-table-column prop="resourceCount" label="资源数" width="100" align="center" />
        <el-table-column prop="category" label="分类" width="100" align="center">
          <template #default="{ row }">
            <el-tag size="small" type="info">{{ row.category }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small">编辑</el-button>
            <el-button type="success" link size="small">查看</el-button>
            <el-button type="danger" link size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { mockCourses } from '@/mock/data'

const courses = ref(mockCourses)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(mockCourses.length)
</script>

<style lang="scss" scoped>
.admin-courses {
  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    span {
      display: flex;
      align-items: center;
      gap: 8px;
      font-weight: 600;
    }
  }
  
  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
