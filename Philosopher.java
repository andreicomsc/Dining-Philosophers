public class Philosopher implements Runnable {
    // The forks on either side of this Philosopher and his/her philosopher number
    private Fork leftFork;
    private Fork rightFork;
    private int philosopherNumber;

    // Shared "philosophersManager" lock object, and the "readyToEat" flag
    private static Object philosophersManager = new Object();
    private boolean readyToEat = false;

    public Philosopher(Fork left, Fork right, int philNumber) {
        leftFork = left;
        rightFork = right;
        philosopherNumber=philNumber;
    }
    
	private void doAction(String action) throws InterruptedException {
        System.out.println("Philosopher number " + philosopherNumber + " is " + action);
        Thread.sleep(((int) (Math.random() * 1000)));
    }

    public void run() {
        try {
            while (true) {
                
                // thinking
                doAction("Thinking");
                // Getting the "lock" to start the checking/picking up forks sequence
                synchronized (philosophersManager)
                {
                    // checking if both forks are available
                    if ((!leftFork.inUse) && (!rightFork.inUse))
                    {
                        // picking both forks up and mark them as being used
                        leftFork.inUse = true;
                        rightFork.inUse = true;   
                        doAction("Picking up both forks"); 
                        readyToEat = true;                   
                    }
                }

                if (readyToEat)
                {
                    // eating
                    doAction("Eating");
                    // putting both forks down and mark them as not being used
                    doAction("Putting down both forks");
                    leftFork.inUse = false;
                    rightFork.inUse = false;
                    // Back to thinking 
                    readyToEat = false;                           
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
    }
}