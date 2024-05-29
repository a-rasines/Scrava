import rsa
import hashlib
import base64
import time

# Generate public and private keys
(publickey, privatekey) = rsa.newkeys(1024)

class RSA:

    @staticmethod
    def decode(orig: str) -> str:
        encrypted_data = base64.b64decode(orig)
        decrypted_data = rsa.decrypt(encrypted_data, privatekey)
        return decrypted_data.decode('utf-8') 
    
    @staticmethod
    def encode(orig: str) -> str:
        encrypted_data = rsa.encrypt(orig.encode('utf-8'), publickey)
        return base64.b64encode(encrypted_data).decode('utf-8') 
    
    @staticmethod
    def get_public_key() -> str:
        return str(publickey)
    
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