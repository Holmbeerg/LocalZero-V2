ALTER TYPE notification_type ADD VALUE IF NOT EXISTS 'JOIN_INITIATIVE';

CREATE TABLE notification (
      id BIGSERIAL PRIMARY KEY,
      title VARCHAR(255) NOT NULL,
      message TEXT NOT NULL,
      type VARCHAR(50) NOT NULL,
      created_by_user_id BIGINT NOT NULL,
      created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
      FOREIGN KEY (created_by_user_id) REFERENCES users(user_id)
);

CREATE TABLE user_notification (
       id BIGSERIAL PRIMARY KEY,
       user_id BIGINT NOT NULL,
       notification_id BIGINT NOT NULL,
       FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
       FOREIGN KEY (notification_id) REFERENCES notification(id) ON DELETE CASCADE,
       CONSTRAINT uq_user_notification UNIQUE (user_id, notification_id)
);

CREATE INDEX idx_user_notification_user ON user_notification(user_id);
CREATE INDEX idx_notification_created ON notification(created_at);