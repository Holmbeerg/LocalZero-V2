import { defineStore } from "pinia";
import { notificationsAPI } from "@/services/apiService";
import type { NotificationResponse } from "@/types/notifications";

export const useNotificationStore = defineStore('notification', {
    state: () => ({
        notifications: [] as NotificationResponse[],
        unreadCount: 0,
        showDropdown: false,
    }),

    actions: {
        async fetchNotifications() {
            try{
                this.notifications = await notificationsAPI.getNotifications();
                this.unreadCount = await notificationsAPI.getUnreadCount();
            }catch (error){
                console.error('Failed to fetch notifications:', error);
            }
        },

        async fetchUnreadCount() {
            try{
                this.unreadCount = await notificationsAPI.getUnreadCount();
            }catch (error){
                console.error('Failed to fetch unread count:', error);
            }
        },

        async deleteNotification(id: number) {
            try{
                await notificationsAPI.deleteNotification(id);
                this.notifications = this.notifications.filter(n => n.id !== id);
                this.unreadCount = this.notifications.length;
            }catch (error){
                console.error('Failed to delete notification:', error);
            }
        },

        async clearAllNotifications() {
            try{
                await notificationsAPI.clearAllNotifications();
                this.notifications = [];
                this.unreadCount = 0;
            }catch (error){
                console.error('Failed to clear notifications:', error);
            }
        },

        toggleDropdown() {
            this.showDropdown = !this.showDropdown;
            if (this.showDropdown){
                this.fetchNotifications();
            }
        },

        closeDropdown() {
            this.showDropdown = false;
        }
    }
})