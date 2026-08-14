MATCH (target:Skill {name: $skillName})
      <-[:HAS_SKILL]-
      (developer:Developer)
      -[:HAS_SKILL]->
      (related:Skill)

WHERE related <> target

RETURN related.name AS skill,
       count(DISTINCT developer) AS developers
ORDER BY developers DESC, skill;