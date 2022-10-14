package blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Random;


public class HashGenerator {

    private int zerosCount;
    private boolean blockIsDeprecated;

    public HashGenerator(int zerosCount) {
        this.zerosCount = zerosCount;
    }

    class Result {

        private final int magicNumber;
        private final String hash;
        private final long generateTime;

        public Result(int magicNumber, String hash, long generateTime) {
            this.magicNumber = magicNumber;
            this.hash = hash;
            this.generateTime = generateTime;
        }

        public int getMagicNumber() {
            return magicNumber;
        }

        public String getHash() {
            return hash;
        }

        public long getGenerateTime() {
            return generateTime;
        }
    }

        public Result generate(String minerName, int id, long timestamp, String prevHash, String messages) {
        Random random = new Random();
        int magicNumber = random.nextInt();
        String hashInput = minerName + prevHash + id + timestamp + magicNumber + messages;
        String hash = applySha256(hashInput);

        long start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < zerosCount) {
            sb.append('0');
        }
        String zeros = sb.toString();

        while (!hash.startsWith(zeros)) {
            if (this.blockIsDeprecated) {
                break;
            }
            magicNumber = random.nextInt();
            hashInput = minerName + prevHash + id + timestamp + magicNumber + messages;
            hash = applySha256(hashInput);
        }
        long end = System.currentTimeMillis();
        long time = (end - start) / 1000;
        return new Result(magicNumber, hash, time);
    }

    /* Applies Sha256 to a string and returns a hash. */
    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte elem: hash) {
                String hex = Integer.toHexString(0xff & elem);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setZerosCount(int zerosCount) {
        this.zerosCount = zerosCount;
    }

    public void setBlockIsDeprecated(boolean blockIsDeprecated) {
        this.blockIsDeprecated = blockIsDeprecated;
    }
}
