package blockchain;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;


public class KeysGenerator {

    private final KeyPairGenerator keyGen;

    public KeysGenerator(int keyLength) throws NoSuchAlgorithmException {
        this.keyGen = KeyPairGenerator.getInstance("RSA");
        this.keyGen.initialize(keyLength);
    }

    public KeyPair generateKeys() {
        return this.keyGen.generateKeyPair();
    }
}


