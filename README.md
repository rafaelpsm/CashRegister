# Cash Register

This program is a Cash Register that is able to accept only $20, $10, $5, $2 and $1 bills.

## Dependencies

### Development:

* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

### Test:

* JUnit - on a lib folder

## Usage

### Run:

`> java Main`

### Commands

* `> change 11` - find and print change bill denominations
* `> charge 11 1 0 0 0 0` - receive the charge amount and bill denominations to be charged from and print the change bill denominations 
* `> excharge 1 0 0 0 0` - receive one dollar bill denomination ($20, $10, $5, $2) and break into and print smaller bill denominations 
* `> put 1 2 3 0 5` - add the bill denominations and print the current state of the Cash Register
* `> show` - print the current state of the Cash Register
* `> take 1 4 3 0 10` - take the bill denominations and print the current state of the Cash Register
* `> quit` - end the program
