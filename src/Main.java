package blockchain;


import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException {
        String filename = ".\\blockchain";
        try (FileOutputStream fos = new FileOutputStream(filename, false);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            int zerosCount = 0;

            BlockChain blockchain = new BlockChain(zerosCount, oos);
            KeysGenerator keysGenerator = new KeysGenerator(512);


            // Initializing miners
            Miner[] miners = IntStream.range(1, 7)
                    .mapToObj(num -> new Miner("miner" + num, keysGenerator.generateKeys(), blockchain))
                    .toArray(Miner[]::new);

            // Initializing users
            User[] users = Stream.concat(
                    Stream.of("Tom", "John", "Kate", "Ann", "Sarah", "Mary", "Kevin")
                    .map(name -> new User(name, keysGenerator.generateKeys(), blockchain)),
                    Arrays.stream(miners))

                .toArray(User[]::new);
            for (User user: users) { blockchain.addUser(user); }
//
//            System.out.println(Arrays.toString(users));
//            System.out.println(Arrays.toString(miners));

            // ThreadPool for users for making transactions
            ExecutorService usersPool = Executors.newFixedThreadPool(users.length);

            // Creating blocks
            for (int i = 0; i < 10; i++) {

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

                // submitting making transactions for all the users
                for (User user : users) {
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
            usersPool.shutdown();
            if (!usersPool.awaitTermination(20, TimeUnit.SECONDS)) {
                System.out.println("Couldn't stop users making transactions");
                usersPool.shutdownNow();
            }
            if (!blockchain.validate()) {
                System.out.println("Not valid Blockchain");
            }
        } catch (SignatureException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
