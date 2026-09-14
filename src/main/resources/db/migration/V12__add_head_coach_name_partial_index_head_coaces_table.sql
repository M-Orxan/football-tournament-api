

DROP INDEX IF EXISTS idx_head_coaches_name_active;

CREATE UNIQUE INDEX idx_head_coaches_name_active
    ON head_coaches (name)
    WHERE is_deleted = false;