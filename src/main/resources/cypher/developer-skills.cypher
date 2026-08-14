MATCH (d:Developer {name: $developerName})
      -[:HAS_SKILL]->(s:Skill)
RETURN s.name AS skill
ORDER BY s.name;