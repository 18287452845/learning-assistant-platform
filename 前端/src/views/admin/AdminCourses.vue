<template>
  <div class="admin-courses">
    <el-card shadow="hover">
      <template #header>
        <div class="page-header">
          <span><el-icon><Reading /></el-icon> 课程管理</span>
          <div class="header-actions">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索课程名称/课程代码..."
              prefix-icon="Search"
              style="width: 200px; margin-right: 15px;"
              clearable
              @keyup.enter="handleSearch"
              @clear="handleSearch"
            />
            <el-button type="primary" size="small" @click="handleAddCourse">
              <el-icon><Plus /></el-icon> 添加课程
            </el-button>
          </div>
        </div>
      </template>

      <el-table :data="courses" stripe v-loading="tableLoading">
        <el-table-column prop="courseName" label="课程名称" min-width="150" />
        <el-table-column prop="courseCode" label="课程代码" width="120" align="center" />
        <el-table-column prop="teacherName" label="授课教师" width="120" align="center" />
        <el-table-column prop="studentCount" label="学生数" width="100" align="center" />
        <el-table-column prop="resourceCount" label="资源数" width="100" align="center" />
        <el-table-column prop="statusDesc" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag size="small" :type="row.status === 1 ? 'success' : 'info'">{{ row.statusDesc || '未知' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="success" link size="small" @click="handleView(row)">查看</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
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
          @current-change="fetchCourses"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminCourseList } from '@/api/admin'

const courses = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchKeyword = ref('')
const tableLoading = ref(false)

const fetchCourses = async () => {
  tableLoading.value = true
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value
    }
    if (searchKeyword.value) {
      params.keyword = searchKeyword.value
    }
    const res = await getAdminCourseList(params)
    total.value = res.total || 0
    courses.value = res.records || []
  } catch (error) {
    ElMessage.error('获取课程列表失败')
  } finally {
    tableLoading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  fetchCourses()
}

const handleSizeChange = () => {
  currentPage.value = 1
  fetchCourses()
}

const handleAddCourse = () => {
  ElMessage.info('添加课程功能开发中')
}

const handleEdit = (row) => {
  ElMessage.info('编辑功能开发中')
}

const handleView = (row) => {
  ElMessage.info('查看课程详情功能开发中')
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除课程 "${row.courseName}" 吗？此操作不可恢复。`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    ElMessage.info('删除功能开发中')
  } catch {
    // 取消操作
  }
}

onMounted(() => {
  fetchCourses()
})
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

  .header-actions {
    display: flex;
    align-items: center;
  }

  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
