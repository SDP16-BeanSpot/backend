FROM elasticsearch:8.12.0

RUN elasticsearch-plugin install analysis-nori

ENV discovery.type=single-node
ENV ES_JAVA_OPTS="-Xms512m -Xmx512m"