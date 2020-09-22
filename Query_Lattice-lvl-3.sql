Create table L3 as Select pm1.actor as actor1, pm2.actor as actor2, pm3.actor as actor3,
   count(pm1.movie) from popular_movie_actors as pm1, 
   popular_movie_actors as pm2, popular_movie_actors as pm3 where pm1.actor < pm2.actor and pm2.actor < pm3.actor 
   and pm1.movie = pm2.movie and  pm2.movie = pm3.movie
   and (pm1.actor, pm2.actor) in (select actor1, actor2 from l2) 
   group by pm1.actor, pm2.actor, pm3.actor 
   having count(pm1.movie)>=5  
