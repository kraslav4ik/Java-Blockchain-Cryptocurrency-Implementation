package blockchain;


import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;

public class Transaction {

    private final User from;
    private final User to;
    private final int amount;
    private byte[] signature;

    public Transaction(User from, User to, int amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.signature = new byte[64];
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public boolean verify() throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(from.getPublicKey());
        sig.update(this.getBytes());

        return sig.verify(signature);
    }

    @Override
    public String toString() {
        return from.getName() +
                " sent " +
                amount +
                "VC to " +
                to.getName();
    }

    public byte[] getBytes() {
        return this.toString().getBytes();
    }
}
