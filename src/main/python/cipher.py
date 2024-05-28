import rsa
import hashlib

(publickey, privatekey) = rsa.newkeys(1024)

class RSA:

    @staticmethod
    def decode(orig: str) -> str:
        return rsa.decrypt(orig.encode(), privatekey)
    
    @staticmethod
    def get_public_key() -> str:
        return publickey
    
class SHA_256:

    @staticmethod
    def encode(orig: str) -> str:
        hash = hashlib.sha256()
        hash.update(orig.encode('utf-8'))
        return hash.hexdigest()


