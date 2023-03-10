CREATE TYPE TYPE_USER AS ENUM ('STUDENT', 'TEACHER', 'ADMIN');

CREATE TABLE T_USER (
    C_ID                UUID PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    C_TENANT_ID         UUID NOT NULL,
    C_USER_TYPE         TYPE_USER NOT NULL,
    C_FIRST_NAME        TEXT NOT NULL,
    C_LAST_NAME         TEXT NOT NULL,
    C_CREATED_AT        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'UTC')
);
