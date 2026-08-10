
alter table players drop constraint uk_players_name;

CREATE UNIQUE INDEX idx_players_name_active
    ON players (name)
    WHERE is_deleted = false;