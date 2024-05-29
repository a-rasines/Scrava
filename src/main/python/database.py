from mysql.connector.errors import IntegrityError
import mysql.connector
import generated.service_pb2 as pb2
from cipher import SHA_256
from datetime import datetime, timedelta
from threading import Thread
import time

mydb = mysql.connector.connect(
  host="localhost",
  user="snJzZHH0mNgTRhrB61ZZQfqjBAA61LOU", # /!\ DO NOT USE THIS CREDENTIALS FOR THE DATABASE AS THIS REPO IS PUBLIC /!\ #
  password="226cfaffa340c20bb2d76359b6f86f02effdbaf99b52905bdfb03a94224a6a3335dbdf2951650f86ebfbb809ae298a0914b073b3633d1e18cfe023c1f0c3485b",
  database="scrava"
)

def to_sql_strings(*args) -> str:
    args = list(args)
    for i in range(len(args)):
        args[i] = "'" + args[i].replace("'", "''") + "'"
    if(len(args) == 1):
        return args[0]
    return tuple(args)

def to_sql_string(str:str) -> str:
    return str.replace("'", "''")

def check_user(username:str, pw:str) -> bool:
    username, pw = to_sql_strings(username, pw)
    mycursor = mydb.cursor()
    mycursor.execute(f"SELECT id FROM User WHERE name={username} AND password={pw}")
    for x in mycursor:
        return x[0]
    return -1

def get_user(id:int, pw:str) -> pb2.ClientData:
    mycursor = mydb.cursor()
    _pw = to_sql_strings(SHA_256.encode(pw))
    mycursor.execute(f"SELECT name from User WHERE id={id} AND password={_pw} LIMIT 1")
    res = mycursor.fetchone()
    if(res == None):
        return pb2.ClientData()
    token = SHA_256.generate_token(pw)
    output = pb2.ClientData(id=id, name=res[0], token=token)

    token = to_sql_strings(token)
    xpdate = to_sql_strings((datetime.now() + timedelta(days = 3)).strftime('%Y-%m-%d %H:%M:%S'))
    mycursor = mydb.cursor()
    mycursor.execute(f"INSERT INTO Token(token, owner, expiration) VALUES ({token}, {id}, {xpdate})")
    mydb.commit()
    return output

def create_user(username:str, pw:str) -> bool:
    username, pw = to_sql_strings(username, pw)
    mycursor = mydb.cursor()
    try:
        mycursor.execute(f"INSERT INTO User(name, password) VALUES ({username}, {pw})")
        mydb.commit()
        return mycursor.rowcount == 1
    except IntegrityError:
        return False

def create_tutorial(author:int, name:str, content:str) -> bool:
    name, content = to_sql_strings(name, content)
    mycursor = mydb.cursor()
    try:
        mycursor.execute(f"INSERT INTO Tutorial(author, name, content) VALUES ({author}, {name}, {content})")
        mydb.commit()
        return mycursor.rowcount == 1
    except IntegrityError:
        return False

def create_project(author:int, name:str, content:str) -> bool:
    name, content = to_sql_strings(name, content)
    mycursor = mydb.cursor()
    try:
        mycursor.execute(f"INSERT INTO Project(author, name, content) VALUES ({author}, {name}, {content})")
        mydb.commit()
        return mycursor.rowcount == 1
    except IntegrityError:
        return False
    
def search_projects(offset:int, query: str) -> list[tuple[int, str]]:
    mycursor = mydb.cursor()
    query = to_sql_string(query)
    mycursor.execute(f"SELECT id, name FROM Project WHERE name LIKE '%{query}%' LIMIT 30 OFFSET {offset}")
    return mycursor.fetchall()

def search_tutorial(offset:int, query: str) -> list[tuple[int, str]]:
    mycursor = mydb.cursor()
    query = to_sql_string(query)
    mycursor.execute(f"SELECT id, name FROM Tutorial WHERE name LIKE '%{query}%' LIMIT 30 OFFSET {offset}")
    return mycursor.fetchall()

def get_project(id: int) -> pb2.SerializedObject:
    mycursor = mydb.cursor()
    mycursor.execute(f"SELECT name, content FROM Project WHERE id = {id} LIMIT 1")
    if mycursor.rowcount == 0: return None
    res = list(mycursor.fetchone())
    return pb2.SerializedObject(id=id, name=res[0], obj = res[1])

def get_tutorial(id: int) -> pb2.SerializedObject:
    mycursor = mydb.cursor()
    mycursor.execute(f"SELECT name, content FROM Tutorial WHERE id = {id} LIMIT 1")
    if mycursor.rowcount == 0: return None
    res = list(mycursor.fetchone())
    return pb2.SerializedObject(id=id, name=res[0], obj = res[1])

def _delete_tokens_thread():
    mycursor = mydb.cursor()
    while True:
        print("Deleting expired tokens")
        now = to_sql_strings((datetime.now()).strftime('%Y-%m-%d %H:%M:%S'))
        mycursor.execute(f"DELETE FROM Token WHERE expiration < {now}")
        mydb.commit()
        time.sleep(300)
    print("exit")

def delete_tokens_thread():
    while not mydb.is_connected():
        print("a")
        time.sleep(5)
    _dtt = Thread(target=_delete_tokens_thread, args = [])
    _dtt.start()

_dtt = Thread(target=delete_tokens_thread, args = [])
_dtt.start()
print("b")

if __name__ == "__main__":
    print(create_user("test", SHA_256.encode("test")))
    print(check_user("test", SHA_256.encode("test")))
    print(get_user(1, "test"))