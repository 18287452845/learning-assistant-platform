<template>
  <div class="admin-home">
    <!-- 顶部概览 -->
    <div class="hero-section">
      <div class="hero-content">
        <h1 class="page-title">管理员控制台</h1>
        <p class="page-subtitle">{{ currentTime }}</p>
      </div>
    </div>

    <!-- 核心统计 -->
    <div class="stats-grid">
      <div class="stat-card purple" v-for="(stat, index) in statsData" :key="index">
        <div class="stat-icon-wrap">
          <div class="stat-icon" :style="{ background: stat.gradient }">
            <span class="stat-icon-text">{{ stat.icon }}</span>
          </div>
        </div>
        <div class="stat-body">
          <span class="stat-value">{{ stat.value }}</span>
          <span class="stat-label">{{ stat.label }}</span>
          <span class="stat-trend" :class="stat.trendClass">
            <span class="trend-arrow">{{ stat.trendArrow }}</span>
            {{ stat.trend }}
          </span>
        </div>
      </div>
    </div>

    <!-- 主体内容 -->
    <div class="content-layout">
      <!-- 左侧图表 -->
      <div class="main-section">
        <!-- 活跃趋势 -->
        <div class="section-card">
          <div class="section-header">
            <h3 class="section-title">活跃用户趋势</h3>
            <div class="time-tabs">
              <span class="tab active">本周</span>
              <span class="tab">本月</span>
              <span class="tab">本年</span>
            </div>
          </div>
          <div class="chart-container" ref="activeChartRef"></div>
        </div>

        <!-- 用户分布 -->
        <div class="section-card">
          <div class="section-header">
            <h3 class="section-title">用户类型分布</h3>
          </div>
          <div class="chart-container small" ref="userDistChartRef"></div>
        </div>
      </div>

      <!-- 右侧状态 -->
      <div class="side-section">
        <!-- 系统状态 -->
        <div class="section-card">
          <div class="section-header">
            <h3 class="section-title">系统状态</h3>
            <span class="status-badge online">运行中</span>
          </div>
          <div class="system-metrics">
            <div class="metric-item">
              <span class="metric-label">CPU 使用率</span>
              <div class="metric-bar">
                <div class="metric-fill" :style="{ width: systemStats.cpu + '%', background: '#7C4DFF' }"></div>
              </div>
              <span class="metric-value">{{ systemStats.cpu }}%</span>
            </div>
            <div class="metric-item">
              <span class="metric-label">内存占用</span>
              <div class="metric-bar">
                <div class="metric-fill" :style="{ width: systemStats.memory + '%', background: '#34A853' }"></div>
              </div>
              <span class="metric-value">{{ systemStats.memory }}%</span>
            </div>
            <div class="metric-item">
              <span class="metric-label">磁盘使用</span>
              <div class="metric-bar">
                <div class="metric-fill" :style="{ width: systemStats.disk + '%', background: '#FF8C42' }"></div>
              </div>
              <span class="metric-value">{{ systemStats.disk }}%</span>
            </div>
          </div>
        </div>

        <!-- 待处理事项 -->
        <div class="section-card">
          <div class="section-header">
            <h3 class="section-title">待处理</h3>
          </div>
          <div class="todo-list">
            <div class="todo-item">
              <span class="todo-dot purple"></span>
              <span class="todo-text">待审核资源</span>
              <span class="todo-count">{{ pendingTasks.reviewResources }}</span>
            </div>
            <div class="todo-item">
              <span class="todo-dot orange"></span>
              <span class="todo-text">待处理举报</span>
              <span class="todo-count">{{ pendingTasks.reports }}</span>
            </div>
            <div class="todo-item">
              <span class="todo-dot blue"></span>
              <span class="todo-text">待激活用户</span>
              <span class="todo-count">{{ pendingTasks.inactiveUsers }}</span>
            </div>
          </div>
        </div>

        <!-- 快捷入口 -->
        <div class="section-card compact">
          <div class="quick-links">
            <router-link to="/admin/users" class="quick-link">
              <span>用户管理</span>
            </router-link>
            <router-link to="/admin/courses" class="quick-link">
              <span>课程管理</span>
            </router-link>
            <router-link to="/admin/review" class="quick-link">
              <span>资源审核</span>
            </router-link>
            <router-link to="/admin/monitor" class="quick-link">
              <span>系统监控</span>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onUnmounted } from 'vue'
