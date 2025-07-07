DO $$
DECLARE
    r RECORD;
BEGIN
    -- Elimina tutte le tabelle nello schema public
    FOR r IN (
        SELECT tablename
        FROM pg_tables
        WHERE schemaname = 'public'
    ) LOOP
        EXECUTE 'DROP TABLE IF EXISTS public.' || quote_ident(r.tablename) || ' CASCADE';
    END LOOP;

    -- Elimina tutti i tipi ENUM definiti nel database
    FOR r IN (
        SELECT n.nspname AS enum_schema,
               t.typname AS enum_type
        FROM pg_type t
        JOIN pg_enum e ON t.oid = e.enumtypid
        JOIN pg_catalog.pg_namespace n ON n.oid = t.typnamespace
        GROUP BY enum_schema, enum_type
    ) LOOP
        EXECUTE 'DROP TYPE IF EXISTS ' || quote_ident(r.enum_schema) || '.' || quote_ident(r.enum_type) || ' CASCADE';
    END LOOP;
END $$;

DO $$
DECLARE
    rec RECORD;
BEGIN
    FOR rec IN 
        SELECT sequence_name
        FROM information_schema.sequences
        WHERE sequence_schema = 'public'
    LOOP
        EXECUTE format('ALTER SEQUENCE %I RESTART WITH 1;', rec.sequence_name);
    END LOOP;
END $$;
