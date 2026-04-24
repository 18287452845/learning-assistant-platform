<template>
  <div class="learning-plan">
    <el-card shadow="hover">
      <template #header>
        <div class="page-header">
          <span><el-icon><TrendCharts /></el-icon> 学习计划</span>
          <el-button type="primary" size="small" @click="createPlan">
            <el-icon><Plus /></el-icon> 创建计划
          </el-button>
        </div>
      </template>
      
      <!-- 计划概览 -->
      <div class="plan-overview">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="overview-card">
              <div class="overview-icon" style="background: #ecf5ff;">
                <el-icon :size="28" color="#409EFF"><Calendar /></el-icon>
              </div>
              <div class="overview-info">
                <span class="value">{{ plans.length }}</span>
                <span class="label">学习计划</span>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="overview-card">
              <div class="overview-icon" style="background: #f0f9ff;">
                <el-icon :size="28" color="#67C23A"><CircleCheck /></el-icon>
              </div>
              <div class="overview-info">
                <span class="value">{{ completedTasks }}</span>
                <span class="label">已完成任务</span>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="overview-card">
              <div class="overview-icon" style="background: #fef0f0;">
                <el-icon :size="28" color="#F56C6C"><Clock /></el-icon>
              </div>
              <div class="overview-info">
                <span class="value">{{ pendingTasks }}</span>
                <span class="label">待完成任务</span>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="overview-card">
              <div class="overview-icon" style="background: #fdf6ec;">
                <el-icon :size="28" color="#E6A23C;"><DataLine /></el-icon>
              </div>
              <div class="overview-info">
                <span class="value">{{ completionRate }}%</span>
                <span class="label">完成率</span>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      
      <!-- 学习计划列表 -->
      <div class="plan-list">
        <el-empty v-if="plans.length === 0" description="暂无学习计划，请选择课程创建计划" />
        <el-timeline v-else>
          <el-timeline-item
            v-for="plan in plans"
            :key="plan.id"
            :timestamp="plan.startDate"
            placement="top"
            :color="getPlanColor(plan.status)"
          >
            <el-card shadow="hover" class="plan-card">
              <div class="plan-header">
                <div class="plan-info">
                  <h3>{{ plan.title }}</h3>
                  <div class="plan-meta">
                    <el-tag size="small" :type="getStatusType(plan.status)">
                      {{ getStatusLabel(plan.status) }}
                    </el-tag>
                    <span class="date-range">
                      {{ plan.startDate }} - {{ plan.endDate }}
                    </span>
                  </div>
                </div>
                <div class="plan-progress">
                  <el-progress
                    type="circle"
                    :percentage="plan.progress"
                    :width="60"
                    :color="getProgressColor(plan.progress)"
                  />
                </div>
              </div>

              <div class="plan-content">
                <p class="plan-desc">{{ plan.description }}</p>

                <div class="plan-targets">
                  <span class="targets-label">目标课程：</span>
                  <el-tag
                    v-for="course in plan.targets"
                    :key="course"
                    size="small"
                    effect="plain"
                    style="margin-right: 6px;"
                  >
                    {{ course }}
                  </el-tag>
                </div>

                <div class="plan-tasks">
                  <h4>今日任务</h4>
                  <div class="task-list">
                    <div
                      v-for="(task, index) in plan.tasks"
                      :key="index"
                      class="task-item"
                      :class="{ completed: task.completed }"
                    >
                      <el-checkbox v-model="task.completed" @change="updateTaskStatus(plan, task)">
                        {{ task.title }}
                      </el-checkbox>
                      <el-tag v-if="task.important" size="small" type="danger">重要</el-tag>
                    </div>
                  </div>
                </div>
              </div>

              <div class="plan-actions">
                <el-button size="small" @click="viewPlanDetail(plan)">查看详情</el-button>
                <el-button size="small" type="primary" @click="continueLearning(plan)">继续学习</el-button>
              </div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
    </el-card>

    <!-- 创建计划弹窗 -->
    <el-dialog v-model="showCreateDialog" title="创建学习计划" width="500px" destroy-on-close>
      <el-form :model="createForm" label-width="100px">
        <el-form-item label="选择课程">
          <el-select v-model="createForm.courseId" placeholder="请选择课程" style="width: 100%;">
            <el-option
              v-for="course in courseOptions"
              :key="course.id"
              :label="course.name"
              :value="course.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="计划天数">
          <el-input-number v-model="createForm.days" :min="1" :max="90" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" :loading="creating" @click="handleCreatePlan">创建</el-button>
      </template>
    </el-dialog>

    <!-- 计划详情弹窗 -->
    <el-dialog v-model="showDetailDialog" title="计划详情" width="650px" destroy-on-close>
      <div v-if="detailPlan">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="计划名称" :span="2">{{ detailPlan.title }}</el-descriptions-item>
          <el-descriptions-item label="开始日期">{{ detailPlan.startDate }}</el-descriptions-item>
          <el-descriptions-item label="结束日期">{{ detailPlan.endDate }}</el-descriptions-item>
          <el-descriptions-item label="每日学时">{{ detailPlan.dailyHours }} 小时</el-descriptions-item>
          <el-descriptions-item label="进度">
            <el-progress :percentage="detailPlan.progress" :color="getProgressColor(detailPlan.progress)" />
          </el-descriptions-item>
          <el-descriptions-item label="计划描述" :span="2">{{ detailPlan.description }}</el-descriptions-item>
        </el-descriptions>

        <div v-if="detailPlan.dailyContent && detailPlan.dailyContent.length" style="margin-top: 20px;">
          <h4>每日安排</h4>
          <el-timeline>
            <el-timeline-item
              v-for="(day, index) in detailPlan.dailyContent"
              :key="index"
              :timestamp="'第' + day.day + '天'"
              placement="top"
            >
              <ul style="margin: 0; padding-left: 18px;">
                <li v-for="(task, ti) in day.tasks" :key="ti">{{ task }}</li>
              </ul>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
      <template #footer>
        <el-button @click="showDetailDialog = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { generateStudyPlan } from '@/api/qa'
