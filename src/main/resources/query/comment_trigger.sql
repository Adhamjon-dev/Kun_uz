CREATE FUNCTION comment_like_trigger_function()
    RETURNS TRIGGER
    LANGUAGE PLPGSQL
AS $$
BEGIN

    If TG_OP = 'INSERT' then
        IF NEW.emotion = 'LIKE' THEN
            UPDATE comment
            SET like_count = like_count + 1
            WHERE id = NEW.comment_id;
        ELSIF NEW.emotion = 'DISLIKE' THEN
            UPDATE comment
            SET dislike_count = dislike_count + 1
            WHERE id = NEW.comment_id;
        END IF;
        return NEW;
    elseif TG_OP = 'UPDATE' then
        IF NEW.emotion = 'LIKE' THEN
            UPDATE comment
            SET like_count = like_count + 1,
                dislike_count = dislike_count - 1
            WHERE id = NEW.comment_id;
        ELSIF NEW.emotion = 'DISLIKE' THEN
            UPDATE comment
            SET dislike_count = dislike_count + 1,
                like_count = like_count - 1
            WHERE id = NEW.comment_id;
        END IF;
        return NEW;
    elseif TG_OP = 'DELETE' then
        IF OLD.emotion = 'LIKE' THEN
            UPDATE comment
            SET like_count = like_count - 1
            WHERE id = OLD.comment_id;
        ELSIF OLD.emotion = 'DISLIKE' THEN
            UPDATE comment
            SET dislike_count = dislike_count - 1
            WHERE id = OLD.comment_id;
        END IF;
        return OLD;
    end if;

END; $$;

CREATE  TRIGGER comment_like_trigger
    BEFORE INSERT OR UPDATE OR DELETE
    ON comment_like
    FOR EACH ROW
EXECUTE PROCEDURE comment_like_trigger_function();