# src/views/modules/MeetingAssist.vue
<template>
  <div class="min-h-screen bg-gray-100">
    <!-- 顶部导航 -->
    <header class="bg-white shadow">
      <div class="max-w-7xl mx-auto px-4 py-3">
        <div class="flex items-center justify-between">
          <div class="flex items-center space-x-4">
            <el-button @click="router.push('/')" class="px-4 py-2 text-gray-600 hover:text-gray-900">
              返回主页
            </el-button>
            <h1 class="text-xl font-bold text-gray-900">智慧会议辅助</h1>
          </div>
          <div class="flex items-center space-x-4">
            <el-button
              type="primary"
              @click="() => window.location.reload()"
              :loading="loading"
              class="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700"
            >
              {{ loading ? '处理中...' : '新建会议' }}
            </el-button>
          </div>
        </div>
      </div>
    </header>

    <!-- 主内容区域 -->
    <main class="max-w-7xl mx-auto py-6 px-4 grid grid-cols-12 gap-6">
      <!-- 左侧边栏 -->
      <div class="col-span-3 space-y-6">
        <!-- 会议流程选择 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b">
            <h2 class="text-lg font-medium">会议流程</h2>
          </div>
          <div class="p-4">
            <nav class="space-y-2">
              <el-button
                v-for="phase in phases"
                :key="phase.id"
                @click="selectedPhase = phase.id"
                :class="[
                  'w-full flex items-center space-x-3 px-4 py-2 rounded-md',
                  selectedPhase === phase.id ? 'bg-red-50 text-red-600' : 'hover:bg-gray-50'
                ]"
              >
                <component :is="phase.icon" class="w-5 h-5" />
                <span>{{ phase.name }}</span>
              </el-button>
            </nav>
          </div>
        </div>

        <!-- 会议类型 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b">
            <h2 class="text-lg font-medium">会议类型</h2>
          </div>
          <div class="p-4 space-y-2">
            <el-button
              v-for="type in meetingTypes"
              :key="type"
              class="w-full px-4 py-2 text-left hover:bg-gray-50 rounded-md"
            >
              {{ type }}
            </el-button>
          </div>
        </div>
      </div>

      <!-- 中间主内容区 -->
      <div class="col-span-6 space-y-6">
        <!-- 工具栏 -->
        <div class="bg-white rounded-lg shadow p-4">
          <div class="flex items-center justify-between">
            <div class="flex items-center space-x-4">
              <template v-if="selectedPhase === 'during'">
                <el-button
                  @click="toggleRecording"
                  :class="[
                    'p-2 rounded-md flex items-center space-x-2',
                    isRecording ? 'bg-red-100 text-red-600' : 'hover:bg-gray-100'
                  ]"
                >
                  <component :is="isRecording ? Pause : Play" class="w-5 h-5" />
                  <span>{{ isRecording ? '暂停记录' : '开始记录' }}</span>
                </el-button>
                <!-- 上传按钮组件 -->
                <div class="flex flex-col">
                  <input
                    type="file"
                    ref="fileInputRef"
                    class="hidden"
                    accept="audio/*"
                    @change="handleFileUpload"
                  />
                  <el-button
                    @click="triggerFileInput"
                    :disabled="uploadProgress > 0"
                    class="p-2 hover:bg-gray-100 rounded-md flex items-center space-x-2"
                  >
                    <Upload class="w-5 h-5" />
                    <span>上传音频</span>
                  </el-button>

                  <div v-if="uploadProgress > 0" class="relative w-full h-2 bg-gray-200 rounded mt-2">
                    <div
                      class="absolute h-full bg-blue-500 rounded transition-all duration-300"
                      :style="{ width: `${uploadProgress}%` }"
                    />
                    <div class="absolute w-full text-center text-xs -top-5">
                      {{ uploadProgress }}%
                    </div>
                  </div>
                </div>
              </template>
              <template v-else-if="selectedPhase === 'before'">
                <el-button class="p-2 hover:bg-gray-100 rounded-md flex items-center space-x-2">
                  <Edit class="w-5 h-5" />
                  <span>编辑</span>
                </el-button>
                <el-button class="p-2 hover:bg-gray-100 rounded-md flex items-center space-x-2">
                  <Save class="w-5 h-5" />
                  <span>保存</span>
                </el-button>
              </template>
              <template v-else>
                <el-select v-model="selectedCompany" placeholder="选择公司" class="w-40">
                  <el-option
                    v-for="company in companies"
                    :key="company.value"
                    :label="company.label"
                    :value="company.value"
                  />
                </el-select>

                <el-select v-model="selectedDepartment" placeholder="选择部门" class="w-40">
                  <el-option
                    v-for="dept in departments"
                    :key="dept.value"
                    :label="dept.label"
                    :value="dept.value"
                  />
                </el-select>

                <el-select v-model="selectedPerson" placeholder="选择人员" class="w-40">
                  <el-option
                    v-for="person in personnel"
                    :key="person.value"
                    :label="person.label"
                    :value="person.value"
                  />
                </el-select>

                <el-button
                  @click="handleSend"
                  :loading="loading"
                  class="p-2 hover:bg-gray-100 rounded-md flex items-center space-x-2"
                >
                  <Send class="w-5 h-5" />
                  <span>发送</span>
                </el-button>
              </template>
            </div>
          </div>
        </div>

        <!-- 主要内容区域 -->
        <div class="bg-white rounded-lg shadow">
          <template v-if="selectedPhase === 'during'">
            <div class="p-4">
              <div class="mb-4 p-4 bg-gray-50 rounded-lg">
                <h3 class="font-medium mb-2">{{ uploadedFile || '实时转写' }}</h3>
                <div
                  ref="contentRef"
                  class="h-96 overflow-y-auto"
                  style="white-space: pre-wrap"
                >
                  {{ transcription || '会议实时转写内容将显示在这里...' }}
                </div>
              </div>
            </div>
          </template>
          <template v-else-if="selectedPhase === 'after'">
            <div class="space-y-6">
              <div class="bg-white rounded-lg shadow">
                <div class="p-4 border-b">
                  <el-input
                    v-model="minutesTitle"
                    placeholder="会议纪要标题"
                    class="w-full text-xl font-medium"
                    readonly
                  />
                </div>
                <div class="p-4">
                  <el-input
                    v-model="minutes"
                    type="textarea"
                    :rows="15"
                    resize="none"
                    placeholder="会议纪要内容..."
                    class="w-full h-96"
                    readonly
                  />
                </div>
              </div>
            </div>
          </template>
          <template v-else>
            <div>
              <div class="p-4 border-b">
                <el-input
                  placeholder="会议主题..."
                  class="w-full text-xl font-medium"
                  readonly
                />
              </div>
              <div class="p-4">
                <el-input
                  type="textarea"
                  :rows="15"
                  resize="none"
                  placeholder="会议议程和材料..."
                  readonly
                />
              </div>
            </div>
          </template>
        </div>

        <div v-if="error" class="text-red-500 text-sm">{{ error }}</div>
      </div>

      <!-- 右侧信息栏 -->
      <div class="col-span-3 space-y-6">
        <!-- 会议信息 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b">
            <h2 class="text-lg font-medium">会议信息</h2>
          </div>
          <div class="p-4 space-y-4">
            <div class="flex items-center space-x-2">
              <Calendar class="w-4 h-4 text-gray-500" />
              <span>2024-12-01</span>
            </div>
            <div class="flex items-center space-x-2">
              <Clock class="w-4 h-4 text-gray-500" />
              <span>14:00-16:00</span>
            </div>
            <div class="flex items-center space-x-2">
              <Users class="w-4 h-4 text-gray-500" />
              <span>参会人数：12人</span>
            </div>
          </div>
        </div>

        <!-- 智能工具 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b">
            <h2 class="text-lg font-medium">智能工具</h2>
          </div>
          <div class="p-4 space-y-4">
            <template v-if="selectedPhase === 'during'">
              <el-button
                @click="startTranscription"
                :disabled="!uploadedFile"
                class="w-full px-4 py-2 text-left hover:bg-gray-50 rounded-md flex items-center space-x-2"
              >
                <Headphones class="w-5 h-5" />
                <span>开始转写</span>
              </el-button>
              <el-button class="w-full px-4 py-2 text-left hover:bg-gray-50 rounded-md flex items-center space-x-2">
                <MessageSquare class="w-5 h-5" />
                <span>智能问答</span>
              </el-button>
              <el-button class="w-full px-4 py-2 text-left hover:bg-gray-50 rounded-md flex items-center space-x-2">
                <Globe class="w-5 h-5" />
                <span>实时翻译</span>
              </el-button>
            </template>
            <template v-else-if="selectedPhase === 'after'">
              <el-button
                @click="generateMinutes"
                :disabled="!transcription"
                class="w-full px-4 py-2 text-left hover:bg-gray-50 rounded-md flex items-center space-x-2"
              >
                <FileText class="w-5 h-5" />
                <span>生成会议纪要</span>
              </el-button>
              <el-button class="w-full px-4 py-2 text-left hover:bg-gray-50 rounded-md flex items-center space-x-2">
                <CheckSquare class="w-5 h-5" />
                <span>分配任务</span>
              </el-button>
            </template>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import {
  FileText,
  Mic,
  MessageSquare,
  CheckSquare,
  Calendar,
  Clock,
  Users,
  Globe,
  Edit,
  Save,
  Play,
  Pause,
  Upload,
  Send,
  Headphones
} from 'lucide-vue-next'

