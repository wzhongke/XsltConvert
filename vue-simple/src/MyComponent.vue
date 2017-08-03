<template>
  <div id="app">
    <p>I have a secret message:</p>
    <p v-if="show" class="secret">{{message}}</p>
    <button v-else @click="show = true">Show Message</button>
  </div>
</template>

<script>
export default {
  name: 'app',
  data () {
    return {
      show:false,
      message: 'much secret. many reactive. wow!'
    }
  }
}
</script>

<style lang="scss">
  .secret {
    background-color: thistle;
  }
</style>