import { mockStatistics } from '@/mock/data'
import * as echarts from 'echarts'

const activeChartRef = ref(null)
const userDistChartRef = ref(null)
let activeChart = null
let userDistChart = null

const currentTime = computed(() => {
  const now = new Date()
  const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' }
  return now.toLocaleDateString('zh-CN', options)
})

const systemStats = reactive({
  cpu: 32,
  memory: 48,
  disk: 65
})

const pendingTasks = reactive({
  reviewResources: 8,
  reports: 3,
  inactiveUsers: 12
})

const statsData = [
  { label: '用户总数', value: '1,234', icon: '👤', gradient: 'linear-gradient(135deg, #7C4DFF, #651FFF)', trend: '+12%', trendArrow: '↑', trendClass: 'up' },
  { label: '问答总数', value: '8,567', icon: '💬', gradient: 'linear-gradient(135deg, #448AFF, #2979FF)', trend: '+8%', trendArrow: '↑', trendClass: 'up' },
  { label: '回答总数', value: '6,789', icon: '✓', gradient: 'linear-gradient(135deg, #00E676, #00C853)', trend: '+15%', trendArrow: '↑', trendClass: 'up' },
  { label: '今日提问', value: '156', icon: '⏰', gradient: 'linear-gradient(135deg, #FF8A80, #FF5252)', trend: '', trendArrow: '', trendClass: '' }
]

const initCharts = () => {
  // 活跃用户趋势
  if (activeChartRef.value) {
    activeChart = echarts.init(activeChartRef.value)
    activeChart.setOption({
      tooltip: {
        trigger: 'axis',
        backgroundColor: 'rgba(255,255,255,0.95)',
        borderColor: '#E8ECEF',
        textStyle: { color: '#2D3436' }
      },
      legend: {
        data: ['学生', '教师'],
        bottom: 0,
        textStyle: { color: '#636E72' }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '15%',
        top: '10px',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
        axisLine: { show: false },
        axisTick: { show: false },
        axisLabel: { color: '#636E72' }
      },
      yAxis: {
        type: 'value',
        show: false,
        splitLine: { show: false }
      },
      series: [
        {
          name: '学生',
          type: 'line',
          smooth: true,
          symbol: 'none',
          lineStyle: { color: '#7C4DFF', width: 3 },
          areaStyle: {
            color: {
              type: 'linear',
              x: 0, y: 0, x2: 0, y2: 1,
              colorStops: [
                { offset: 0, color: 'rgba(124, 77, 255, 0.2)' },
                { offset: 1, color: 'rgba(124, 77, 255, 0)' }
              ]
            }
          },
          data: [320, 452, 380, 520, 480, 620, 580]
        },
        {
          name: '教师',
          type: 'line',
          smooth: true,
          symbol: 'none',
          lineStyle: { color: '#34A853', width: 3 },
          areaStyle: {
            color: {
              type: 'linear',
              x: 0, y: 0, x2: 0, y2: 1,
              colorStops: [
                { offset: 0, color: 'rgba(52, 168, 83, 0.15)' },
                { offset: 1, color: 'rgba(52, 168, 83, 0)' }
              ]
            }
          },
          data: [120, 180, 150, 210, 190, 260, 240]
        }
      ]
    })
  }

  // 用户分布
  if (userDistChartRef.value) {
    userDistChart = echarts.init(userDistChartRef.value)
    userDistChart.setOption({
      tooltip: {
        trigger: 'item',
        backgroundColor: 'rgba(255,255,255,0.95)',
        borderColor: '#E8ECEF',
        textStyle: { color: '#2D3436' }
      },
      series: [{
        type: 'pie',
        radius: ['50%', '75%'],
        center: ['50%', '50%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 6,
          borderColor: '#FFFFFF',
          borderWidth: 2
        },
        label: { show: false },
        data: [
          { value: 980, name: '学生', itemStyle: { color: '#7C4DFF' } },
          { value: 234, name: '教师', itemStyle: { color: '#34A853' } },
          { value: 20, name: '管理员', itemStyle: { color: '#FF8C42' } }
        ]
      }]
    })
  }
}

