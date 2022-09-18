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
                <tr v-for="user in users" :key="user.id">
                    <td>
                        <span>{{ ++ranking }}</span>
                    </td>
                    <td>
                        <img class="user-profile" :src="user.profile" alt="头像" />
                        &nbsp;
                        <span>{{ user.username }}</span>
                    </td>
                    <td>
                        <span>{{ user.rating }}</span>
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
        let ranking = 0;
        const page_num = 10;

        const pull_page = page => {
            current_page = page;
            $.ajax({
                url: 'http://124.70.17.77:3000/api/rank/list',
                type: 'get',
                headers: {
                    Authorization: 'Bearer ' + store.state.user.token,
                },
                data: {
                    page,
                },
                success(resp) {
                    console.log(resp);
                    users.value = resp.users;
                    total = resp.total;
                    update_pages();
                },
                error(resp) {
                    console.log(resp);
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
            ranking,
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