<template>
  <div class="pagination-wrapper">
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      :page-sizes="pageSizes"
      :layout="layout"
      :background="background"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  total: {
    type: Number,
    default: 0
  },
  page: {
    type: Number,
    default: 1
  },
  limit: {
    type: Number,
    default: 10
  },
  pageSizes: {
    type: Array,
    default: () => [10, 20, 50, 100]
  },
  layout: {
    type: String,
    default: 'total, sizes, prev, pager, next, jumper'
  },
  background: {
    type: Boolean,
    default: true
  }
})

const emit = defineEmits(['pagination', 'update:page', 'update:limit'])

const currentPage = computed({
  get: () => props.page,
  set: (val) => emit('update:page', val)
})

const pageSize = computed({
  get: () => props.limit,
  set: (val) => emit('update:limit', val)
})

const handleSizeChange = (val) => {
  emit('pagination', { page: currentPage.value, limit: val })
}

const handleCurrentChange = (val) => {
  emit('pagination', { page: val, limit: pageSize.value })
}
</script>

<style lang="scss" scoped>
.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  padding: 20px 0;
}
</style>
