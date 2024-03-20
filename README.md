Project that will eventually become a java rest api built using plain java as far as possible
At this stage I can parse http requests and make a connection to my database.If a GET request
is made to the default endpoint the api returns a list of json containing all the users in the database.
The database provider is MySQL
I am using a simple database and the sql file for it can be found in resources.
To run this application you should create your own APIConnection class which should have a MySQL
url, your username and password. Then you can run the project using your preferred build tool and
make a get request.
POST implimentastion is currently incomplete
