<template>
  <div>
    <svg-icon icon-class="question" @click="goto" />
  </div>
</template>

<script>
export default {
  name: 'Project-Cloud-Plus-Wiki',
  data() {
    return {
      url: 'https://gitee.com/JavaLionLi/Project-Cloud-Plus/wikis'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
