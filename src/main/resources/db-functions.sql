CREATE OR REPLACE FUNCTION delete_tickets_by_venue(venue_id_param BIGINT)
RETURNS INTEGER AS $$
DECLARE
    deleted_count INTEGER;
BEGIN
    DELETE FROM tickets WHERE venue_id = venue_id_param;
    GET DIAGNOSTICS deleted_count = ROW_COUNT;
    RETURN deleted_count;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION find_ticket_with_min_number()
RETURNS TABLE (
    out_id INTEGER,
    out_name VARCHAR,
    out_number BIGINT,
    out_price DOUBLE PRECISION,
    out_discount BIGINT,
    out_type VARCHAR
) AS $$
BEGIN
    RETURN QUERY
    SELECT t.id, t.name, t.number, t.price, t.discount, t.type
    FROM tickets t
    WHERE t.number = (SELECT MIN(number) FROM tickets)
    LIMIT 1;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION find_ticket_with_max_discount()
RETURNS TABLE (
    out_id INTEGER,
    out_name VARCHAR,
    out_discount BIGINT,
    out_price DOUBLE PRECISION,
    out_number BIGINT,
    out_type VARCHAR
) AS $$
BEGIN
    RETURN QUERY
    SELECT t.id, t.name, t.discount, t.price, t.number, t.type
    FROM tickets t
    WHERE t.discount = (SELECT MAX(discount) FROM tickets)
    LIMIT 1;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sell_ticket_to_person(
    ticket_id_param INTEGER,
    price_param DOUBLE PRECISION,
    passport_id_param VARCHAR,
    eye_color_param VARCHAR,
    hair_color_param VARCHAR,
    weight_param DOUBLE PRECISION,
    nationality_param VARCHAR,
    location_x_param REAL,
    location_y_param BIGINT,
    location_name_param VARCHAR
)
RETURNS VOID AS $$
BEGIN
    UPDATE tickets
    SET price = price_param,
        person_passport_id = passport_id_param,
        person_eye_color = eye_color_param,
        person_hair_color = hair_color_param,
        person_weight = weight_param,
        person_nationality = nationality_param,
        person_location_x = location_x_param,
        person_location_y = location_y_param,
        person_location_name = location_name_param
    WHERE id = ticket_id_param;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Ticket not found';
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION cancel_bookings_by_passport(passport_id_param VARCHAR)
RETURNS INTEGER AS $$
DECLARE
    updated_count INTEGER;
BEGIN
    UPDATE tickets
    SET person_passport_id = NULL,
        person_eye_color = NULL,
        person_hair_color = NULL,
        person_weight = NULL,
        person_nationality = NULL,
        person_location_x = NULL,
        person_location_y = NULL,
        person_location_name = NULL
    WHERE person_passport_id = passport_id_param;

    GET DIAGNOSTICS updated_count = ROW_COUNT;
    RETURN updated_count;
END;
$$ LANGUAGE plpgsql;