const handleResize = () => {
  activeChart?.resize()
  userDistChart?.resize()
}

onMounted(() => {
  initCharts()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  activeChart?.dispose()
  userDistChart?.dispose()
})
</script>

<style lang="scss" scoped>
.admin-home {
  padding: 24px 32px;
  background: linear-gradient(180deg, #FAFBFC 0%, #F5F6F8 100%);
  min-height: 100vh;
}

// 顶部概览
.hero-section {
  margin-bottom: 24px;
  padding: 28px 32px;
  background: linear-gradient(135deg, #7C4DFF 0%, #651FFF 100%);
  border-radius: 20px;
  color: #FFFFFF;
}

.page-title {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 8px;
}

.page-subtitle {
  font-size: 14px;
  opacity: 0.9;
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
  gap: 16px;
  padding: 20px 24px;
  background: #FFFFFF;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  transition: all 0.25s ease;
  
  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  }
}

.stat-icon-wrap {
  flex-shrink: 0;
}

.stat-icon {
  width: 52px;
  height: 52px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 14px;
}

.stat-icon-text {
  font-size: 22px;
}

.stat-body {
  display: flex;
  flex-direction: column;
  gap: 2px;
  position: relative;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #2D3436;
  line-height: 1;
}

.stat-label {
  font-size: 13px;
  color: #636E72;
}

.stat-trend {
  font-size: 12px;
  margin-top: 4px;
  
  &.up {
    color: #34A853;
  }
  
  &.down {
    color: #EF5350;
  }
}

.trend-arrow {
  margin-right: 2px;
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

.status-badge {
  padding: 4px 10px;
  font-size: 11px;
  font-weight: 500;
  border-radius: 6px;
  
  &.online {
    background: #E8F5E9;
    color: #34A853;
  }
}

.time-tabs {
  display: flex;
  gap: 4px;
  background: #F5F6F8;
  padding: 3px;
  border-radius: 8px;
}

.tab {
  padding: 6px 12px;
  font-size: 12px;
  color: #636E72;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  
  &.active {
    background: #FFFFFF;
    color: #7C4DFF;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
  }
}

// 图表
.chart-container {
  height: 240px;
  
  &.small {
    height: 180px;
  }
}

// 系统指标
.system-metrics {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.metric-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.metric-label {
  width: 70px;
  font-size: 13px;
  color: #636E72;
}

.metric-bar {
  flex: 1;
  height: 8px;
  background: #F0F2F5;
  border-radius: 4px;
  overflow: hidden;
}

.metric-fill {
  height: 100%;
  border-radius: 4px;
  transition: width 0.5s ease;
}

.metric-value {
  width: 40px;
  font-size: 13px;
  font-weight: 500;
  color: #2D3436;
  text-align: right;
}

// 待办事项
.todo-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.todo-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 14px;
  background: #FAFBFC;
  border-radius: 10px;
}

.todo-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  
  &.purple { background: #7C4DFF; }
  &.orange { background: #FF8C42; }
  &.blue { background: #448AFF; }
}

.todo-text {
  flex: 1;
  font-size: 13px;
  color: #2D3436;
}

.todo-count {
  padding: 2px 8px;
  background: #F0F2F5;
  font-size: 12px;
  font-weight: 500;
  color: #636E72;
  border-radius: 6px;
}

// 快捷链接
.quick-links {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
}

.quick-link {
  padding: 12px;
  background: linear-gradient(135deg, #7C4DFF, #651FFF);
  color: #FFFFFF;
  text-align: center;
  font-size: 13px;
  font-weight: 500;
  border-radius: 10px;
  text-decoration: none;
  transition: all 0.2s ease;
  
  &:hover {
    transform: scale(1.02);
    box-shadow: 0 4px 12px rgba(124, 77, 255, 0.3);
  }
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
  .admin-home {
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
