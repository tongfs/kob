<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
      <router-link
        class="navbar-brand"
        :to="{ name: 'home' }"
      >
        Snake Fight
      </router-link>
      <div
        id="navbarText"
        class="collapse navbar-collapse"
      >
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link
              :class="route_name == 'home' ? 'nav-link active' : 'nav-link'"
              :to="{ name: 'home' }"
            >
              {{ homePage }}
            </router-link>
          </li>
          <li class="nav-item">
            <router-link
              :class="route_name == 'game_index' ? 'nav-link active' : 'nav-link'"
              :to="{ name: 'game_index' }"
            >
              {{ game }}
            </router-link>
          </li>
          <li class="nav-item">
            <router-link
              :class="route_name == 'rank_index' ? 'nav-link active' : 'nav-link'"
              :to="{ name: 'rank_index' }"
            >
              {{ rankingList }}
            </router-link>
          </li>
          <li class="nav-item">
            <router-link
              :class="route_name == 'record_index' ? 'nav-link active' : 'nav-link'"
              :to="{ name: 'record_index' }"
            >
              {{ record }}
            </router-link>
          </li>
        </ul>
        <ul
          v-if="$store.state.user.is_login"
          class="navbar-nav"
        >
          <li class="nav-item dropdown">
            <a
              id="navbarDropdown"
              class="nav-link dropdown-toggle"
              href="#"
              role="button"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              {{ $store.state.user.username }}
            </a>
            <ul
              class="dropdown-menu"
              aria-labelledby="navbarDropdown"
            >
              <li>
                <router-link
                  class="dropdown-item"
                  :to="{ name: 'user_space' }"
                >
                  个人中心
                </router-link>
              </li>
              <li>
                <hr class="dropdown-divider">
              </li>
              <li>
                <a
                  class="dropdown-item"
                  href="#"
                  @click="logout"
                >退出</a>
              </li>
            </ul>
          </li>
        </ul>
        <ul
          v-else
          class="navbar-nav"
        >
          <li class="nav-item">
            <router-link
              class="nav-link"
              :to="{ name: 'user_login' }"
              role="button"
            >
              登录
            </router-link>
          </li>
          <li class="nav-item">
            <router-link
              class="nav-link"
              :to="{ name: 'user_register' }"
              role="button"
            >
              注册
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
import { useRoute } from 'vue-router';
import { computed } from 'vue';
import { useStore } from 'vuex';
import router from '@/router';
import { ref } from 'vue';

export default {
    setup() {
        const route = useRoute();
        let route_name = computed(() => route.name);

        const homePage = ref(' 首页 ');
        const game = ref(' 对战 ');
        const record = ref('对局记录');
        const rankingList = ref(' 排行榜 ')
      
        const store = useStore();
        const logout = () => {
            store.dispatch('logout');
            alert("您已退出登录")
            router.push({ name: 'home' })
        };

        return {
            route_name,
            logout,
            homePage,
            game,
            record,
            rankingList
        }
    }
}
</script>

<style scoped>

</style>