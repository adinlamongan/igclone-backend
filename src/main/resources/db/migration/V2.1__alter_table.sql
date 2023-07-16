CREATE INDEX master_users_id_idx ON public.master_users (id);
CREATE INDEX posts_id_idx ON public.posts (id,user_id);
CREATE INDEX followers_id_idx ON public.followers (id,user_id,follower_user_id);
CREATE INDEX comment_post_id_idx ON public.comment_post (id,user_id,post_id);
CREATE INDEX like_post_id_idx ON public.like_post (id,user_id,post_id);
CREATE INDEX stories_id_idx ON public.stories (id,user_id);