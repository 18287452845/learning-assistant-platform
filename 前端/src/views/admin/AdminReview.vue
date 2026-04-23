<template>
  <div class="admin-review">
    <el-card shadow="hover">
      <template #header>
        <div class="page-header">
          <span><el-icon><DocumentChecked /></el-icon> 资源审核</span>
          <el-tag type="warning">{{ pendingCount }} 条待审核</el-tag>
        </div>
      </template>
      
      <el-table :data="pendingList" stripe>
        <el-table-column prop="title" label="资源名称" min-width="200" />
        <el-table-column prop="type" label="类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag size="small">{{ row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="submitter" label="提交者" width="100" align="center" />
        <el-table-column prop="submitTime" label="提交时间" width="160" />
        <el-table-column prop="size" label="大小" width="100" align="center" />
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
import { ref, computed, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { mockPendingReviews } from '@/mock/data'

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(mockPendingReviews.length)
const showRejectDialog = ref(false)
const showPreview = ref(false)
const rejectReason = ref('')
const currentResource = ref(null)

const pendingList = reactive([...mockPendingReviews])

const pendingCount = computed(() => pendingList.length)

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
    
    const index = pendingList.findIndex(r => r.id === row.id)
    if (index > -1) {
      pendingList.splice(index, 1)
      total.value = pendingList.length
      ElMessage.success('审核通过')
    }
  } catch {
    // 取消操作
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
  
  const index = pendingList.findIndex(r => r.id === currentResource.value.id)
  if (index > -1) {
    pendingList.splice(index, 1)
    total.value = pendingList.length
  }
  
  showRejectDialog.value = false
  ElMessage.success('已驳回')
}
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
