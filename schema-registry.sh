curl -X DELETE http://schema-registry:8081/subjects/measurement-value/versions/1

curl -X DELETE http://schema-registry:8081/subjects/measurement-key
curl -X DELETE http://schema-registry:8081/subjects/measurement-value


curl http://schema-registry:8081/subjects/measurements-value/versions

