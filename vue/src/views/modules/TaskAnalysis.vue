# src/views/modules/TaskAnalysis.vue
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
            <h1 class="text-xl font-bold text-gray-900">智慧督办分析</h1>
          </div>
          <div class="flex items-center space-x-4">
            <el-button type="primary" class="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700">
              新建督办
            </el-button>
          </div>
        </div>
      </div>
    </header>

    <!-- 主内容区 -->
    <main class="max-w-7xl mx-auto py-6 px-4 grid grid-cols-12 gap-6">
      <!-- 左侧功能导航 -->
      <div class="col-span-3 space-y-6">
        <!-- 任务来源 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b">
            <el-button
              @click="showSourceTypes = !showSourceTypes"
              class="w-full flex items-center justify-between text-lg font-medium"
            >
              <span>督办任务生成</span>
              <el-icon :class="{'transform rotate-180': showSourceTypes}">
                <ArrowDown />
              </el-icon>
            </el-button>
          </div>
          <div v-if="showSourceTypes" class="p-4 space-y-2">
            <el-button
              v-for="source in taskSources"
              :key="source.id"
              class="w-full px-4 py-2 text-left hover:bg-gray-50 rounded-md"
            >
              <div class="font-medium">{{ source.name }}</div>
              <div class="text-sm text-gray-500">{{ source.desc }}</div>
            </el-button>
          </div>
        </div>

        <!-- 其他功能导航 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4">
            <nav class="space-y-2">
              <el-button
                v-for="tab in navigationTabs"
                :key="tab.id"
                @click="selectedTab = tab.id"
                :class="[
                  'w-full flex items-center space-x-3 px-4 py-2 rounded-md',
                  selectedTab === tab.id ? 'bg-red-50 text-red-600' : 'hover:bg-gray-50'
                ]"
              >
                <component :is="tab.icon" class="w-5 h-5" />
                <span>{{ tab.name }}</span>
              </el-button>
            </nav>
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
                  placeholder="搜索督办任务..."
                  class="pl-10 pr-4 py-2"
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
                <Calendar class="w-5 h-5" />
              </el-button>
            </div>
          </div>
        </div>

        <!-- 内容编辑区 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b">
            <el-input
              v-model="taskTitle"
              placeholder="督办任务标题..."
              class="w-full text-xl font-medium focus:outline-none"
            />
          </div>
          <div class="p-4">
            <el-input
              v-model="taskContent"
              type="textarea"
              :rows="15"
              resize="none"
              placeholder="督办内容..."
            />
          </div>
        </div>
      </div>

      <!-- 右侧统计分析 -->
      <div class="col-span-3 space-y-6">
        <!-- 任务统计 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b">
            <h2 class="text-lg font-medium">任务统计</h2>
          </div>
          <div class="p-4 space-y-4">
            <div v-for="stat in taskStats" :key="stat.id" class="flex justify-between items-center">
              <span>{{ stat.name }}</span>
              <span :class="stat.color">{{ stat.value }}</span>
            </div>
          </div>
        </div>

        <!-- 智能工具 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b">
            <h2 class="text-lg font-medium">智能工具</h2>
          </div>
          <div class="p-4 space-y-4">
            <el-button
              v-for="tool in tools"
              :key="tool.id"
              class="w-full px-4 py-2 text-left hover:bg-gray-50 rounded-md flex items-center space-x-2"
            >
              <component :is="tool.icon" class="w-5 h-5" />
              <span>{{ tool.name }}</span>
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
  Clock,
  Search,
  Filter,
  PieChart,
  Calendar,
  BarChart2,
  FileText,
  ArrowDown
} from 'lucide-vue-next'

const router = useRouter()

// 状态
const selectedTab = ref('tracking')
const showSourceTypes = ref(false)
const searchQuery = ref('')
const taskTitle = ref('')
const taskContent = ref('')

// 督办任务来源
const taskSources = [
  { id: 'meeting', name: '会议纪要', desc: '从会议纪要中提取待办事项' },
  { id: 'decision', name: '"三重一大"决策', desc: '从决策管理系统中识别督办事项' },
  { id: 'manual', name: '手动创建', desc: '手动录入督办任务' }
]

// 导航标签
const navigationTabs = [
  { id: 'tracking', name: '进度跟踪', icon: Clock },
  { id: 'analysis', name: '督办分析', icon: BarChart2 },
  { id: 'report', name: '督办报告', icon: FileText }
]

// 任务统计数据
const taskStats = [
  { id: 'total', name: '总任务数', value: '128', color: 'font-medium' },
  { id: 'inProgress', name: '进行中', value: '45', color: 'text-blue-600 font-medium' },
  { id: 'completed', name: '已完成', value: '68', color: 'text-green-600 font-medium' },
  { id: 'overdue', name: '逾期', value: '15', color: 'text-red-600 font-medium' }
]

// 智能工具
const tools = [
  { id: 'analysis', name: '完成度分析', icon: PieChart },
  { id: 'progress', name: '进度评估', icon: BarChart2 },
  { id: 'report', name: '生成报告', icon: FileText }
]
</script>