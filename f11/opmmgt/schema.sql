--
-- run with psql.exe -U postgres -f schema.sql

DROP DATABASE opmdb;
DROP USER opmuser;

CREATE DATABASE opmdb;

-- connect 
\c opmdb

CREATE TABLE schemainfo
(
    name VARCHAR(255) PRIMARY KEY NOT NULL,
    value VARCHAR(1024) NOT NULL,
    jdoc JSONB
);

CREATE SEQUENCE opmlocation_id_seq;
CREATE TABLE opmlocation
(
    location_id INTEGER DEFAULT nextval('opmlocation_id_seq') PRIMARY KEY NOT NULL,
    category INTEGER UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1024)
);

CREATE SEQUENCE opmfunction_id_seq;
CREATE TABLE opmfunction
(
    function_id INTEGER DEFAULT nextval('opmfunction_id_seq') PRIMARY KEY NOT NULL,
    category INTEGER UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1024),
    family VARCHAR(255)
);

CREATE SEQUENCE opmaction_id_seq;
CREATE TABLE opmaction
(
    action_id INTEGER DEFAULT nextval('opmaction_id_seq') PRIMARY KEY NOT NULL,
    category INTEGER UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    abbrev VARCHAR(255) NOT NULL,
    description VARCHAR(1024)
);

CREATE SEQUENCE opmprofile_id_seq;
CREATE TABLE opmprofile
(
    profile_id INTEGER DEFAULT nextval('opmprofile_id_seq') PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1024)
);

CREATE SEQUENCE opmmask_id_seq;
CREATE TABLE opmmask
(
    mask_id INTEGER DEFAULT nextval('opmmask_id_seq') PRIMARY KEY NOT NULL,
    mask1 VARCHAR(255) NOT NULL,
    mask2 VARCHAR(255),
    mask3 VARCHAR(255),
    mask4 VARCHAR(255),
    profile_fk INTEGER,
    CONSTRAINT opmmask_opmprofile_profile_id_fk FOREIGN KEY (profile_fk) REFERENCES opmprofile (profile_id),
    location_fk INTEGER,
    CONSTRAINT opmmask_opmlocation_location_id_fk FOREIGN KEY (location_fk) REFERENCES opmlocation (location_id),
    function_fk INTEGER,
    CONSTRAINT opmmask_opmfunction_function_id_fk FOREIGN KEY (function_fk) REFERENCES opmfunction (function_id)
);

CREATE USER opmuser WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION
  PASSWORD 'opmuser';

GRANT ALL ON SEQUENCE public.opmaction_id_seq TO opmuser;

GRANT ALL ON SEQUENCE public.opmfunction_id_seq TO opmuser;

GRANT ALL ON SEQUENCE public.opmlocation_id_seq TO opmuser;

GRANT ALL ON SEQUENCE public.opmmask_id_seq TO opmuser;

GRANT ALL ON SEQUENCE public.opmprofile_id_seq TO opmuser;

GRANT ALL ON TABLE public.opmaction TO opmuser;

GRANT ALL ON TABLE public.opmfunction TO opmuser;

GRANT ALL ON TABLE public.opmlocation TO opmuser;

GRANT ALL ON TABLE public.opmmask TO opmuser;

GRANT ALL ON TABLE public.opmprofile TO opmuser;

GRANT ALL ON TABLE public.schemainfo TO opmuser;

INSERT INTO public.schemainfo(name, value)
    VALUES ('schema_name', 'opm'),
     ('schema_version', '1.0'),
     ('creation_date', now());
