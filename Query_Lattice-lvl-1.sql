Create table L1 as select pm1.actor as actor1, count(movie) as count 
	from popular_movie_actors as pm1
	group by pm1.actor having count(pm1.movie) >= 5;

