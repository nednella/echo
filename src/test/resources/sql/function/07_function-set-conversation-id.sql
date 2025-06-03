
/*
 * Function ensures all posts belonging to the same conversation thread share
 * the same conversation_id for efficient grouping/filtering.
 * 
 * On INSERT, sets the conversation_id of a new post. For posts with no parent_id,
 * the conversation_id is set to its own id (it becomes the root). For replies, 
 * the conversation_id is set equal to its parents conversation_id.
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
