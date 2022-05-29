<template>
  <div>
    <svg-icon icon-class="github" @click="goto" />
  </div>
</template>

<script>
export default {
  name: 'Project-Cloud-Plus-Git',
  data() {
    return {
      url: 'https://gitee.com/JavaLionLi/Project-Cloud-Plus'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
