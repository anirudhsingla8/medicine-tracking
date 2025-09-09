-- If you already have the tables created, use this command to add the password_last_changed column:
ALTER TABLE users ADD COLUMN password_last_changed TIMESTAMPTZ DEFAULT NOW();
UPDATE users SET password_last_changed = created_at WHERE password_last_changed IS NULL;