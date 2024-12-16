# src/views/modules/DocWriting.vue
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
            <h1 class="text-xl font-bold text-gray-900">智慧文稿写作</h1>
          </div>
          <div class="flex items-center space-x-4">
            <el-button
              type="primary"
              @click="handleNewDoc"
              :loading="loading"
              class="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700"
            >
              {{ loading ? '处理中...' : '新建文稿' }}
            </el-button>
          </div>
        </div>
      </div>
    </header>

    <!-- 主内容区域 -->
    <main class="max-w-7xl mx-auto py-6 px-4 grid grid-cols-12 gap-6">
      <!-- 左侧边栏 - 文稿类型选择 -->
      <div class="col-span-3 bg-white rounded-lg shadow">
        <div class="p-4 border-b">
          <h2 class="text-lg font-medium">文稿类型</h2>
        </div>
        <div class="p-4">
          <div class="space-y-2">
            <!-- 对内重要文稿 -->
            <div>
              <el-button
                @click="toggleCategory('internal')"
                class="w-full text-left px-4 py-2 rounded-md flex items-center justify-between"
                :class="{'bg-red-50 text-red-600': showCategoryTypes.internal}"
              >
                <span>对内重要文稿</span>
                <el-icon :class="{'transform rotate-180': showCategoryTypes.internal}">
                  <ArrowDown />
                </el-icon>
              </el-button>
              <div v-if="showCategoryTypes.internal" class="ml-4 mt-2 space-y-1">
                <el-button
                  v-for="(type, index) in documentTypes.internal"
                  :key="index"
                  @click="handleDocTypeClick(type)"
                  class="w-full text-left px-4 py-2 text-sm text-gray-600 hover:bg-gray-50 rounded-md"
                  :class="{'bg-gray-100': index === 0}"
                >
                  {{ type }}
                </el-button>
              </div>
            </div>

            <!-- 对上对外文稿 -->
            <div>
              <el-button
                @click="toggleCategory('external')"
                class="w-full text-left px-4 py-2 rounded-md flex items-center justify-between hover:bg-gray-50"
              >
                <span>对上对外文稿</span>
                <el-icon :class="{'transform rotate-180': showCategoryTypes.external}">
                  <ArrowDown />
                </el-icon>
              </el-button>
              <div v-if="showCategoryTypes.external" class="ml-4 mt-2 space-y-1">
                <el-button
                  v-for="(type, index) in documentTypes.external"
                  :key="index"
                  @click="handleDocTypeClick(type)"
                  class="w-full text-left px-4 py-2 text-sm text-gray-600 hover:bg-gray-50 rounded-md"
                >
                  {{ type }}
                </el-button>
              </div>
            </div>

            <!-- 其他文稿 -->
            <div>
              <el-button
                @click="toggleCategory('other')"
                class="w-full text-left px-4 py-2 rounded-md flex items-center justify-between hover:bg-gray-50"
              >
                <span>其他文稿</span>
                <el-icon :class="{'transform rotate-180': showCategoryTypes.other}">
                  <ArrowDown />
                </el-icon>
              </el-button>
              <div v-if="showCategoryTypes.other" class="ml-4 mt-2 space-y-1">
                <el-button
                  v-for="(type, index) in documentTypes.other"
                  :key="index"
                  @click="handleDocTypeClick(type)"
                  class="w-full text-left px-4 py-2 text-sm text-gray-600 hover:bg-gray-50 rounded-md"
                >
                  {{ type }}
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 中间内容区 - 写作区域 -->
      <div class="col-span-6 space-y-6">
        <!-- 写作工具栏 -->
        <div class="bg-white rounded-lg shadow p-4">
          <div class="flex items-center space-x-4">
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

            <el-button @click="handleSend" :loading="loading" class="p-2 hover:bg-gray-100 rounded-md">
              <Send class="w-5 h-5" />
              <span class="ml-2">发送</span>
            </el-button>
          </div>
        </div>

        <!-- 中间编辑区 -->
        <div class="bg-white rounded-lg shadow p-4">
          <el-input
            v-model="title"
            placeholder="请输入标题..."
            class="mb-4 text-xl font-medium"
          />
          <el-input
            v-model="content"
            type="textarea"
            :rows="15"
            resize="none"
            placeholder="请输入正文内容..."
            ref="textareaRef"
          />
        </div>
        <div v-if="error" class="text-red-500 text-sm">{{ error }}</div>
      </div>

      <!-- 右侧边栏 -->
      <div class="col-span-3 space-y-6">
        <!-- 智能写作 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b">
            <h2 class="text-lg font-medium">智能写作</h2>
          </div>
          <div class="p-4 space-y-4">
            <el-button
              @click="handleGenerateDoc"
              :loading="loading"
              class="w-full px-4 py-2 text-left hover:bg-gray-50 rounded-md"
            >
              自定义生成文稿
            </el-button>
            <el-button
              @click="handleSidebarClick('材料引用及评价')"
              class="w-full px-4 py-2 text-left hover:bg-gray-50 rounded-md"
            >
              材料引用及评价
            </el-button>
            <el-button
              @click="handleSidebarClick('提纲生成及修改')"
              class="w-full px-4 py-2 text-left hover:bg-gray-50 rounded-md"
            >
              提纲生成及修改
            </el-button>
            <el-button
              @click="handleSidebarClick('内容创新与优化')"
              class="w-full px-4 py-2 text-left hover:bg-gray-50 rounded-md"
            >
              内容创新与优化
            </el-button>
          </div>
        </div>

        <!-- 文稿校对 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b">
            <h2 class="text-lg font-medium">文稿校对</h2>
          </div>
          <div class="p-4 space-y-4">
            <el-button
              v-for="feature in ['智能校对', '格式规范检查', '政治性检查', '保密性检查']"
              :key="feature"
              @click="handleSidebarClick(feature)"
              class="w-full px-4 py-2 text-left hover:bg-gray-50 rounded-md"
            >
              {{ feature }}
            </el-button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue' // 移除未使用的 onMounted
