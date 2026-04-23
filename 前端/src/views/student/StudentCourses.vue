<template>
  <div class="student-courses">
    <!-- 页面标题和筛选 -->
    <div class="page-header">
      <div class="header-left">
        <h2>我的课程</h2>
        <el-tag type="info">{{ filteredCourses.length }} 门课程</el-tag>
      </div>
      <div class="header-right">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索课程..."
          prefix-icon="Search"
          style="width: 200px; margin-right: 15px;"
          clearable
        />
        <el-select v-model="categoryFilter" placeholder="课程分类" style="width: 150px;">
          <el-option label="全部" value="" />
          <el-option label="专业课" value="专业课" />
          <el-option label="基础课" value="基础课" />
          <el-option label="选修课" value="选修课" />
        </el-select>
      </div>
    </div>

    <!-- 课程列表 -->
    <div class="course-grid">
      <el-card 
        v-for="course in filteredCourses" 
        :key="course.id" 
        class="course-card"
        shadow="hover"
        @click="$router.push(`/student/course/${course.id}`)"
      >
        <div class="course-cover" :style="{ background: getCourseColor(course.id) }">
          <span class="course-letter">{{ course.name.charAt(0) }}</span>
        </div>
        
        <div class="course-info">
          <h3 class="course-name">{{ course.name }}</h3>
          <p class="course-teacher">
            <el-icon><User /></el-icon> {{ course.teacher }}
          </p>
          <div class="course-meta">
            <span><el-icon><UserFilled /></el-icon> {{ course.students }} 人学习</span>
            <span><el-icon><Document /></el-icon> {{ course.resourceCount }} 资源</span>
          </div>
          
          <div class="course-progress">
            <div class="progress-header">
              <span>学习进度</span>
              <span class="progress-value">{{ getRandomProgress() }}%</span>
            </div>
            <el-progress 
              :percentage="getRandomProgress()" 
              :stroke-width="8" 
              :show-text="false"
              color="#67C23A"
            />
          </div>
          
          <div class="course-tags">
            <el-tag size="small" type="info">{{ course.category }}</el-tag>
          </div>
        </div>
      </el-card>

      <!-- 添加课程卡片 -->
      <el-card class="course-card add-card" shadow="hover">
        <div class="add-content">
          <el-icon :size="48" color="#c0c4cc"><Plus /></el-icon>
          <span>添加课程</span>
        </div>
      </el-card>
    </div>

    <!-- 空状态 -->
    <el-empty v-if="!filteredCourses.length && !searchKeyword" description="暂无课程" />
    <el-empty v-if="!filteredCourses.length && searchKeyword" description="未找到相关课程" />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { mockCourses } from '@/mock/data'

const searchKeyword = ref('')
const categoryFilter = ref('')

const filteredCourses = computed(() => {
  return mockCourses.filter(course => {
    const matchKeyword = !searchKeyword.value || 
      course.name.toLowerCase().includes(searchKeyword.value.toLowerCase())
    const matchCategory = !categoryFilter.value || course.category === categoryFilter.value
    return matchKeyword && matchCategory
  })
})

const courseColors = [
  'linear-gradient(135deg, #667eea, #764ba2)',
  'linear-gradient(135deg, #f093fb, #f5576c)',
  'linear-gradient(135deg, #4facfe, #00f2fe)',
  'linear-gradient(135deg, #43e97b, #38f9d7)',
  'linear-gradient(135deg, #fa709a, #fee140)',
  'linear-gradient(135deg, #a8edea, #fed6e3)',
  'linear-gradient(135deg, #ff9a9e, #fecfef)',
  'linear-gradient(135deg, #ffecd2, #fcb69f)'
]

const getCourseColor = (id) => {
  return courseColors[(id - 1) % courseColors.length]
}

const progressCache = {}
const getRandomProgress = () => {
  const id = Math.random()
  if (!progressCache[id]) {
    progressCache[id] = Math.floor(Math.random() * 60) + 20
  }
  return progressCache[id]
}
</script>

<style lang="scss" scoped>
.student-courses {
  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    .header-left {
      display: flex;
      align-items: center;
      gap: 12px;
      
      h2 {
        font-size: 20px;
        color: #303133;
        margin: 0;
      }
    }
  }
}

.course-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.course-card {
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  border: none;
  overflow: hidden;
  
  &:hover {
    transform: translateY(-8px);
    box-shadow: 0 12px 32px rgba(0, 0, 0, 0.12) !important;
  }
  
  .course-cover {
    height: 140px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: -20px -20px 15px -20px;
    
    .course-letter {
      font-size: 48px;
      font-weight: 700;
      color: white;
      text-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
    }
  }
  
  .course-info {
    .course-name {
      font-size: 16px;
      color: #303133;
      margin-bottom: 8px;
    }
    
    .course-teacher {
      font-size: 13px;
      color: #606266;
      margin-bottom: 10px;
      display: flex;
      align-items: center;
      gap: 4px;
    }
    
    .course-meta {
      display: flex;
      gap: 15px;
      font-size: 12px;
      color: #909399;
      margin-bottom: 12px;
      
      span {
        display: flex;
        align-items: center;
        gap: 4px;
      }
    }
    
    .course-progress {
      margin-bottom: 12px;
      
      .progress-header {
        display: flex;
        justify-content: space-between;
        font-size: 12px;
        color: #606266;
        margin-bottom: 6px;
        
        .progress-value {
          color: #67C23A;
          font-weight: 600;
        }
      }
    }
    
    .course-tags {
      display: flex;
      gap: 6px;
    }
  }
  
  &.add-card {
    border: 2px dashed #dcdfe6;
    background: #fafafa;
    
    &:hover {
      border-color: #409EFF;
      background: #ecf5ff;
    }
    
    .add-content {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      height: 200px;
      
      span {
        margin-top: 10px;
        color: #909399;
        font-size: 14px;
      }
    }
  }
}
</style>
