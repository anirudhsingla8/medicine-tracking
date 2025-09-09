-- Migration script to add composition and form fields to user_medicines table

-- Add the composition column to user_medicines table
ALTER TABLE user_medicines ADD COLUMN IF NOT EXISTS composition JSONB DEFAULT '[]';

-- Add the form column to user_medicines table
ALTER TABLE user_medicines ADD COLUMN IF NOT EXISTS form VARCHAR(20);

-- Add the composition column to medicines table (for databases that haven't been migrated yet)
ALTER TABLE medicines ADD COLUMN IF NOT EXISTS composition JSONB DEFAULT '[]';

-- Add the form column to medicines table (for databases that haven't been migrated yet)
ALTER TABLE medicines ADD COLUMN IF NOT EXISTS form VARCHAR(20);