const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 3021
  },
  chainWebpack: config => {
    config.plugin('html').tap(args => {
      args[0].title = '金石系统演示'
      return args
    })
  }
})
