<template>
  <div class="admin-review">
    <el-card shadow="hover">
      <template #header>
        <div class="page-header">
          <span><el-icon><DocumentChecked /></el-icon> 资源审核</span>
          <el-tag type="warning">{{ total }} 条待审核</el-tag>
        </div>
      </template>

      <el-table :data="pendingList" stripe v-loading="tableLoading">
        <el-table-column prop="title" label="资源名称" min-width="200" />
        <el-table-column prop="fileType" label="类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag size="small">{{ row.fileType || '未知' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="uploaderName" label="提交者" width="100" align="center" />
        <el-table-column prop="createTime" label="提交时间" width="160">
          <template #default="{ row }">
            {{ formatTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="fileSizeDesc" label="大小" width="100" align="center" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="previewResource(row)">预览</el-button>
            <el-button type="success" link size="small" @click="approve(row)">通过</el-button>
            <el-button type="danger" link size="small" @click="reject(row)">驳回</el-button>
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
          @current-change="fetchPendingList"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>

    <!-- 驳回原因弹窗 -->
    <el-dialog v-model="showRejectDialog" title="驳回原因" width="400px">
      <el-input
        v-model="rejectReason"
        type="textarea"
        :rows="4"
        placeholder="请输入驳回原因..."
      />
      <template #footer>
        <el-button @click="showRejectDialog = false">取消</el-button>
        <el-button type="danger" @click="confirmReject">确认驳回</el-button>
      </template>
    </el-dialog>

    <!-- 预览弹窗 -->
    <el-dialog v-model="showPreview" title="资源预览" width="800px" destroy-on-close>
      <div class="preview-content">
        <p>资源预览功能开发中...</p>
        <p>资源名称：{{ currentResource?.title }}</p>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPendingReviews, reviewResource } from '@/api/admin'

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const showRejectDialog = ref(false)
const showPreview = ref(false)
const rejectReason = ref('')
const currentResource = ref(null)
const tableLoading = ref(false)

const pendingList = ref([])

const formatTime = (dateTime) => {
  if (!dateTime) return ''
  if (Array.isArray(dateTime)) {
    const [y, m, d, h = 0, min = 0, s = 0] = dateTime
    return `${y}-${String(m).padStart(2, '0')}-${String(d).padStart(2, '0')} ${String(h).padStart(2, '0')}:${String(min).padStart(2, '0')}:${String(s).padStart(2, '0')}`
  }
  return String(dateTime).replace('T', ' ').substring(0, 19)
}

const fetchPendingList = async () => {
  tableLoading.value = true
  try {
    const res = await getPendingReviews({ page: currentPage.value, size: pageSize.value })
    total.value = res.total || 0
    pendingList.value = res.records || []
  } catch (error) {
    ElMessage.error('获取待审核资源列表失败')
  } finally {
    tableLoading.value = false
  }
}

const handleSizeChange = () => {
  currentPage.value = 1
  fetchPendingList()
}

const previewResource = (row) => {
  currentResource.value = row
  showPreview.value = true
}

const approve = async (row) => {
  try {
    await ElMessageBox.confirm(`确定通过资源 "${row.title}" 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'success'
    })

    await reviewResource(row.id, 1, null)
    ElMessage.success('审核通过')
    fetchPendingList()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('审核操作失败')
    }
  }
}

const reject = (row) => {
  currentResource.value = row
  rejectReason.value = ''
  showRejectDialog.value = true
}

const confirmReject = async () => {
  if (!rejectReason.value.trim()) {
    ElMessage.warning('请输入驳回原因')
    return
  }

  try {
    await reviewResource(currentResource.value.id, 2, rejectReason.value)
    showRejectDialog.value = false
    ElMessage.success('已驳回')
    fetchPendingList()
  } catch (error) {
    ElMessage.error('驳回操作失败')
  }
}

onMounted(() => {
  fetchPendingList()
})
</script>

<style lang="scss" scoped>
.admin-review {
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

  .preview-content {
    padding: 20px;
    text-align: center;

    p {
      margin-bottom: 10px;
      color: #606266;
    }
  }
}
</style>
