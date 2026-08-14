MATCH path =
    (start:Skill {name: $skillName})
    -[:PREREQUISITE_OF*1..3]->(next:Skill)

RETURN next.name AS skill,
       min(length(path)) AS distance
ORDER BY distance;