const router = useRouter()
const fileInputRef = ref(null)
const contentRef = ref(null)

// 状态
const selectedPhase = ref('before')
const isRecording = ref(false)
const uploadedFile = ref(null)
const transcription = ref('')
const minutesTitle = ref('')
const minutes = ref('')
const uploadProgress = ref(0)
const error = ref(null)
const isTranscribing = ref(false)
const selectedCompany = ref('')
const selectedDepartment = ref('')
const selectedPerson = ref('')
const loading = ref(false)

// API基础URL
const API_BASE_URL = `http://${window.location.hostname}:3030/api`

// 数据
const phases = [
  { id: 'before', name: '会前准备', icon: Calendar },
  { id: 'during', name: '会议进行', icon: Mic },
  { id: 'after', name: '会后处理', icon: FileText }
]

const meetingTypes = [
  '董事会会议',
  '党组会',
  '董事长专题会',
  '总经理会',
  '董事长月度例会',
  '总经理专题会'
]

const companies = [
  { value: 'group', label: '集团公司' },
  { value: 'digital', label: '数智科技' },
  { value: 'energy', label: '能源公司' }
]

const departments = [
    { value: 'comprehensive', label: '综合部' },
  { value: 'production', label: '生产部' },
  { value: 'research', label: '研发部' }
]

