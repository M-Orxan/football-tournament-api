CREATE UNIQUE INDEX idx_teams_name_active
    ON teams (name)
    WHERE is_deleted = false;