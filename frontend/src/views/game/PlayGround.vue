<template>
  <div class="playground">
    <GameMap />
  </div>
</template>

<script>
import GameMap from '@/views/game/GameMap'

export default {
  components: {
    GameMap
  }
}
</script>

<style scoped>
div.playground {
  width: 60vw;
  height: 70vh;
  margin: 10vh auto;
}
</style>
