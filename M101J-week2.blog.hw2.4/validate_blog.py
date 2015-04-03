
import pymongo
import urllib2
import urllib
import cookielib
import random
import re
import string

# makes a little salt
def make_salt(n):
    salt = ""
    for i in range(n):
        salt = salt + random.choice(string.ascii_letters)
    return salt


# this is a validation program to make sure that the blog works correctly.

def create_user(username, password):
    try:
        print "Trying to create a test user ", username
        cj = cookielib.CookieJar()
        url = "http://localhost:8082/signup"

        data = urllib.urlencode([("email",""),("username",username), ("password",password), ("verify",password)])
        request = urllib2.Request(url=url, data=data)
        opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cj))
        f = opener.open(request)

        # check that the user is in the user table
        connection = pymongo.Connection("mongodb://localhost", safe=True)
        db = connection.blog
        users = db.users
        user = users.find_one({'_id':username})
        if (user == None):
            print "Could not find the test user ", username, "in the users collection."
            return False
        print "Found the test user ", username, " in the users collection"

        # check that the user has been built
        result = f.read()
        expr = re.compile("Welcome\s+"+ username)
        if expr.search(result):
            return True
        
        print "When we tried to create a user, here is the output we got\n"
        print result
        
        return False
    except:
        print "the request to ", url, " failed, so your blog may not be running."
        return False


def try_to_login(username, password):

    try:
        print "Trying to login for test user ", username
        cj = cookielib.CookieJar()
        url = "http://localhost:8082/login"

        data = urllib.urlencode([("username",username), ("password",password)])
        request = urllib2.Request(url=url, data=data)
        opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cj))
        f = opener.open(request)

        # check for successful login
        result = f.read()
        expr = re.compile("Welcome\s+"+ username)
        if expr.search(result):
            return True

        print "When we tried to login, here is the output we got\n"
        print result
        return False
    except:
        print "the request to ", url, " failed, so your blog may not be running."
        raise
        return False


username = make_salt(7)
password = make_salt(8)

# try to create user

if (create_user(username, password)):
    print "User creation successful. "
    # try to login
    if (try_to_login(username, password)):
        print "User login successful."
        print "Validation Code is ", "fhj837hf9376hgf93hf832jf9"
    else:
        print "User login failed"
        print "Sorry, you have not solved it yet."

else:
    print "Sorry, you have not solved it yet."








