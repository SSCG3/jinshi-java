<template>
  <div class="min-h-screen bg-blue-100 flex items-center justify-center bg-cover bg-center relative"
       :style="{ backgroundImage: 'url(/images/background.jpg)' }">
    <!-- 顶部导航 - 分为左右两部分 -->
    <div class="absolute top-0 left-0 right-0 p-6 flex justify-between items-center">
      <!-- 左侧 Logo -->
      <img src="/images/logo.png" alt="国家能源集团" class="h-12" />

      <!-- 右侧导航链接 -->
      <div class="text-white text-lg">
        <a href="#" class="hover:underline">集团外网</a>
        <span class="mx-3">|</span>
        <a href="#" class="hover:underline">集团内网</a>
      </div>
    </div>

    <div class="max-w-md w-full">
      <!-- 登录框 -->
      <div class="bg-white/90 backdrop-blur-sm p-8 rounded-lg shadow-lg">
        <h2 class="text-2xl font-bold text-center mb-8">集团统一身份认证</h2>

        <div class="mb-6">
          <h3 class="text-lg mb-4">方式一：账号登录</h3>
          <form @submit.prevent="handleLogin">
            <div class="space-y-4">
              <input
                type="text"
                v-model="username"
                placeholder="请输入账号"
                class="w-full px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              />

              <input
                type="password"
                v-model="password"
                placeholder="请输入密码"
                class="w-full px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              />

              <p v-if="error" class="text-red-500 text-sm">{{ error }}</p>

              <button
                type="submit"
                class="w-full bg-red-600 text-white py-2 rounded hover:bg-red-700 transition duration-200"
                :disabled="loading"
              >
                {{ loading ? '登录中...' : '登录' }}
              </button>
            </div>
          </form>

          <div class="mt-4 flex justify-between text-sm text-gray-600">
            <div class="space-x-4">
              <a href="#" class="hover:text-red-600">账号查询</a>
              <span>|</span>
              <a href="#" class="hover:text-red-600">首次登录</a>
              <span>|</span>
              <a href="#" class="hover:text-red-600">修改密码</a>
            </div>
            <a href="#" class="hover:text-red-600">忘记密码？</a>
          </div>
        </div>

        <div class="border-t pt-6">
          <h3 class="text-lg mb-4">方式二：扫码登录</h3>
          <div class="flex items-center space-x-4">
            <div class="flex-1">
              <p class="text-sm text-gray-600 mb-2">请使用 集团ICE APP</p>
              <p class="text-sm text-gray-600">扫码登录</p>
            </div>
            <div class="w-24 h-24 bg-gray-100 rounded-lg flex items-center justify-center">
              <img src="/images/qr.png" alt="QR Code" class="w-20 h-20" />
            </div>
          </div>
        </div>

        <div class="mt-6 text-sm text-red-600">
          温馨提示：运维人员不会以任何方式向用户索要密码！
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref('')
const password = ref('')
const error = ref('')
const loading = ref(false)

const handleLogin = async () => {
  if (!username.value || !password.value) {
    error.value = '请输入用户名和密码'
    return
  }

  loading.value = true
  error.value = ''

  // 模拟登录逻辑，与 React 版本保持一致
  if (username.value === 'e0172915' && password.value === '111111') {
    localStorage.setItem('isLoggedIn', 'true')
    router.push('/')
  } else {
    error.value = '用户名或密码错误'
  }

  loading.value = false
}
</script>

<style scoped>
/* 移除 Element Plus 默认样式，使用 Tailwind 类 */
</style>