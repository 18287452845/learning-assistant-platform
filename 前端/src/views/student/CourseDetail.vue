<template>
  <div class="course-detail">
    <!-- 课程头部信息 -->
    <div class="course-header" :style="{ background: courseColor }">
      <div class="header-content">
        <div class="course-info">
          <h1>{{ course.name }}</h1>
          <p class="course-code">{{ course.code }}</p>
          <div class="course-meta">
            <span><el-icon><User /></el-icon> {{ course.teacher }}</span>
            <span><el-icon><Reading /></el-icon> {{ course.students }} 人在学</span>
            <span><el-icon><Document /></el-icon> {{ course.resourceCount }} 个资源</span>
          </div>
        </div>
        <div class="course-actions">
          <el-button type="primary" size="large" @click="continueLearning">
            <el-icon><VideoPlay /></el-icon> 继续学习
          </el-button>
          <el-button size="large" @click="$router.push('/student/qa')">
            <el-icon><ChatDotRound /></el-icon> 提问
          </el-button>
        </div>
      </div>
    </div>
    
    <!-- 课程内容 -->
    <div class="course-content">
      <el-row :gutter="20">
        <!-- 左侧主内容 -->
        <el-col :span="16">
          <!-- 课程简介 -->
          <el-card shadow="hover" class="info-card">
            <template #header>
              <span><el-icon><InfoFilled /></el-icon> 课程简介</span>
            </template>
            <p class="course-description">{{ course.description }}</p>
          </el-card>
          
          <!-- 学习进度 -->
          <el-card shadow="hover" class="progress-card">
            <template #header>
              <span><el-icon><TrendCharts /></el-icon> 学习进度</span>
            </template>
            <div class="progress-overview">
              <div class="progress-chart" ref="progressChartRef" style="height: 200px;"></div>
            </div>
          </el-card>
          
          <!-- 课程资源 -->
          <el-card shadow="hover" class="resource-card">
            <template #header>
              <div class="card-header">
                <span><el-icon><Document /></el-icon> 课程资源</span>
                <el-button type="primary" size="small">
                  <el-icon><Upload /></el-icon> 上传资源
                </el-button>
              </div>
            </template>
            <el-table :data="course.recentResources" stripe>
              <el-table-column prop="title" label="资源名称" min-width="200" />
              <el-table-column prop="type" label="类型" width="100" align="center">
                <template #default="{ row }">
                  <el-tag size="small" :type="getResourceType(row.type)">
                    {{ row.type }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="uploadTime" label="上传时间" width="120" />
              <el-table-column label="操作" width="150" align="center">
                <template #default>
                  <el-button type="primary" link size="small">下载</el-button>
                  <el-button type="primary" link size="small">预览</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
        
        <!-- 右侧边栏 -->
        <el-col :span="8">
          <!-- 知识点 -->
          <el-card shadow="hover" class="knowledge-card">
            <template #header>
              <span><el-icon><Collection /></el-icon> 知识点</span>
            </template>
            <div class="knowledge-list">
              <div 
                v-for="(point, index) in course.knowledgePoints" 
                :key="index"
                class="knowledge-item"
              >
                <span class="knowledge-index">{{ index + 1 }}</span>
                <span class="knowledge-name">{{ point }}</span>
                <el-icon class="knowledge-status" :class="{ mastered: Math.random() > 0.5 }">
                  <CircleCheck v-if="Math.random() > 0.5" />
                  <Clock v-else />
                </el-icon>
              </div>
            </div>
          </el-card>
          
          <!-- 相关课程 -->
          <el-card shadow="hover" class="related-card">
            <template #header>
              <span><el-icon><Reading /></el-icon> 相关课程</span>
            </template>
            <div class="related-list">
              <div 
                v-for="related in relatedCourses" 
                :key="related.id"
                class="related-item"
                @click="$router.push(`/student/course/${related.id}`)"
              >
                <div class="related-cover" :style="{ background: related.color }">
                  {{ related.name.charAt(0) }}
                </div>
                <div class="related-info">
                  <h4>{{ related.name }}</h4>
                  <p>{{ related.teacher }}</p>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import { mockCourseDetail, mockCourses } from '@/mock/data'
import * as echarts from 'echarts'

const route = useRoute()
const courseId = route.params.id
const progressChartRef = ref(null)
let chart = null

const course = reactive({ ...mockCourseDetail })
const courseColor = 'linear-gradient(135deg, #667eea, #764ba2)'

const relatedCourses = ref(mockCourses.slice(0, 3).map((c, i) => ({
  ...c,
  color: ['#f093fb', '#4facfe', '#43e97b'][i]
})))

const getResourceType = (type) => {
  const types = { PDF: 'danger', PPT: 'warning', ZIP: 'info', DOC: 'success' }
  return types[type] || 'info'
}

const continueLearning = () => {
  // 跳转学习
}

const initProgressChart = () => {
  if (!progressChartRef.value) return
  
  chart = echarts.init(progressChartRef.value)
  const option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      bottom: '5%',
      left: 'center'
    },
    series: [{
      name: '学习进度',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: false,
        position: 'center'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: 20,
          fontWeight: 'bold'
        }
      },
      data: [
        { value: 65, name: '已完成', itemStyle: { color: '#67C23A' } },
        { value: 20, name: '进行中', itemStyle: { color: '#409EFF' } },
        { value: 15, name: '未开始', itemStyle: { color: '#E6E7EB' } }
      ]
    }]
  }
  chart.setOption(option)
}

