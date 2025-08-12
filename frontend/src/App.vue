<script setup lang="ts">
import { RouterView, useRoute } from 'vue-router'

import Header from './components/global/AppHeader.vue'
import { computed, watch } from 'vue'
import { useAuthStore } from './stores/auth'
import { useNotificationStore } from './stores/notifications'

const route = useRoute()
const authStore = useAuthStore()
const notificationStore = useNotificationStore()
const showHeader = computed(() => !route.meta.hideHeader)

//Watch auth state
watch(
  () => authStore.user,
  (user) => {
    if (user) {
      notificationStore.fetchUnreadCount()
    }else{
      notificationStore.clearAllNotifications()
    }
  },
  {immediate: true}
)
</script>

<template>
  <Header v-if="showHeader" />
  <RouterView />
</template>
