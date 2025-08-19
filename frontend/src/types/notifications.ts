export interface NotificationResponse {
    id: number;
    title: string;
    message: string;
    type: string;
    createdAt: string;
    readAt: string | null;
}

export enum NotificationType {
    NEW_INITIATIVE = "NEW_INITIATIVE",
    POST_COMMENT = "POST_COMMENT",
    NEW_LIKE_ON_POST = "NEW_LIKE_ON_POST",
    COMMENT_REPLY = "COMMENT_REPLY",
    NEW_MESSAGE = "NEW_MESSAGE"
}
