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
              @keyup.enter="handleSearch"
              @clear="handleSearch"
            />
            <el-select v-model="roleFilter" placeholder="角色筛选" style="width: 100px; margin-right: 10px;" @change="handleSearch">
              <el-option label="全部" value="" />
              <el-option label="学生" value="学生" />
              <el-option label="教师" value="教师" />
              <el-option label="管理员" value="管理员" />
            </el-select>
            <el-select v-model="statusFilter" placeholder="状态筛选" style="width: 100px;" @change="handleSearch">
              <el-option label="全部" value="" />
              <el-option label="正常" value="正常" />
              <el-option label="禁用" value="禁用" />
            </el-select>
          </div>
        </div>
      </template>

      <el-table :data="users" stripe v-loading="tableLoading">
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
          @current-change="fetchUsers"
          @size-change="handleSizeChange"
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
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserList, toggleUserStatus, resetUserPassword } from '@/api/admin'

const searchKeyword = ref('')
const roleFilter = ref('')
const statusFilter = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const showDetail = ref(false)
const currentUser = ref(null)
const tableLoading = ref(false)

const users = ref([])

// Map UserVO from backend to table row format
const mapUser = (user) => ({
  id: user.id,
  username: user.username || '',
  name: user.realName || '',
  role: mapRoleDisplay(user.roles),
  phone: user.phone || '',
  email: user.email || '',
  status: user.statusDesc || (user.status === 1 ? '正常' : '禁用'),
  lastLogin: formatTime(user.lastLoginTime),
  createTime: formatTime(user.createTime),
  gender: user.genderDesc || '',
  avatar: user.avatar || '',
  faceRegistered: user.faceRegistered || false,
  rawStatus: user.status,
  rawRoles: user.roles
})

const mapRoleDisplay = (roles) => {
  if (!roles || !roles.length) return '未知'
  const map = { STUDENT: '学生', TEACHER: '教师', ADMIN: '管理员' }
  return roles.map(r => map[r] || r).join('/')
}

const formatTime = (dateTime) => {
  if (!dateTime) return ''
  if (Array.isArray(dateTime)) {
    const [y, m, d, h = 0, min = 0, s = 0] = dateTime
    return `${y}-${String(m).padStart(2, '0')}-${String(d).padStart(2, '0')} ${String(h).padStart(2, '0')}:${String(min).padStart(2, '0')}:${String(s).padStart(2, '0')}`
  }
  return String(dateTime).replace('T', ' ').substring(0, 19)
}

const fetchUsers = async () => {
  tableLoading.value = true
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value
    }
    if (searchKeyword.value) {
      params.keyword = searchKeyword.value
    }
    if (statusFilter.value) {
      params.status = statusFilter.value === '正常' ? 1 : 0
    }
    const res = await getUserList(params)
    total.value = res.total || 0
    users.value = (res.records || []).map(mapUser)
  } catch (error) {
    ElMessage.error('获取用户列表失败')
  } finally {
    tableLoading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  fetchUsers()
}

const handleSizeChange = () => {
  currentPage.value = 1
  fetchUsers()
}

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

    await toggleUserStatus(row.id, row.status === '正常' ? 0 : 1)
    ElMessage.success(`${action}成功`)
    fetchUsers()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

const resetPassword = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要重置用户 ${row.name} 的密码吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await resetUserPassword(row.id)
    ElMessage.success('密码已重置为默认密码：123456')
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('重置密码失败')
    }
  }
}

onMounted(() => {
  fetchUsers()
})
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
