<template>
  <div class="teacher-home">
    <!-- 顶部欢迎区 -->
    <div class="hero-section">
      <div class="hero-content">
        <span class="greeting-label">你好</span>
        <h1 class="user-name">{{ userStore.userInfo.name }} 老师</h1>
        <p class="hero-subtitle">今天有 <strong>{{ pendingQuestions }}</strong> 个问题等待您的回答</p>
      </div>
    </div>

    <!-- 统计概览 - 横向卡片 -->
    <div class="stats-grid">
      <div class="stat-card" v-for="(stat, index) in statsData" :key="index" :style="{ background: stat.gradient }">
        <div class="stat-info">
          <span class="stat-value">{{ stat.value }}</span>
          <span class="stat-label">{{ stat.label }}</span>
        </div>
        <div class="stat-indicator"></div>
      </div>
    </div>

    <!-- 主体内容 -->
    <div class="content-layout">
      <!-- 左侧 - 待处理问题 -->
      <div class="main-section">
        <div class="section-card">
          <div class="section-header">
            <h3 class="section-title">待处理问题</h3>
            <span class="badge-count">{{ recentQuestions.length }} 条</span>
          </div>
          
          <div class="question-list">
            <div 
              v-for="q in recentQuestions" 
              :key="q.id"
              class="question-item"
              @click="answerQuestion(q)"
            >
              <div class="question-body">
                <h4>{{ q.title }}</h4>
                <div class="question-meta">
                  <span class="course-tag">{{ q.course }}</span>
                  <span class="student-name">{{ q.student }}</span>
                  <span class="time-ago">{{ q.time }}</span>
                </div>
              </div>
              <button class="answer-btn">回答</button>
            </div>
            
            <div v-if="!recentQuestions.length" class="empty-state">
              <span>暂无待处理问题</span>
            </div>
          </div>
          
          <router-link to="/teacher/qa" class="view-more">查看全部 →</router-link>
        </div>

        <!-- 问题类型分布 -->
        <div class="section-card">
          <div class="section-header">
            <h3 class="section-title">问题类型分布</h3>
          </div>
          <div class="chart-container" ref="typeChartRef"></div>
        </div>
      </div>

      <!-- 右侧 - 趋势和数据 -->
      <div class="side-section">
        <!-- 高频问题 -->
        <div class="section-card">
          <div class="section-header">
            <h3 class="section-title">高频问题</h3>
          </div>
          <div class="keywords-list">
            <div v-for="(keyword, i) in highFrequencyKeywords" :key="i" class="keyword-item">
              <span class="keyword-rank">{{ i + 1 }}</span>
              <span class="keyword-text">{{ keyword.text }}</span>
              <span class="keyword-count">{{ keyword.count }}次</span>
            </div>
          </div>
        </div>

        <!-- 问答趋势 -->
        <div class="section-card">
          <div class="section-header">
            <h3 class="section-title">本周趋势</h3>
          </div>
          <div class="chart-container small" ref="trendChartRef"></div>
        </div>

        <!-- 快捷操作 -->
        <div class="section-card compact">
          <div class="quick-actions">
            <div class="quick-btn" @click="$router.push('/teacher/qa')">
              <span>答疑管理</span>
            </div>
            <div class="quick-btn" @click="$router.push('/teacher/students')">
              <span>学生列表</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { mockQuestions, mockStatistics } from '@/mock/data'
import * as echarts from 'echarts'

const router = useRouter()
const userStore = useUserStore()
const typeChartRef = ref(null)
const trendChartRef = ref(null)
let typeChart = null
let trendChart = null

const pendingQuestions = ref(5)
const answeredQuestions = ref(128)
const studentCount = ref(156)
const todayAnswers = ref(12)

const statsData = [
  { value: pendingQuestions, label: '待答疑', gradient: 'linear-gradient(135deg, #FF8A80, #FF5252)' },
  { value: answeredQuestions, label: '已回答', gradient: 'linear-gradient(135deg, #82B1FF, #448AFF)' },
  { value: studentCount, label: '学生数', gradient: 'linear-gradient(135deg, #B388FF, #7C4DFF)' },
  { value: todayAnswers, label: '今日回答', gradient: 'linear-gradient(135deg, #69F0AE, #00E676)' }
]

const recentQuestions = ref(mockQuestions.slice(0, 4))

const highFrequencyKeywords = [
  { text: '如何理解递归算法', count: 45 },
  { text: '数据库索引原理', count: 38 },
  { text: '前端性能优化', count: 32 },
  { text: '设计模式应用', count: 28 },
  { text: 'Git协作流程', count: 24 }
]

const answerQuestion = (q) => {
  ElMessage.info('跳转到回答页面...')
}

const initCharts = () => {
  // 问题类型分布
  if (typeChartRef.value) {
    typeChart = echarts.init(typeChartRef.value)
    typeChart.setOption({
      tooltip: { 
        trigger: 'item',
        backgroundColor: 'rgba(255,255,255,0.95)',
        borderColor: '#E8ECEF',
        textStyle: { color: '#2D3436' }
      },
      series: [{
        type: 'pie',
        radius: ['45%', '70%'],
        center: ['50%', '50%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 6,
          borderColor: '#FFFFFF',
          borderWidth: 2
        },
        label: {
          show: false
        },
        data: mockStatistics.questionTypes
      }],
      color: ['#FF8A80', '#82B1FF', '#69F0AE', '#FFD180', '#B388FF']
    })
  }
  
  // 问答趋势
  if (trendChartRef.value) {
    trendChart = echarts.init(trendChartRef.value)
    trendChart.setOption({
      tooltip: {
        trigger: 'axis',
        backgroundColor: 'rgba(255,255,255,0.95)',
        borderColor: '#E8ECEF',
        textStyle: { color: '#2D3436' }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        top: '10px',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
        axisLine: { show: false },
        axisTick: { show: false },
        axisLabel: { color: '#636E72', fontSize: 11 }
      },
      yAxis: {
        type: 'value',
        show: false,
        splitLine: { show: false }
      },
      series: [
        {
          name: '提问',
          type: 'bar',
          barWidth: '35%',
          itemStyle: {
            color: {
              type: 'linear',
              x: 0, y: 0, x2: 0, y2: 1,
              colorStops: [
                { offset: 0, color: '#69F0AE' },
                { offset: 1, color: '#00E676' }
              ]
            },
            borderRadius: [4, 4, 0, 0]
          },
          data: [12, 18, 15, 22, 19, 25, 20]
        }
      ]
    })
  }
}

