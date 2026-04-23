<template>
  <div class="teacher-stats">
    <el-row :gutter="20">
      <!-- 高频问题统计 -->
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span><el-icon><TrendCharts /></el-icon> 高频问题 TOP 10</span>
          </template>
          <div class="chart-container" ref="highFreqChartRef" style="height: 350px;"></div>
        </el-card>
      </el-col>
      
      <!-- 问题类型分布 -->
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span><el-icon><PieChart /></el-icon> 问题类型分布</span>
          </template>
          <div class="chart-container" ref="typeChartRef" style="height: 350px;"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px;">
      <!-- 知识点掌握度 -->
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header>
            <span><el-icon><DataAnalysis /></el-icon> 知识点掌握度分布</span>
          </template>
          <div class="chart-container" ref="knowledgeChartRef" style="height: 350px;"></div>
        </el-card>
      </el-col>
      
      <!-- 学生活跃度 -->
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <span><el-icon><UserFilled /></el-icon> 学生活跃度</span>
          </template>
          <div class="activity-list">
            <div v-for="s in activeStudents" :key="s.id" class="activity-item">
              <div class="student-info">
                <el-avatar :size="32">{{ s.name.charAt(0) }}</el-avatar>
                <div class="info">
                  <span class="name">{{ s.name }}</span>
                  <span class="class">{{ s.class }}</span>
                </div>
              </div>
              <div class="activity-count">
                <span class="count">{{ s.questions + s.answers }}</span>
                <span class="label">次互动</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { mockStatistics, mockStudentList } from '@/mock/data'
import * as echarts from 'echarts'

const highFreqChartRef = ref(null)
const typeChartRef = ref(null)
const knowledgeChartRef = ref(null)
let highFreqChart = null
let typeChart = null
let knowledgeChart = null

const activeStudents = mockStudentList.slice(0, 5)

const initCharts = () => {
  // 高频问题柱状图
  if (highFreqChartRef.value) {
    highFreqChart = echarts.init(highFreqChartRef.value)
    highFreqChart.setOption({
      tooltip: { trigger: 'axis' },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      xAxis: {
        type: 'category',
        data: mockStatistics.highFrequencyQuestions.map(q => q.keyword),
        axisLabel: { rotate: 30 }
      },
      yAxis: { type: 'value' },
      series: [{
        type: 'bar',
        data: mockStatistics.highFrequencyQuestions.map(q => q.count),
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#667eea' },
            { offset: 1, color: '#764ba2' }
          ])
        },
        barWidth: '50%'
      }]
    })
  }
  
  // 问题类型饼图
  if (typeChartRef.value) {
    typeChart = echarts.init(typeChartRef.value)
    typeChart.setOption({
      tooltip: { trigger: 'item' },
      legend: { bottom: '5%', left: 'center' },
      series: [{
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
        label: { show: false },
        emphasis: { label: { show: true, fontSize: 16, fontWeight: 'bold' } },
        data: mockStatistics.questionTypes
      }]
    })
  }
  
  // 知识点雷达图
  if (knowledgeChartRef.value) {
    knowledgeChart = echarts.init(knowledgeChartRef.value)
    knowledgeChart.setOption({
      tooltip: {},
      radar: {
        indicator: [
          { name: '递归', max: 100 },
          { name: '排序', max: 100 },
          { name: '树', max: 100 },
          { name: '图', max: 100 },
          { name: '查找', max: 100 },
          { name: '动态规划', max: 100 }
        ]
      },
      series: [{
        type: 'radar',
        data: [{
          value: [85, 72, 68, 45, 78, 52],
          name: '掌握度',
          areaStyle: { color: 'rgba(103, 194, 58, 0.3)' },
          lineStyle: { color: '#67C23A' },
          itemStyle: { color: '#67C23A' }
        }]
      }]
    })
  }
}

onMounted(() => {
  initCharts()
  window.addEventListener('resize', () => {
    highFreqChart?.resize()
    typeChart?.resize()
    knowledgeChart?.resize()
  })
})

onUnmounted(() => {
  highFreqChart?.dispose()
  typeChart?.dispose()
  knowledgeChart?.dispose()
})
</script>

<style lang="scss" scoped>
.teacher-stats {
  .activity-list {
    .activity-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 15px;
      border-bottom: 1px solid #f0f0f0;
      
      &:last-child {
        border-bottom: none;
      }
      
      .student-info {
        display: flex;
        align-items: center;
        
        .info {
          margin-left: 10px;
          display: flex;
          flex-direction: column;
          
          .name {
            font-size: 14px;
            color: #303133;
          }
          
          .class {
            font-size: 12px;
            color: #909399;
          }
        }
      }
      
      .activity-count {
        text-align: right;
        
        .count {
          display: block;
          font-size: 18px;
          font-weight: 600;
          color: #409EFF;
        }
        
        .label {
          font-size: 12px;
          color: #909399;
        }
      }
    }
  }
}
</style>
