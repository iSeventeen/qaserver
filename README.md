#Setting up qaserver project

The qaserver project currently runs on Scala 2.10.1 and Play 2.1.4

##Installing Scala and Play

###On Mac

Update brew

	brew --prefix

	cd /usr/local
	git fetch origin
	git reset --hard origin/master

	brew update
	brew upgrade

Install Scala and Play using brew

	brew install scala
	brew install play


###Clone the repository using git

	git clone git@github.com:lvxineye/qaserver.git

###Install local Postgres database

First make sure you can log in to your local database. If this fails, update the pg-hba.conf file.
For each of these commands:
If necessary, use "sudo -u postgres" to issue the command as postgres user.
If necessary, use "-U postgres" to issue the command as postgres user.
If necessary, include "-h localhost".

	psql -U _postgres postgres
	sudo -u _postgres psql postgres

Create qaserver database.

	ceatedb -U _postgres qaserver
	sudo -u _postgres createdb qaserver

Create qaserver user. Set password to qaserver. You can say no to all the options.

	createuser -U postgres -P oc123oc123
	sudo -u _postgres createuser -P oc123oc123

###Set configuration for local server to use local postgres database

Uncomment these lines in applicaion.conf

	db.default.url="jdbc:postgresql://localhost/qaserver"
	db.default.user=qaserver
	db.default.password="oc123oc123"

###Compile using Play

	cd qaserver
	play compile

###Run using Play

	play run
