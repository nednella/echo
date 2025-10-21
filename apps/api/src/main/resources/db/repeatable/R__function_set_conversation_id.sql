/*
 * fn ensures all posts belonging to the same conversation thread share the same
 * conversation_id.
 * 
 * Root post: conversation_id = id
 * Replies:   conversation_id = parent's conversation_id
 */

CREATE OR REPLACE FUNCTION set_conversation_id()
RETURNS TRIGGER
AS
'
    BEGIN
        IF NEW.parent_id IS NULL THEN
            NEW.conversation_id = NEW.id;
        ELSE
            SELECT conversation_id 
            INTO NEW.conversation_id
            FROM "post"
            WHERE id = NEW.parent_id;
        END IF;
        RETURN NEW;
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trigger_set_conversation_id
BEFORE INSERT ON "post"
FOR EACH row
EXECUTE FUNCTION set_conversation_id();