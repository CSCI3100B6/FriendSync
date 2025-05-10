import { fileURLToPath, URL } from "node:url";

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import vueDevTools from "vite-plugin-vue-devtools";
import postcssViewport from "postcss-px-to-viewport";

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue(), vueDevTools()],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
  server: {
    proxy: {
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
        rewrite: (path) => path,
      },
    },
  },
  css: {
    postcss: {
      plugins: [
        postcssViewport({
          viewportWidth: 375, // 设计稿的视口宽度
          unitPrecision: 5, // 单位转换后保留的精度
          viewportUnit: "vw", // 希望使用的视口单位
          selectorBlackList: [".ignore", ".hairlines"], // 不转换的类名
          minPixelValue: 1, // 小于或等于1px不转换为视口单位
          mediaQuery: false, // 是否在媒体查询中转换px
          exclude: [/node_modules\/vant/i], // 排除vant组件库，使用其自带的适配
        }),
      ],
    },
  },
});
