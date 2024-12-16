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
            <h1 class="text-xl font-bold text-gray-900">智慧从严治党</h1>
          </div>
          <div class="flex items-center space-x-4">
            <el-button
              type="primary"
              class="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700"
            >
              新建材料
            </el-button>
          </div>
        </div>
      </div>
    </header>

    <!-- 主内容区 -->
    <main class="max-w-7xl mx-auto py-6 px-4 grid grid-cols-12 gap-6">
      <!-- 左侧导航 -->
      <div class="col-span-3 space-y-6">
        <!-- 功能导航 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b">
            <h2 class="text-lg font-medium">功能导航</h2>
          </div>
          <div class="p-4">
            <nav class="space-y-2">
              <el-button
                v-for="(nav, index) in navigation"
                :key="index"
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

        <!-- 材料类型 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b">
            <h2 class="text-lg font-medium">材料类型</h2>
          </div>
          <div class="p-4 space-y-2">
            <el-button
              v-for="type in materialTypes"
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
        <!-- 工具栏 -->
        <div class="bg-white rounded-lg shadow p-4">
          <div class="flex items-center justify-between">
            <div class="flex items-center space-x-4">
              <div class="relative">
                <el-input
                  v-model="searchQuery"
                  placeholder="搜索材料..."
                  class="pl-10"
                >
                  <template #prefix>
                    <Search class="w-5 h-5 text-gray-400" />
                  </template>
                </el-input>
              </div>
              <el-button class="p-2 hover:bg-gray-100 rounded-md">
                <Filter class="w-5 h-5" />
              </el-button>
            </div>
            <div class="flex items-center space-x-4">
              <el-button class="p-2 hover:bg-gray-100 rounded-md">
                <Lock class="w-5 h-5" />
              </el-button>
            </div>
          </div>
        </div>

        <!-- 内容区域 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b">
            <el-input
              v-model="title"
              placeholder="材料标题..."
              class="text-xl font-medium"
            />
          </div>
          <div class="p-4">
            <el-input
              v-model="content"
              type="textarea"
              :rows="15"
              resize="none"
              placeholder="材料内容..."
            />
          </div>
        </div>
      </div>

      <!-- 右侧工具区 -->
      <div class="col-span-3 space-y-6">
        <!-- 智能工具 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b">
            <h2 class="text-lg font-medium">智能工具</h2>
          </div>
          <div class="p-4 space-y-4">
            <el-button
              v-for="tool in intelligentTools"
              :key="tool.id"
              class="w-full px-4 py-2 text-left hover:bg-gray-50 rounded-md flex items-center space-x-2"
            >
              <component :is="tool.icon" class="w-5 h-5" />
              <span>{{ tool.name }}</span>
            </el-button>
          </div>
        </div>

        <!-- 写作助手 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b">
            <h2 class="text-lg font-medium">写作助手</h2>
          </div>
          <div class="p-4 space-y-4">
            <el-button
              v-for="assistant in writingAssistants"
              :key="assistant.id"
              class="w-full px-4 py-2 text-left hover:bg-gray-50 rounded-md flex items-center space-x-2"
            >
              <component :is="assistant.icon" class="w-5 h-5" />
              <span>{{ assistant.name }}</span>
            </el-button>
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
  FileText,
  CheckSquare,
  Search,
  Book,
  Edit3,
  Lock,
  FileCheck,
  Database,
  BookOpen,
  Filter,
  Archive,
  ClipboardList
} from 'lucide-vue-next'

const router = useRouter()

// 状态变量
const selectedTab = ref('materials')
const searchQuery = ref('')
const title = ref('')
const content = ref('')

// 左侧导航配置
const navigation = [
  { id: 'materials', name: '材料管理', icon: FileText },
  { id: 'review', name: '材料审核', icon: CheckSquare },
  { id: 'ledger', name: '自动台账', icon: Database },
  { id: 'writing', name: '个性化写作', icon: Edit3 },
  { id: 'search', name: '智能调阅', icon: Search }
]

// 材料类型
const materialTypes = [
  { id: 'report', name: '汇报材料', desc: '包括基层调研、作风建设等汇报' },
  { id: 'study', name: '党建学习资料', desc: '党建理论学习、教育培训资料' },
  { id: 'summary', name: '专项报告', desc: '包括专题调研报告、总结分析' },
  { id: 'research', name: '调研材料', desc: '基层党建工作调研材料' },
  { id: 'compilation', name: '汇编材料', desc: '各类党建材料汇编整理' }
]

// 智能工具配置
const intelligentTools = [
  { id: 'check', name: '智能校对', icon: FileCheck },
  { id: 'archive', name: '自动归档', icon: Archive },
  { id: 'evaluate', name: '材料评价', icon: ClipboardList }
]

// 写作助手配置
const writingAssistants = [
  { id: 'create', name: '内容创作', icon: Edit3 },
  { id: 'reference', name: '材料参考', icon: Book },
  { id: 'polish', name: '智能润色', icon: BookOpen }
]
</script>