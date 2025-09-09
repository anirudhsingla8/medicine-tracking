-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create the users table
CREATE TABLE users (
   id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
   email VARCHAR(255) UNIQUE NOT NULL,
   password_hash VARCHAR(255) NOT NULL,
   password_last_changed TIMESTAMPTZ DEFAULT NOW(),
   created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Create the medicines table
CREATE TABLE medicines (
   id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
   user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
   name VARCHAR(255) NOT NULL,
   image_url TEXT,
   dosage TEXT,
   quantity INTEGER NOT NULL DEFAULT 0,
   expiry_date DATE NOT NULL,
   category VARCHAR(100),
   notes TEXT,
   composition JSONB DEFAULT '[]',
   form VARCHAR(20),
   created_at TIMESTAMPTZ DEFAULT NOW(),
   updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- Create an index for faster lookups
CREATE INDEX idx_medicines_user_id ON medicines(user_id);

-- Create token blacklist table for invalidated tokens
CREATE TABLE token_blacklist (
   id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
   token TEXT NOT NULL,
   user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
   expires_at TIMESTAMPTZ NOT NULL,
   blacklisted_at TIMESTAMPTZ DEFAULT NOW()
);

-- Create an index for faster token lookups
CREATE INDEX idx_token_blacklist_token ON token_blacklist(token);
CREATE INDEX idx_token_blacklist_user_id ON token_blacklist(user_id);
CREATE INDEX idx_token_blacklist_expires_at ON token_blacklist(expires_at);