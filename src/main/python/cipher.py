from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_v1_5
import hashlib
import base64
import time

# Generate public and private keys
keypair = RSA.generate(1024)
cipher = PKCS1_v1_5.new(keypair)

class RSA:
    @staticmethod
    def decode(orig: str) -> str:
        return cipher.decrypt(base64.b64decode(orig), None).decode('utf-8') 
    
    @staticmethod
    def get_public_key() -> str:
        return keypair.publickey().export_key(format='DER')
    
class SHA_256:

    @staticmethod
    def encode(orig: str) -> str:
        hash_obj = hashlib.sha256()
        hash_obj.update(orig.encode('utf-8'))
        return hash_obj.hexdigest()
    
    @staticmethod
    def generate_token(orig:str):
        return SHA_256.encode(orig + SHA_256.encode(orig + str(time.time())))

if __name__=="__main__":
    test = RSA.encode("test")
    print(f"Encoded: {test}")
    test = RSA.decode(test)
    print(f"Decoded: {test}")
    test = SHA_256.encode(test)
    print(f"SHA-256: {test}")
    test = SHA_256.generate_token("test")
    print(f"Token: {test}")