## Docker

- To build `docker build -t booking-services ./runner`
- To run/test locally `docker run -p 8080:8080 -e DB_PASSWORD=[] -e DB_URL=[] -e DB_USERNAME=[] -e SPRING_PROFILES_ACTIVE=dev booking-services`
- Supabase has different connect strings, some don't support IPv4 connection and will lead to an error with docker