-- Remove unique constraint on key_value to allow multiple users to share the same API key
-- Each user will have their own api_key_id and remaining_calls tracking

-- Drop the unique constraint
ALTER TABLE api_keys DROP CONSTRAINT IF EXISTS api_keys_key_value_key;

-- Verify the constraint is removed
-- You can check with: SELECT * FROM information_schema.table_constraints WHERE table_name = 'api_keys';

