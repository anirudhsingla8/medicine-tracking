-- This schema is a consolidated version of the original project's migration scripts.

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 1. Create users table
CREATE TABLE users (
   id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
   email VARCHAR(255) UNIQUE NOT NULL,
   password_hash VARCHAR(255) NOT NULL,
   password_last_changed TIMESTAMPTZ DEFAULT NOW(),
   fcm_token TEXT,
   created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 2. Create profiles table
CREATE TABLE profiles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_profiles_user_id ON profiles(user_id);


-- 3. Create user_medicines table (renamed from medicines)
CREATE TABLE user_medicines (
   id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
   user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
   profile_id UUID NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
   name VARCHAR(255) NOT NULL,
   image_url TEXT,
   dosage TEXT,
   quantity INTEGER NOT NULL DEFAULT 0,
   expiry_date DATE,
   category VARCHAR(100),
   notes TEXT,
   composition JSONB DEFAULT '[]',
   form VARCHAR(20),
   status VARCHAR(20) DEFAULT 'active',
   created_at TIMESTAMPTZ DEFAULT NOW(),
   updated_at TIMESTAMPTZ DEFAULT NOW(),
   CONSTRAINT chk_medicines_status CHECK (status IN ('active', 'inactive'))
);
CREATE INDEX idx_user_medicines_user_id ON user_medicines(user_id);
CREATE INDEX idx_user_medicines_profile_id ON user_medicines(profile_id);
CREATE INDEX idx_user_medicines_status ON user_medicines(status);


-- 4. Create schedules table
CREATE TABLE schedules (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    medicine_id UUID NOT NULL REFERENCES user_medicines(id) ON DELETE CASCADE,
    profile_id UUID NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    time_of_day TIME NOT NULL,
    frequency VARCHAR(50) NOT NULL DEFAULT 'daily',
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_schedules_medicine_id ON schedules(medicine_id);


-- 5. Create global_medicines table
CREATE TABLE global_medicines (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    brand_name VARCHAR(255),
    generic_name VARCHAR(255),
    dosage_form VARCHAR(100),
    strength VARCHAR(100),
    manufacturer VARCHAR(255),
    description TEXT,
    indications TEXT[],
    contraindications TEXT[],
    side_effects TEXT[],
    warnings TEXT[],
    interactions TEXT[],
    storage_instructions TEXT,
    category VARCHAR(100),
    atc_code VARCHAR(10),
    fda_approval_date DATE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_global_medicines_name ON global_medicines(name);
CREATE INDEX idx_global_medicines_category ON global_medicines(category);


-- 6. Create token_blacklist table (Note: This is not used by the Spring Boot app, but included for completeness)
CREATE TABLE token_blacklist (
   id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
   token TEXT NOT NULL,
   user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
   expires_at TIMESTAMPTZ NOT NULL,
   blacklisted_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_token_blacklist_token ON token_blacklist(token);
