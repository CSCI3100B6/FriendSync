import axios from 'axios'
import { Toast } from 'vant'
import router from '@/router'

// 創建axios實例
const http = axios.create({
  baseURL: '/api', // 根據實際部署環境配置
  timeout: 10000,
  withCredentials: true // 允許跨域攜帶cookie
})

// 請求攔截器
http.interceptors.request.use(
  config => {
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 響應攔截器
http.interceptors.response.use(
  response => {
    const res = response.data
    
    // 調試日誌
    if (response.config.url?.includes('/user/current')) {
      console.log('用户認證接口響應:', {
        url: response.config.url,
        status: response.status,
        data: res
      })
    }

    
    // 如果返回的是文件流或其他特殊類型，直接返回
    if (response.config.responseType === 'blob' || response.config.responseType === 'arraybuffer') {
      return response
    }

    
    // 如果直接返回數組，説明是有效數據
    if (Array.isArray(res)) {
      return res
    }
    
    // 檢查返回的是否為null或undefined
    if (res === null || res === undefined) {
      console.error('API返回空數據:', response.config.url)
      return Promise.reject('服務器返回空數據')
    }

    
    // 與後端約定的響應格式
    // if (res.code === 0) {
    //   // 特殊處理用户當前信息接口
    //   if (response.config.url?.includes('/user/current')) {
    //     // 如果data為null但code為0，可能是未登錄
    //     if (res.data === null) {
    //       localStorage.removeItem('friendsync_user_info')
    //       return Promise.reject('未登錄狀態')
    //     }
    //   }
    //   return res.data
    // } 
    // else {
    //   // 處理特定錯誤碼
    //   handleErrorCode(res.code, res.message)
    //   return Promise.reject(res.message || '請求失敗')
    // }
    return res
  },
  error => {
    // 調試日誌
    console.error('請求錯誤:', error)
    
    if (error.response) {
      // 請求已發出，但服務器響應狀態碼不在 2xx 範圍內
      const status = error.response.status
      
      if (status === 401) {
        // 未登錄狀態，清除本地存儲的用户信息
        localStorage.removeItem('friendsync_user_info')
        
        Toast.fail('登錄已過期，請重新登錄')
        // 使用路由跳轉
        setTimeout(() => {
          router.replace('/login')
        }, 1500)
      } else if (status === 403) {
        Toast.fail('沒有操作權限')
      } else {
        Toast.fail(error.response.data || '請求失敗')
      }
    } else if (error.message.includes('timeout')) {
      Toast.fail('請求超時，請稍後再試')
    } else {
      Toast.fail('網絡錯誤，請檢查您的網絡連接')
    }
    
    return Promise.reject(error)
  }
)

// 處理錯誤碼
function handleErrorCode(code: number, message: string) {
  switch (code) {
    case 401:
      // 清除本地存儲的用户信息
      localStorage.removeItem('friendsync_user_info')
      
      Toast.fail('登錄已過期，請重新登錄')
      // 使用路由跳轉
      setTimeout(() => {
        router.replace('/login')
      }, 1500)
      break
    case 403:
      Toast.fail('沒有操作權限')
      break
    default:
      Toast.fail(message || '請求失敗')
  }
}

export default http 