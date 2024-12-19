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
            <h1 class="text-xl font-bold text-gray-900">智慧学习培训</h1>
          </div>
          <div class="flex items-center space-x-4">
            <el-button
              type="primary"
              class="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700"
            >
              开始学习
            </el-button>
          </div>
        </div>
      </div>
    </header>

    <main class="max-w-7xl mx-auto py-6 px-4 grid grid-cols-12 gap-6">
      <!-- 左侧导航 -->
      <div class="col-span-3 space-y-6">
        <!-- 功能导航 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b">
            <h2 class="text-lg font-medium">智能学习</h2>
          </div>
          <div class="p-4">
            <nav class="space-y-2">
              <el-button
                v-for="nav in navigation"
                :key="nav.id"
                @click="selectedTab = nav.id"
                :class="[
                  'w-full flex items-center space-x-3 px-4 py-2 rounded-md',
                  selectedTab === nav.id ? 'bg-red-50 text-red-600' : 'hover:bg-gray-50'
                ]"
              >
                <component :is="nav.icon" class="w-5 h-5" />
                <span>{{ nav.name }}</span>
              </el-button>
            </nav>
          </div>
        </div>

        <!-- 课程分类 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b">
            <h2 class="text-lg font-medium">课程分类</h2>
          </div>
          <div class="p-4 space-y-2">
            <el-button
              v-for="type in courseTypes"
              :key="type.id"
              class="w-full px-4 py-2 text-left hover:bg-gray-50 rounded-md"
            >
              <div class="font-medium">{{ type.name }}</div>
              <div class="text-sm text-gray-500">{{ type.desc }}</div>
            </el-button>
          </div>
        </div>
      </div>

      <!-- 中间内容区 -->
      <div class="col-span-6 space-y-6">
        <!-- 搜索栏 -->
        <div class="bg-white rounded-lg shadow p-4">
          <div class="flex items-center space-x-4">
            <div class="relative flex-1">
              <el-input
                v-model="searchQuery"
                placeholder="搜索课程、问答、资料..."
                class="w-full"
              >
                <template #prefix>
                  <Search class="text-gray-400" />
                </template>
              </el-input>
            </div>
            <el-button class="p-2 hover:bg-gray-100 rounded-md flex items-center space-x-2">
              <Settings class="w-5 h-5" />
              <span>高级筛选</span>
            </el-button>
          </div>
        </div>

        <!-- 课程内容展示 -->
        <div class="bg-white rounded-lg shadow">
          <div v-if="selectedTab === 'courses'" class="p-4 space-y-4">
            <!-- 课程卡片示例 -->
            <div class="border rounded-lg p-4">
              <div class="flex items-center justify-between mb-4">
                <h3 class="font-medium">课程标题</h3>
                <el-button type="primary" class="flex items-center space-x-1 text-red-600">
                  <Play class="w-4 h-4" />
                  <span>开始学习</span>
                </el-button>
              </div>
              <p class="text-gray-600 mb-4">课程简介和内容描述...</p>
              <div class="flex items-center text-sm text-gray-500">
                <Clock class="w-4 h-4 mr-1" />
                <span>课程时长: 2小时</span>
              </div>
            </div>
          </div>

          <div v-if="selectedTab === 'exam'" class="p-4">
            <h3 class="font-medium mb-4">AI智能出题</h3>
            <el-input
              type="textarea"
              v-model="examRequirements"
              :rows="8"
              placeholder="输入考试要求和范围，AI将为您生成合适的试题..."
              class="w-full"
            />
          </div>
        </div>
      </div>

      <!-- 右侧工具区 -->
      <div class="col-span-3 space-y-6">
        <!-- AI学习助手 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b">
            <h2 class="text-lg font-medium">AI学习助手</h2>
          </div>
          <div class="p-4 space-y-4">
            <el-button
              v-for="tool in learningTools"
              :key="tool.id"
              class="w-full px-4 py-2 text-left hover:bg-gray-50 rounded-md flex items-center space-x-2"
            >
              <component :is="tool.icon" class="w-5 h-5" />
              <span>{{ tool.name }}</span>
            </el-button>
          </div>
        </div>

        <!-- 导师互动 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b">
            <h2 class="text-lg font-medium">导师互动</h2>
          </div>
          <div class="p-4">
            <div class="relative">
              <el-input
                v-model="tutorQuery"
                placeholder="向AI导师提问..."
                class="w-full"
              >
                <template #prefix>
                  <MessageCircle class="text-gray-400" />
                </template>
              </el-input>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import {
  BookOpen,
  FileQuestion,
  Users,
  Lightbulb,
  Video,
  MessageCircle,
  PenTool,
  Award,
  Settings,
  Play,
  Clock,
  Search
} from 'lucide-vue-next'

const router = useRouter()

// 状态变量
const selectedTab = ref('courses')
const searchQuery = ref('')
const examRequirements = ref('')
const tutorQuery = ref('')

// 导航配置
const navigation = [
  { id: 'courses', name: 'AI智课', icon: BookOpen },
  { id: 'exam', name: 'AI考试出题', icon: FileQuestion },
  { id: 'tutor', name: 'AI导师', icon: Users },
  { id: 'guide', name: 'AI导学', icon: Lightbulb }
]

// 课程类型
const courseTypes = [
  { id: 'party', name: '党的理论', desc: '党建理论知识学习' },
  { id: 'skills', name: '技术技能', desc: '专业技术能力培训' },
  { id: 'safety', name: '安全法规', desc: '安全生产法律法规' },
  { id: 'cases', name: '警示案例', desc: '典型案例学习分析' }
]

// 学习工具
const learningTools = [
  { id: 'recommendation', name: '课程推荐', icon: Video },
  { id: 'qa', name: '智能问答', icon: MessageCircle },
  { id: 'notes', name: '笔记助手', icon: PenTool },
  { id: 'progress', name: '学习进度', icon: Award }
]
</script>