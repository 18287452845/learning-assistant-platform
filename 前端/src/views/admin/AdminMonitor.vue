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
              <el-tag :type="systemStatus.tagType" size="large">
                <el-icon><CircleCheck /></el-icon> {{ systemStatus.statusText }}
              </el-tag>
              <span class="uptime">{{ systemStatus.uptime }}</span>
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
            <div class="service-status">
              <div class="service-item">
                <span class="label">数据库</span>
                <el-tag :type="systemStatus.services.database === 'UP' ? 'success' : 'danger'" size="small">
                  {{ systemStatus.services.database }}
                </el-tag>
              </div>
              <div class="service-item">
                <span class="label">Redis</span>
                <el-tag :type="systemStatus.services.redis === 'UP' ? 'success' : 'danger'" size="small">
                  {{ systemStatus.services.redis }}
                </el-tag>
              </div>
              <div class="service-item">
                <span class="label">API</span>
                <el-tag :type="systemStatus.services.api === 'UP' ? 'success' : 'danger'" size="small">
                  {{ systemStatus.services.api }}
                </el-tag>
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
              <span>暂无趋势数据</span>
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
              <span class="value error">{{ systemStatus.errorRate }}</span>
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

      <el-table :data="operationLogs" stripe v-loading="opLogLoading">
        <el-table-column prop="time" label="时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.time || row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="user" label="操作用户" width="120">
          <template #default="{ row }">
            {{ row.user || row.username || row.operatorName || '' }}
          </template>
        </el-table-column>
        <el-table-column prop="action" label="操作类型" width="120">
          <template #default="{ row }">
            {{ row.action || row.operationType || '' }}
          </template>
        </el-table-column>
        <el-table-column prop="ip" label="IP地址" width="150" />
        <el-table-column prop="detail" label="操作详情" min-width="300">
          <template #default="{ row }">
            {{ row.detail || row.description || '' }}
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="opCurrentPage"
          v-model:page-size="opPageSize"
          :total="opTotal"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @current-change="fetchOperationLogs"
          @size-change="handleOpSizeChange"
        />
      </div>
    </el-card>

    <!-- 登录日志 -->
    <el-card shadow="hover" style="margin-top: 20px;">
      <template #header>
        <span><el-icon><Key /></el-icon> 登录日志</span>
      </template>

      <el-table :data="loginLogs" stripe v-loading="loginLogLoading">
        <el-table-column prop="time" label="时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.time || row.createTime || row.loginTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户名" width="150" />
        <el-table-column prop="ip" label="IP地址" width="150" />
        <el-table-column prop="location" label="登录地点" width="150">
          <template #default="{ row }">
            {{ row.location || row.loginLocation || '' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 'success' || row.status === 1 || row.loginSuccess ? 'success' : 'danger'" size="small">
              {{ row.status === 'success' || row.status === 1 || row.loginSuccess ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="loginCurrentPage"
          v-model:page-size="loginPageSize"
          :total="loginTotal"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @current-change="fetchLoginLogs"
          @size-change="handleLoginSizeChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getSystemStatus, getOperationLogs, getLoginLogs } from '@/api/admin'
import * as echarts from 'echarts'

const onlineChartRef = ref(null)
let onlineChart = null

const systemStatus = reactive({
  cpu: 0,
  memory: 0,
  disk: 0,
  onlineUsers: 'N/A',
  avgResponse: 'N/A',
  p99: 'N/A',
  qps: 'N/A',
  errorRate: 'N/A',
  statusText: '检测中...',
  tagType: 'info',
  uptime: '',
  services: {
    database: 'N/A',
    redis: 'N/A',
    api: 'N/A'
  }
})

const operationLogs = ref([])
const loginLogs = ref([])

// Operation log pagination
const opCurrentPage = ref(1)
const opPageSize = ref(10)
const opTotal = ref(0)
const opLogLoading = ref(false)

// Login log pagination
const loginCurrentPage = ref(1)
const loginPageSize = ref(10)
const loginTotal = ref(0)
const loginLogLoading = ref(false)

const formatTime = (dateTime) => {
  if (!dateTime) return ''
  if (Array.isArray(dateTime)) {
    const [y, m, d, h = 0, min = 0, s = 0] = dateTime
    return `${y}-${String(m).padStart(2, '0')}-${String(d).padStart(2, '0')} ${String(h).padStart(2, '0')}:${String(min).padStart(2, '0')}:${String(s).padStart(2, '0')}`
  }
  return String(dateTime).replace('T', ' ').substring(0, 19)
}

const fetchSystemStatus = async () => {
  try {
    const res = await getSystemStatus()
    const data = res || {}

    if (data.status === 'UP') {
      systemStatus.statusText = '运行正常'
      systemStatus.tagType = 'success'
    } else {
      systemStatus.statusText = '异常'
      systemStatus.tagType = 'danger'
    }

    if (data.timestamp) {
      systemStatus.uptime = `更新于 ${formatTime(data.timestamp)}`
    }

    if (data.services) {
      systemStatus.services.database = data.services.database || 'N/A'
      systemStatus.services.redis = data.services.redis || 'N/A'
      systemStatus.services.api = data.services.api || 'N/A'
    }
  } catch (error) {
    systemStatus.statusText = '获取失败'
    systemStatus.tagType = 'danger'
  }
}

const fetchOperationLogs = async () => {
  opLogLoading.value = true
  try {
    const res = await getOperationLogs({ page: opCurrentPage.value, size: opPageSize.value })
    opTotal.value = res.total || 0
    operationLogs.value = res.records || []
  } catch (error) {
    ElMessage.error('获取操作日志失败')
  } finally {
    opLogLoading.value = false
  }
}

const fetchLoginLogs = async () => {
  loginLogLoading.value = true
  try {
    const res = await getLoginLogs({ page: loginCurrentPage.value, size: loginPageSize.value })
    loginTotal.value = res.total || 0
    loginLogs.value = res.records || []
  } catch (error) {
    ElMessage.error('获取登录日志失败')
  } finally {
    loginLogLoading.value = false
  }
}

const handleOpSizeChange = () => {
  opCurrentPage.value = 1
  fetchOperationLogs()
}

const handleLoginSizeChange = () => {
  loginCurrentPage.value = 1
  fetchLoginLogs()
}

const initChart = () => {
  if (!onlineChartRef.value) return

  onlineChart = echarts.init(onlineChartRef.value)
  onlineChart.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: '3%', right: '4%', bottom: '3%', top: '10%', containLabel: true },
    xAxis: {
      type: 'category',
      data: []
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
      data: []
    }]
  })
}

const exportLogs = () => {
  ElMessage.info('导出日志功能开发中...')
}

onMounted(() => {
  initChart()
  window.addEventListener('resize', () => onlineChart?.resize())
  fetchSystemStatus()
  fetchOperationLogs()
  fetchLoginLogs()
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

    .service-status {
      margin-top: 15px;
      display: flex;
      gap: 15px;
      flex-wrap: wrap;

      .service-item {
        display: flex;
        align-items: center;
        gap: 6px;

        .label {
          font-size: 13px;
          color: #606266;
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
