CREATE TABLE auth_tokens (
    id          UUID         NOT NULL DEFAULT gen_random_uuid(),
    user_id     UUID         NOT NULL,
    token       TEXT         NOT NULL,
    type        VARCHAR(32)  NOT NULL,
    used        BOOLEAN      NOT NULL DEFAULT FALSE,
    revoked     BOOLEAN      NOT NULL DEFAULT FALSE,
    expires_at  TIMESTAMP    NOT NULL,

    CONSTRAINT pk_auth_tokens    PRIMARY KEY (id),
    CONSTRAINT fk_auth_tokens_user FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT chk_auth_tokens_type CHECK (
        type IN ('ACCESS', 'REFRESH', 'EMAIL_VERIFICATION', 'PASSWORD_RESET')
    )
);

CREATE INDEX idx_auth_tokens_token   ON auth_tokens (token);
CREATE INDEX idx_auth_tokens_user    ON auth_tokens (user_id);
CREATE INDEX idx_auth_tokens_expires ON auth_tokens (expires_at);
