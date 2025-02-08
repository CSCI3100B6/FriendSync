import { createApp, ref } from 'vue'
import './style.css'
import App from './App.vue'
import { Button, NavBar, Icon, Tabbar, TabbarItem, Toast} from 'vant';

const app = createApp(App);
app.use(Button);
app.use(NavBar);
app.use(Icon);
app.use(Tabbar);
app.use(TabbarItem);
app.use(Toast);
app.mount('#app')


