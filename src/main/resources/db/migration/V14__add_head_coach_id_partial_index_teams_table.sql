CREATE UNIQUE INDEX idx_head_coach_id_active
    ON teams (head_coach_id)
    WHERE is_deleted = false;