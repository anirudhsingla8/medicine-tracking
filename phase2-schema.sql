-- 1. Create a new 'profiles' table to store family member details.
-- Each profile is linked to a primary user account.
CREATE TABLE profiles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Index for faster profile lookups by user account
CREATE INDEX idx_profiles_user_id ON profiles(user_id);

-- 2. ALTER the existing 'medicines' table to link each medicine to a specific profile.
-- This is the core change for multi-user inventory.
ALTER TABLE medicines ADD COLUMN profile_id UUID;

-- Add a foreign key constraint to ensure data integrity.
ALTER TABLE medicines ADD CONSTRAINT fk_medicines_profile_id
FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE;

-- Make the column NOT NULL. This assumes a clean setup or that a data migration will be performed.
ALTER TABLE medicines ALTER COLUMN profile_id SET NOT NULL;


-- 3. Create the 'schedules' table for managing dosage times.
-- Each schedule is linked to a specific medicine AND profile.
CREATE TABLE schedules (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    medicine_id UUID NOT NULL REFERENCES medicines(id) ON DELETE CASCADE,
    profile_id UUID NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE, -- For security scoping
    time_of_day TIME NOT NULL,
    frequency VARCHAR(50) NOT NULL DEFAULT 'daily', -- e.g., 'daily', 'weekly'
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Index for faster lookups
CREATE INDEX idx_schedules_medicine_id ON schedules(medicine_id);

-- 4. ALTER the 'users' table to store push notification tokens.
ALTER TABLE users ADD COLUMN fcm_token TEXT;