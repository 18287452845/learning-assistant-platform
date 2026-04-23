<template>
  <div class="student-home">
    <!-- 顶部欢迎区 - 更紧凑自然 -->
    <div class="hero-section">
      <div class="hero-content">
        <div class="greeting">
          <span class="greeting-time">{{ timeGreeting }}</span>
          <h1 class="user-name">{{ userStore.userInfo.name }}</h1>
        </div>
        <p class="hero-subtitle">继续加油，距离目标又近了一步</p>
      </div>
      
      <!-- 简洁统计 - 横向排列 -->
      <div class="quick-stats">
        <div class="stat-item">
          <span class="stat-num">{{ statsData.learningDays }}</span>
          <span class="stat-label">天</span>
        </div>
        <div class="stat-divider"></div>
        <div class="stat-item">
          <span class="stat-num">{{ statsData.questionsCount }}</span>
          <span class="stat-label">问答</span>
        </div>
        <div class="stat-divider"></div>
        <div class="stat-item">
          <span class="stat-num">{{ statsData.knowledgePoints }}</span>
          <span class="stat-label">知识点</span>
        </div>
      </div>
    </div>

    <!-- 快捷入口 - 横向滚动风格 -->
    <div class="quick-actions">
      <div class="action-scroll">
        <div class="action-card primary" @click="$router.push('/student/qa')">
          <span class="action-text">智能问答</span>
          <span class="action-arrow">→</span>
        </div>
        <div class="action-card secondary" @click="$router.push('/student/courses')">
          <span class="action-text">我的课程</span>
          <span class="action-arrow">→</span>
        </div>
        <div class="action-card accent" @click="$router.push('/student/wrong-questions')">
          <span class="action-text">错题本</span>
          <span class="action-arrow">→</span>
        </div>
        <div class="action-card green" @click="$router.push('/student/learning-plan')">
          <span class="action-text">学习计划</span>
          <span class="action-arrow">→</span>
        </div>
      </div>
    </div>

    <!-- 主体内容区 - 瀑布流风格 -->
    <div class="content-masonry">
      <!-- 左侧主栏 -->
      <div class="main-column">
        <!-- 课程卡片 - 大卡片突出 -->
        <div class="section-card highlight">
          <div class="section-header">
            <h3 class="section-title">在学课程</h3>
            <span class="section-count">{{ recentCourses.length }}门</span>
          </div>
          <div class="course-list">
            <div 
              v-for="course in recentCourses" 
              :key="course.id" 
              class="course-item"
              @click="$router.push(`/student/course/${course.id}`)"
            >
              <div class="course-thumb" :style="{ background: course.gradient }">
                {{ course.name.charAt(0) }}
              </div>
              <div class="course-body">
                <h4>{{ course.name }}</h4>
                <p>{{ course.teacher }}</p>
                <div class="progress-bar">
                  <div class="progress-fill" :style="{ width: course.progress + '%' }"></div>
                </div>
                <span class="progress-text">{{ course.progress }}%</span>
              </div>
            </div>
            <div v-if="!recentCourses.length" class="empty-state">
              <span>暂无在学课程</span>
            </div>
          </div>
        </div>

        <!-- 学习记录图表 -->
        <div class="section-card">
          <div class="section-header">
            <h3 class="section-title">本周学习</h3>
          </div>
          <div class="chart-container" ref="chartRef"></div>
        </div>
      </div>

      <!-- 右侧边栏 -->
      <div class="side-column">
        <!-- 公告 - 小卡片 -->
        <div class="section-card compact">
          <div class="section-header">
            <h3 class="section-title">系统公告</h3>
          </div>
          <div class="announcement-list">
            <div 
              v-for="item in announcements" 
              :key="item.id" 
              class="announcement-item"
            >
              <span v-if="item.important" class="badge-important">重要</span>
              <p class="announcement-title">{{ item.title }}</p>
              <span class="announcement-time">{{ item.time }}</span>
            </div>
            <div v-if="!announcements.length" class="empty-hint">暂无公告</div>
          </div>
        </div>

        <!-- 薄弱点 -->
        <div class="section-card compact">
          <div class="section-header">
            <h3 class="section-title">待加强</h3>
          </div>
          <div class="weak-list">
            <div 
              v-for="point in weakPoints" 
              :key="point.id" 
              class="weak-item"
            >
              <div class="weak-info">
                <span class="weak-subject">{{ point.subject }}</span>
                <span class="weak-error">错误{{ point.errorCount }}次</span>
              </div>
              <div class="weak-rate">{{ Math.round(point.correctRate * 100) }}%</div>
            </div>
            <div v-if="!weakPoints.length" class="empty-hint">暂无薄弱点</div>
          </div>
        </div>

        <!-- 快捷操作 -->
        <div class="section-card action-card-mini">
          <div class="mini-action" @click="$router.push('/student/qa')">
            <span>发起提问</span>
          </div>
          <div class="mini-action" @click="$router.push('/student/qa/history')">
            <span>查看历史</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onUnmounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { mockCourses, mockAnnouncements, mockWrongQuestions } from '@/mock/data'
