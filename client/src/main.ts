import { createApp } from "vue";
import "./style.css";
import "./assets/form.css";
import App from "./App.vue";
import {
  Button,
  NavBar,
  Icon,
  Tabbar,
  TabbarItem,
  Toast,
  Form,
  Field,
  CellGroup,
} from "vant";
import router from "./router";

import { library } from "@fortawesome/fontawesome-svg-core";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { faUserFriends } from "@fortawesome/free-solid-svg-icons";

library.add(faUserFriends);

const app = createApp(App);
app.component("font-awesome-icon", FontAwesomeIcon);

app.use(Button);
app.use(NavBar);
app.use(Icon);
app.use(Tabbar);
app.use(TabbarItem);
app.use(Toast);
app.use(Form);
app.use(Field);
app.use(CellGroup);
app.use(router);
app.mount("#app");
