<template>
  <div class="teacher-students">
    <el-card shadow="hover">
      <template #header>
        <div class="page-header">
          <span><el-icon><UserFilled /></el-icon> 学生管理</span>
          <div class="header-actions">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索学生..."
              prefix-icon="Search"
              style="width: 200px; margin-right: 15px;"
              clearable
            />
            <el-select v-model="statusFilter" placeholder="活跃状态" style="width: 120px;">
              <el-option label="全部" value="" />
              <el-option label="活跃" value="active" />
              <el-option label="不活跃" value="inactive" />
            </el-select>
          </div>
        </div>
      </template>
      
      <el-table :data="filteredStudents" stripe>
        <el-table-column prop="id" label="学号" width="130" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="class" label="班级" width="130" />
        <el-table-column prop="questions" label="提问数" width="100" align="center" />
        <el-table-column prop="answers" label="回答数" width="100" align="center" />
        <el-table-column prop="lastActive" label="最后活跃" width="130" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'info'" size="small">
              {{ row.status === 'active' ? '活跃' : '不活跃' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="viewDetail(row)">查看</el-button>
            <el-button type="warning" link size="small" @click="sendMessage(row)">发消息</el-button>
            <el-button type="info" link size="small" @click="viewLearningRecord(row)">学习记录</el-button>
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
import { ref, computed, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { mockStudentList } from '@/mock/data'

const searchKeyword = ref('')
const statusFilter = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(mockStudentList.length)

const students = reactive([...mockStudentList])

const filteredStudents = computed(() => {
  return students.filter(s => {
    const matchKeyword = !searchKeyword.value || 
      s.name.includes(searchKeyword.value) || s.id.includes(searchKeyword.value) || s.class.includes(searchKeyword.value)
    const matchStatus = !statusFilter.value || s.status === statusFilter.value
    return matchKeyword && matchStatus
  })
})

const viewDetail = (row) => {
  ElMessage.info(`查看学生 ${row.name} 的详细信息...`)
}

const sendMessage = (row) => {
  ElMessage.info(`向学生 ${row.name} 发送消息...`)
}

const viewLearningRecord = (row) => {
  ElMessage.info(`查看学生 ${row.name} 的学习记录...`)
}
</script>

<style lang="scss" scoped>
.teacher-students {
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
    
    .header-actions {
      display: flex;
      align-items: center;
    }
  }
  
  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
