/* eslint-disable */
// @ts-ignore
import { createApp } from 'vue'
// @ts-ignore
import { createPinia } from 'pinia'
// 引入Vant組件庫
import { 
  Button,
  Form,
  Field,
  CellGroup,
  NavBar,
  Toast,
  Dialog,
  Icon,
  Tabbar,
  TabbarItem,
  Cell,
  Search,
  Tab,
  Tabs,
  Swipe,
  SwipeItem,
  ActionBar,
  ActionBarButton,
  Badge,
  Empty,
  NoticeBar,
  Loading,
  Popover,
  Image as VanImage,
  PullRefresh
} from 'vant'
// 引入Vant樣式
import 'vant/lib/index.css'
// 移動端適配
import './assets/mobile.css'

import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(createPinia())
app.use(router)

// 註冊Vant組件
app.use(Button)
app.use(Form)
app.use(Field)
app.use(CellGroup)
app.use(NavBar)
app.use(Toast)
app.use(Dialog)
app.use(Icon)
app.use(Tabbar)
app.use(TabbarItem)
app.use(Cell)
app.use(Search)
app.use(Tab)
app.use(Tabs)
app.use(Swipe)
app.use(SwipeItem)
app.use(ActionBar)
app.use(ActionBarButton)
app.use(Badge)
app.use(Empty)
app.use(NoticeBar)
app.use(Loading)
app.use(Popover)
app.use(VanImage)
app.use(PullRefresh)

app.mount('#app')
