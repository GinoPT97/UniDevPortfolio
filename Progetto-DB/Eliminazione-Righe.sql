DO $$
DECLARE
    rec RECORD;
BEGIN
    -- Drop tutte le tabelle nello schema public
    FOR rec IN SELECT tablename FROM pg_tables WHERE schemaname = 'public' LOOP
        EXECUTE format('DROP TABLE IF EXISTS public.%I CASCADE', rec.tablename);
    END LOOP;

    -- Drop tutti i tipi ENUM nello schema public
    FOR rec IN
        SELECT n.nspname AS enum_schema, t.typname AS enum_type
        FROM pg_type t
        JOIN pg_enum e ON t.oid = e.enumtypid
        JOIN pg_namespace n ON n.oid = t.typnamespace
        WHERE n.nspname = 'public'
        GROUP BY n.nspname, t.typname
    LOOP
        EXECUTE format('DROP TYPE IF EXISTS %I.%I CASCADE', rec.enum_schema, rec.enum_type);
    END LOOP;

    -- Reset di tutte le sequenze nello schema public
    FOR rec IN SELECT sequence_name FROM information_schema.sequences WHERE sequence_schema = 'public' LOOP
        EXECUTE format('ALTER SEQUENCE public.%I RESTART WITH 1', rec.sequence_name);
    END LOOP;
END $$;