const personnel = [
  { value: '20088561@ceic.com', label: '杨天择' },
  { value: 'e0172915@ceic.com', label: '周佺喜' },
  { value: '20049925@ceic.com', label: '宋家宝' }
]

// 方法
const triggerFileInput = () => {
  fileInputRef.value?.click()
}

const toggleRecording = () => {
  isRecording.value = !isRecording.value
}

const handleFileUpload = async (event) => {
  const file = event.target?.files?.[0]
  if (!file) return

  const formData = new FormData()
  formData.append('file', file)

  let intervalId = null

  try {
    const response = await axios.post(`${API_BASE_URL}/meeting/upload`, formData, {
      onUploadProgress: (() => {
        let startTime = null
        let uploadComplete = false
        let uploadDuration = null
        let progress = 0

        const continueProgress = () => {
          const totalDuration = ((100 / 15) - 1) * uploadDuration
          const startTimePostUpload = Date.now()

          intervalId = setInterval(() => {
            const elapsed = Date.now() - startTimePostUpload
            let newProgress = 15 + (elapsed / totalDuration) * 85
            newProgress = Math.floor(newProgress)

            if (newProgress >= 100) {
              newProgress = 100
              uploadProgress.value = newProgress
              clearInterval(intervalId)
            } else {
              uploadProgress.value = newProgress
            }
          }, 100)
        }

        return (progressEvent) => {
          if (!startTime) {
            startTime = Date.now()
          }

          if (progressEvent.total) {
            const actualProgress = (progressEvent.loaded * 100) / progressEvent.total

            if (!uploadComplete) {
              if (actualProgress < 100) {
                progress = actualProgress * 0.15
                progress = Math.floor(progress)
                uploadProgress.value = progress
              } else {
                uploadComplete = true
                uploadDuration = Date.now() - startTime
                uploadProgress.value = 15
                continueProgress()
              }
            }
          }
        }
      })()
    })

    if (response.data.filename) {
      if (intervalId) {
        clearInterval(intervalId)
      }
      uploadProgress.value = 0
      uploadedFile.value = response.data.filename
      error.value = null
    }
  } catch (err) {
    error.value = `上传失败: ${err.message}`
  }
}

