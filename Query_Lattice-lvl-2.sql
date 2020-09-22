Create table L2 as Select pm1.actor as actor1, pm2.actor as actor2, 
   count(pm1.movie) as count from popular_movie_actors as pm1, 
   popular_movie_actors as pm2 where pm1.actor < pm2.actor and pm1.movie = pm2.movie 
   and pm1.actor in (select actor1 from l1) 
   group by pm1.actor, pm2.actor 
   having count(pm1.movie)>=5  
