package blockchain;


import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class Block implements Serializable {

    private final int id;
    private final long timestamp;
    private final String prevHash;
    private final int magicNumber;
    private final String hash;
    private final List<Transaction> transactions;
    private final long generationTime;
    private final String minerName;

    public Block(BlockChain.BlockChainState blockChainState, String minerName) {
        this.id = blockChainState.getCurrentId() + 1;
        this.timestamp = new Date().getTime();
        this.prevHash = blockChainState.getPrevHash();
        this.transactions = blockChainState.getTransactions();
        this.minerName = minerName;
        HashGenerator hashGenerator = blockChainState.getHashGenerator();
        HashGenerator.Result result = hashGenerator.generate(this.minerName, this.id, this.timestamp, this.prevHash, this.getTransactionsAsString());
        this.magicNumber = result.getMagicNumber();
        this.hash = result.getHash();
        this.generationTime = result.getGenerateTime();
    }

    @Override
    public String toString() {
        return String.format("Block:\nCreated by: %s\n%s gets 100 VC\nId: %d\nTimestamp: %d\nMagic number: %d\nHash of the previous block:\n%s\nHash of the block:\n%s\nBlock data:%sBlock was generating for %d seconds\n",
                this.getMinerName(),
                this.getMinerName(),
                this.getId(),
                this.getTimestamp(),
                this.getMagicNumber(),
                this.getPrevHash(),
                this.getHash(),
                this.getTransactionsAsString(),
                this.getGenerationTime());
    }

    public int getId() {
        return this.id;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public String getPrevHash() {
        return this.prevHash;
    }

    public int getMagicNumber() {
        return magicNumber;
    }

    public String getHash() {
        return this.hash;
    }

    public long getGenerationTime() {
        return generationTime;
    }

    public String getMinerName() { return minerName; }

    public List<Transaction> getTransactions() { return transactions; }

    private String getTransactionsAsString() {
        if (this.transactions.size() == 0) {
            return " no messages\n";
        }
        StringBuilder sb = new StringBuilder("\n");
        this.transactions.forEach(t -> sb.append(t).append("\n"));
        return sb.toString();
    }
}
