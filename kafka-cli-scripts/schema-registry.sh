curl -X DELETE http://schema-registry:8081/subjects/car-metric-value/versions/1

curl -X DELETE http://schema-registry:8081/subjects/car-metric-key
curl -X DELETE http://schema-registry:8081/subjects/car-metric-value


curl http://schema-registry:8081/subjects/car-metric-value/versions