const handleResize = () => {
  typeChart?.resize()
  trendChart?.resize()
}

onMounted(() => {
  initCharts()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  typeChart?.dispose()
  trendChart?.dispose()
})
</script>

<style lang="scss" scoped>
.teacher-home {
  padding: 24px 32px;
  background: linear-gradient(180deg, #FAFBFC 0%, #F5F6F8 100%);
  min-height: 100vh;
}

// 顶部欢迎区
.hero-section {
  margin-bottom: 24px;
  padding: 28px 32px;
  background: linear-gradient(135deg, #34A853 0%, #1E8449 100%);
  border-radius: 20px;
  color: #FFFFFF;
}

.greeting-label {
  font-size: 14px;
  opacity: 0.85;
}

.user-name {
  font-size: 26px;
  font-weight: 700;
  margin: 4px 0 8px;
  letter-spacing: 1px;
}

.hero-subtitle {
  font-size: 14px;
  opacity: 0.9;
  
  strong {
    font-weight: 700;
    font-size: 18px;
  }
}

// 统计卡片
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.stat-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px;
  border-radius: 14px;
  color: #FFFFFF;
  position: relative;
  overflow: hidden;
}

.stat-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
  position: relative;
  z-index: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  line-height: 1;
}

.stat-label {
  font-size: 13px;
  opacity: 0.9;
}

.stat-indicator {
  position: absolute;
  right: -20px;
  bottom: -20px;
  width: 80px;
  height: 80px;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 50%;
}

// 内容布局
.content-layout {
  display: grid;
  grid-template-columns: 1fr 340px;
  gap: 24px;
}

.main-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.side-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

// 区块卡片
.section-card {
  background: #FFFFFF;
  border-radius: 18px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  
  &.compact {
    padding: 16px;
  }
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #2D3436;
}

.badge-count {
  font-size: 12px;
  color: #B2BEC3;
  padding: 4px 10px;
  background: #F5F6F8;
  border-radius: 10px;
}

// 问题列表
.question-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.question-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  background: #FAFBFC;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
  
  &:hover {
    background: #F0F2F5;
    transform: translateX(4px);
  }
}

.question-body {
  flex: 1;
  min-width: 0;
  
  h4 {
    font-size: 14px;
    font-weight: 500;
    color: #2D3436;
    margin-bottom: 8px;
  }
}

.question-meta {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 12px;
  color: #636E72;
}

.course-tag {
  padding: 2px 8px;
  background: linear-gradient(135deg, #34A853, #1E8449);
  color: #FFFFFF;
  border-radius: 4px;
  font-size: 11px;
}

.student-name {
  color: #2D3436;
}

.time-ago {
  color: #B2BEC3;
}

.answer-btn {
  padding: 8px 20px;
  background: linear-gradient(135deg, #34A853, #1E8449);
  color: #FFFFFF;
  border: none;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  
  &:hover {
    transform: scale(1.05);
    box-shadow: 0 4px 12px rgba(52, 168, 83, 0.3);
  }
}

.view-more {
  display: block;
  text-align: center;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px dashed #E8ECEF;
  color: #34A853;
  font-size: 13px;
  text-decoration: none;
  transition: color 0.2s;
  
  &:hover {
    color: #1E8449;
  }
}

// 高频关键词
.keywords-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.keyword-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 14px;
  background: #F8F9FA;
  border-radius: 10px;
}

.keyword-rank {
  width: 22px;
  height: 22px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #34A853, #1E8449);
  color: #FFFFFF;
  font-size: 11px;
  font-weight: 600;
  border-radius: 6px;
}

.keyword-text {
  flex: 1;
  font-size: 13px;
  color: #2D3436;
}

.keyword-count {
  font-size: 12px;
  color: #B2BEC3;
}

// 图表
.chart-container {
  height: 200px;
  
  &.small {
    height: 140px;
  }
}

// 快捷操作
.quick-actions {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.quick-btn {
  padding: 14px;
  background: linear-gradient(135deg, #34A853, #1E8449);
  color: #FFFFFF;
  text-align: center;
  font-size: 13px;
  font-weight: 500;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s ease;
  
  &:hover {
    transform: scale(1.02);
    box-shadow: 0 4px 12px rgba(52, 168, 83, 0.3);
  }
}

// 空状态
.empty-state {
  padding: 32px;
  text-align: center;
  color: #B2BEC3;
  font-size: 13px;
}

// 响应式
@media (max-width: 1200px) {
  .content-layout {
    grid-template-columns: 1fr;
  }
  
  .side-section {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }
}

@media (max-width: 768px) {
  .teacher-home {
    padding: 16px;
  }
  
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .side-section {
    grid-template-columns: 1fr;
  }
  
  .hero-section {
    padding: 20px;
  }
}
</style>
