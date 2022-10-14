package blockchain;

import java.io.IOException;
import java.security.KeyPair;


public class Miner extends User {

    public Miner(String name, KeyPair keyPair, BlockChain blockChain) {
        super(name, keyPair, blockChain);
    }

    public void mine(BlockChain.BlockChainState blockChainState) throws IOException {
        Block block = new Block(blockChainState, this.getName());
        if (blockChain.addBlock(block).equals(AddBlockStatus.ADDED)) {
            this.money += 100;
        }
    }
}
