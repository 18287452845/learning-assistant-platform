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
        <el-timeline>
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
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'

const plans = reactive([
  {
    id: 1,
    title: '数据结构与算法强化训练',
    description: '系统学习数据结构与算法，完成树、图、排序等核心章节的学习和练习',
    startDate: '2024-03-01',
    endDate: '2024-04-01',
    status: 'active',
    progress: 65,
    targets: ['数据结构与算法', '计算机网络'],
    tasks: [
      { title: '完成二叉树遍历练习题', completed: true, important: false },
      { title: '复习图的遍历算法', completed: true, important: false },
      { title: '完成快速排序代码实现', completed: false, important: true }
    ]
  },
  {
    id: 2,
    title: '人工智能导论入门',
    description: '了解人工智能基础知识，为后续深入学习打下基础',
    startDate: '2024-03-15',
    endDate: '2024-04-15',
    status: 'active',
    progress: 30,
    targets: ['人工智能导论'],
    tasks: [
      { title: '观看机器学习基础视频', completed: true, important: false },
      { title: '完成机器学习概述笔记', completed: false, important: true },
      { title: '理解监督学习和无监督学习', completed: false, important: false }
    ]
  },
  {
    id: 3,
    title: '期末考试冲刺',
    description: '全面复习各门课程，迎接期末考试',
    startDate: '2024-06-01',
    endDate: '2024-06-30',
    status: 'pending',
    progress: 0,
    targets: ['数据结构与算法', '计算机网络', '操作系统原理'],
    tasks: []
  }
])

const completedTasks = computed(() => {
  return plans.reduce((sum, plan) => {
    return sum + plan.tasks.filter(t => t.completed).length
  }, 0)
})

const pendingTasks = computed(() => {
  return plans.reduce((sum, plan) => {
    return sum + plan.tasks.filter(t => !t.completed).length
  }, 0)
})

const completionRate = computed(() => {
  if (!plans.length) return 0
  const totalProgress = plans.reduce((sum, plan) => sum + plan.progress, 0)
  return Math.round(totalProgress / plans.length)
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

const createPlan = () => {
  ElMessage.info('创建计划功能开发中...')
}

const viewPlanDetail = (plan) => {
  ElMessage.info('查看详情功能开发中...')
}

const continueLearning = (plan) => {
  ElMessage.success('正在跳转学习页面...')
}
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
