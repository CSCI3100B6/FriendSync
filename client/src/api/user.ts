import http from './http'

// 用户登錄
export function login(userAccount: string, userPassword: string) {
  return http.post('/user/login', {
    userAccount,
    userPassword
  })
}

// 用户註冊
export function register(username: string, password: string, email: string) {
  return http.post('/user/register', {
    username,
    password,
    email
  })
}

// 獲取當前登錄用户信息
export function getCurrentUser() {
  return http.get('/user/current')
}

// 搜索用户
export function searchUsers(params?: { username?: string; id?: number; tagNameList?: string[] }) {
  return http.post('/user/search', params)
}

// 更新用户信息
export function updateUserInfo(data: {
  nickname?: string,
  avatar?: string,
  bio?: string
}) {
  return http.post('/user/update', data)
}

// 修改密碼
export function changePassword(oldPassword: string, newPassword: string) {
  return http.post('/user/change-password', {
    oldPassword,
    newPassword
  })
}

// 獲取所有用户（管理員）
export function getAllUsers() {
  return http.get('/user/all')
}

// 刪除用户（管理員）
export function deleteUser(id: number) {
  return http.get('/user/delete', {
    params: { id }
  })
}

// 用户退出登錄
export function logout() {
  return http.post('/user/logout')
}

export default {
  login,
  register,
  getCurrentUser,
  searchUsers,
  updateUserInfo,
  changePassword,
  getAllUsers,
  deleteUser,
  logout
} 