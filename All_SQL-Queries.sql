
-- Creating popular_movie_actors table --

Create table popular_movie_actors as select ma.actor, ma.movie
 from movie_actor as ma JOIN movie as m on ma.movie = m.id
 where type like 'movie' and avgrating > 5; 

-- Creating L1 --

Create table L1 as select pm1.actor as actor1, count(movie) as count 
	from popular_movie_actors as pm1
	group by pm1.actor having count(pm1.movie) >= 5;



-- Creating L2 --

 Create table L2 as Select pm1.actor as actor1, pm2.actor as actor2, 
   count(pm1.movie) as count from popular_movie_actors as pm1, 
   popular_movie_actors as pm2 where pm1.actor < pm2.actor and pm1.movie = pm2.movie 
   and pm1.actor in (select actor1 from l1) 
   group by pm1.actor, pm2.actor 
   having count(pm1.movie)>=5  



-- Creating L3 --

 Create table L3 as Select pm1.actor as actor1, pm2.actor as actor2, pm3.actor as actor3,
   count(pm1.movie) from popular_movie_actors as pm1, 
   popular_movie_actors as pm2, popular_movie_actors as pm3 where pm1.actor < pm2.actor and pm2.actor < pm3.actor 
   and pm1.movie = pm2.movie and  pm2.movie = pm3.movie
   and (pm1.actor, pm2.actor) in (select actor1, actor2 from l2) 
   group by pm1.actor, pm2.actor, pm3.actor 
   having count(pm1.movie)>=5  

