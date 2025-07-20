<script setup lang="ts">
    import { formatRelativeTime } from '@/utils/dateUtils';
    import type { NotificationResponse } from '@/types/notifications';
    import { useNotificationStore } from '@/stores/notifications';

    defineProps<{ notifications: NotificationResponse[]; }> ();

    const notificationStore = useNotificationStore();

    const handleDelete = (id: number, event: Event) => {
        event.stopPropagation();
        notificationStore.deleteNotification(id);
    }

    const handleClearAll = () => {
        notificationStore.clearAllNotifications();
    }
</script>

<template>
    <div class="absolute right-0 mt-2 w-80 bg-white rounded-md shadow-lg overflow-hidden
                z-50 border-gray-200">
        
        <div class="p-4 border-b border-gray-200 flex justify-between items-center">
            <h3 class="text-lg font-medium text-gray-900">Notifications</h3>
            <button 
                @click="handleClearAll" 
                class="text-sm text-blue-800 disabled:text-gray-400" 
                :disabled="notifications.length === 0">
                    Clear All
            </button>
        </div>
        <div class="max-h-96 overflow-y-auto divide-y divide-gray-100">
            <div v-for="notification in notifications" 
            :key="notification.id" 
            class="p-4 hover:bg-gray-50 cursor-pointer transition-colors duration-150">
        
            <div class="flex justify-between items-start">
                <div class="flex-1 min-w-0">
                    <h4 class="text-sm font-medium text-gray-900 truncate">{{ notification.title }}</h4>
                    <p class="text-sm text-gray-500 mt-1"> {{ notification.message }}</p>
                    <p class="text-sm text-gray-400 mt-2"> {{ formatRelativeTime(notification.createdAt) }}</p>
                </div>
                <button 
                @click="handleDelete(notification.id, $event)"
                class="text-gray-400 hover:text-red-500 ml-2 flex-shrink-0"
                aria-label="Delete notification">

                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" view-box="0 0 20 20" fill="currentColor">
                        <path 
                        fill-rule="evenodd" 
                        d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 
                        1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z" 
                        clip-rule="evenodd" />
                    </svg>
                </button>
            </div>
        </div>
            <div v-if="notifications.length === 0" class="p-4 text-center text-gray-500">No notifications</div>
        </div>
    </div>
</template>

<style scoped></style>
