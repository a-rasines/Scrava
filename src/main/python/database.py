from mysql.connector.errors import IntegrityError
import mysql.connector
import generated.service_pb2 as pb2
from cipher import SHA_256
from datetime import datetime, timedelta
import threading
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
        args[i] = "'" + args[i].replace("\\", "\\\\").replace("'", "''") + "'"
    if(len(args) == 1):
        return args[0]
    return tuple(args)

def to_sql_string(str:str) -> str:
    return str.replace("\\", "\\\\").replace("'", "''")

def get_last_id() -> int:
    mycursor = mydb.cursor()
    mycursor.execute("SELECT LAST_INSERT_ID()")
    id = mycursor.fetchone()[0]
    mycursor.close()
    return id

def check_user(username:str, pw:str) -> pb2.ClientData:
    username = to_sql_strings(username)
    mycursor = mydb.cursor()
    _pw = to_sql_strings(SHA_256.encode(pw))
    mycursor.execute(f"SELECT id, name FROM User WHERE name={username} AND password={_pw}")
    res = mycursor.fetchone()
    if res != None:
        token = SHA_256.generate_token(pw)
        xpdate = to_sql_strings((datetime.now() + timedelta(days = 3)).strftime('%Y-%m-%d %H:%M:%S'))
        mycursor = mydb.cursor()
        _token = to_sql_strings(token)
        mycursor.execute(f"INSERT INTO Token(token, owner, expiration) VALUES ({_token}, {res[0]}, {xpdate})")
        mydb.commit()
        mycursor.close()
        return pb2.ClientData(id = res[0], name=res[1], token=token)
    mycursor.close()
    return pb2.ClientData(id = -1)

def check_token(token:str, uid:int) -> bool:
    token = to_sql_strings(token)
    mycursor = mydb.cursor()
    mycursor.execute(f"SELECT token FROM Token WHERE token={token} AND owner={uid}")
    output = mycursor.fetchone() != None
    mycursor.close()
    return output

def _delete(table:str, conditions:str) -> bool:
    mycursor = mydb.cursor()
    try:
        mycursor.execute(f"DELETE FROM {table} WHERE {conditions}")
        mydb.commit()
        output = mycursor.rowcount == 1
        mycursor.close()
        return output
    except IntegrityError:
        mycursor.close()
        return False

def create_user(username:str, pw:str) -> bool:
    username, pw = to_sql_strings(username, pw)
    mycursor = mydb.cursor()
    try:
        mycursor.execute(f"INSERT INTO User(name, password) VALUES ({username}, {pw})")
        mydb.commit()
        output = mycursor.rowcount == 1
        mycursor.close()
    except IntegrityError:
        mycursor.close()
        return False

def create_tutorial(author:int, name:str, content:str) -> bool:
    name, content = to_sql_strings(name, content)
    mycursor = mydb.cursor()
    try:
        mycursor.execute(f"INSERT INTO Tutorial(author, name, content) VALUES ({author}, {name}, {content})")
        mydb.commit()
        output = mycursor.rowcount == 1
        mycursor.close()
        return output
    except IntegrityError:
        mycursor.close()
        return False

def save_project(p_id: int, author:int, name:str, content:str) -> bool:
    name, content = to_sql_strings(name, content)
    mycursor = mydb.cursor()
    try:
        print(f"id={p_id}, author={author}")
        mycursor.execute(f"SELECT * FROM Project WHERE id = {p_id} AND author = {author}")
        mydb.commit()
        if(mycursor.fetchone() != None):
            mycursor.execute(f"UPDATE Project SET name= {name}, content={content} WHERE id = {p_id} AND author = {author}")
            mydb.commit()
        else:
            mycursor.execute(f"INSERT INTO Project(author, name, content) VALUES ({author}, {name}, {content})")
            mydb.commit()
            output = mycursor.rowcount == 1
            print(output)
            mycursor.close()
            return output
        return True
    except IntegrityError:
        mycursor.close()
        return False
