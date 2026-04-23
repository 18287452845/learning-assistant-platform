<template>
  <div class="forbidden">
    <el-result
      icon="error"
      title="403"
      sub-title="抱歉，您没有权限访问该页面"
    >
      <template #extra>
        <el-button type="primary" @click="$router.push('/')">返回首页</el-button>
        <el-button @click="$router.back()">返回上页</el-button>
      </template>
    </el-result>
  </div>
</template>

<script setup>
</script>

<style lang="scss" scoped>
.forbidden {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 120px);
}
</style>
