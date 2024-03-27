Project that will eventually become a java rest api built using plain java as far as possible.
At this stage I can parse http requests and make a connection to my database.
Get requests can be made of the users using /user and users can be queried based on attributes by using /user/{attribute}/{value}
Post requests can be made to the user table, for now this only supports posting a json user in the form {"username": "{value}"}
The database provider is MySQL
I am using a simple database and the sql file for it can be found in resources.
To run this application you should create your own APIConnection class which should have a MySQL
url, your username and password. Then you can run the project using your preferred build tool and
make a request.
