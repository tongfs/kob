<template>
  <div class="container">
    <div class="row">
      <div class="col-3">
        <div class="card">
          <div class="card-body">
            <img
              :src="$store.state.user.avatar"
              alt="头像"
              style="width: 100%;"
            >
          </div>
        </div>
      </div>
      <div class="col-9">
        <div class="card">
          <div class="card-header">
            <span>我的Bot</span>
            <button
              type="button"
              class="btn btn-success float-end"
              data-bs-toggle="modal"
              data-bs-target="#add-bot-btn"
            >
              创建Bot
            </button>
            <!-- Modal -->
            <div
              id="add-bot-btn"
              class="modal fade"
              tabindex="-1"
            >
              <div class="modal-dialog modal-xl">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title">
                      创建Bot
                    </h5>
                    <button
                      type="button"
                      class="btn-close"
                      data-bs-dismiss="modal"
                      aria-label="Close"
                    />
                  </div>
                  <div class="modal-body">
                    <div class="mb-3">
                      <label
                        for="add-bot-title"
                        class="form-label"
                      >名称</label>
                      <input
                        id="add-bot-title"
                        v-model="new_bot.title"
                        type="text"
                        class="form-control"
                        placeholder="请输入Bot名称"
                      >
                    </div>
                    <div class="mb-3">
                      <label
                        for="add-bot-desc"
                        class="form-label"
                      >简介</label>
                      <textarea
                        id="add-bot-desc"
                        v-model="new_bot.description"
                        class="form-control"
                        rows="3"
                        placeholder="请输入Bot简介"
                      />
                    </div>
                    <div class="mb-3">
                      <label
                        for="add-bot-code"
                        class="form-label"
                      >{{ lang_desc }}</label>
                      <VAceEditor
                        v-model:value="new_bot.code"
                        :options="{ fontSize: 16 }"
                        lang="java"
                        theme="textmate"
                        style="height: 300px"
                      />
                    </div>
                  </div>
                  <div class="modal-footer">
                    <div class="msg">
                      {{ msg }}
                    </div>
                    <button
                      type="button"
                      class="btn btn-success"
                      @click="add_bot"
                    >
                      新建
                    </button>
                    <button
                      type="button"
                      class="btn btn-secondary"
                      data-bs-dismiss="modal"
                      @click="refresh_bots"
                    >
                      取消
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="card-body">
            <table class="table table-hover">
              <thead>
                <tr>
                  <th>名称</th>
                  <th>创建时间</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr
                  v-for="bot in bots"
                  :key="bot.id"
                >
                  <td>{{ bot.title }}</td>
                  <td>{{ bot.createTime }}</td>
                  <td>
                    <div class="msg">
                      {{ msg }}
                    </div>
                    <button
                      type="button"
                      class="btn btn-secondary"
                      style="margin-right: 10px;"
                      data-bs-toggle="modal"
                      :data-bs-target="'#update-bot-btn-' + bot.id"
                    >
                      修改
                    </button>
                    <button
                      type="button"
                      class="btn btn-danger"
                      @click="remove_bot(bot)"
                    >
                      删除
                    </button>
                    <div
                      :id="'update-bot-btn-' + bot.id"
                      class="modal fade"
                      tabindex="-1"
                    >
                      <div class="modal-dialog modal-xl">
                        <div class="modal-content">
                          <div class="modal-header">
                            <h5 class="modal-title">
                              更新Bot
                            </h5>
                            <button
                              type="button"
                              class="btn-close"
                              data-bs-dismiss="modal"
                              aria-label="Close"
                            />
                          </div>
                          <div class="modal-body">
                            <div class="mb-3">
                              <label
                                for="add-bot-title"
                                class="form-label"
                              >名称</label>
                              <input
                                id="add-bot-title"
                                v-model="bot.title"
                                type="text"
                                class="form-control"
                                placeholder="请输入Bot名称"
                              >
                            </div>
                            <div class="mb-3">
                              <label
                                for="add-bot-desc"
                                class="form-label"
                              >简介</label>
                              <textarea
                                id="add-bot-desc"
                                v-model="bot.description"
                                class="form-control"
                                rows="3"
                                placeholder="请输入Bot简介"
                              />
                            </div>
                            <div class="mb-3">
                              <label
                                for="add-bot-code"
                                class="form-label"
                              >{{ lang_desc }}</label>
                              <VAceEditor
                                v-model:value="bot.code"
                                :options="{ fontSize: 16 }"
                                lang="java"
                                theme="textmate"
                                style="height: 300px"
                              />
                            </div>
                          </div>
                          <div class="modal-footer">
                            <div class="msg">
                              {{ msg }}
                            </div>
                            <button
                              type="button"
                              class="btn btn-success"
                              @click="update_bot(bot)"
                            >
                              保存修改
                            </button>
                            <button
                              type="button"
                              class="btn btn-secondary"
                              data-bs-dismiss="modal"
                              @click="refresh_bots"
                            >
                              取消
                            </button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Modal } from 'bootstrap/dist/js/bootstrap';
import $ from 'jquery';
import { reactive, ref } from 'vue';
import { useStore } from 'vuex';
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';

export default {
  components: {
    VAceEditor
  },

  setup() {
    ace.config.set(
      "basePath",
      "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/");
    const store = useStore();
    let bots = ref([]);
    let msg = ref('');
    let lang_desc = ref('代码（暂仅支持Java）');
    const new_bot = reactive({
      title: '',
      description: '',
      code: '',
    });

    const refresh_bots = () => {
      $.ajax({
        url: store.state.url + '/bot/list',
        type: 'get',
        headers: {
          Authorization: 'Bearer ' + store.state.user.token
        },
        success(resp) {
          bots.value = resp.data;
        },
      });
      msg.value = '';
    };

    refresh_bots();

    const add_bot = () => {
      $.ajax({
        url: store.state.url + '/bot/add',
        type: 'post',
        contentType: 'application/json;charset=UTF-8',
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        data: JSON.stringify({
          title: new_bot.title,
          description: new_bot.description,
          code: new_bot.code,
        }),
        success(resp) {
          if (resp.code === 0) {
            new_bot.title = '';
            new_bot.description = '';
            new_bot.code = '';
            Modal.getInstance('#add-bot-btn').hide();
            refresh_bots();
          } else {
            msg.value = resp.message;
          }
        }
      })
    };

    const remove_bot = (bot) => {
      $.ajax({
        url: store.state.url + '/bot/remove',
        type: 'post',
        contentType: 'application/json;charset=UTF-8',
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        data: JSON.stringify({
          botId: bot.id
        }),
        success(resp) {
          if (resp.code === 0) {
            refresh_bots();
          } else {
            msg.value = resp.message;
          }
        }
      })
    };

    const update_bot = (bot) => {
      $.ajax({
        url: store.state.url + '/bot/update',
        type: 'post',
        contentType: 'application/json;charset=UTF-8',
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        data: JSON.stringify({
          botId: bot.id,
          title: bot.title,
          description: bot.description,
          code: bot.code,
        }),
        success(resp) {
          if (resp.code === 0) {
            Modal.getInstance('#update-bot-btn-' + bot.id).hide();
            refresh_bots();
          } else {
            msg.value = resp.message;
          }
        }
      })
    };

    return {
      bots,
      new_bot,
      msg,
      lang_desc,
      refresh_bots,
      add_bot,
      remove_bot,
      update_bot,
    };
  }
}
</script>

<style scoped>
div.card {
  margin-top: 20px;
}

span {
  font-size: 150%;
  font-weight: bold;
}

div.msg {
  color: red;
}
</style>