<template>
  <div class="wrong-questions">
    <el-card shadow="hover">
      <template #header>
        <div class="page-header">
          <span><el-icon><WarnTriangleFilled /></el-icon> 错题本</span>
          <div class="header-actions">
            <el-button size="small" @click="exportWrongQuestions">
              <el-icon><Download /></el-icon> 导出
            </el-button>
            <el-button type="primary" size="small" @click="addWrongQuestion">
              <el-icon><Plus /></el-icon> 添加错题
            </el-button>
          </div>
        </div>
      </template>
      
      <!-- 统计概览 -->
      <div class="stats-overview">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-icon" style="background: linear-gradient(135deg, #667eea, #764ba2);">
                <el-icon :size="24" color="white"><Document /></el-icon>
              </div>
              <div class="stat-info">
                <span class="stat-value">{{ wrongQuestions.length }}</span>
                <span class="stat-label">错题总数</span>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-icon" style="background: linear-gradient(135deg, #f093fb, #f5576c);">
                <el-icon :size="24" color="white"><RefreshRight /></el-icon>
              </div>
              <div class="stat-info">
                <span class="stat-value">{{ repeatedCount }}</span>
                <span class="stat-label">重复错误</span>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-icon" style="background: linear-gradient(135deg, #4facfe, #00f2fe);">
                <el-icon :size="24" color="white"><TrendCharts /></el-icon>
              </div>
              <div class="stat-info">
                <span class="stat-value">{{ averageCorrectRate }}%</span>
                <span class="stat-label">平均正确率</span>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-icon" style="background: linear-gradient(135deg, #43e97b, #38f9d7);">
                <el-icon :size="24" color="white"><CircleCheck /></el-icon>
              </div>
              <div class="stat-info">
                <span class="stat-value">{{ masteredCount }}</span>
                <span class="stat-label">已掌握</span>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      
      <!-- 错题列表 -->
      <el-table :data="wrongQuestions" stripe>
        <el-table-column prop="subject" label="科目" width="150" />
        <el-table-column prop="question" label="错题内容" min-width="300" />
        <el-table-column prop="correctAnswer" label="正确答案" width="200" />
        <el-table-column prop="errorCount" label="错误次数" width="100" align="center">
          <template #default="{ row }">
            <el-tag type="danger">{{ row.errorCount }} 次</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="correctRate" label="正确率" width="150" align="center">
          <template #default="{ row }">
            <el-progress 
              :percentage="Math.round(row.correctRate * 100)" 
              :color="getProgressColor(row.correctRate)"
              :stroke-width="10"
            />
          </template>
        </el-table-column>
        <el-table-column prop="lastTime" label="最近错误" width="120" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="viewDetail(row)">查看</el-button>
            <el-button type="success" link size="small" @click="markAsMastered(row)">已掌握</el-button>
            <el-button type="danger" link size="small" @click="deleteQuestion(row)">删除</el-button>
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
    
    <!-- 错题详情弹窗 -->
    <el-dialog v-model="showDetail" title="错题详情" width="600px" destroy-on-close>
      <div class="detail-content" v-if="currentQuestion">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="科目">{{ currentQuestion.subject }}</el-descriptions-item>
          <el-descriptions-item label="错题内容">{{ currentQuestion.question }}</el-descriptions-item>
          <el-descriptions-item label="正确答案">{{ currentQuestion.correctAnswer }}</el-descriptions-item>
          <el-descriptions-item label="错误原因">
            <span class="error-reason">{{ currentQuestion.errorReason || '未填写' }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="改进措施">
            <span class="improvement">{{ currentQuestion.improvement || '未填写' }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="错误次数">{{ currentQuestion.errorCount }} 次</el-descriptions-item>
          <el-descriptions-item label="正确率">{{ Math.round(currentQuestion.correctRate * 100) }}%</el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <el-button @click="showDetail = false">关闭</el-button>
        <el-button type="primary" @click="practiceQuestion">专项练习</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { mockWrongQuestions } from '@/mock/data'

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(mockWrongQuestions.length)
const showDetail = ref(false)
const currentQuestion = ref(null)

const wrongQuestions = reactive([
  ...mockWrongQuestions.map(item => ({
    ...item,
    correctAnswer: '详见答案解析',
    errorReason: '对概念理解不够深入',
    improvement: '加强相关知识点的学习'
  }))
])

const repeatedCount = computed(() => {
  return wrongQuestions.filter(item => item.errorCount >= 3).length
})

const averageCorrectRate = computed(() => {
  if (!wrongQuestions.length) return 0
  const sum = wrongQuestions.reduce((acc, item) => acc + item.correctRate, 0)
  return Math.round(sum / wrongQuestions.length * 100)
})

const masteredCount = computed(() => {
  return wrongQuestions.filter(item => item.correctRate >= 0.8).length
})

const getProgressColor = (rate) => {
  if (rate >= 0.8) return '#67C23A'
  if (rate >= 0.5) return '#E6A23C'
  return '#F56C6C'
}

const viewDetail = (row) => {
  currentQuestion.value = row
  showDetail.value = true
}

const markAsMastered = async (row) => {
  try {
    await ElMessageBox.confirm('确定已掌握这道题了吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'success'
    })
    
    const index = wrongQuestions.findIndex(item => item.id === row.id)
    if (index > -1) {
      wrongQuestions.splice(index, 1)
      total.value = wrongQuestions.length
      ElMessage.success('已标记为掌握')
    }
  } catch {
    // 取消操作
  }
}

const deleteQuestion = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除这条错题吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const index = wrongQuestions.findIndex(item => item.id === row.id)
    if (index > -1) {
      wrongQuestions.splice(index, 1)
      total.value = wrongQuestions.length
      ElMessage.success('删除成功')
    }
  } catch {
    // 取消删除
  }
}

const addWrongQuestion = () => {
  ElMessage.info('添加错题功能开发中...')
}

const exportWrongQuestions = () => {
  ElMessage.info('导出功能开发中...')
}

const practiceQuestion = () => {
  ElMessage.info('专项练习功能开发中...')
}
</script>

<style lang="scss" scoped>
.wrong-questions {
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
      gap: 10px;
    }
  }
  
  .stats-overview {
    margin-bottom: 20px;
    
    .stat-card {
      display: flex;
      align-items: center;
      padding: 20px;
      background: #f9fafb;
      border-radius: 8px;
      
      .stat-icon {
        width: 50px;
        height: 50px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 15px;
      }
      
      .stat-info {
        display: flex;
        flex-direction: column;
        
        .stat-value {
          font-size: 24px;
          font-weight: 600;
          color: #303133;
        }
        
        .stat-label {
          font-size: 12px;
          color: #909399;
        }
      }
    }
  }
  
  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}

.detail-content {
  .error-reason,
  .improvement {
    color: #606266;
  }
}
</style>