onMounted(() => {
  initProgressChart()
  window.addEventListener('resize', () => chart?.resize())
})

onUnmounted(() => {
  chart?.dispose()
  window.removeEventListener('resize', () => chart?.resize())
})
</script>

<style lang="scss" scoped>
.course-detail {
  .course-header {
    padding: 40px;
    color: white;
    margin-bottom: 20px;
    
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      max-width: 1200px;
      margin: 0 auto;
      
      .course-info {
        h1 {
          font-size: 32px;
          margin-bottom: 8px;
        }
        
        .course-code {
          font-size: 14px;
          opacity: 0.8;
          margin-bottom: 15px;
        }
        
        .course-meta {
          display: flex;
          gap: 25px;
          font-size: 14px;
          
          span {
            display: flex;
            align-items: center;
            gap: 6px;
          }
        }
      }
      
      .course-actions {
        display: flex;
        gap: 15px;
      }
    }
  }
  
  .course-content {
    max-width: 1200px;
    margin: 0 auto;
    
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    
    .info-card {
      margin-bottom: 20px;
      
      .course-description {
        line-height: 1.8;
        color: #606266;
      }
    }
    
    .progress-card,
    .resource-card {
      margin-bottom: 20px;
    }
    
    .knowledge-card {
      margin-bottom: 20px;
      
      .knowledge-list {
        .knowledge-item {
          display: flex;
          align-items: center;
          padding: 12px;
          border-bottom: 1px solid #f0f0f0;
          
          &:last-child {
            border-bottom: none;
          }
          
          .knowledge-index {
            width: 24px;
            height: 24px;
            background: #ecf5ff;
            color: #409EFF;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 12px;
            font-weight: 600;
            margin-right: 12px;
          }
          
          .knowledge-name {
            flex: 1;
            color: #303133;
          }
          
          .knowledge-status {
            color: #909399;
            
            &.mastered {
              color: #67C23A;
            }
          }
        }
      }
    }
    
    .related-card {
      .related-list {
        .related-item {
          display: flex;
          align-items: center;
          padding: 12px;
          cursor: pointer;
          border-radius: 8px;
          transition: background 0.3s;
          
          &:hover {
            background: #f5f7fa;
          }
          
          .related-cover {
            width: 48px;
            height: 48px;
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-weight: 600;
            margin-right: 12px;
          }
          
          .related-info {
            h4 {
              font-size: 14px;
              color: #303133;
              margin-bottom: 4px;
            }
            
            p {
              font-size: 12px;
              color: #909399;
            }
          }
        }
      }
    }
  }
}
</style>
