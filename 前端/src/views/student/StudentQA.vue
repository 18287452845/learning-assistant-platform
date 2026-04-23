<template>
  <div class="student-qa">
    <!-- 功能介绍卡片 -->
    <el-card class="intro-card" shadow="never">
      <div class="intro-content">
        <div class="intro-item">
          <div class="intro-icon" style="background: linear-gradient(135deg, #667eea, #764ba2);">
            <el-icon :size="32" color="white"><ChatLineSquare /></el-icon>
          </div>
          <div class="intro-text">
            <h4>全网问答</h4>
            <p>基于通用AI模型，提供标准化的知识解答，覆盖各学科领域</p>
          </div>
        </div>
        
        <div class="intro-arrow">
          <el-icon :size="32" color="#67C23A"><Right /></el-icon>
        </div>
        
        <div class="intro-item">
          <div class="intro-icon" style="background: linear-gradient(135deg, #43e97b, #38f9d7);">
            <el-icon :size="32" color="white"><UserFilled /></el-icon>
          </div>
          <div class="intro-text">
            <h4>个人问答</h4>
            <p>结合您的学习记录和薄弱点，提供个性化的精准答案</p>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 问答对比区域 -->
    <div class="qa-comparison">
      <!-- 左侧：全网问答 -->
      <el-card class="qa-panel general-qa" shadow="hover">
        <template #header>
          <div class="panel-header">
            <div class="header-left">
              <el-icon :size="20"><ChatLineSquare /></el-icon>
              <span>全网问答</span>
            </div>
            <el-tag type="info" size="small">通用AI模型</el-tag>
          </div>
        </template>
        
        <div class="chat-container" ref="generalChatContainer">
          <div v-if="!generalMessages.length" class="empty-state">
            <div class="empty-illustration">
              <el-icon :size="80" color="#dcdfe6"><ChatDotRound /></el-icon>
            </div>
            <p class="empty-title">开始提问吧</p>
            <p class="empty-tip">输入您的问题，AI将为您提供专业的解答</p>
          </div>
          
          <div v-else class="message-list">
            <div 
              v-for="(msg, index) in generalMessages" 
              :key="index"
              class="message-item"
              :class="msg.role"
            >
              <div class="avatar">
                <el-avatar :size="40" :style="{ background: msg.role === 'user' ? '#409EFF' : '#909399' }">
                  {{ msg.role === 'user' ? userStore.userInfo.name?.charAt(0) : 'AI' }}
                </el-avatar>
              </div>
              
              <div class="message-content">
                <div class="message-bubble" :class="{ loading: msg.isLoading }">
                  <div v-if="msg.isLoading" class="loading-dots">
                    <span></span><span></span><span></span>
                  </div>
                  <div v-else v-html="formatContent(msg.content)"></div>
                </div>
                
                <div v-if="msg.sources && msg.sources.length" class="message-meta">
                  <span class="sources-label"><el-icon><Link /></el-icon> 参考来源：</span>
                  <div class="sources-tags">
                    <el-tag 
                      v-for="source in msg.sources" 
                      :key="source" 
                      size="small" 
                      effect="plain"
                      style="margin-right: 6px; margin-bottom: 4px;"
                    >
                      {{ source }}
                    </el-tag>
                  </div>
                </div>
                
                <div v-if="msg.confidence" class="confidence">
                  <span>置信度：</span>
                  <el-rate v-model="msg.confidence" disabled show-score text-color="#ff9900" />
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <div class="input-area">
          <div class="input-wrapper">
            <el-input
              v-model="generalInput"
              type="textarea"
              :rows="2"
              :autosize="{ minRows: 2, maxRows: 5 }"
              placeholder="请输入您的问题... (Ctrl+Enter 发送)"
              resize="none"
              @keydown.enter.ctrl="sendGeneralMessage"
            />
          </div>
          <div class="input-actions">
            <span class="input-tip">{{ generalInput.length }}/500</span>
            <el-button 
              type="primary" 
              :loading="generalLoading"
              :disabled="!generalInput.trim() || generalInput.length > 500"
              @click="sendGeneralMessage"
            >
              <el-icon v-if="!generalLoading"><Promotion /></el-icon>
              发送
            </el-button>
          </div>
        </div>
      </el-card>

      <!-- 右侧：个人问答 -->
      <el-card class="qa-panel personal-qa" shadow="hover">
        <template #header>
          <div class="panel-header">
            <div class="header-left">
              <el-icon :size="20"><UserFilled /></el-icon>
              <span>个人问答</span>
            </div>
            <el-tag type="success" size="small">个性化推荐</el-tag>
          </div>
        </template>
        
        <div class="chat-container" ref="personalChatContainer">
          <div v-if="!personalMessages.length" class="empty-state">
            <div class="empty-illustration">
              <el-icon :size="80" color="#67C23A"><MagicStick /></el-icon>
            </div>
            <p class="empty-title">智能个性化问答</p>
            <p class="empty-tip">结合您的学习记录，提供定制化解答</p>
          </div>
          
          <div v-else class="message-list">
            <div 
              v-for="(msg, index) in personalMessages" 
              :key="index"
              class="message-item"
              :class="msg.role"
            >
              <div class="avatar">
                <el-avatar :size="40" :style="{ background: msg.role === 'user' ? '#67C23A' : '#909399' }">
                  {{ msg.role === 'user' ? userStore.userInfo.name?.charAt(0) : 'AI+' }}
                </el-avatar>
              </div>
              
              <div class="message-content">
                <div class="message-bubble" :class="{ loading: msg.isLoading }">
                  <div v-if="msg.isLoading" class="loading-dots">
                    <span></span><span></span><span></span>
                  </div>
                  <div v-else v-html="formatContent(msg.content)"></div>
                </div>
                
                <!-- 薄弱点提示 -->
                <div v-if="msg.weakPoints && msg.weakPoints.length" class="weak-points-card">
                  <div class="card-title">
                    <el-icon color="#E6A23C"><Warning /></el-icon>
                    您的薄弱点识别
                  </div>
                  <div class="points-list">
                    <el-tag 
                      v-for="wp in msg.weakPoints" 
                      :key="wp" 
                      type="warning" 
                      size="small" 
                      effect="dark"
                      style="margin: 4px;"
                    >
                      {{ wp }}
                    </el-tag>
                  </div>
                </div>
                
                <!-- 推荐学习资源 -->
                <div v-if="msg.recommendations && msg.recommendations.length" class="recommendations-card">
                  <div class="card-title">
                    <el-icon color="#409EFF"><Link /></el-icon>
                    推荐学习资源
                  </div>
                  <div class="recommendations-list">
                    <el-link 
                      v-for="rec in msg.recommendations" 
                      :key="rec" 
                      type="primary" 
                      :underline="false"
                      class="recommendation-item"
                    >
                      <el-icon><Document /></el-icon> {{ rec }}
                    </el-link>
                  </div>
                </div>
                
                <div v-if="msg.sources && msg.sources.length" class="message-meta">
                  <span class="sources-label"><el-icon><Link /></el-icon> 参考来源：</span>
                  <div class="sources-tags">
                    <el-tag 
                      v-for="source in msg.sources" 
                      :key="source" 
                      size="small" 
                      effect="plain"
                      style="margin-right: 6px; margin-bottom: 4px;"
                    >
                      {{ source }}
                    </el-tag>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <div class="input-area">
          <div class="input-wrapper">
            <el-input
              v-model="personalInput"
              type="textarea"
              :rows="2"
              :autosize="{ minRows: 2, maxRows: 5 }"
              placeholder="请输入您的问题... (Ctrl+Enter 发送)"
              resize="none"
              @keydown.enter.ctrl="sendPersonalMessage"
            />
          </div>
          <div class="input-actions">
            <span class="input-tip">{{ personalInput.length }}/500</span>
            <el-button 
              type="success" 
              :loading="personalLoading"
              :disabled="!personalInput.trim() || personalInput.length > 500"
              @click="sendPersonalMessage"
            >
              <el-icon v-if="!personalLoading"><Promotion /></el-icon>
              发送
            </el-button>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 差异对比说明 -->
    <el-card class="difference-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span><el-icon><DataAnalysis /></el-icon> 全网问答 vs 个人问答</span>
        </div>
      </template>
      <el-table :data="differenceData" stripe>
        <el-table-column prop="aspect" label="对比维度" width="150" align="center" />
        <el-table-column label="全网问答" align="center">
          <template #default="{ row }">
            <span v-html="row.general" class="cell-content"></span>
          </template>
        </el-table-column>
        <el-table-column label="个人问答" align="center">
          <template #default="{ row }">
            <span v-html="row.personal" class="cell-content"></span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, nextTick, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { mockAIAnswers } from '@/mock/data'

