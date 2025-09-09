-- Add status column to medicines table with default value 'active'
ALTER TABLE medicines ADD COLUMN status VARCHAR(20) DEFAULT 'active';

-- Add constraint to ensure status is either 'active' or 'inactive'
ALTER TABLE medicines ADD CONSTRAINT chk_medicines_status CHECK (status IN ('active', 'inactive'));

-- Create an index for faster status lookups
CREATE INDEX idx_medicines_status ON medicines(status);