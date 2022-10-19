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
    private final Miner miner;

    public Block(BlockChain.BlockChainState blockChainState, Miner miner) {
        this.id = blockChainState.getCurrentId() + 1;
        this.timestamp = new Date().getTime();
        this.prevHash = blockChainState.getPrevHash();
        this.transactions = blockChainState.getTransactions();
        this.miner = miner;
        HashGenerator hashGenerator = blockChainState.getHashGenerator();
        HashGenerator.Result result = hashGenerator.generate(this.miner.getName(), this.id, this.timestamp, this.prevHash, this.getTransactionsAsString());
        this.magicNumber = result.getMagicNumber();
        this.hash = result.getHash();
        this.generationTime = result.getGenerateTime();
    }

    @Override
    public String toString() {
        return String.format("Block:\nCreated by: %s\n%s gets %d VC\nId: %d\nTimestamp: %d\nMagic number: %d\nHash of the previous block:\n%s\nHash of the block:\n%s\nBlock data:%sBlock was generating for %d seconds\n",
                this.miner.getName(),
                this.miner.getName(),
                this.miner.getAwardForMining(),
                this.getId(),
                this.getTimestamp(),
                this.getMagicNumber(),
                this.getPrevHash(),
                this.getHash(),
                this.getTransactionsAsString(),
                this.getGenerationTime());
    }

    public String toHTML() {
        return String.format("<html>Block:<br>Created by: %s<br>%s gets %d VC<br>Id: %d<br>Timestamp: %d<br>Magic number: %d<br>Hash of the previous block:<br>%s<br>Hash of the block:<br>%s<br>Block data:%sBlock was generating for %d seconds<br>",
                this.miner.getName(),
                this.miner.getName(),
                this.miner.getAwardForMining(),
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

    public List<Transaction> getTransactions() { return transactions; }

    private String getTransactionsAsString() {
        if (this.transactions.size() == 0) {
            return " no messages<br>";
        }
        StringBuilder sb = new StringBuilder("<br>");
        this.transactions.forEach(t -> sb.append(t).append("<br>"));
        return sb.toString();
    }
}
