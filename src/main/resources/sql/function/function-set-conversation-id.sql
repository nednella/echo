CREATE OR REPLACE FUNCTION set_conversation_id()
RETURNS TRIGGER AS
'
    BEGIN
        IF NEW.parent_id IS NULL THEN
            NEW.conversation_id = NEW.id;
        ELSE
            SELECT conversation_id 
            INTO NEW.conversation_id
            FROM "post"
            WHERE id = NEW.parent_id;

            IF NOT FOUND THEN
                RAISE EXCEPTION ''Parent post with id % does not exist'', NEW.parent_id;
            END IF;
        END IF;
        RETURN NEW;
    END;
'
LANGUAGE plpgsql;
