SELECT DISTINCT m.name, m.id FROM member AS m join L6 AS l 
ON l.actor1 = m.id OR l.actor2 = m.id OR l.actor3 = m.id OR l.actor4 = m.id OR l.actor5 = m.id OR l.actor6 = m.id;