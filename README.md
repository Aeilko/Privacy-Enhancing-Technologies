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