<template>
  <div class="teacher-qa">
    <el-card shadow="hover">
      <template #header>
        <div class="page-header">
          <el-radio-group v-model="statusFilter" size="default">
            <el-radio-button label="pending">
              待处理 <el-badge :value="pendingCount" :max="99" />
            </el-radio-button>
            <el-radio-button label="answered">已回答</el-radio-button>
            <el-radio-button label="all">全部</el-radio-button>
          </el-radio-group>
        </div>
      </template>
      
      <el-table :data="filteredQuestions" stripe>
        <el-table-column prop="title" label="问题" min-width="300">
          <template #default="{ row }">
            <div class="question-cell">
              <span class="question-text">{{ row.title }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="course" label="课程" width="150" align="center" />
        <el-table-column prop="student" label="提问学生" width="120" align="center" />
        <el-table-column prop="time" label="时间" width="160" align="center" />
        <el-table-column prop="answers" label="回答数" width="100" align="center" />
        <el-table-column prop="views" label="浏览数" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'warning'" size="small">
              {{ row.status === 1 ? '已回答' : row.status === 2 ? '已关闭' : '待处理' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="viewDetail(row)">查看</el-button>
            <el-button type="success" link size="small" @click="answerQuestion(row)">回答</el-button>
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
    
    <!-- 回答弹窗 -->
    <el-dialog v-model="showAnswerDialog" title="回答问题" width="700px" destroy-on-close>
      <div class="answer-form" v-if="currentQuestion">
        <div class="question-section">
          <h4><el-icon><ChatLineSquare /></el-icon> 问题</h4>
          <div class="question-content">{{ currentQuestion.title }}</div>
          <div class="question-meta">
            <el-tag size="small" type="info">{{ currentQuestion.course }}</el-tag>
            <span>{{ currentQuestion.student }} · {{ currentQuestion.time }}</span>
          </div>
        </div>
        
        <el-divider />
        
        <div class="answer-section">
          <h4><el-icon><ChatDotRound /></el-icon> 您的回答</h4>
          <el-input
            v-model="answerContent"
            type="textarea"
            :rows="6"
            placeholder="请输入回答内容..."
          />
        </div>
        
        <div class="knowledge-section">
          <h4><el-icon><Collection /></el-icon> 关联知识点</h4>
          <el-select v-model="selectedKnowledge" multiple placeholder="选择关联知识点" style="width: 100%;">
            <el-option label="递归算法" value="递归算法" />
            <el-option label="排序算法" value="排序算法" />
            <el-option label="时间复杂度" value="时间复杂度" />
            <el-option label="空间复杂度" value="空间复杂度" />
            <el-option label="二叉树遍历" value="二叉树遍历" />
          </el-select>
        </div>
      </div>
      <template #footer>
        <el-button @click="showAnswerDialog = false">取消</el-button>
        <el-button type="success" @click="submitAnswer">提交回答</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getPendingRequests, answerQuestion as answerQuestionAPI } from '@/api/qa'

const statusFilter = ref('pending')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const showAnswerDialog = ref(false)
const currentQuestion = ref(null)
const answerContent = ref('')
const selectedKnowledge = ref([])

const questions = reactive([])

const pendingCount = computed(() => {
  return questions.filter(q => q.status === 0).length
})

const filteredQuestions = computed(() => {
  if (statusFilter.value === 'all') return questions
  const statusValue = statusFilter.value === 'pending' ? 0 : 1
  return questions.filter(q => q.status === statusValue)
})

const statusLabel = (status) => {
  const map = { 0: 'pending', 1: 'answered', 2: 'closed' }
  return map[status] ?? 'pending'
}

const loadData = async () => {
  try {
    const res = await getPendingRequests({ page: 1, size: 20 })
    if (res.data?.records) {
      questions.splice(0, questions.length)
      res.data.records.forEach(q => {
        questions.push({
          id: q.id,
          title: q.question,
          course: q.courseName,
          student: q.studentName,
          time: q.createTime,
          answers: 0,
          views: 0,
          status: q.status
        })
      })
      total.value = res.data.total || questions.length
    }
  } catch (e) {
    console.error('Failed to load pending requests:', e)
  }
}

const viewDetail = (row) => {
  currentQuestion.value = row
  ElMessage.info('查看问题详情...')
}

const answerQuestion = (row) => {
  currentQuestion.value = row
  answerContent.value = ''
  selectedKnowledge.value = []
  showAnswerDialog.value = true
}

const submitAnswer = async () => {
  if (!answerContent.value.trim()) {
    ElMessage.warning('请输入回答内容')
    return
  }

  try {
    await answerQuestionAPI(currentQuestion.value.id, { answer: answerContent.value })
    showAnswerDialog.value = false
    ElMessage.success('回答提交成功')
    loadData()
  } catch (e) {
    console.error('Failed to submit answer:', e)
    ElMessage.error('回答提交失败')
  }
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.teacher-qa {
  .page-header {
    display: flex;
    align-items: center;
  }
  
  .question-cell {
    .question-text {
      color: #303133;
      cursor: pointer;
      
      &:hover {
        color: #409EFF;
      }
    }
  }
  
  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}

.answer-form {
  .question-section,
  .answer-section,
  .knowledge-section {
    h4 {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 12px;
      color: #303133;
    }
  }
  
  .question-content {
    padding: 15px;
    background: #f5f7fa;
    border-radius: 8px;
    color: #606266;
    line-height: 1.6;
    margin-bottom: 10px;
  }
  
  .question-meta {
    display: flex;
    align-items: center;
    gap: 10px;
    font-size: 13px;
    color: #909399;
  }
}
</style>
