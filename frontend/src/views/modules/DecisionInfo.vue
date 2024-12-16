# src/views/modules/DecisionInfo.vue
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
            <h1 class="text-xl font-bold text-gray-900">智慧决策信息</h1>
          </div>
          <div class="flex items-center space-x-4">
            <el-button type="primary" class="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700">
              新建信息
            </el-button>
          </div>
        </div>
      </div>
    </header>

    <!-- 主内容区 -->
    <main class="max-w-7xl mx-auto py-6 px-4 grid grid-cols-12 gap-6">
      <!-- 左侧功能导航 -->
      <div class="col-span-3 space-y-6">
        <!-- 信息稿件类型 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b">
            <el-button
              @click="showDocTypes = !showDocTypes"
              class="w-full flex items-center justify-between text-lg font-medium"
            >
              <span>信息稿件撰写</span>
              <el-icon :class="{'transform rotate-180': showDocTypes}">
                <ArrowDown />
              </el-icon>
            </el-button>
          </div>
          <div v-if="showDocTypes" class="p-4 space-y-2">
            <el-button
              v-for="type in infoTypes"
              :key="type.id"
              class="w-full px-4 py-2 text-left hover:bg-gray-50 rounded-md"
            >
              <div class="font-medium">{{ type.name }}</div>
              <div class="text-sm text-gray-500">{{ type.desc }}</div>
            </el-button>
          </div>
        </div>

        <!-- 周刊类型 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b">
            <el-button
              @click="showWeeklyTypes = !showWeeklyTypes"
              class="w-full flex items-center justify-between text-lg font-medium"
            >
              <span>周刊自动套打汇编</span>
              <el-icon :class="{'transform rotate-180': showWeeklyTypes}">
                <ArrowDown />
              </el-icon>
            </el-button>
          </div>
          <div v-if="showWeeklyTypes" class="p-4 space-y-2">
            <el-button
              v-for="(type, index) in weeklyTypes"
              :key="index"
              class="w-full px-4 py-2 text-left hover:bg-gray-50 rounded-md"
            >
              {{ type }}
            </el-button>
          </div>
        </div>

        <!-- 其他功能导航 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4">
            <nav class="space-y-2">
              <el-button
                v-for="tab in tabs"
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
              <el-button-group>
                <el-button class="p-2 hover:bg-gray-100 rounded-md flex items-center space-x-2">
                  <FilePlus class="w-5 h-5" />
                  <span>新建</span>
                </el-button>
                <el-button class="p-2 hover:bg-gray-100 rounded-md flex items-center space-x-2">
                  <Save class="w-5 h-5" />
                  <span>保存</span>
                </el-button>
                <el-button class="p-2 hover:bg-gray-100 rounded-md flex items-center space-x-2">
                  <Send class="w-5 h-5" />
                  <span>发送</span>
                </el-button>
              </el-button-group>
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
              v-model="title"
              class="w-full text-xl font-medium focus:outline-none"
              placeholder="请输入标题..."
            />
          </div>
          <div class="p-4">
            <el-input
              v-model="content"
              type="textarea"
              :rows="15"
              resize="none"
              class="w-full h-96"
              placeholder="请输入正文内容..."
            />
          </div>
        </div>
      </div>

      <!-- 右侧智能助手 -->
      <div class="col-span-3 space-y-6">
        <!-- 智能问答 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b">
            <h2 class="text-lg font-medium">智能问答</h2>
          </div>
          <div class="p-4">
            <div class="relative">
              <el-input
                v-model="query"
                class="w-full pl-10 pr-4 py-2"
                placeholder="请输入您的问题..."
              >
                <template #prefix>
                  <Search class="w-5 h-5 text-gray-400" />
                </template>
              </el-input>
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
  BookOpen,
  BarChart2,
  Globe,
  Search,
  MessageCircle,
  ArrowDown,
  Send,
  Save,
  FilePlus,
  FileCheck,
  TrendingUp,
  Calendar
} from 'lucide-vue-next'

const router = useRouter()
const title = ref('')
const content = ref('')
const query = ref('')
const selectedTab = ref('topics')
const showDocTypes = ref(false)
const showWeeklyTypes = ref(false)

// 信息稿件类型
const infoTypes = [
  { id: 'dynamic', name: '动态信息', desc: '300字左右的集团内部动态' },
  { id: 'experience', name: '经验分享', desc: '1500字的经验总结' },
  { id: 'policy', name: '政策建议', desc: '2000字以内的政策建议' },
  { id: 'weekly-policy', name: '一周政策要报', desc: '本周重要政策汇总' },
  { id: 'weekly-market', name: '一周市场报告', desc: '本周市场动态分析' },
  { id: 'leadership', name: '领导决策信息', desc: '领导层决策参考' },
  { id: 'work-suggest', name: '一周工作建议', desc: '下周工作计划建议' }
]

// 周刊类型
const weeklyTypes = [
  '国家能源集团信息',
  '一周政策要报',
  '一周市场报告',
  '领导决策信息',
  '一周工作建议'
]

// 导航标签
const tabs = [
  { id: 'topics', name: '信息选题推荐', icon: BookOpen },
  { id: 'industry', name: '行业分析报告', icon: BarChart2 },
  { id: 'relations', name: '公共关系信息', icon: Globe }
]

// 智能工具
const tools = [
  { id: 'audit', name: '智能审核', icon: FileCheck },
  { id: 'analysis', name: '数据分析', icon: TrendingUp },
  { id: 'summary', name: '智能摘要', icon: MessageCircle }
]
</script>