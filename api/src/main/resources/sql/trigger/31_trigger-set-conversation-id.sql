CREATE OR REPLACE TRIGGER trigger_set_conversation_id
BEFORE INSERT ON "post"
FOR EACH row
EXECUTE FUNCTION set_conversation_id();