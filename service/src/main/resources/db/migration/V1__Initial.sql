--------------------------------------------------
--------------------------------------------------
--                                              --
--       Extension for UUID Generation          --
--                                              --
--------------------------------------------------
--------------------------------------------------
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

--------------------------------------------------
--------------------------------------------------
--                                              --
--       Updated At Timestamp Function          --
--                                              --
--------------------------------------------------
--------------------------------------------------
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TABLE users (
  id                      VARCHAR NOT NULL,
  name                    VARCHAR NOT NULL,
  color                   VARCHAR NOT NULL,
  created_at              TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at              TIMESTAMP NOT NULL DEFAULT NOW(),
  PRIMARY KEY (id)
);

CREATE TRIGGER set_updated_at BEFORE UPDATE ON users FOR EACH ROW EXECUTE PROCEDURE set_updated_at();