const userStore = useUserStore()

const generalInput = ref('')
const personalInput = ref('')
const generalLoading = ref(false)
const personalLoading = ref(false)
const generalMessages = reactive([])
const personalMessages = reactive([])

const generalChatContainer = ref(null)
const personalChatContainer = ref(null)

const differenceData = [
  { 
    aspect: '知识来源', 
    general: '互联网公开知识库<br/><span class="tag info">通用</span>', 
    personal: '课程资源 + 个人学习记录<br/><span class="tag success">个性化</span>' 
  },
  { 
    aspect: '回答特点', 
    general: '标准化、通用性答案<br/><span class="tag info">标准化</span>', 
    personal: '结合学习进度定制<br/><span class="tag success">精准化</span>' 
  },
  { 
    aspect: '薄弱点识别', 
    general: '不支持<br/><span class="tag danger">✗</span>', 
    personal: '自动识别薄弱知识点<br/><span class="tag success">✓</span>' 
  },
  { 
    aspect: '推荐资源', 
    general: '通用学习资料<br/><span class="tag info">通用</span>', 
    personal: '量身推荐练习资源<br/><span class="tag success">定制</span>' 
  }
]

const scrollToBottom = (container) => {
  if (container) {
    nextTick(() => {
      container.scrollTop = container.scrollHeight
    })
  }
}

