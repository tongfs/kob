<template>
  <ContentField>
    <table class="table table-striped table-hover">
      <thead>
        <tr>
          <th>排名</th>
          <th>玩家</th>
          <th>天梯分</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="user in users"
          :key="user.id"
        >
          <td>
            <span>{{ ++ranking }}</span>
          </td>
          <td>
            <img
              class="user-avatar"
              :src="user.avatar"
              alt="头像"
            >
            &nbsp;
            <span>{{ user.username }}</span>
          </td>
          <td>
            <span>{{ user.score }}</span>
          </td>
        </tr>
      </tbody>
    </table>
    <nav aria-label="...">
      <ul class="pagination">
        <li
          class="page-item"
          @click="click_page(-2)"
        >
          <a
            class="page-link"
            href="#"
          >上一页</a>
        </li>
        <li
          v-for="page in pages"
          :key="page.number"
          :class="'page-item ' + page.is_active"
          @click="click_page(page.number)"
        >
          <a
            class="page-link"
            href="#"
          >{{ page.number }}</a>
        </li>
        <li
          class="page-item"
          @click="click_page(-1)"
        >
          <a
            class="page-link"
            href="#"
          >下一页</a>
        </li>
      </ul>
    </nav>
  </ContentField>
</template>

<script>
import ContentField from '@/components/ContentField.vue'
import { useStore } from 'vuex';
import { ref } from 'vue';
import $ from 'jquery';

export default {
  components: {
    ContentField
  },

  setup() {
    const store = useStore();
    let current_page = 1;
    let users = ref([]);
    let total = 0;
    let pages = ref([]);
    const page_num = 10;
    let ranking = 0;

    const pull_page = page => {
      current_page = page;
      $.ajax({
        url: store.state.url + '/user/rank/' + page,
        type: 'get',
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        success(resp) {
          users.value = resp.data.data;
          total = resp.data.total;
          update_pages();
        }
      });
    }

    const update_pages = () => {
      let max_pages = parseInt(Math.ceil(total / page_num));
      let new_pages = [];
      for (let i = current_page - 2; i <= current_page + 2; i++) {
        if (i >= 1 && i <= max_pages) {
          new_pages.push({
            number: i,
            is_active: i === current_page ? 'active' : '',
          });
        }
      }
      pages.value = new_pages;
      ranking = current_page * 10;
    }

    const click_page = page => {
      if (page === -2) page = current_page - 1;
      else if (page === -1) page = current_page + 1;
      let max_pages = parseInt(Math.ceil(total / page_num));
      if (page >= 1 && page <= max_pages) {
        pull_page(page);
      }
    }

    pull_page(current_page);

    return {
      users,
      pages,
      click_page,
      ranking
    }
  }
}
</script>

<style scoped>
.table {
  text-align: center;
}

img.user-avatar {
  width: 5vh;
  border-radius: 50%;
}

.pagination {
  float: right;
}
</style>