<template>
    <ContentField>
        <table class="table table-striped table-hover">
            <thead>
                <tr>
                    <th>Player1</th>
                    <th>Player2</th>
                    <th>胜者</th>
                    <th>对战时间</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="record in records" :key="record.record.id">
                    <td>
                        <img class="user-profile" :src="record.profile1" alt="头像1" />
                        &nbsp;
                        <span>{{ record.username1 }}</span>
                    </td>
                    <td>
                        <img class="user-profile" :src="record.profile2" alt="头像1" />
                        &nbsp;
                        <span>{{ record.username2 }}</span>
                    </td>
                    <td>{{ record.winner }}</td>
                    <td>{{ record.record.createTime }}</td>
                    <td>
                        <button type="button" class="btn btn-danger"
                            @click="play_record(record.record.id)">查看录像</button>
                    </td>
                </tr>
            </tbody>
        </table>
        <nav aria-label="...">
            <ul class="pagination">
                <li class="page-item" @click="click_page(-2)">
                    <a class="page-link" href="#">上一页</a>
                </li>
                <li :class="'page-item ' + page.is_active" v-for="page in pages" :key="page.number"
                    @click="click_page(page.number)">
                    <a class="page-link" href="#">{{ page.number }}</a>
                </li>
                <li class="page-item" @click="click_page(-1)">
                    <a class="page-link" href="#">下一页</a>
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
                url: 'http://124.70.17.77:3000/api/record/list',
                type: 'get',
                headers: {
                    Authorization: 'Bearer ' + store.state.user.token,
                },
                data: {
                    page,
                },
                success(resp) {
                    console.log(resp);
                    records.value = resp.records;
                    total = resp.total;
                    update_pages();
                },
                error(resp) {
                    console.log(resp);
                }
            });
        }

        const stringTo2d = map => {
            let g = [];
            for (let i = 0, k = 0; i < 13; i ++) {
                let line = [];
                for (let j = 0; j < 14; j ++, k ++ ) {
                    if (map[k] === '1') line.push(1);
                    else line.push(0);
                }
                g.push(line);
            }
            return g;
        }

        const play_record = recordId => {
            for (const record of records.value) {
                if (record.record.id === recordId) {
                    store.commit('updateIsRecord', true);
                    store.commit('updateGame', {
                        identity: 1,
                        gameMap: stringTo2d(record.record.map),
                    });
                    store.commit('updateOpponent', {
                        username: record.username2,
                        profile: record.profile2,
                    });
                    store.commit('updateSteps', {
                        steps1: record.record.asteps,
                        steps2: record.record.bsteps,
                    });
                    store.commit('updateRecordLoser', record.record.loserIdentity);
                    router.push({
                        name: 'record_playing',
                        params: {
                            recordId,
                        }
                    })
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
            click_page,
        }
    }
}
</script>

<style scoped>
.table{
    text-align: center;
}
img.user-profile {
    width: 5vh;
    border-radius: 50%;
}
.pagination {
    float: right;
}
</style>