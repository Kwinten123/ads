package models;

public abstract class Wagon {
    protected int id;               // some unique ID of a Wagon
    private Wagon nextWagon;        // another wagon that is appended at the tail of this wagon
    // a.k.a. the successor of this wagon in a sequence
    // set to null if no successor is connected
    private Wagon previousWagon;    // another wagon that is prepended at the front of this wagon
    // a.k.a. the predecessor of this wagon in a sequence
    // set to null if no predecessor is connected


    // representation invariant propositions:
    // tail-connection-invariant:   wagon.nextWagon == null or wagon == wagon.nextWagon.previousWagon
    // front-connection-invariant:  wagon.previousWagon == null or wagon = wagon.previousWagon.nextWagon

    public Wagon (int wagonId) {
        this.id = wagonId;
    }

    public int getId() {
        return id;
    }

    public Wagon getNextWagon() {
        return nextWagon;
    }

    public Wagon getPreviousWagon() {
        return previousWagon;
    }

    /**
     * @return  whether this wagon has a wagon appended at the tail
     */
    public boolean hasNextWagon() {
        return nextWagon != null;
    }

    /**
     * @return  whether this wagon has a wagon prepended at the front
     */
    public boolean hasPreviousWagon() {
        return previousWagon != null;
    }

    /**
     * Returns the last wagon attached to it,
     * if there are no wagons attached to it then this wagon is the last wagon.
     * @return  the last wagon
     */
    public Wagon getLastWagonAttached() {
        if (!hasNextWagon()) return this;

        Wagon currentWagon = this;

        while (currentWagon.hasNextWagon()) {
            currentWagon = currentWagon.getNextWagon();
        }

        return currentWagon;
    }

    /**
     * @return  the length of the sequence of wagons towards the end of its tail
     * including this wagon itself.
     */
    public int getSequenceLength() {
        int length = 1; // Start with 1 to count the current wagon itself

        Wagon currentWagon = this;

        if (!currentWagon.hasNextWagon()) return length;

        while (currentWagon.hasNextWagon()) {
            currentWagon = currentWagon.getNextWagon();
            length++;
        }

        return length;
    }


    /**
     * Attaches the tail wagon and its connected successors behind this wagon,
     * if and only if this wagon has no wagon attached at its tail
     * and if the tail wagon has no wagon attached in front of it.
     * @param tail the wagon to attach behind this wagon.
     * @throws IllegalStateException if this wagon already has a wagon appended to it.
     * @throws IllegalStateException if tail is already attached to a wagon in front of it.
     *          The exception should include a message that reports the conflicting connection,
     *          e.g.: "%s is already pulling %s"
     *          or:   "%s has already been attached to %s"
     */
    public void attachTail(Wagon tail) {
        if (hasNextWagon()) throw new IllegalStateException( this + " is already pulling " + this.nextWagon);

        if (tail.hasPreviousWagon()) throw new IllegalStateException(tail + " has already been attached to " + tail.previousWagon);

        this.setNextWagon(tail);
        tail.setPreviousWagon(this);
    }

    /**
     * Detaches the tail from this wagon and returns the first wagon of this tail.
     * @return the first wagon of the tail that has been detached
     *          or <code>null</code> if it had no wagons attached to its tail.
     */
    public Wagon detachTail() {
        if (!this.hasNextWagon()) return null;

        Wagon succesor = this.nextWagon;

        succesor.previousWagon = null;
        this.nextWagon = null;

        return succesor;
    }

    /**
     * Detaches this wagon from the wagon in front of it.
     * No action if this wagon has no previous wagon attached.
     * @return  the former previousWagon that has been detached from,
     *          or <code>null</code> if it had no previousWagon.
     */
    public Wagon detachFront() {
        if (!hasPreviousWagon()) return null;

        Wagon predecessor = this.previousWagon;

        predecessor.nextWagon = null;
        this.previousWagon = null;

        return predecessor;
    }

    /**
     * Replaces the tail of the <code>front</code> wagon by this wagon and its connected successors
     * Before such reconfiguration can be made,
     * the method first disconnects this wagon from its predecessor,
     * and the <code>front</code> wagon from its current tail.
     * @param front the wagon to which this wagon must be attached to.
     */
    public void reAttachTo(Wagon front) {

        this.detachFront();

        front.detachTail();

        front.nextWagon = this;
        this.previousWagon = front;
    }

    /**
     * Removes this wagon from the sequence that it is part of,
     * and reconnects its tail to the wagon in front of it, if any.
     */
    public void removeFromSequence() {
        if (!hasPreviousWagon()){//first wagon
            this.detachTail();
        }

        if (hasPreviousWagon() && hasNextWagon()){//middle wagon
            Wagon succesor = this.detachFront();
            Wagon predecessor = this.detachTail();

            succesor.nextWagon = predecessor;
            predecessor.previousWagon = succesor;
        }

        if (!hasNextWagon()){//last wagon
            this.detachFront();
        }
    }


    /**
     * Reverses the order in the sequence of wagons from this Wagon until its final successor.
     * The reversed sequence is attached again to the wagon in front of this Wagon, if any.
     * No action if this Wagon has no succeeding next wagon attached.
     * @return the new start Wagon of the reversed sequence (with is the former last Wagon of the original sequence)
     */
    public Wagon reverseSequence() {
        int sequenceLength = getSequenceLength();

        if (sequenceLength < 2) return null;


        Wagon currentWagon = this;
        Wagon nextWagon;

        //wagon to attach the new head of the sequence to.
        Wagon toAttachToWagon = this.previousWagon;

        for (int i = 0; i < sequenceLength; i++) {

            //detach first wagon from front
            if (i == 0){
                currentWagon.detachFront();
            }

            nextWagon = currentWagon.nextWagon;

            currentWagon.nextWagon = currentWagon.previousWagon;
            currentWagon.previousWagon = nextWagon;


            if (currentWagon.hasPreviousWagon()){
                currentWagon = currentWagon.previousWagon;
            }
        }

        if (toAttachToWagon != null){
            toAttachToWagon.attachTail(currentWagon);
        }

        return currentWagon;
    }


    public void setNextWagon(Wagon nextWagon) {
        this.nextWagon = nextWagon;
    }

    public void setPreviousWagon(Wagon previousWagon) {
        this.previousWagon = previousWagon;
    }

    //string reprisentation of a Wagon
    @Override
    public String toString() {
        return  "[Wagon-" + this.id + "]";
//        StringBuilder stringBuilder = new StringBuilder();
//
//        stringBuilder.append("[id : ").append(this.id).append("]").append("\n");//id
//
//        stringBuilder.append("[previousWagon : ");
//
//        if (hasPreviousWagon()) {
//            stringBuilder.append(this.previousWagon.getId());//pw
//        }
//
//        stringBuilder.append("]").append("\n");
//
//
//        stringBuilder.append("[nextWagon : ");
//
//        if (hasNextWagon()) {
//            stringBuilder.append(this.nextWagon.getId());//nw
//        }
//        stringBuilder.append("]").append("\n");
//
//
//        //sequence with this wagon as starting point
//        stringBuilder.append("Sequence : ");
//
//        Wagon wagon = this;
//        int sequenceLength = getSequenceLength();
//
//            for (int i = 0; i < sequenceLength; i++) {
//            stringBuilder.append("[Wagon-").append(wagon.id).append("]");
//            wagon = wagon.getNextWagon();
//        }
//            stringBuilder.append("\n");
//
//        return stringBuilder.toString();
    }
}
