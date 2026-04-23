<template>
  <div class="teacher-profile">
    <el-card shadow="hover">
      <template #header>
        <span>个人中心</span>
      </template>
      <el-result
        icon="info"
        title="功能开发中"
        sub-title="教师端个人中心页面正在开发中..."
      >
        <template #extra>
          <el-button type="primary" @click="$router.push('/teacher/home')">返回首页</el-button>
        </template>
      </el-result>
    </el-card>
  </div>
</template>

<script setup>
// 复用学生端的个人中心逻辑
</script>

<style lang="scss" scoped>
.teacher-profile {
  max-width: 800px;
  margin: 0 auto;
}
</style>