def save_tutorial(p_id: int, author:int, name:str, content:str) -> bool:
    name, content = to_sql_strings(name, content)
    mycursor = mydb.cursor()
    try:
        mycursor.execute(f"UPDATE Tutorial SET name= {name}, content={content} WHERE id = {p_id} AND author = {author}")
        mydb.commit()
        if mycursor.rowcount == 0:
            mycursor.execute(f"INSERT INTO Tutorial(author, name, content) VALUES ({author}, {name}, {content})")
            mydb.commit()
            output = mycursor.rowcount == 1
            mycursor.close()
            return output
        return True
    except IntegrityError:
        mycursor.close()
        return False

def delete_token(owner:int, token:str) -> bool:
    token = to_sql_strings(token)
    return _delete("Token", f"owner = {owner} AND token = {token}")
"""     token = to_sql_strings(token)
    mycursor = mydb.cursor()
    try:
        mycursor.execute(f"DELETE FROM Token WHERE owner = {owner} AND token = {token}")
        mydb.commit()
        output = mycursor.rowcount == 1
        mycursor.close()
        return output
    except IntegrityError:
        mycursor.close()
        return False """
def delete_project(owner: int, id: int) -> bool:
    return _delete("Project", "owner = " + str(owner) + " AND id = " + str(id))

def check_sqlinj(input:str):
    is_str = 0
    input = list(input)
    for char in range(len(input)):
        if input[char] == ';' and is_str == 0:
           return True
        elif input[char] == '"' and input[char - 1] != '\\':
            if is_str == 0: is_str = 1
            elif is_str == 1: is_str = 0
        elif input[char] == "'" and input[char - 1] != '\\':
            if is_str == 0: is_str = 2
            elif is_str == 2: is_str = 0
    return False

def search_projects(offset:int, query: str) -> list[tuple[int, str]]:
    mycursor = mydb.cursor()
    if(check_sqlinj(query)):
        return []
    mycursor.execute(f"SELECT id, name FROM Project WHERE {query} LIMIT 30 OFFSET {offset}")
    output = mycursor.fetchall()
    mycursor.close()
    return output

def search_tutorial(offset:int, query: str) -> list[tuple[int, str]]:
    mycursor = mydb.cursor()
    if(check_sqlinj(query)):
        return []
    mycursor.execute(f"SELECT id, name FROM Tutorial WHERE {query} LIMIT 30 OFFSET {offset}")
    output = mycursor.fetchall()
    mycursor.close()
    return output

def get_project(id: int) -> pb2.SerializedObject:
    mycursor = mydb.cursor()
    mycursor.execute(f"SELECT name, content FROM Project WHERE id = {id} LIMIT 1")
    res = list(mycursor.fetchone())
    mycursor.close()
    if res == None:
        return None
    return pb2.SerializedObject(id=id, name=res[0], obj = res[1])

def get_tutorial(id: int) -> pb2.SerializedObject:
    mycursor = mydb.cursor()
    mycursor.execute(f"SELECT name, content FROM Tutorial WHERE id = {id} LIMIT 1")
    res = list(mycursor.fetchone())
    if res == None: 
        mycursor.close()
        return None
    mycursor.close()
    return pb2.SerializedObject(id=id, name=res[0], obj = res[1])

def _delete_tokens_thread():
    mycursor = mydb.cursor()
    a = True
    while a:
        print("Deleting expired tokens")
        now = to_sql_strings((datetime.now()).strftime('%Y-%m-%d %H:%M:%S'))
        mycursor.execute(f"DELETE FROM Token WHERE expiration < {now}")
        mydb.commit()
        time.sleep(300)
        for i in threading.enumerate():
            if i.name == "MainThread" and not i.is_alive():
                a = False #Stop zombie thread

    print("exit")

def delete_tokens_thread():
    while not mydb.is_connected():
        print("a")
        time.sleep(5)
    _dtt = Thread(target=_delete_tokens_thread, args = [])
    _dtt.start()

_dtt = Thread(target=delete_tokens_thread, args = [])
_dtt.start()

if __name__ == "__main__":
    print(create_user("test", SHA_256.encode("test")))
    print(check_user("test", SHA_256.encode("test")))