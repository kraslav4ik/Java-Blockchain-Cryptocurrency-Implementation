package blockchain;

import java.security.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class User {

    protected final String name;
    protected int money;
    protected final PrivateKey privateKey;
    protected final PublicKey publicKey;
    protected final BlockChain blockChain;

    public User(String name, KeyPair keyPair, BlockChain blockChain, int money) {
        this.name = name;
        this.money = money;
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
        this.blockChain = blockChain;
    }

    protected void sign(Transaction transaction) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
        Signature rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(this.privateKey);
        rsa.update(transaction.getBytes());
        transaction.setSignature(rsa.sign());
    }

    public void makeTransaction(User to, int amount) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        Transaction transaction = new Transaction(this, to, amount);
        this.sign(transaction);
        if (transaction.verify()) {
            this.sentMoney(amount);
            to.getMoney(amount);
            this.blockChain.addTransaction(transaction);
        }
    }

    public void work() throws InterruptedException, SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        int maxTransactions = 2;
        Random random = new Random();
        int transactionsAmount = random.nextInt(maxTransactions);
        for (int i = 0; i < transactionsAmount; i++) {
            if (this.money <= 0) {
                Thread.sleep(5000);
                continue;
            }
            if (this.blockChain.getTransactions().size() == 10) { break; }
            int transactionSum = random.nextInt(this.money / 2) + 1;
            List<User> users = new ArrayList<>(this.blockChain.getUsers());
            users.remove(this);
            User to = users.get(random.nextInt(users.size()));
            this.makeTransaction(to, transactionSum);
        }
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public String getName() {
        return name;
    }

    protected void sentMoney(int amount) {
        this.money -= amount;
    }

    public void getMoney(int amount) {
        this.money += amount;
    }

    @Override
    public String toString() {
        return this.name + " has " + this.money + "\nHer/his PublicKey: " + this.publicKey;
    }
}
