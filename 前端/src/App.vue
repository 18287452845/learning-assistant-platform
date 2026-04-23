<template>
  <router-view />
</template>

<script setup>
import { onMounted } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

onMounted(() => {
  // 初始化应用
  console.log('智能学习助手平台启动')
})
</script>

<style>
@import './assets/styles/global.scss';
</style>
