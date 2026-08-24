

ALTER TABLE matches
    ADD COLUMN round_number Bigint NOT NULL;

alter table matches
    ADD COLUMN winner_team_id Bigint NOT NULL;