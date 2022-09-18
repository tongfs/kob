<template>
    <div class="container">
        <div class="row">
            <div class="col-3">
                <div class="card">
                    <div class="card-body">
                        <img :src="$store.state.user.profile" alt="头像" style="width: 100%;">
                    </div>
                </div>
            </div>
            <div class="col-9">
                <div class="card">
                    <div class="card-header">
                        <span>我的Bot</span>
                        <button type="button" class="btn btn-success float-end" data-bs-toggle="modal"
                            data-bs-target="#add-bot-btn">创建Bot</button>
                        <!-- Modal -->
                        <div class="modal fade" id="add-bot-btn" tabindex="-1">
                            <div class="modal-dialog modal-xl">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title">创建Bot</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                            aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="mb-3">
                                            <label for="add-bot-title" class="form-label">名称</label>
                                            <input v-model="new_bot.title" type="text" class="form-control"
                                                id="add-bot-title" placeholder="请输入Bot名称">
                                        </div>
                                        <div class="mb-3">
                                            <label for="add-bot-desc" class="form-label">简介</label>
                                            <textarea v-model="new_bot.description" class="form-control"
                                                id="add-bot-desc" rows="3" placeholder="请输入Bot简介"></textarea>
                                        </div>
                                        <div class="mb-3">
                                            <label for="add-bot-code" class="form-label">代码</label>
                                            <VAceEditor v-model:value="new_bot.content" lang="c_cpp"
                                                theme="textmate" style="height: 300px" />
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <div class="msg">{{ msg }}</div>
                                        <button type="button" class="btn btn-success" @click="add_bot">新建</button>
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"
                                            @click="refresh_bots">取消</button>
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
                                <tr v-for="bot in bots" :key="bot.id">
                                    <td>{{ bot.title }}</td>
                                    <td>{{ bot.createTime }}</td>
                                    <td>
                                        <button type="button" class="btn btn-secondary" style="margin-right: 10px;"
                                            data-bs-toggle="modal"
                                            :data-bs-target="'#update-bot-btn-' + bot.id">修改</button>
                                        <button type="button" class="btn btn-danger"
                                            @click="remove_bot(bot)">删除</button>
                                        <div class="modal fade" :id="'update-bot-btn-' + bot.id" tabindex="-1">
                                            <div class="modal-dialog modal-xl">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title">更新Bot</h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                            aria-label="Close"></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <div class="mb-3">
                                                            <label for="add-bot-title" class="form-label">名称</label>
                                                            <input v-model="bot.title" type="text" class="form-control"
                                                                id="add-bot-title" placeholder="请输入Bot名称">
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="add-bot-desc" class="form-label">简介</label>
                                                            <textarea v-model="bot.description" class="form-control"
                                                                id="add-bot-desc" rows="3"
                                                                placeholder="请输入Bot简介"></textarea>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="add-bot-code" class="form-label">代码</label>
                                                            <VAceEditor v-model:value="bot.content" lang="c_cpp" theme="textmate"
                                                                style="height: 300px" />
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <div class="msg">{{ msg }}</div>
                                                        <button type="button" class="btn btn-success"
                                                            @click="update_bot(bot)">保存修改</button>
                                                        <button type="button" class="btn btn-secondary"
                                                            data-bs-dismiss="modal" @click="refresh_bots">取消</button>
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

// 代码编辑器
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

        const new_bot = reactive({
            title: '',
            description: '',
            content: '',
        });

        const refresh_bots = () => {
            $.ajax({
                url: 'http://124.70.17.77:3000/api/user/bot/all',
                type: 'get',
                headers: {
                    Authorization: 'Bearer ' + store.state.user.token,
                },
                success(resp) {
                    bots.value = resp;
                },
            });
            msg.value = '';
        };

        refresh_bots();

        const add_bot = () => {
            $.ajax({
                url: 'http://124.70.17.77:3000/api/user/bot/add',
                type: 'post',
                headers: {
                    Authorization: 'Bearer ' + store.state.user.token,
                },
                data: {
                    title: new_bot.title,
                    description: new_bot.description,
                    content: new_bot.content,
                },
                success(resp) {
                    if (resp.msg === 'success') {
                        new_bot.title = '';
                        new_bot.description = '';
                        new_bot.content = '';
                        Modal.getInstance('#add-bot-btn').hide();
                        refresh_bots();
                    } else {
                        msg.value = resp.msg;
                    }
                }
            })
        };

        const remove_bot = (bot) => {
            $.ajax({
                url: 'http://124.70.17.77:3000/api/user/bot/remove',
                type: 'post',
                headers: {
                    Authorization: 'Bearer ' + store.state.user.token,
                },
                data: {
                    botId: bot.id,
                },
                success(resp) {
                    if (resp.msg === 'success') {
                        refresh_bots();
                    }
                }
            })
        };

        const update_bot = (bot) => {
            $.ajax({
                url: 'http://124.70.17.77:3000/api/user/bot/update',
                type: 'post',
                headers: {
                    Authorization: 'Bearer ' + store.state.user.token,
                },
                data: {
                    botId: bot.id,
                    title: bot.title,
                    description: bot.description,
                    content: bot.content,
                },
                success(resp) {
                    if (resp.msg === 'success') {
                        Modal.getInstance('#update-bot-btn-' + bot.id).hide();
                        refresh_bots();
                    } else {
                        msg.value = resp.msg;
                    }
                }
            })
        }

        return {
            bots,
            new_bot,
            msg,
            refresh_bots,
            add_bot,
            remove_bot,
            update_bot,
        }
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