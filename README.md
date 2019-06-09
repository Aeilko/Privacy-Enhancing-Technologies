# Privacy Enhancing Technologies
Using both Java and Python, because Python is preferred but i'am not skilled enough to create working programs in it.

## Assignment 1 - Anonymous Communication

### Mixnets (Java)
The encryption for the mixnet messages are created using the mixnet.MixNetwork class, which creates the necessary nodes from a list of public keys.
The mixnet.Connector class connects to the pets server, encrypts the message using the MixNetwork class and sends it.

These classes require the Bouncy Castle library for encryption purposes, a jar containing this library can be found in /lib/.

For the assignments we created the assignments.Assignment_1_1 and assignments.Assignment_1_2 classes. You should be able to run these directly, however you might need to change the port number.

The Assignment_1_1 class will sent a single message to the mixnet containing the message "Group 34".

The Assignment_1_2 class will flush the network, after which it will sent X (70) messages and count the number of messages in the exit log.

## Assignment 3 - Private Logarithm Evaluation
The Paillier system is encrypted in the [Paillier](/src/encryption/Paillier.java) class, which can create it's own primes or use provided primes.
If you provide your own primes this class will not check whether or not these values are actually primes.
This class also contains some static methods which can perform arithmetic in the cipherspace.
These methods require the publicaly available information of the cryptosystem, implemented in [PaillierPublic](src/encryption/PaillierPublic.java) and retrievable by using the Paillier.getPaillierPublic() method.

The [Assignment_3_1](src/assignments/Assignment_3_1.java) class contains some test cases with random numbers.
It will create 3 random numbers and than perform and display the secure actions.
After displaying it it will do the same thing 50 more times, only displaying something when an error occurs.

The [Assignment_3_2](src/assignments/Assignment_3_2.java) class contains the same type of testcases as in 3_1.