import { useRouter } from 'vue-router'
import axios from 'axios'
import { Send, ArrowDown } from 'lucide-vue-next'
import { ElMessage } from 'element-plus' // 添加 ElMessage 导入

const router = useRouter()
const API_BASE_URL = `http://${window.location.hostname}:3031/api`

// 状态变量
const title = ref('')
const content = ref('')
const loading = ref(false)
const error = ref('')
const textareaRef = ref(null)
const selectedCompany = ref('')
const selectedDepartment = ref('')
const selectedPerson = ref('')
const showCategoryTypes = ref({
  internal: true,
  external: false,
  other: false
})

// 数据
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

const documentTypes = {
  internal: [
    '工作会讲话稿',
    '专题工作会讲话稿',
    '集团党组会材料',
    '集团董事会材料',
    '董事长专题会材料'
  ],
  external: [
    '向上综合类汇报',
    '向上专项汇报',
    '署名文章',
    '论坛讲话',
    '重要采访材料'
  ],
  other: [
    '董事长月度例会材料',
    '总经理专题会材料',
    '调研讲话材料',
    '中心组学习发言',
    '党课讲稿'
  ]
}

// 方法
const handleNewDoc = async () => {
  try {
    loading.value = true
    title.value = ''
    content.value = ''
    error.value = ''
    router.go(0)
  } catch (err) {
    error.value = '新建文稿失败'
  } finally {
    loading.value = false
  }
}

const toggleCategory = (category) => {
  if (category !== 'internal') {
    showCategoryTypes.value[category] = !showCategoryTypes.value[category]
  }
}

const handleDocTypeClick = (type) => {
  if (type !== '工作会讲话稿') {
    error.value = `${type}在开发中`
  } else {
    error.value = ''
  }
}

const handleGenerateDoc = async () => {
  if (!title.value.trim()) {
    error.value = '请先输入标题才能为您自动撰写稿件'
    return
  }

  try {
    loading.value = true
    content.value = ''
    error.value = ''

    const response = await fetch(`${API_BASE_URL}/generate-doc`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        doc_type: 'speech',
        requirements: title.value.trim(),
        reference_materials: []
      })
    })

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
            const data = JSON.parse(line.slice(5))
            if (data.content) {
              const formattedContent = data.content.replace(/\\n/g, '\n')
              content.value += formattedContent
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
    error.value = '生成文稿失败'
    console.error(err)
  } finally {
    loading.value = false
  }
}

const handleSidebarClick = (feature) => {
  if (feature !== '自定义生成文稿') {
    error.value = `${feature}在开发中`
  } else {
    error.value = ''
  }
}

const handleSend = async () => {
  if (!title.value.trim()) {
    error.value = "请填写标题"
    return
  }
  if (!content.value.trim()) {
    error.value = "请生成文章内容"
    return
  }
  if (!selectedCompany.value) {
    error.value = "请选择公司"
    return
  }
  if (!selectedDepartment.value) {
    error.value = "请选择部门"
    return
  }
  if (!selectedPerson.value) {
    error.value = "请选择人员"
    return
  }

  try {
    loading.value = true
    const selectedPersonObj = personnel.find(p => p.value === selectedPerson.value)

    const response = await axios.post(`${API_BASE_URL}/send-email`, {
      to_email: selectedPersonObj.value,
      subject: title.value,
      content: content.value,
      sender_name: "AI管理员",
      receiver_name: selectedPersonObj.label
    })

    if (response.data.message === "邮件发送成功") {
      error.value = ""
      ElMessage.success("邮件发送成功!")  // 使用Element Plus的消息提示
    }
  } catch (err) {
    error.value = err.response?.data?.detail || "邮件发送失败"
  } finally {
    loading.value = false
  }
}
</script>