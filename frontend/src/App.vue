<script setup lang="ts">
import { RouterView, useRoute } from 'vue-router'

import Header from './components/global/AppHeader.vue'
import { computed } from 'vue'

const route = useRoute()
const showHeader = computed(() => !route.meta.hideHeader)
</script>

<template>
  <Header v-if="showHeader" />
  <RouterView />
</template>
