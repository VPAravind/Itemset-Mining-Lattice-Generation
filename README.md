Data Source: https://datasets.imdbws.com/

Note: Preprocessing, data cleaning and data integration were done prior to item-set mining. The database used was Postgres. 
The function dependencis directory generates the functional dependencies through a naive approach. The functional dependencies report attached to it give the canonical cover
required to normalize the dataset. 

# Itemset-Mining-Lattice-Generation

Read report: Itemset-mining-lattice-generation.pdf

Implementation of the apiori algorithm without pruning, on the IMDB dataset where a new SQL table is created for each lattice level. The actors are considered to be items for itemset mining while the transactions are the movies in which actors have worked together. 
