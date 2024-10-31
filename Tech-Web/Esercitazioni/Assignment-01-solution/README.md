# Assignment #01 - HTML

## Exercise 2
A possible solution to this exercise is reported in the `./website` directory.

## Exercise 4
Two alternative solutions are reported in the solution for Exercise 4. The first solution uses NGINX to deploy the website in the `./website` directory, while the second uses Apache HTTPd.

The first solution is available in `Dockerfile.nginx`, and is mostly a repetition of the instructions already provided in the Assignment. Differently from the basic solution provided in the Assignment, in this solution we use a non-standard directory as the document root. Indeed, the website is copied to the `/var/www/book-of-programming` directory in the container. To make the website work, we need to use a custom configuration for the webserver, specifying that we're using a different root directory. We do that in the `nginx.conf` file, which is also copied in the container (see `Dockerfile.nginx`). You can build the custom Dockerfile using the following command in the same directory where the Dockerfile is.

```
docker build -t book-of-programming-nginx-image -f .\Dockerfile.nginx .
```

The second solution is a mere replication of the NGINX solution provided in the assignment, and shows how to use the HTTPd container. To build the image for the HTTPd version of the solution, run the following command in the directory containing the Dockerfiles.

```
docker build -t book-of-programming-httpd-image -f .\Dockerfile.httpd .
```

To run the images you built, you can use, respectively, the commands
```
docker run --name book-of-programming-nginx-container -p 80:80 -d book-of-programming-nginx-image
```
and
```
docker run --name book-of-programming-httpd-container -p 80:80 -d book-of-programming-httpd-image
```

Note that only one container can be bound to port 80 in the host, so you will have to terminate one container before starting the other one (or you can specify a different port for one of the containers).