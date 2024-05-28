from mysql.connector.errors import IntegrityError
import mysql.connector
from cipher import SHA_256

mydb = mysql.connector.connect(
  host="localhost",
  user="snJzZHH0mNgTRhrB61ZZQfqjBAA61LOU", # /!\ DO NOT USE THIS CREDENTIALS FOR THE DATABASE AS THIS REPO IS PUBLIC /!\ #
  password="226cfaffa340c20bb2d76359b6f86f02effdbaf99b52905bdfb03a94224a6a3335dbdf2951650f86ebfbb809ae298a0914b073b3633d1e18cfe023c1f0c3485b",
  database="scrava"
)

def to_sql_string(*args) -> str:
    args = list(args)
    for i in range(len(args)):
        args[i] = "'" + args[i].replace("'", "''") + "'"
    return tuple(args)

def check_user(username:str, pw:str) -> bool:
    username, pw = to_sql_string(username, pw)
    mycursor = mydb.cursor()
    mycursor.execute(f"SELECT id FROM User WHERE name={username} AND password={pw}")
    for x in mycursor:
        return x[0]
    return -1


def create_user(username:str, pw:str) -> bool:
    username, pw = to_sql_string(username, pw)
    mycursor = mydb.cursor()
    try:
        mycursor.execute(f"INSERT INTO User(name, password) VALUES ({username}, {pw})")
        mydb.commit()
        return mycursor.rowcount == 1
    except IntegrityError:
        return False

def create_tutorial(author:int, name:str, content:str) -> bool:
    name, content = to_sql_string(name, content)
    mycursor = mydb.cursor()
    try:
        mycursor.execute(f"INSERT INTO Tutorial(author, name, content) VALUES ({author}, {name}, {content})")
        mydb.commit()
        return mycursor.rowcount == 1
    except IntegrityError:
        return False

def create_project(author:int, name:str, content:str) -> bool:
    name, content = to_sql_string(name, content)
    mycursor = mydb.cursor()
    try:
        mycursor.execute(f"INSERT INTO Project(author, name, content) VALUES ({author}, {name}, {content})")
        mydb.commit()
        return mycursor.rowcount == 1
    except IntegrityError:
        return False

if __name__ == "__main__":
    print(check_user("test", SHA_256.encode("test")))