const formatContent = (content) => {
  if (!content) return ''
  return content
    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    .replace(/\*(.*?)\*/g, '<em>$1</em>')
    .replace(/^## (.+)$/gm, '<h3>$1</h3>')
    .replace(/^### (.+)$/gm, '<h4>$1</h4>')
    .replace(/^- (.+)$/gm, '<li>$1</li>')
    .replace(/(<li>.*<\/li>)/s, '<ul>$1</ul>')
    .replace(/\n\n/g, '</p><p>')
    .replace(/\n/g, '<br>')
    .replace(/^/, '<p>')
    .replace(/$/, '</p>')
}

const sendGeneralMessage = async () => {
  if (!generalInput.value.trim()) {
    ElMessage.warning('请输入问题')
    return
  }
  
  if (generalInput.value.length > 500) {
    ElMessage.warning('问题长度不能超过500字')
    return
  }
  
  const question = generalInput.value
  generalMessages.push({
    role: 'user',
    content: question,
    time: new Date().toLocaleTimeString()
  })
  generalMessages.push({
    role: 'assistant',
    content: '',
    isLoading: true
  })
  generalInput.value = ''
  scrollToBottom(generalChatContainer.value)
  
  generalLoading.value = true
  
  // 模拟AI响应
  await new Promise(resolve => setTimeout(resolve, 1500 + Math.random() * 1000))
  
  const lastMsg = generalMessages[generalMessages.length - 1]
  lastMsg.isLoading = false
  lastMsg.content = mockAIAnswers.general.answer
  lastMsg.sources = mockAIAnswers.general.sources
  lastMsg.confidence = mockAIAnswers.general.confidence
  
  generalLoading.value = false
  scrollToBottom(generalChatContainer.value)
}

const sendPersonalMessage = async () => {
  if (!personalInput.value.trim()) {
    ElMessage.warning('请输入问题')
    return
  }
  
  if (personalInput.value.length > 500) {
    ElMessage.warning('问题长度不能超过500字')
    return
  }
  
  const question = personalInput.value
  personalMessages.push({
    role: 'user',
    content: question,
    time: new Date().toLocaleTimeString()
  })
  personalMessages.push({
    role: 'assistant',
    content: '',
    isLoading: true
  })
  personalInput.value = ''
  scrollToBottom(personalChatContainer.value)
  
  personalLoading.value = true
  
  // 模拟AI个性化响应
  await new Promise(resolve => setTimeout(resolve, 2000 + Math.random() * 1000))
  
  const lastMsg = personalMessages[personalMessages.length - 1]
  lastMsg.isLoading = false
  lastMsg.content = mockAIAnswers.personalized.answer
  lastMsg.sources = mockAIAnswers.personalized.sources
  lastMsg.weakPoints = mockAIAnswers.personalized.weakPoints
  lastMsg.recommendations = mockAIAnswers.personalized.recommendations
  
  personalLoading.value = false
  scrollToBottom(personalChatContainer.value)
}

// 监听消息变化，自动滚动
watch(() => generalMessages.length, () => scrollToBottom(generalChatContainer.value))
watch(() => personalMessages.length, () => scrollToBottom(personalChatContainer.value))
</script>

<style lang="scss" scoped>
.student-qa {
  max-width: 1600px;
  margin: 0 auto;
}

.intro-card {
  margin-bottom: 20px;
  background: linear-gradient(135deg, #f8f9ff 0%, #f0fff4 100%);
  border: none;
  
  .intro-content {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 40px;
    
    .intro-item {
      display: flex;
      align-items: center;
      gap: 16px;
      
      .intro-icon {
        width: 60px;
        height: 60px;
        border-radius: 16px;
        display: flex;
        align-items: center;
        justify-content: center;
      }
      
      .intro-text {
        h4 {
          font-size: 18px;
          color: #303133;
          margin-bottom: 4px;
        }
        
        p {
          font-size: 13px;
          color: #909399;
        }
      }
    }
    
    .intro-arrow {
      color: #67C23A;
    }
  }
}

.qa-comparison {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.qa-panel {
  height: calc(100vh - 380px);
  min-height: 500px;
  display: flex;
  flex-direction: column;
  
  .panel-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .header-left {
      display: flex;
      align-items: center;
      gap: 8px;
      font-weight: 600;
      color: #303133;
    }
  }
}

.chat-container {
  flex: 1;
  overflow-y: auto;
  padding: 15px;
  background: #f9fafb;
  border-radius: 8px;
  margin-bottom: 15px;
  
  .empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
    color: #909399;
    
    .empty-title {
      font-size: 18px;
      margin-top: 15px;
      margin-bottom: 6px;
      color: #606266;
    }
    
    .empty-tip {
      font-size: 13px;
    }
  }
  
  .message-list {
    .message-item {
      display: flex;
      margin-bottom: 20px;
      
      &.user {
        flex-direction: row-reverse;
        
        .message-content {
          align-items: flex-end;
        }
        
        .message-bubble {
          background: #409EFF;
          color: white;
          border-radius: 18px 18px 4px 18px;
        }
      }
      
      &.assistant {
        .message-bubble {
          background: white;
          color: #303133;
          border-radius: 18px 18px 18px 4px;
        }
      }
      
      .message-content {
        display: flex;
        flex-direction: column;
        max-width: 85%;
        margin-left: 12px;
        
        .message-bubble {
          padding: 12px 16px;
          line-height: 1.6;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
          
          &.loading {
            display: flex;
            align-items: center;
            min-height: 50px;
          }
        }
        
        .loading-dots {
          display: flex;
          gap: 4px;
          
          span {
            width: 8px;
            height: 8px;
            background: #909399;
            border-radius: 50%;
            animation: bounce 1.4s infinite ease-in-out both;
            
            &:nth-child(1) { animation-delay: -0.32s; }
            &:nth-child(2) { animation-delay: -0.16s; }
          }
        }
        
        @keyframes bounce {
          0%, 80%, 100% { transform: scale(0); }
          40% { transform: scale(1); }
        }
        
        .message-meta {
          margin-top: 8px;
          font-size: 12px;
          color: #909399;
          
          .sources-label {
            display: flex;
            align-items: center;
            gap: 4px;
          }
          
          .sources-tags {
            margin-top: 4px;
          }
        }
        
        .confidence {
          margin-top: 8px;
          font-size: 12px;
          color: #909399;
          display: flex;
          align-items: center;
          gap: 8px;
        }
        
        .weak-points-card,
        .recommendations-card {
          margin-top: 12px;
          padding: 12px;
          background: #fdf6ec;
          border-radius: 8px;
          
          .card-title {
            display: flex;
            align-items: center;
            gap: 6px;
            font-size: 13px;
            color: #E6A23C;
            margin-bottom: 8px;
          }
          
          .points-list {
            display: flex;
            flex-wrap: wrap;
          }
          
          .recommendations-list {
            display: flex;
            flex-direction: column;
            gap: 4px;
            
            .recommendation-item {
              font-size: 13px;
              display: flex;
              align-items: center;
              gap: 6px;
            }
          }
        }
        
        .recommendations-card {
          background: #ecf5ff;
          
          .card-title {
            color: #409EFF;
          }
        }
      }
    }
  }
}

.input-area {
  .input-wrapper {
    margin-bottom: 10px;
  }
  
  .input-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .input-tip {
      font-size: 12px;
      color: #909399;
    }
  }
}

.difference-card {
  .card-header {
    font-weight: 600;
    display: flex;
    align-items: center;
    gap: 8px;
  }
  
  .cell-content {
    line-height: 1.8;
    
    .tag {
      display: inline-block;
      padding: 2px 8px;
      border-radius: 4px;
      font-size: 12px;
      margin-top: 4px;
      
      &.info {
        background: #ecf5ff;
        color: #409EFF;
      }
      
      &.success {
        background: #f0f9ff;
        color: #67C23A;
      }
      
      &.danger {
        background: #fef0f0;
        color: #F56C6C;
      }
    }
  }
}

@media (max-width: 1200px) {
  .qa-comparison {
    grid-template-columns: 1fr;
  }
  
  .intro-content {
    flex-direction: column;
    gap: 20px !important;
    
    .intro-arrow {
      transform: rotate(90deg);
    }
  }
}
</style>
