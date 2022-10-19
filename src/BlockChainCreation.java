package blockchain;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BlockChainCreation {


    static void createBlockChain(InitFrame.ParsedInput input, BlockChainFrame blockChainFrame) {
        String filename = ".\\data\\blockchain";
        try (FileOutputStream fos = new FileOutputStream(filename, false);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            int zerosCount = 0;
            BlockChain blockchain = new BlockChain(zerosCount, oos);
            KeysGenerator keysGenerator = new KeysGenerator(512);

            int blocksAmount = input.getBlocksNum();
            int minersAmount = input.getMinersNum();
            int usersAmount = input.getUsersNum();
            int moneyPerUser = input.getMoneyPerUser();
            boolean minersAsUsers = input.isMinersAsUsers();

            // Initializing miners
            Miner[] miners = IntStream.rangeClosed(1, minersAmount)
                    .mapToObj(num -> new Miner("miner" + num, keysGenerator.generateKeys(), blockchain, moneyPerUser))
                    .toArray(Miner[]::new);

            // Initializing users
            User[] users = Stream.of("Tom", "John", "Kate", "Ann", "Sarah", "Mary", "Kevin", "Jack", "Terry", "Emily")
                    .limit(usersAmount)
                    .map(name -> new User(name, keysGenerator.generateKeys(), blockchain, moneyPerUser))
                    .toArray(User[]::new);
            if (minersAsUsers) {
                users = Stream.concat(Arrays.stream(users), Arrays.stream(miners)).toArray(User[]::new);
            }
            for (User user: users) { blockchain.addUser(user); }

            // ThreadPool for users for making transactions
            ExecutorService usersPool = Executors.newFixedThreadPool(users.length);

            // Creating blocks
            for (int i = 0; i < blocksAmount; i++) {

                // ThreadPool for miners
                ExecutorService minersPool = Executors.newFixedThreadPool(miners.length);
                BlockChain.BlockChainState state = blockchain.getBlockChainState();
                for (Miner miner : miners) {
                    minersPool.submit(() -> {
                        try {
                            miner.mine(state);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                minersPool.shutdown();
                if (!minersPool.awaitTermination(1200, TimeUnit.SECONDS)) {
                    System.out.println("Time limit for generating block has been reached");
                    minersPool.shutdownNow();
                    break;
                }
                blockChainFrame.addBlockToFrame(blockchain.getLastBlockInfo());


                // submitting making transactions for all the users
                for (User user : users) {
                    if (i == blocksAmount - 1) {
                        usersPool.shutdown();
                        if (!usersPool.awaitTermination(20, TimeUnit.SECONDS)) {
                            System.out.println("Couldn't stop users making transactions");
                            usersPool.shutdownNow();
                        }
                        break;
                    }
                    usersPool.submit(() -> {
                        try {
                            user.work();
                        } catch (InterruptedException | SignatureException | NoSuchAlgorithmException |
                                 InvalidKeyException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }

            if (!blockchain.validate()) {
                System.out.println("Not valid Blockchain");
            }
        } catch (SignatureException | InvalidKeyException | NoSuchAlgorithmException | InterruptedException |
                 IOException  e) {
            throw new RuntimeException(e);
        }
    }
}
