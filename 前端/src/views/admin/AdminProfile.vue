<template>
  <div class="admin-profile">
    <el-card shadow="hover">
      <template #header>
        <span>个人中心</span>
      </template>
      <el-result
        icon="info"
        title="功能开发中"
        sub-title="管理员端个人中心页面正在开发中..."
      >
        <template #extra>
          <el-button type="primary" @click="$router.push('/admin/home')">返回首页</el-button>
        </template>
      </el-result>
    </el-card>
  </div>
</template>

<script setup>
// 复用个人中心逻辑
</script>

<style lang="scss" scoped>
.admin-profile {
  max-width: 800px;
  margin: 0 auto;
}
</style>
