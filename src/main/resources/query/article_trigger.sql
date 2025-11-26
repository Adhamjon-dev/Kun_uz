CREATE FUNCTION article_like_trigger_function()
    RETURNS TRIGGER
    LANGUAGE PLPGSQL
AS $$
BEGIN

    If TG_OP = 'INSERT' then
        IF NEW.emotion = 'LIKE' THEN
            UPDATE article
            SET like_count = like_count + 1
            WHERE id = NEW.article_id;
        ELSIF NEW.emotion = 'DISLIKE' THEN
            UPDATE article
            SET dislike_count = dislike_count + 1
            WHERE id = NEW.article_id;
        END IF;
        return NEW;
    elseif TG_OP = 'UPDATE' then
        IF NEW.emotion = 'LIKE' THEN
            UPDATE article
            SET like_count = like_count + 1,
                dislike_count = dislike_count - 1
            WHERE id = NEW.article_id;
        ELSIF NEW.emotion = 'DISLIKE' THEN
            UPDATE article
            SET dislike_count = dislike_count + 1,
                like_count = like_count - 1
            WHERE id = NEW.article_id;
        END IF;
        return NEW;
    elseif TG_OP = 'DELETE' then
        IF OLD.emotion = 'LIKE' THEN
            UPDATE article
            SET like_count = like_count - 1
            WHERE id = OLD.article_id;
        ELSIF OLD.emotion = 'DISLIKE' THEN
            UPDATE article
            SET dislike_count = dislike_count - 1
            WHERE id = OLD.article_id;
        END IF;
        return OLD;
    end if;

END; $$;

CREATE  TRIGGER article_like_trigger
    BEFORE INSERT OR UPDATE OR DELETE
    ON article_like
    FOR EACH ROW
EXECUTE PROCEDURE article_like_trigger_function();