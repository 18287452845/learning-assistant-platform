<template>
  <div class="admin-monitor">
    <el-row :gutter="20">
      <!-- 系统状态 -->
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <span><el-icon><Monitor /></el-icon> 系统状态</span>
          </template>
          <div class="system-status">
            <div class="status-header">
              <el-tag type="success" size="large">
                <el-icon><CircleCheck /></el-icon> 运行正常
              </el-tag>
              <span class="uptime">已运行 15 天 8 小时</span>
            </div>
            <div class="status-charts">
              <div class="chart-item">
                <span>CPU</span>
                <el-progress :percentage="systemStatus.cpu" :stroke-width="8" />
              </div>
              <div class="chart-item">
                <span>内存</span>
                <el-progress :percentage="systemStatus.memory" :stroke-width="8" color="#67C23A" />
              </div>
              <div class="chart-item">
                <span>磁盘</span>
                <el-progress :percentage="systemStatus.disk" :stroke-width="8" color="#E6A23C" />
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <!-- 实时在线 -->
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <span><el-icon><User /></el-icon> 在线用户</span>
          </template>
          <div class="online-info">
            <div class="online-number">{{ systemStatus.onlineUsers }}</div>
            <div class="online-trend">
              <span>较昨日</span>
              <span class="trend up">+23</span>
            </div>
          </div>
          <div class="online-chart" ref="onlineChartRef" style="height: 150px;"></div>
        </el-card>
      </el-col>
      
      <!-- 接口性能 -->
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <span><el-icon><Odometer /></el-icon> 接口性能</span>
          </template>
          <div class="performance">
            <div class="perf-item">
              <span class="label">平均响应</span>
              <span class="value">{{ systemStatus.avgResponse }}ms</span>
            </div>
            <div class="perf-item">
              <span class="label">99分位</span>
              <span class="value">{{ systemStatus.p99 }}ms</span>
            </div>
            <div class="perf-item">
              <span class="label">错误率</span>
              <span class="value error">0.12%</span>
            </div>
            <div class="perf-item">
              <span class="label">QPS</span>
              <span class="value">{{ systemStatus.qps }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 操作日志 -->
    <el-card shadow="hover" style="margin-top: 20px;">
      <template #header>
        <div class="page-header">
          <span><el-icon><Document /></el-icon> 操作日志</span>
          <el-button size="small" @click="exportLogs">
            <el-icon><Download /></el-icon> 导出
          </el-button>
        </div>
      </template>
      
      <el-table :data="operationLogs" stripe>
        <el-table-column prop="time" label="时间" width="180" />
        <el-table-column prop="user" label="操作用户" width="120" />
        <el-table-column prop="action" label="操作类型" width="120" />
        <el-table-column prop="ip" label="IP地址" width="150" />
        <el-table-column prop="detail" label="操作详情" min-width="300" />
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

    <!-- 登录日志 -->
    <el-card shadow="hover" style="margin-top: 20px;">
      <template #header>
        <span><el-icon><Key /></el-icon> 登录日志</span>
      </template>
      
      <el-table :data="loginLogs" stripe>
        <el-table-column prop="time" label="时间" width="180" />
        <el-table-column prop="username" label="用户名" width="150" />
        <el-table-column prop="ip" label="IP地址" width="150" />
        <el-table-column prop="location" label="登录地点" width="150" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 'success' ? 'success' : 'danger'" size="small">
              {{ row.status === 'success' ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { mockOperationLogs, mockLoginLogs, mockSystemStats } from '@/mock/data'
import * as echarts from 'echarts'

const onlineChartRef = ref(null)
let onlineChart = null

const systemStatus = reactive({
  cpu: 45,
  memory: 62,
  disk: 38,
  onlineUsers: 234,
  avgResponse: 125,
  p99: 380,
  qps: 156
})

const operationLogs = ref(mockOperationLogs)
const loginLogs = ref(mockLoginLogs)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(mockOperationLogs.length)

const initChart = () => {
  if (!onlineChartRef.value) return
  
  onlineChart = echarts.init(onlineChartRef.value)
  onlineChart.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: '3%', right: '4%', bottom: '3%', top: '10%', containLabel: true },
    xAxis: {
      type: 'category',
      data: mockSystemStats.onlineUsers.map(u => u.time)
    },
    yAxis: { type: 'value' },
    series: [{
      type: 'line',
      smooth: true,
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
          { offset: 1, color: 'rgba(64, 158, 255, 0.05)' }
        ])
      },
      itemStyle: { color: '#409EFF' },
      data: mockSystemStats.onlineUsers.map(u => u.count)
    }]
  })
}

const exportLogs = () => {
  ElMessage.info('导出日志功能开发中...')
}

onMounted(() => {
  initChart()
  window.addEventListener('resize', () => onlineChart?.resize())
})

onUnmounted(() => {
  onlineChart?.dispose()
})
</script>

<style lang="scss" scoped>
.admin-monitor {
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
  
  .system-status {
    .status-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      
      .uptime {
        font-size: 13px;
        color: #909399;
      }
    }
    
    .status-charts {
      .chart-item {
        margin-bottom: 15px;
        
        span {
          display: block;
          font-size: 13px;
          color: #606266;
          margin-bottom: 6px;
        }
      }
    }
  }
  
  .online-info {
    text-align: center;
    margin-bottom: 15px;
    
    .online-number {
      font-size: 36px;
      font-weight: 600;
      color: #409EFF;
    }
    
    .online-trend {
      font-size: 13px;
      color: #909399;
      
      .trend {
        font-weight: 600;
        margin-left: 5px;
        
        &.up {
          color: #67C23A;
        }
        
        &.down {
          color: #F56C6C;
        }
      }
    }
  }
  
  .performance {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 15px;
    
    .perf-item {
      text-align: center;
      
      .label {
        display: block;
        font-size: 12px;
        color: #909399;
        margin-bottom: 4px;
      }
      
      .value {
        font-size: 18px;
        font-weight: 600;
        color: #303133;
        
        &.error {
          color: #F56C6C;
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
</style>
