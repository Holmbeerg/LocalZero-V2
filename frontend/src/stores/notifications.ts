import { defineStore } from 'pinia'
import { notificationsAPI } from '@/services/apiService'
import type { NotificationResponse } from '@/types/notifications'

export const useNotificationStore = defineStore('notifications', {
  state: () => ({
    notifications: [] as NotificationResponse[],
    unreadCount: 0,
    showDropdown: false,
    loading: false,
    error: null as string | null,
    pagination: {
      page: 0,
      size: 10,
      totalElements: 0,
      hasMore: false,
    },
  }),

  actions: {
    async fetchNotifications(page: number = 0, size: number = 10) {
      this.loading = true
      this.error = null
      try {
        const response = await notificationsAPI.getNotifications(page, size)

        if (response && response.data) {
          const { content, totalElements } = response.data

          this.notifications =
            page === 0 ? content || [] : [...this.notifications, ...(content || [])]

          this.pagination = {
            page,
            size,
            totalElements: totalElements || 0,
            hasMore: (page + 1) * size < (totalElements || 0),
          }

          this.unreadCount = this.notifications.filter((n) => !n.readAt).length
        } else {
          this.notifications = []
          this.pagination = {
            page: 0,
            size,
            totalElements: 0,
            hasMore: false,
          }
        }
      } catch (error) {
        console.error('Error fetching notifications:', error)
        this.error = 'Failed to load notifications'
        this.notifications = []
      } finally {
        this.loading = false
      }
    },

    async loadMoreNotifications() {
      if (this.pagination.hasMore && !this.loading) {
        await this.fetchNotifications(this.pagination.page + 1, this.pagination.size)
      }
    },

    async fetchUnreadCount() {
      try {
        const count = await notificationsAPI.getUnreadCount()
        this.unreadCount = count
      } catch (error) {
        console.error('Failed to fetch unread count:', error)
        return 0
      }
    },

    async deleteNotification(id: number) {
      try {
        await notificationsAPI.deleteNotification(id)
        this.notifications = this.notifications.filter((n) => n.id !== id)
        this.unreadCount = this.notifications.filter((n) => !n.readAt).length
      } catch (error) {
        console.error('Failed to delete notification:', error)
      }
    },

    async clearAllNotifications() {
      try {
        await notificationsAPI.clearAllNotifications()
        this.notifications = []
        this.unreadCount = 0
        this.pagination = {
          page: 0,
          size: 10,
          totalElements: 0,
          hasMore: false,
        }
      } catch (error) {
        console.error('Failed to clear notifications:', error)
      }
    },

    async toggleDropdown() {
      this.showDropdown = !this.showDropdown
      if (this.showDropdown) {
        try {
          await Promise.all([this.fetchUnreadCount(), this.fetchNotifications()])
        } catch (error) {
          console.error('Error loading notifications:', error)
        }
      }
    },

    closeDropdown() {
      this.showDropdown = false
    },
  },
})