import * as echarts from 'echarts'

const userStore = useUserStore()
const chartRef = ref(null)
let chart = null

// 动态问候语
const timeGreeting = computed(() => {
  const hour = new Date().getHours()
  if (hour < 12) return '上午好'
  if (hour < 18) return '下午好'
  return '晚上好'
})

const statsData = reactive({
  learningDays: 45,
  questionsCount: 28,
  knowledgePoints: 156
})

const gradients = [
  'linear-gradient(135deg, #FF8C42 0%, #FFB380 100%)',
  'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
  'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
  'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)'
]

const recentCourses = ref(mockCourses.slice(0, 3).map((c, i) => ({
  ...c,
  progress: [65, 78, 42][i],
  gradient: gradients[i]
})))

const announcements = ref(mockAnnouncements)
const weakPoints = ref(mockWrongQuestions.slice(0, 3))

const initChart = () => {
  if (!chartRef.value) return
  
  chart = echarts.init(chartRef.value)
  const option = {
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
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
      axisLabel: { color: '#636E72', fontSize: 12 }
    },
    yAxis: {
      type: 'value',
      show: false,
      splitLine: { show: false }
    },
    series: [{
      type: 'line',
      smooth: true,
      symbol: 'none',
      lineStyle: {
        color: '#FF8C42',
        width: 3
      },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0, y: 0, x2: 0, y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(255, 140, 66, 0.25)' },
            { offset: 1, color: 'rgba(255, 140, 66, 0)' }
          ]
        }
      },
      data: [12, 18, 15, 22, 19, 25, 20]
    }]
  }
  chart.setOption(option)
}

const handleResize = () => {
  chart?.resize()
}

onMounted(() => {
  initChart()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  chart?.dispose()
})
</script>

<style lang="scss" scoped>
.student-home {
  padding: 24px 32px;
  background: linear-gradient(180deg, #FAFBFC 0%, #F5F6F8 100%);
  min-height: 100vh;
}

// 顶部欢迎区
.hero-section {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 24px;
  padding: 28px 32px;
  background: linear-gradient(135deg, #FF8C42 0%, #FF6B35 100%);
  border-radius: 20px;
  color: #FFFFFF;
}

.greeting-time {
  font-size: 13px;
  opacity: 0.85;
  display: block;
  margin-bottom: 4px;
}

.user-name {
  font-size: 28px;
  font-weight: 700;
  letter-spacing: 1px;
}

.hero-subtitle {
  font-size: 14px;
  opacity: 0.9;
  margin-top: 8px;
}

.quick-stats {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 16px 24px;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 14px;
  backdrop-filter: blur(10px);
}

.stat-item {
  text-align: center;
}

.stat-num {
  font-size: 24px;
  font-weight: 700;
  display: block;
  line-height: 1;
}

.stat-label {
  font-size: 11px;
  opacity: 0.8;
  margin-top: 4px;
  display: block;
}

.stat-divider {
  width: 1px;
  height: 32px;
  background: rgba(255, 255, 255, 0.25);
}

// 快捷入口 - 横向滚动
.quick-actions {
  margin-bottom: 28px;
}

.action-scroll {
  display: flex;
  gap: 12px;
  overflow-x: auto;
  padding-bottom: 8px;
  
  &::-webkit-scrollbar {
    display: none;
  }
}

.action-card {
  flex-shrink: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  border-radius: 14px;
  cursor: pointer;
  transition: all 0.25s ease;
  
  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  }
}

.action-text {
  font-size: 15px;
  font-weight: 600;
  color: #FFFFFF;
}

.action-arrow {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.8);
  margin-left: 16px;
}

