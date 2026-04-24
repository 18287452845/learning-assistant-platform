<template>
  <div class="course-detail">
    <!-- 加载状态 -->
    <div v-if="loading" style="text-align: center; padding: 60px 0;">
      <el-icon class="is-loading" :size="32"><Loading /></el-icon>
      <p style="color: #909399; margin-top: 12px;">加载课程详情中...</p>
    </div>

    <template v-else>
    <!-- 课程头部信息 -->
    <div class="course-header" :style="{ background: courseColor }">
      <div class="header-content">
        <div class="course-info">
          <h1>{{ course.courseName }}</h1>
          <p class="course-code">{{ course.courseCode }}</p>
          <div class="course-meta">
            <span><el-icon><User /></el-icon> {{ course.teacherName }}</span>
            <span><el-icon><Reading /></el-icon> {{ course.studentCount || 0 }} 人在学</span>
            <span><el-icon><Document /></el-icon> {{ course.resourceCount || 0 }} 个资源</span>
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
            <p class="course-description">{{ course.description || '暂无课程简介' }}</p>
          </el-card>

          <!-- 学习进度 -->
          <el-card shadow="hover" class="progress-card">
            <template #header>
              <span><el-icon><TrendCharts /></el-icon> 学习进度</span>
            </template>
            <div class="progress-overview">
              <el-empty description="暂无学习进度数据" :image-size="100" />
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
            <el-table :data="resources" stripe>
              <el-table-column prop="title" label="资源名称" min-width="200" />
              <el-table-column prop="fileType" label="类型" width="100" align="center">
                <template #default="{ row }">
                  <el-tag size="small" :type="getResourceType(row.fileType)">
                    {{ row.fileType || '未知' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="上传时间" width="180" />
              <el-table-column label="操作" width="150" align="center">
                <template #default="{ row }">
                  <el-button type="primary" link size="small">下载</el-button>
                  <el-button type="primary" link size="small">预览</el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-empty v-if="!resources.length" description="暂无课程资源" />
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
                v-for="(point, index) in knowledgePoints"
                :key="index"
                class="knowledge-item"
              >
                <span class="knowledge-index">{{ index + 1 }}</span>
                <span class="knowledge-name">{{ point.name || point }}</span>
                <el-icon class="knowledge-status">
                  <Clock />
                </el-icon>
              </div>
            </div>
            <el-empty v-if="!knowledgePoints.length" description="暂无知识点" />
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
                  {{ (related.courseName || '').charAt(0) }}
                </div>
                <div class="related-info">
                  <h4>{{ related.courseName }}</h4>
                  <p>{{ related.teacherName }}</p>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    </template>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getCourseDetail, getCourseResources, getCourseKnowledgePoints } from '@/api/course'
import { Loading } from '@element-plus/icons-vue'

const route = useRoute()
const courseId = route.params.id

const loading = ref(false)
const course = reactive({
  courseName: '',
  courseCode: '',
  teacherName: '',
  studentCount: 0,
  resourceCount: 0,
  description: ''
})
const resources = ref([])
const knowledgePoints = ref([])
const relatedCourses = ref([])
const courseColor = 'linear-gradient(135deg, #667eea, #764ba2)'

const getResourceType = (type) => {
  const types = { PDF: 'danger', PPT: 'warning', ZIP: 'info', DOC: 'success' }
  return types[type] || 'info'
}

const continueLearning = () => {
  // 跳转学习
}

const relatedColors = ['#f093fb', '#4facfe', '#43e97b']

onMounted(async () => {
  loading.value = true
  try {
    const [courseDetail, resourceRes] = await Promise.all([
      getCourseDetail(courseId),
      getCourseResources(courseId, { page: 1, size: 20 })
    ])

    // Map course detail
    if (courseDetail) {
      course.courseName = courseDetail.courseName || ''
      course.courseCode = courseDetail.courseCode || ''
      course.teacherName = courseDetail.teacherName || ''
      course.studentCount = courseDetail.studentCount || 0
      course.resourceCount = courseDetail.resourceCount || 0
      course.description = courseDetail.description || ''
    }

    // Map resources
    resources.value = resourceRes?.records || resourceRes || []

    // Fetch knowledge points (non-blocking)
    try {
      const kpRes = await getCourseKnowledgePoints(courseId)
      knowledgePoints.value = Array.isArray(kpRes) ? kpRes : (kpRes?.records || [])
    } catch (e) {
      console.warn('Failed to load knowledge points:', e)
    }

    // Fetch related courses (reuse course list, pick 3 others)
    try {
      const { getCourseList } = await import('@/api/course')
      const listRes = await getCourseList({ page: 1, size: 20 })
      const allCourses = listRes?.records || []
      relatedCourses.value = allCourses
        .filter(c => String(c.id) !== String(courseId))
        .slice(0, 3)
        .map((c, i) => ({ ...c, color: relatedColors[i] || relatedColors[0] }))
    } catch (e) {
      console.warn('Failed to load related courses:', e)
    }
  } catch (e) {
    console.error('Failed to load course detail:', e)
  } finally {
    loading.value = false
  }
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
