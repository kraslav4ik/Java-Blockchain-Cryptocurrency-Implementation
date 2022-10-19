package blockchain;


import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.io.*;


public class BlockChain implements Serializable {
    private final List<Block> blocks;
    private volatile String prevHash;
    private int currentId;
    private int zerosCount;
    private volatile List<Transaction> transactions = new ArrayList<>();
    private final transient HashGenerator hashGenerator;
    private final List<User> users;
    private final transient ObjectOutputStream oos;
    private String lastBlockInfo;

    public BlockChain(int zerosCount, ObjectOutputStream oos) {
        this.currentId = 0;
        this.prevHash = "0";
        this.blocks = new ArrayList<>();
        this.zerosCount = zerosCount;
        this.users = new ArrayList<>();
        this.hashGenerator = new HashGenerator(zerosCount);
        this.oos = oos;
    }

    synchronized public AddBlockStatus addBlock(Block block) throws IOException {
        if (!this.prevHash.equals(block.getPrevHash())) {
            return AddBlockStatus.NOT_ADDED;
        }
        this.hashGenerator.setBlockIsDeprecated(true);
        this.prevHash = block.getHash();
        ++this.currentId;
        this.blocks.add(block);
        this.updateZeros();
        this.oos.writeObject(this);
        return AddBlockStatus.ADDED;
    }

    public boolean validate() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        List<Block> blocks = this.blocks;
        String prevHash = "0";
        for (Block block: blocks) {
            if (!block.getPrevHash().equals(prevHash)) {
                return false;
            }
            for (Transaction t: block.getTransactions()) {
                if (!t.verify()) {
                    return false;
                }
            }
            prevHash = block.getHash();
        }
        return true;
    }

    public String getLastBlockInfo() {
        return this.lastBlockInfo;
    }


    private void updateZeros() {
        Block lastBlock = this.blocks.get(this.currentId - 1);
        this.lastBlockInfo = lastBlock.toHTML();
        long timeForLastBlock = lastBlock.getGenerationTime();
        if (timeForLastBlock  < 1) {
            this.zerosCount++;
            this.hashGenerator.setZerosCount(this.zerosCount);
            this.lastBlockInfo += String.format("Number of hash leading zeros was increased to %d</html>", this.zerosCount);
            return;
        }
        if (timeForLastBlock  > 10) {
            this.lastBlockInfo += "Number of hash leading zeros was decreased by 1</html>";
            this.zerosCount--;
            this.hashGenerator.setZerosCount(this.zerosCount);
            return;
        }
        this.lastBlockInfo += "Number of hash leading zeros stays the same\n\n</html>";
    }

    static class BlockChainState {

        private final int currentId;
        private final String prevHash;
        private final List<Transaction> transactions;
        private final HashGenerator hashGenerator;

        private BlockChainState(int currentId, String prevHash, List<Transaction> transactions, HashGenerator hashGenerator) {
            this.currentId = currentId;
            this.prevHash = prevHash;
            this.transactions = transactions;
            this.hashGenerator = hashGenerator;
        }

        public List<Transaction> getTransactions() {
            return transactions;
        }

        public int getCurrentId() {
            return currentId;
        }

        public String getPrevHash() {
            return prevHash;
        }

        public HashGenerator getHashGenerator() {
            return hashGenerator;
        }
    }

    public synchronized BlockChainState getBlockChainState() {

        BlockChainState state = new BlockChainState(this.currentId, this.prevHash, this.transactions, this.hashGenerator);
        this.transactions = new ArrayList<>();
        this.hashGenerator.setBlockIsDeprecated(false);
        return state;
    }

    synchronized public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }

    public synchronized List<Transaction> getTransactions() {
        return transactions;
    }
}
