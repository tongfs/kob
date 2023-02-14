<template>
  <div class="container">
    <div class="row">
      <div class="col-6">
        <div
          id="accordionPanelsStayOpenExample"
          class="accordion"
        >
          <div class="accordion-item">
            <h2
              id="panelsStayOpen-headingOne"
              class="accordion-header"
            >
              <button
                class="accordion-button"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#panelsStayOpen-collapseOne"
                aria-expanded="true"
                aria-controls="panelsStayOpen-collapseOne"
              >
                <span>项目说明</span>
              </button>
            </h2>
            <div
              id="panelsStayOpen-collapseOne"
              class="accordion-collapse collapse show"
              aria-labelledby="panelsStayOpen-headingOne"
            >
              <div class="accordion-body">
                <div>
                  一款贪吃蛇策略性双人对战小游戏。玩家通过控制蛇的走向来躲避障碍物和对手，直到赢得最后的胜利。
                </div>
                <ul>
                  <li>
                    <b>对战：</b>游戏界面。两名玩家匹配完成后开始游戏，支持玩家手动控制，或编写 Bot 代码自动控制，从而实现人人、人机、机机对战；支持15秒匹配不到对手时系统派出内置 Bot 与玩家对战（<u>一定要等15秒啊</u>）
                  </li>
                  <li>
                    <b>排行榜：</b>由玩家积分决定，每次游戏都会更新玩家的积分
                  </li>
                  <li>
                    <b>对局记录：</b>可以看到所有对战，同时支持游戏录像回放
                  </li>
                  <li>
                    <b>个人中心：</b>网页右上角，管理 Bot 信息。系统会为每名玩家提供一只默认 Bot。你也可以自己编写 Bot 代码，打造属于你的最强 Bot
                  </li>
                </ul>
                <div>
                  如果你不想注册，可以使用我提供给你的默认账号/密码 <u>snake/snake</u> 登录体验。
                </div>
              </div>
            </div>
          </div>
          <div class="accordion-item">
            <h2
              id="panelsStayOpen-headingTwo"
              class="accordion-header"
            >
              <button
                class="accordion-button collapsed"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#panelsStayOpen-collapseTwo"
                aria-expanded="false"
                aria-controls="panelsStayOpen-collapseTwo"
              >
                <span>技术相关</span>
              </button>
            </h2>
            <div
              id="panelsStayOpen-collapseTwo"
              class="accordion-collapse collapse"
              aria-labelledby="panelsStayOpen-headingTwo"
            >
              <div class="accordion-body">
                <ul>
                  <li>基于 Vue + SpringBoot 开发的前后端分离 Web 项目，使用 MySQL 实现数据持久化</li>
                  <li>使用 SpringSecurity + JWT 实现玩家身份认证</li>
                  <li>前后端通过 HTTP 实现常用的 CRUD 需求；游戏时，前后端通过建立 WebSocket 连接实现全双工通信</li>
                  <li>后端按照微服务的思想（如右图），分为 主服务（main-server）、匹配服务（match-server）、Bot代码运行服务（bot-server） 3个 SpringBoot 项目，各个服务之间使用 RestTemplate 发送请求</li>
                  <li>为保证游戏公平性，地图由后端随机生成，并使用 FloodFill 算法判断地图合法性</li>
                  <li>在 bot-server 中，参考生产者-消费者模型和消息队列的思想，手动实现一个线程池，用于动态编译运行 Bot 代码；动态编译使用反射工具 JOOR 完成</li>
                  <li><a href="https://github.com/tongfs/kob">项目 Git 仓库地址</a> </li>
                </ul>
              </div>
            </div>
          </div>
          <div class="accordion-item">
            <h2
              id="panelsStayOpen-headingThree"
              class="accordion-header"
            >
              <button
                class="accordion-button collapsed"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#panelsStayOpen-collapseThree"
                aria-expanded="false"
                aria-controls="panelsStayOpen-collapseThree"
              >
                <span>未竟之志</span>
              </button>
            </h2>
            <div
              id="panelsStayOpen-collapseThree"
              class="accordion-collapse collapse"
              aria-labelledby="panelsStayOpen-headingThree"
            >
              <div class="accordion-body">
                <ul>
                  <li>适当场景引入 Redis 作为（如用户认证、游戏过程、排行榜）</li>
                  <li>使用 Minimax 和 Alpha-beta 剪枝算法优化 Bot 逻辑</li>
                  <li>尝试将 Bot 代码的编译运行放在容器中进行</li>
                  <li>完善用户体验：修改头像、修改密码等</li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="col-6">
        <div class="card">
          <div class="card-header">
            <span>项目结构</span>
          </div>
          <div class="card-body">
            <img
              src="https://youpeng-yp.oss-cn-chengdu.aliyuncs.com/avatar/2023-01-01/structure.jpg"
              style="width: 100%;"
            >
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  components: {
  }
}
</script>

<style scoped>
div.card {
  margin-top: 20px;
}

div.card-header {
  padding-top: 12px;
  padding-bottom: 12px;
}

.accordion {
  margin-top: 20px;
}

span {
  font-size: 150%;
  font-weight: bold;
}

li {
  padding-top: 8px;
}

ul {
  margin-bottom: 8px;
}
</style>