import { getCourseList } from '@/api/course'

const plans = ref([])

const showCreateDialog = ref(false)
const showDetailDialog = ref(false)
const detailPlan = ref(null)
const creating = ref(false)
const courseOptions = ref([])

const createForm = reactive({
  courseId: null,
  days: 7
})

const completedTasks = computed(() => {
  return plans.value.reduce((sum, plan) => {
    return sum + plan.tasks.filter(t => t.completed).length
  }, 0)
})

const pendingTasks = computed(() => {
  return plans.value.reduce((sum, plan) => {
    return sum + plan.tasks.filter(t => !t.completed).length
  }, 0)
})

const completionRate = computed(() => {
  if (!plans.value.length) return 0
  const totalProgress = plans.value.reduce((sum, plan) => sum + plan.progress, 0)
  return Math.round(totalProgress / plans.value.length)
})

const getPlanColor = (status) => {
  const colors = { active: '#67C23A', completed: '#409EFF', pending: '#909399' }
  return colors[status] || '#909399'
}

const getStatusType = (status) => {
  const types = { active: 'success', completed: 'primary', pending: 'info' }
  return types[status] || 'info'
}

const getStatusLabel = (status) => {
  const labels = { active: '进行中', completed: '已完成', pending: '未开始' }
  return labels[status] || status
}

const getProgressColor = (progress) => {
  if (progress >= 80) return '#67C23A'
  if (progress >= 50) return '#E6A23C'
  return '#409EFF'
}

const updateTaskStatus = (plan, task) => {
  const completedCount = plan.tasks.filter(t => t.completed).length
  plan.progress = Math.round((completedCount / plan.tasks.length) * 100)
  ElMessage.success(task.completed ? '任务已完成！' : '任务已取消')
}

const loadCourses = async () => {
  try {
    const res = await getCourseList({ page: 1, size: 100 })
    courseOptions.value = res.data?.records || res.data || []
  } catch {
    courseOptions.value = []
  }
}

