<template>
  <div class="admin-users">
    <el-card shadow="hover">
      <template #header>
        <div class="page-header">
          <span><el-icon><UserFilled /></el-icon> 用户管理</span>
          <div class="header-actions">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索用户名/姓名..."
              prefix-icon="Search"
              style="width: 200px; margin-right: 15px;"
              clearable
            />
            <el-select v-model="roleFilter" placeholder="角色筛选" style="width: 100px; margin-right: 10px;">
              <el-option label="全部" value="" />
              <el-option label="学生" value="学生" />
              <el-option label="教师" value="教师" />
              <el-option label="管理员" value="管理员" />
            </el-select>
            <el-select v-model="statusFilter" placeholder="状态筛选" style="width: 100px;">
              <el-option label="全部" value="" />
              <el-option label="正常" value="正常" />
              <el-option label="禁用" value="禁用" />
            </el-select>
          </div>
        </div>
      </template>
      
      <el-table :data="filteredUsers" stripe>
        <el-table-column prop="username" label="用户名" width="130" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="role" label="角色" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getRoleType(row.role)" size="small">
              {{ row.role }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === '正常' ? 'success' : 'danger'" size="small">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastLogin" label="最后登录" width="160" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="viewDetail(row)">查看</el-button>
            <el-button 
              :type="row.status === '正常' ? 'warning' : 'success'" 
              link 
              size="small" 
              @click="toggleStatus(row)"
            >
              {{ row.status === '正常' ? '禁用' : '启用' }}
            </el-button>
            <el-button type="info" link size="small" @click="resetPassword(row)">重置密码</el-button>
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
    
    <!-- 用户详情弹窗 -->
    <el-dialog v-model="showDetail" title="用户详情" width="600px" destroy-on-close>
      <el-descriptions v-if="currentUser" :column="2" border>
        <el-descriptions-item label="用户名">{{ currentUser.username }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ currentUser.name }}</el-descriptions-item>
        <el-descriptions-item label="角色">{{ currentUser.role }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="currentUser.status === '正常' ? 'success' : 'danger'" size="small">
            {{ currentUser.status }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="手机号">{{ currentUser.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱" :span="2">{{ currentUser.email }}</el-descriptions-item>
        <el-descriptions-item label="注册时间">{{ currentUser.createTime }}</el-descriptions-item>
        <el-descriptions-item label="最后登录">{{ currentUser.lastLogin }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { mockUserList } from '@/mock/data'

const searchKeyword = ref('')
const roleFilter = ref('')
const statusFilter = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(mockUserList.length)
const showDetail = ref(false)
const currentUser = ref(null)

const users = reactive([...mockUserList])

const filteredUsers = computed(() => {
  return users.filter(u => {
    const matchKeyword = !searchKeyword.value || 
      u.username.includes(searchKeyword.value) || 
      u.name.includes(searchKeyword.value)
    const matchRole = !roleFilter.value || u.role === roleFilter.value
    const matchStatus = !statusFilter.value || u.status === statusFilter.value
    return matchKeyword && matchRole && matchStatus
  })
})

const getRoleType = (role) => {
  const types = { '学生': 'primary', '教师': 'success', '管理员': 'warning' }
  return types[role] || 'info'
}

const viewDetail = (row) => {
  currentUser.value = row
  showDetail.value = true
}

const toggleStatus = async (row) => {
  const action = row.status === '正常' ? '禁用' : '启用'
  try {
    await ElMessageBox.confirm(`确定要${action}用户 ${row.name} 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const index = users.findIndex(u => u.id === row.id)
    if (index > -1) {
      users[index].status = row.status === '正常' ? '禁用' : '正常'
      ElMessage.success(`${action}成功`)
    }
  } catch {
    // 取消操作
  }
}

const resetPassword = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要重置用户 ${row.name} 的密码吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    ElMessage.success('密码已重置为默认密码：123456')
  } catch {
    // 取消操作
  }
}
</script>

<style lang="scss" scoped>
.admin-users {
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
