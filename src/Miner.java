package blockchain;

import java.io.IOException;
import java.security.KeyPair;


public class Miner extends User {
    private final int awardForMining;
    public Miner(String name, KeyPair keyPair, BlockChain blockChain, int money) {
        super(name, keyPair, blockChain, money);
        this.awardForMining = money;
    }

    public void mine(BlockChain.BlockChainState blockChainState) throws IOException {
        Block block = new Block(blockChainState, this);
        if (blockChain.addBlock(block).equals(AddBlockStatus.ADDED)) {
            this.money += this.awardForMining;
        }
    }

    public int getAwardForMining() {
        return awardForMining;
    }
}