const createPlan = () => {
  createForm.courseId = null
  createForm.days = 7
  showCreateDialog.value = true
}

const handleCreatePlan = async () => {
  if (!createForm.courseId) {
    ElMessage.warning('请选择课程')
    return
  }
  creating.value = true
  try {
    const res = await generateStudyPlan(createForm.courseId, createForm.days)
    const data = res.data

    const selectedCourse = courseOptions.value.find(c => c.id === createForm.courseId)
    const courseName = selectedCourse ? selectedCourse.name : '未知课程'

    const today = new Date()
    const endDate = new Date(today)
    endDate.setDate(endDate.getDate() + (data.days || createForm.days))

    const formatDate = (d) => {
      const y = d.getFullYear()
      const m = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${y}-${m}-${day}`
    }

    const newPlan = {
      id: Date.now(),
      title: data.planName || `${courseName}学习计划`,
      description: `${courseName} ${data.days || createForm.days}天学习计划，每日${data.dailyHours || 2}小时`,
      startDate: formatDate(today),
      endDate: formatDate(endDate),
      status: 'active',
      progress: 0,
      dailyHours: data.dailyHours || 2,
      targets: [courseName],
      tasks: (data.content && data.content.length > 0)
        ? data.content[0].tasks.map(t => ({
            title: typeof t === 'string' ? t : t.title,
            completed: false,
            important: false
          }))
        : [],
      dailyContent: data.content || []
    }

    plans.value.unshift(newPlan)
    showCreateDialog.value = false
    ElMessage.success('学习计划创建成功')
  } catch (e) {
    ElMessage.error(e.message || '创建计划失败，请稍后重试')
  } finally {
    creating.value = false
  }
}

const viewPlanDetail = (plan) => {
  detailPlan.value = plan
  showDetailDialog.value = true
}

const continueLearning = (plan) => {
  ElMessage.success('正在跳转学习页面...')
}

onMounted(() => {
  loadCourses()
})
</script>

<style lang="scss" scoped>
.learning-plan {
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
  
  .plan-overview {
    margin-bottom: 30px;
    
    .overview-card {
      display: flex;
      align-items: center;
      padding: 20px;
      background: #fafafa;
      border-radius: 8px;
      
      .overview-icon {
        width: 56px;
        height: 56px;
        border-radius: 14px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 15px;
      }
      
      .overview-info {
        display: flex;
        flex-direction: column;
        
        .value {
          font-size: 28px;
          font-weight: 600;
          color: #303133;
        }
        
        .label {
          font-size: 13px;
          color: #909399;
        }
      }
    }
  }
  
  .plan-card {
    .plan-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      margin-bottom: 15px;
      
      .plan-info {
        h3 {
          font-size: 18px;
          color: #303133;
          margin-bottom: 8px;
        }
        
        .plan-meta {
          display: flex;
          align-items: center;
          gap: 10px;
          
          .date-range {
            font-size: 13px;
            color: #909399;
          }
        }
      }
    }
    
    .plan-content {
      .plan-desc {
        color: #606266;
        margin-bottom: 15px;
        line-height: 1.6;
      }
      
      .plan-targets {
        margin-bottom: 15px;
        
        .targets-label {
          font-size: 13px;
          color: #909399;
        }
      }
      
      .plan-tasks {
        margin-bottom: 15px;
        
        h4 {
          font-size: 14px;
          color: #303133;
          margin-bottom: 10px;
        }
        
        .task-list {
          .task-item {
            display: flex;
            align-items: center;
            padding: 8px 0;
            border-bottom: 1px solid #f0f0f0;
            
            &:last-child {
              border-bottom: none;
            }
            
            &.completed {
              .el-checkbox__label {
                text-decoration: line-through;
                color: #909399;
              }
            }
          }
        }
      }
    }
    
    .plan-actions {
      display: flex;
      justify-content: flex-end;
      gap: 10px;
      padding-top: 15px;
      border-top: 1px solid #f0f0f0;
    }
  }
}
</style>
