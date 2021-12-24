CREATE TABLE IF NOT EXISTS public."user" (
                               user_id integer NOT NULL,
                               first_name character varying(50),
                               last_name character varying(50),
                               nickname character varying(50),
                               role_id integer,
                               password character varying(50),
                               email character varying(100),
                               "avatar_image_URL" text
);

CREATE TABLE IF NOT EXISTS public."user_short" (
                                             user_id integer NOT NULL,
                                             first_name character varying(50),
                                             last_name character varying(50)
);