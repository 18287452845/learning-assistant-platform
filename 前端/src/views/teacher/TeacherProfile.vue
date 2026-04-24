<template>
  <div class="profile-page">
    <div class="profile-layout">
      <!-- Left: Profile Card -->
      <div class="profile-card">
        <div class="avatar-section">
          <div class="avatar">{{ userStore.userInfo.realName?.[0] || 'T' }}</div>
          <h3>{{ userStore.userInfo.realName || userStore.userInfo.username }}</h3>
          <el-tag type="success" size="small">教师</el-tag>
        </div>
        <div class="info-list">
          <div class="info-item">
            <span class="label">工号</span>
            <span class="value">{{ userStore.userInfo.username }}</span>
          </div>
          <div class="info-item">
            <span class="label">邮箱</span>
            <span class="value">{{ userStore.userInfo.email || '-' }}</span>
          </div>
          <div class="info-item">
            <span class="label">手机</span>
            <span class="value">{{ userStore.userInfo.phone || '-' }}</span>
          </div>
        </div>
      </div>

      <!-- Right: Edit Forms -->
      <div class="profile-forms">
        <el-card shadow="never">
          <template #header><span>基本信息</span></template>
          <el-form :model="profileForm" label-width="80px">
            <el-form-item label="姓名">
              <el-input v-model="profileForm.realName" />
            </el-form-item>
            <el-form-item label="手机">
              <el-input v-model="profileForm.phone" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="profileForm.email" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveProfile" :loading="saving">保存修改</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card shadow="never" style="margin-top: 20px">
          <template #header><span>修改密码</span></template>
          <el-form :model="passwordForm" label-width="80px">
            <el-form-item label="旧密码">
              <el-input v-model="passwordForm.oldPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="新密码">
              <el-input v-model="passwordForm.newPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="确认密码">
              <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
            </el-form-item>
            <el-form-item>
              <el-button type="warning" @click="changePassword" :loading="changingPwd">修改密码</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getUserInfo, updateUserInfo, changePassword as changePasswordApi } from '@/api/user'

const userStore = useUserStore()
const saving = ref(false)
const changingPwd = ref(false)

const profileForm = reactive({
  realName: '',
  phone: '',
  email: ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const fetchUserInfo = async () => {
  try {
    const user = await getUserInfo()
    userStore.setUserInfo(user)
    profileForm.realName = user.realName || ''
    profileForm.phone = user.phone || ''
    profileForm.email = user.email || ''
  } catch (error) {
    ElMessage.error('获取用户信息失败')
  }
}

const saveProfile = async () => {
  saving.value = true
  try {
    await updateUserInfo({
      realName: profileForm.realName,
      phone: profileForm.phone,
      email: profileForm.email
    })
    await fetchUserInfo()
    ElMessage.success('保存成功')
  } catch (error) {
    ElMessage.error(error.message || '保存失败')
  } finally {
    saving.value = false
  }
}

const changePassword = async () => {
  changingPwd.value = true
  try {
    await changePasswordApi({
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword,
      confirmPassword: passwordForm.confirmPassword
    })
    ElMessage.success('密码修改成功')
    passwordForm.oldPassword = ''
    passwordForm.newPassword = ''
    passwordForm.confirmPassword = ''
  } catch (error) {
    ElMessage.error(error.message || '密码修改失败')
  } finally {
    changingPwd.value = false
  }
}

onMounted(() => {
  fetchUserInfo()
})
</script>

<style lang="scss" scoped>
.profile-page {
  .profile-layout {
    display: flex;
    gap: 24px;
    align-items: flex-start;
  }

  .profile-card {
    width: 280px;
    min-width: 280px;
    background: #fff;
    border-radius: 8px;
    padding: 30px 24px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);

    .avatar-section {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding-bottom: 20px;
      border-bottom: 1px solid #e8e8e8;

      .avatar {
        width: 80px;
        height: 80px;
        border-radius: 50%;
        background: linear-gradient(135deg, #52c41a, #73d13d);
        color: #fff;
        font-size: 32px;
        font-weight: 600;
        display: flex;
        align-items: center;
        justify-content: center;
      }

      h3 {
        margin: 12px 0 8px;
        font-size: 18px;
        color: #333;
      }
    }

    .info-list {
      padding-top: 16px;

      .info-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 10px 0;
        border-bottom: 1px dashed #f0f0f0;

        &:last-child {
          border-bottom: none;
        }

        .label {
          color: #999;
          font-size: 14px;
        }

        .value {
          color: #333;
          font-size: 14px;
          font-weight: 500;
        }
      }
    }
  }

  .profile-forms {
    flex: 1;

    :deep(.el-card) {
      border-radius: 8px;

      .el-card__header {
        background: linear-gradient(135deg, #f6ffed, #fcffe6);
        border-bottom: 1px solid #b7eb8f;
        font-weight: 600;
        color: #389e0d;
      }
    }

    :deep(.el-button--primary) {
      background-color: #52c41a;
      border-color: #52c41a;

      &:hover,
      &:focus {
        background-color: #73d13d;
        border-color: #73d13d;
      }
    }

    :deep(.el-button--warning) {
      background-color: #faad14;
      border-color: #faad14;

      &:hover,
      &:focus {
        background-color: #ffc53d;
        border-color: #ffc53d;
      }
    }
  }
}
</style>