const startTranscription = async () => {
  if (!uploadedFile.value) return
  transcription.value = ''
  isTranscribing.value = true
  let transcriptionText = ''
  let processingText = ''

  try {
    const response = await fetch(`${API_BASE_URL}/meeting/transcribe`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ filename: uploadedFile.value })
    })

    if (!response.body) return

    const reader = response.body.getReader()
    const decoder = new TextDecoder()
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
              if (data.content === '转写中...') {
                processingText += '后台正在转写中，请稍后...\n'
                transcription.value = processingText
              } else {
                if (transcriptionText === '') {
                  transcription.value = ''
                }
                transcriptionText += data.content
                transcription.value = transcriptionText
                await scrollToBottom()
              }
            } else if (data.error) {
              error.value = data.error
            }
          } catch (e) {
            continue
          }
        }
      }
    }

    transcription.value = transcriptionText
    isTranscribing.value = false
  } catch (err) {
    error.value = '转写失败: ' + err.message
    isTranscribing.value = false
  }
}

const generateMinutes = async () => {
  if (!transcription.value) return
  minutes.value = ''

  try {
    const response = await fetch(`${API_BASE_URL}/meeting/generate-minutes`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        transcription: transcription.value
      })
    })

    if (!response.body) return

    const reader = response.body.getReader()
    const decoder = new TextDecoder()
    let fullContent = ''
    let reading = true

    const updateContentAndScroll = async (content) => {
      const titleMatch = content.match(/\*\*会议主题\*\*：\s*(.+?)(?=\s*\*\*)/)
      if (titleMatch) {
        const title = titleMatch[1].trim()
        minutesTitle.value = title
        const contentWithoutTitle = content.replace(/\*\*会议主题\*\*：[^\n]*\n/, '').trim()
        minutes.value = contentWithoutTitle
      } else {
        minutes.value = content
      }

      await nextTick()
      const textarea = document.querySelector('.el-textarea__inner')
      if (textarea) {
        textarea.scrollTop = textarea.scrollHeight
      }
    }

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
              const formattedContent = data.content.replace(/\\n/g, '\n')
              fullContent += formattedContent
              await updateContentAndScroll(fullContent)
            } else if (data.error) {
              error.value = data.error
            }
          } catch (e) {
            continue
          }
        }
      }
    }
  } catch (err) {
    error.value = '生成会议纪要失败: ' + err.message
  }
}

const handleSend = async () => {
  if (!minutesTitle.value.trim()) {
    ElMessage.error("请填写标题");
    error.value = "请填写标题";
    return;
  }

  try {
    loading.value = true;
    const selectedPersonObj = personnel.find(p => p.value === selectedPerson.value);

    console.log('Sending request to:', `${API_BASE_URL}/send-email`);

    // 打印请求数据
    console.log('Request payload:', {
      to_email: selectedPersonObj.value,
      subject: minutesTitle.value,
      content: minutes.value,
      sender_name: "AI管理员",
      receiver_name: selectedPersonObj.label
    });

    const response = await axios.post(`${API_BASE_URL}/send-email`, {
      to_email: selectedPersonObj.value,
      subject: minutesTitle.value,
      content: minutes.value,
      sender_name: "AI管理员",
      receiver_name: selectedPersonObj.label
    });

    console.log('Full Response:', response);

    if (response.data.message === "邮件发送成功") {
      error.value = "";
      ElMessage.success("邮件发送成功!");
    } else {
      throw new Error("未收到成功响应");
    }
  } catch (err) {
    console.error('Error details:', err);
    error.value = err.response?.data?.detail || "邮件发送失败";
    ElMessage.error(error.value);
  } finally {
    loading.value = false;
  }
};

const scrollToBottom = async () => {
  await nextTick()
  if (contentRef.value) {
    contentRef.value.scrollTop = contentRef.value.scrollHeight
  }
}
</script>