.action-card.primary { background: linear-gradient(135deg, #FF8C42, #FF6B35); }
.action-card.secondary { background: linear-gradient(135deg, #667eea, #764ba2); }
.action-card.accent { background: linear-gradient(135deg, #f093fb, #f5576c); }
.action-card.green { background: linear-gradient(135deg, #43e97b, #38f9d7); }

// 内容区 - 瀑布流
.content-masonry {
  display: grid;
  grid-template-columns: 1fr 340px;
  gap: 24px;
}

.main-column {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.side-column {
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
  
  &.highlight {
    position: relative;
    overflow: hidden;
    
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      height: 4px;
      background: linear-gradient(90deg, #FF8C42, #FFB380);
    }
  }
  
  &.compact {
    padding: 20px;
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

.section-count {
  font-size: 12px;
  color: #B2BEC3;
  padding: 4px 10px;
  background: #F5F6F8;
  border-radius: 10px;
}

// 课程列表
.course-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.course-item {
  display: flex;
  gap: 16px;
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

.course-thumb {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: 700;
  color: #FFFFFF;
  flex-shrink: 0;
}

.course-body {
  flex: 1;
  min-width: 0;
  
  h4 {
    font-size: 15px;
    font-weight: 600;
    color: #2D3436;
    margin-bottom: 4px;
  }
  
  p {
    font-size: 12px;
    color: #636E72;
    margin-bottom: 10px;
  }
}

.progress-bar {
  height: 4px;
  background: #E8ECEF;
  border-radius: 2px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #FF8C42, #FFB380);
  border-radius: 2px;
  transition: width 0.5s ease;
}

.progress-text {
  font-size: 11px;
  color: #B2BEC3;
  margin-top: 6px;
  display: block;
}

// 图表
.chart-container {
  height: 180px;
}

// 公告列表
.announcement-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.announcement-item {
  padding-bottom: 14px;
  border-bottom: 1px dashed #F0F2F5;
  
  &:last-child {
    border-bottom: none;
    padding-bottom: 0;
  }
}

.badge-important {
  display: inline-block;
  font-size: 10px;
  padding: 2px 6px;
  background: #FFEBEE;
  color: #EF5350;
  border-radius: 4px;
  margin-bottom: 6px;
}

.announcement-title {
  font-size: 13px;
  color: #2D3436;
  line-height: 1.5;
  margin-bottom: 4px;
}

.announcement-time {
  font-size: 11px;
  color: #B2BEC3;
}

// 薄弱点
.weak-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.weak-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 14px;
  background: #FFF8F5;
  border-radius: 10px;
}

.weak-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.weak-subject {
  font-size: 13px;
  font-weight: 500;
  color: #2D3436;
}

.weak-error {
  font-size: 11px;
  color: #636E72;
}

.weak-rate {
  font-size: 14px;
  font-weight: 600;
  color: #EF5350;
}

// 快捷操作
.action-card-mini {
  display: flex;
  gap: 12px;
  padding: 16px;
}

.mini-action {
  flex: 1;
  text-align: center;
  padding: 12px;
  background: linear-gradient(135deg, #FF8C42 0%, #FF6B35 100%);
  color: #FFFFFF;
  font-size: 13px;
  font-weight: 500;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s ease;
  
  &:hover {
    transform: scale(1.02);
    box-shadow: 0 4px 12px rgba(255, 140, 66, 0.3);
  }
}

.empty-state {
  padding: 32px;
  text-align: center;
  color: #B2BEC3;
  font-size: 13px;
}

.empty-hint {
  text-align: center;
  color: #B2BEC3;
  font-size: 12px;
  padding: 16px 0;
}

// 响应式
@media (max-width: 1200px) {
  .content-masonry {
    grid-template-columns: 1fr;
  }
  
  .side-column {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }
}

@media (max-width: 768px) {
  .student-home {
    padding: 16px;
  }
  
  .hero-section {
    flex-direction: column;
    align-items: flex-start;
    gap: 20px;
    padding: 20px;
  }
  
  .side-column {
    grid-template-columns: 1fr;
  }
  
  .action-scroll {
    gap: 10px;
  }
  
  .action-card {
    padding: 14px 18px;
  }
}
</style>
