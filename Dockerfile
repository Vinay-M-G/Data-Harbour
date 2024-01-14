FROM public.ecr.aws/amazoncorretto/amazoncorretto:17
EXPOSE 1000
ADD target/Data-Harbour-0.0.1-SNAPSHOT.jar Data-Harbour-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/Data-Harbour-0.0.1-SNAPSHOT.jar"]