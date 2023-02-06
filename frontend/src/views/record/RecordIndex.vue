<template>
  <ContentField>
    <table class="table table-striped table-hover">
      <colgroup>
        <col width="5vw">
        <col width="5vw">
        <col width="10vw">
        <col width="10vw">
        <col width="5vw">
        <col width="5vw">
        <col width="10vw">
        <col width="10vw">
        <col width="25vw">
        <col width="25vw">
      </colgroup>
      <thead>
        <tr>
          <th />
          <th colspan="2">
            Player1
          </th>
          <th>天梯分</th>
          <th />
          <th colspan="2">
            Player2
          </th>
          <th>天梯分</th>
          <th>对战时间</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="record in records"
          :key="record.id"
        >
          <td />
          <td>
            <img
              class="user-avatar"
              :src="record.avatar1"
              alt="头像1"
            >
          </td>
          <td>
            <span>{{ record.username1 }}</span>
          </td>
          <td>
            <span>{{ (record.originalScore1 + record.getScore1) + '(' + (record.getScore1 >= 0 ? '+' : '') +
              record.getScore1 + ')' }}</span>
          </td>
          <td />
          <td>
            <img
              class="user-avatar"
              :src="record.avatar2"
              alt="头像2"
            >
          </td>
          <td>
            <span>{{ record.username2 }}</span>
          </td>
          <td>
            <span>{{ (record.originalScore2 + record.getScore2) + '(' + (record.getScore2 >= 0 ? '+' : '') +
              record.getScore2 + ')' }}</span>
          </td>
          <td>{{ record.createTime }}</td>
          <td>
            <button
              type="button"
              class="btn btn-danger"
              @click="play_record(record.id)"
            >
              查看录像
            </button>
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
import router from '@/router';

export default {
  components: {
    ContentField
  },

  setup() {
    const store = useStore();
    let current_page = 1;
    let records = ref([]);
    let total = 0;
    let pages = ref([]);

    const pull_page = page => {
      current_page = page;
      $.ajax({
        url: store.state.url + '/record/list/' + page,
        type: 'get',
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        success(resp) {
          records.value = resp.data.data;
          total = resp.data.total;
          update_pages();
        }
      });
    }

    const play_record = recordId => {
      for (const record of records.value) {
        if (record.id === recordId) {
          store.commit('updateIsRecord', true);
          store.commit('updateGame', {
            identity: 0,
            gameMap: record.gameMap
          });
          store.commit('updateSteps', {
            steps1: record.steps1,
            steps2: record.steps2,
          });
          store.commit('updateRecordLoser', record.loserIdentity);
          store.commit('updatePlayingStatus', 'playing');
          router.push({
            name: 'record_playing',
            params: {
              recordId,
            }
          });
          break;
        }
      }
    }

    const update_pages = () => {
      let max_pages = parseInt(Math.ceil(total / 10));
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
    }

    const click_page = page => {
      if (page === -2) page = current_page - 1;
      else if (page === -1) page = current_page + 1;
      let max_pages = parseInt(Math.ceil(total / 10));
      if (page >= 1 && page <= max_pages) {
        pull_page(page);
      }
    }

    pull_page(current_page);

    return {
      records,
      play_record,
      pages,
      click_page
    }
  }
}
</script>

<style scoped>
.table {
  text-align: center;
  vertical-align: middle;
  table-layout: fixed;
}

img.user-avatar {
  width: 5vh;
  border-radius: 50%;
}

.pagination {
  float: right;
}
</style>