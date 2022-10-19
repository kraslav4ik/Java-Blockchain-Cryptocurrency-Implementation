# Java BlockChain for cryptocurrency transactions

Application for visualization of how virtual currencies work.

Here you're able to create your pool of users, your own miners, which will interact with each other, making transactions and creating new blocks!

In this program, `multithreading` is using for implementing users and miners activities.

![result](https://github.com/kraslav4ik/Java-Blockchain-Cryptocurrency-Implementation/blob/main/img/2022-10-19_23-25-04.png)

Features:
* Integrity of the blockchain and its validation after creation
* Controlling the number of hash's leading zeros based on creation time
* Magic numbers as a proof of work for each block
* BlockChain validation after creating
* Transactions between users with digital signatures, public & private keys
* Validation of each transaction

Program is made with simple GUI (using `java swing` framework).




## Launching

Works with: `java11`

Information about blockchain is stored in file and is updating after adding new block. Add the file before launching.
```bash
Java-Blockchain-Cryptocurrency-Implementation/> touch ./data/blockchain
```


Compile and Run:
```bash
Java-Blockchain-Cryptocurrency-Implementation/> java ./src/Main
```
