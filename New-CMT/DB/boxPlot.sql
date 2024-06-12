SELECT
    c.date_day,
    TO_CHAR(c.date_time,'HH24') hour,
    machine_name,
    MIN(c.cpu_util) min_,
    PERCENTILE_DISC(.25) WITHIN GROUP(ORDER BY c.cpu_util) lower_,
    PERCENTILE_DISC(.5) WITHIN GROUP(ORDER BY c.cpu_util) median_,
    PERCENTILE_DISC(.75) WITHIN GROUP(ORDER BY c.cpu_util) upper_,
    MAX(c.cpu_util) max_
FROM
    cpmon_cpu c
WHERE
    date_day >= TO_DATE('01-10-2018','dd-mm-yyyy')
    AND   date_day < TO_DATE('02-10-2018','dd-mm-yyyy')
GROUP BY
    date_day,
    TO_CHAR(date_time,'HH24'),
    machine_name