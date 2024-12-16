# src/views/JinshiSystem.vue
<template>
  <div class="h-screen bg-gray-100 flex flex-col">
    <!-- Header -->
    <header class="bg-white shadow flex-none">
      <div class="mx-auto px-3 py-2">
        <div class="flex items-center justify-between">
          <div class="flex-1"></div>
          <div class="flex items-center justify-center flex-1">
            <span class="text-lg font-bold text-red-600">集团AI智能政研工作平台</span>
          </div>
          <div class="flex items-center space-x-2 flex-1 justify-end">
            <el-button class="p-1.5 hover:bg-gray-100 rounded-full" text>
              <QuestionMarkCircle class="w-4 h-4 text-gray-600" />
            </el-button>
            <el-button class="p-1.5 hover:bg-gray-100 rounded-full" text>
              <UserCircle class="w-4 h-4 text-gray-600" />
            </el-button>
            <el-button @click="handleLogout" class="p-1.5 hover:bg-gray-100 rounded-full" text>
              <LogOut class="w-4 h-4 text-gray-600" />
            </el-button>
          </div>
        </div>
      </div>
    </header>

    <main class="flex-1 px-3 py-4 flex flex-col overflow-hidden">
      <!-- Module Cards Grid -->
      <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-3 mb-4">
        <div v-for="module in modules" :key="module.id"
          @click="handleModuleClick(module.path)"
          class="p-3 bg-white border rounded-lg hover:shadow-md cursor-pointer transition-shadow">
          <div class="flex items-center space-x-2">
            <div class="p-1.5 bg-red-50 rounded-lg">
              <component :is="module.icon" class="w-4 h-4" />
            </div>
            <span class="text-sm text-gray-700">{{ module.name }}</span>
          </div>
          <p class="mt-1.5 text-xs text-gray-500">{{ module.desc }}</p>
        </div>
      </div>

      <!-- RAG Chat Interface -->
      <div class="bg-white rounded-lg shadow flex flex-col flex-1 overflow-hidden">
        <!-- Chat Header -->
        <div class="flex items-center space-x-2 p-3 border-b">
          <MessageCircle class="w-4 h-4 text-red-600" />
          <span class="text-sm font-medium">国能AI问答</span>
        </div>

        <!-- Chat History -->
        <div ref="chatContainerRef" class="flex-1 p-3 space-y-3 overflow-y-auto">
          <template v-if="chatHistory.length === 0">
            <div class="text-center text-sm text-gray-500 mt-3">
              您好，我是您的智能小秘书，您可以向我询问任何有关国能的事项...
            </div>
          </template>
          <template v-else>
            <div v-for="(message, index) in chatHistory" :key="index"
              :class="['flex', message.role === 'user' ? 'justify-end' : 'justify-start']">
              <div :class="['max-w-[80%] rounded-lg p-2',
                message.role === 'user' ? 'bg-red-50 text-gray-800' : 'bg-gray-100 text-gray-800']">
                <p class="text-sm whitespace-pre-wrap">{{ message.content }}</p>
              </div>
            </div>
          </template>
          <div v-if="isLoading" class="flex justify-start">
            <div class="bg-gray-100 text-gray-500 rounded-lg p-2 text-sm">
              正在思考...
            </div>
          </div>
        </div>

        <!-- Chat Input -->
        <div class="border-t p-3">
          <form @submit.prevent="handleSubmit" class="flex space-x-2">
            <input
              v-model="query"
              type="text"
              placeholder="请输入您的问题..."
              class="flex-1 p-1.5 text-sm border rounded-lg focus:outline-none focus:border-red-600"
              :disabled="isLoading"
            />
            <el-button
              type="primary"
              :disabled="isLoading"
              class="p-1.5 bg-red-600 text-white rounded-lg hover:bg-red-700 disabled:bg-gray-400 disabled:cursor-not-allowed"
            >
              <Send class="w-4 h-4" />
            </el-button>
          </form>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import {
  FileText,
  MessageCircle,
  Calendar,
  CheckSquare,
  BookOpen,
  GraduationCap,
  UserCircle,
  QuestionMarkCircle,
  LogOut,
  Send
} from 'lucide-vue-next'

const router = useRouter()
const query = ref('')
const chatHistory = ref([])
const isLoading = ref(false)
const chatContainerRef = ref(null)

const modules = [
  { id: 'docs', name: '智慧文稿写作', icon: FileText, desc: '点击进入智慧文稿写作模块...', path: '/doc-writing' },
  { id: 'decisions', name: '智慧决策信息', icon: MessageCircle, desc: '点击进入智慧决策信息模块...', path: '/decision-info' },
  { id: 'meetings', name: '智慧会议辅助', icon: Calendar, desc: '点击进入智慧会议辅助模块...', path: '/meeting-assist' },
  { id: 'tasks', name: '智慧督办分析', icon: CheckSquare, desc: '点击进入智慧督办分析模块...', path: '/task-analysis' },
  { id: 'party', name: '智慧从严治党', icon: BookOpen, desc: '点击进入智慧从严治党模块...', path: '/party-management' },
  { id: 'learning', name: '智慧学习培训', icon: GraduationCap, desc: '点击进入智慧学习培训模块...', path: '/learning-training' }
]

const handleModuleClick = (path) => {
  router.push(path)
}

const handleLogout = () => {
  localStorage.removeItem('isLoggedIn')
  router.push('/login')
}

const scrollToBottom = async () => {
  await nextTick()
  if (chatContainerRef.value) {
    chatContainerRef.value.scrollTop = chatContainerRef.value.scrollHeight
  }
}

const handleSubmit = async () => {
  if (!query.value.trim() || isLoading.value) return

  const userQuery = query.value.trim()
  query.value = ''
  isLoading.value = true

  // Add user message immediately
  chatHistory.value.push({ role: 'user', content: userQuery })
  await scrollToBottom()

  try {
    const response = await fetch(`/api/rag/chat`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ query: userQuery })
    })

    if (!response.ok) {
      throw new Error('Network response was not ok')
    }

    const reader = response.body.getReader()
    const decoder = new TextDecoder()

    let assistantResponse = ''
    // 修改 while 循环的实现方式
    let reading = true
    while (reading) {
      const { value, done } = await reader.read()
      if (done) {
        reading = false
        break
      }

      const chunk = decoder.decode(value)
      const lines = chunk.split('\n')

      for (const line of lines) {
        if (line.startsWith('data: ')) {
          try {
            const data = JSON.parse(line.slice(6))
            if (data.content) {
              assistantResponse = data.content
              const lastMessage = chatHistory.value[chatHistory.value.length - 1]
              if (lastMessage && lastMessage.role === 'assistant') {
                lastMessage.content = assistantResponse
              } else {
                chatHistory.value.push({ role: 'assistant', content: assistantResponse })
              }
              await scrollToBottom()
            }
          } catch (e) {
            continue
          }
        }
      }
    }
  } catch (error) {
    console.error('Chat error:', error)
    chatHistory.value.push({
      role: 'assistant',
      content: '抱歉，发生了错误。请稍后重试。'
    })
  } finally {
    isLoading.value = false
    await scrollToBottom()
  }
}

// Scroll to bottom when component mounts
onMounted(() => {
  scrollToBottom()
})
</script>