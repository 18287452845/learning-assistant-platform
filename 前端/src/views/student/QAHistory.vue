<template>
  <div class="qa-history">
    <el-card shadow="hover">
      <template #header>
        <div class="page-header">
          <span><el-icon><Clock /></el-icon> 问答历史</span>
          <el-button type="danger" size="small" :disabled="!selectedRows.length" @click="handleDelete">
            <el-icon><Delete /></el-icon> 删除
          </el-button>
        </div>
      </template>
      
      <div class="filter-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索问题..."
          prefix-icon="Search"
          style="width: 200px; margin-right: 15px;"
          clearable
        />
        <el-select v-model="typeFilter" placeholder="问答类型" style="width: 150px;">
          <el-option label="全部" value="" />
          <el-option label="全网问答" value="general" />
          <el-option label="个人问答" value="personal" />
        </el-select>
      </div>
      
      <el-table :data="filteredHistory" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column label="问题" prop="question" min-width="300">
          <template #default="{ row }">
            <div class="question-cell">
              <span class="question-text">{{ row.question }}</span>
              <el-tag size="small" :type="row.type === 'personal' ? 'success' : 'info'">
                {{ row.type === 'personal' ? '个人' : '全网' }}
              </el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="答案摘要" prop="answer" min-width="300">
          <template #default="{ row }">
            <span class="answer-preview">{{ row.answer }}</span>
          </template>
        </el-table-column>
        <el-table-column label="时间" prop="time" width="160" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="viewDetail(row)">查看</el-button>
            <el-button type="danger" link size="small" @click="handleDeleteOne(row)">删除</el-button>
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
    
    <!-- 详情弹窗 -->
    <el-dialog v-model="showDetail" title="问答详情" width="700px" destroy-on-close>
      <div class="detail-content" v-if="currentRecord">
        <div class="detail-section">
          <h4><el-icon><ChatLineSquare /></el-icon> 问题</h4>
          <div class="question-box">{{ currentRecord.question }}</div>
        </div>
        
        <div class="detail-section">
          <h4><el-icon><ChatDotRound /></el-icon> 答案</h4>
          <div class="answer-box" v-html="formatContent(currentRecord.answer)"></div>
        </div>
        
        <div class="detail-meta">
          <el-tag :type="currentRecord.type === 'personal' ? 'success' : 'info'">
            {{ currentRecord.type === 'personal' ? '个人问答' : '全网问答' }}
          </el-tag>
          <span class="time">{{ currentRecord.time }}</span>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { mockQAHistory } from '@/mock/data'

const searchKeyword = ref('')
const typeFilter = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(mockQAHistory.length)
const selectedRows = ref([])
const showDetail = ref(false)
const currentRecord = ref(null)

const historyList = reactive([...mockQAHistory])

const filteredHistory = computed(() => {
  return historyList.filter(item => {
    const matchKeyword = !searchKeyword.value || 
      item.question.toLowerCase().includes(searchKeyword.value.toLowerCase())
    const matchType = !typeFilter.value || item.type === typeFilter.value
    return matchKeyword && matchType
  })
})

const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

const viewDetail = (row) => {
  currentRecord.value = row
  showDetail.value = true
}

const handleDelete = async () => {
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 条记录吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // 模拟删除
    historyList.splice(0, selectedRows.value.length)
    total.value = historyList.length
    ElMessage.success('删除成功')
  } catch {
    // 取消删除
  }
}

const handleDeleteOne = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除这条记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const index = historyList.findIndex(item => item.id === row.id)
    if (index > -1) {
      historyList.splice(index, 1)
      total.value = historyList.length
      ElMessage.success('删除成功')
    }
  } catch {
    // 取消删除
  }
}

const formatContent = (content) => {
  if (!content) return ''
  return content
    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    .replace(/\n/g, '<br>')
}
</script>

<style lang="scss" scoped>
.qa-history {
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
  
  .filter-bar {
    display: flex;
    margin-bottom: 20px;
  }
  
  .question-cell {
    display: flex;
    align-items: center;
    gap: 10px;
    
    .question-text {
      flex: 1;
    }
  }
  
  .answer-preview {
    color: #606266;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    display: block;
    max-width: 280px;
  }
  
  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}

.detail-content {
  .detail-section {
    margin-bottom: 20px;
    
    h4 {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 10px;
      color: #303133;
    }
    
    .question-box {
      padding: 15px;
      background: #f5f7fa;
      border-radius: 8px;
      color: #303133;
    }
    
    .answer-box {
      padding: 15px;
      background: #f0f9ff;
      border-radius: 8px;
      line-height: 1.8;
      color: #606266;
    }
  }
  
  .detail-meta {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-top: 15px;
    border-top: 1px solid #ebeef5;
    
    .time {
      color: #909399;
      font-size: 13px;
    }
  }
}
</style>
