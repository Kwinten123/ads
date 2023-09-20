package models;


import com.sun.source.tree.BreakTree;

public class Train {
    private final String origin;
    private final String destination;
    private final Locomotive engine;
    private Wagon firstWagon;

//     Representation invariants:
//        firstWagon == null || firstWagon.previousWagon == null
//        engine != null


    public Train(Locomotive engine, String origin, String destination) {
        this.engine = engine;
        this.destination = destination;
        this.origin = origin;
    }

    /**
     * Indicates whether the train has at least one connected Wagon
     * @return
     */
    public boolean hasWagons() {
        return this.firstWagon != null;   // replace by proper outcome
    }

    /**
     * A train is a passenger train when its first wagon is a PassengerWagon
     * (we do not worry about the posibility of mixed compositions here)
     * @return
     */
    public boolean isPassengerTrain() {
        return this.firstWagon instanceof PassengerWagon;
    }

    /**
     * A train is a freight train when its first wagon is a FreightWagon
     * (we do not worry about the posibility of mixed compositions here)
     * @return
     */
    public boolean isFreightTrain() {
        return this.firstWagon instanceof FreightWagon;
    }

    public Locomotive getEngine() {
        return engine;
    }

    public Wagon getFirstWagon() {
        return this.firstWagon;
    }

    /**
     * Replaces the current sequence of wagons (if any) in the train
     * by the given new sequence of wagons (if any)
     * @param wagon the first wagon of a sequence of wagons to be attached (can be null)
     */
    public void setFirstWagon(Wagon wagon) {
        this.firstWagon = wagon;
    }

    /**
     * loop through all wagons and return the last one
     * @return  the last wagon attached to the train
     */
    public Wagon getLastWagonAttached() {
        if (!hasWagons()) return null;

        Wagon wagon = this.firstWagon;

        while (wagon.hasNextWagon()) {
            wagon = wagon.getNextWagon();
        }

        return wagon;
    }

    /**
     * @return  the total number of seats on a passenger train
     *          (return 0 for a freight train)
     */
    public int getTotalNumberOfSeats() {
        if (!isPassengerTrain()) return 0;

        if (!hasWagons()) return 0;

        if (getNumberOfWagons() == 1) return ((PassengerWagon) this.firstWagon).getNumberOfSeats();

        Wagon wagon = this.firstWagon;

        int numberOfSeats = 0;

        while (wagon != null) {
            numberOfSeats += ((PassengerWagon) wagon).getNumberOfSeats();

            wagon = wagon.getNextWagon();
        }

        return numberOfSeats;
    }

    /**
     * calculates the total maximum weight of a freight train
     * @return  the total maximum weight of a freight train
     *          (return 0 for a passenger train)
     *
     */
    public int getTotalMaxWeight() {
        if (!hasWagons()) return 0;

        if (!isFreightTrain()) return 0;

        if (getNumberOfWagons() == 1) return ((FreightWagon) this.firstWagon).getMaxWeight();

        Wagon wagon = this.firstWagon;

        int totalMaxWeight = 0;

        while (wagon != null) {
            totalMaxWeight += ((FreightWagon) wagon).getMaxWeight();

            wagon = wagon.getNextWagon();
        }

        return totalMaxWeight;
    }

    /**
     * Finds the wagon at the given position (starting at 0 for the first wagon of the train)
     * @param position
     * @return  the wagon found at the given position
     *          (return null if the position is not valid for this train)
     */
    public Wagon findWagonAtPosition(int position) {
        if (!hasWagons()) return null;

        int totalNumberOfWagons = getNumberOfWagons();

        Wagon wagon = firstWagon;
        for (int i = 0; i < totalNumberOfWagons; i++) {
            if (position == i){
                return wagon;
            }

            wagon = wagon.getNextWagon();
        }

        return null;
    }

    /**
     * Finds the wagon with a given wagonId
     * @param wagonId
     * @return  the wagon found
     *          (return null if no wagon was found with the given wagonId)
     */
    public Wagon findWagonById(int wagonId) {
        if (!hasWagons()) return null; //no wagons

        Wagon wagon = this.firstWagon;
//        System.out.println(wagonId);

        while (wagon != null) {
            if (wagon.id == wagonId){
                return wagon;
            }
            wagon = wagon.getNextWagon();
        }
        return null;
    }

    /**
     * Determines if the given sequence of wagons can be attached to this train
     * Verifies that the wagon is not part of the train already
     * Verifies if the type of wagons match the type of train (Passenger or Freight)
     * Verifies that the capacity of the engine is sufficient to also pull the additional wagons
     * Ignores the predecessors before the head wagon, if any
     * @param wagon the head wagon of a sequence of wagons to consider for attachment
     * @return whether type and capacity of this train can accommodate attachment of the sequence
     */
    public boolean canAttach(Wagon wagon) {
        if (findWagonById(wagon.id) == wagon) {  // Wagon exists in the train already
            return false;
        }

        if (isPassengerTrain() && !(wagon instanceof PassengerWagon)) {
            return false;
        }

        if (isFreightTrain() && !(wagon instanceof FreightWagon)) {
            return false;
        }

        int totalNumberOfWagons = getNumberOfWagons() + wagon.getSequenceLength();
        if (this.engine.getMaxWagons() < totalNumberOfWagons) {
            return false;
        }

        return true; // If all checks pass, return true.
    }


    /**
     * Tries to attach the given sequence of wagons to the rear of the train
     * No change is made if the attachment cannot be made.
     * (when the sequence is not compatible or the engine has insufficient capacity)
     * if attachment is possible, the head wagon is first detached from its predecessors, if any
     * @param wagon the head wagon of a sequence of wagons to be attached
     * @return  whether the attachment could be completed successfully
     */
    public boolean attachToRear(Wagon wagon) {
        if (!canAttach(wagon)) return false;

        if (wagon.hasPreviousWagon()) {
            wagon.detachFront();
        }


        if (!hasWagons()) {
            this.firstWagon = wagon;  //no wagons exist yet so assign the first 1
            return true;
        }

        Wagon lastWagon = getLastWagonAttached();
        lastWagon.setNextWagon(wagon);
        wagon.setPreviousWagon(lastWagon);

        return true;
    }

    /**
     * @return  the number of Wagons connected to the train
     */
    public int getNumberOfWagons() {
        if (!hasWagons()) return 0; //no wagons

        int length = 1; // Start with 1 to count the current wagon itself

        Wagon wagon = this.firstWagon;

        while (wagon.hasNextWagon()) {
            wagon = wagon.getNextWagon();
            length++;
        }
        return length;
    }

    /**
     * Tries to insert the given sequence of wagons at the front of the train
     * (the front is at position one, before the current first wagon, if any)
     * No change is made if the insertion cannot be made.
     * (when the sequence is not compatible or the engine has insufficient capacity)
     * if insertion is possible, the head wagon is first detached from its predecessors, if any
     * @param wagon the head wagon of a sequence of wagons to be inserted
     * @return  whether the insertion could be completed successfully
     */
    public boolean insertAtFront(Wagon wagon) {
        if (!canAttach(wagon)) return false;

        //detatch head wagon from predecessor, if any
        wagon.detachFront();

        if (hasWagons()){
            wagon.getLastWagonAttached().attachTail(firstWagon);
        }

        setFirstWagon(wagon);

        return true;
    }

    /**
     * Tries to insert the given sequence of wagons at/before the given position in the train.
     * (The current wagon at given position including all its successors shall then be reattached
     *    after the last wagon of the given sequence.)
     * No change is made if the insertion cannot be made.
     * (when the sequence is not compatible or the engine has insufficient capacity
     *    or the given position is not valid for insertion into this train)
     * if insertion is possible, the head wagon of the sequence is first detached from its predecessors, if any
     * @param position the position where the head wagon and its successors shall be inserted
     *                 0 <= position <= numWagons
     *                 (i.e. insertion immediately after the last wagon is also possible)
     * @param wagon the head wagon of a sequence of wagons to be inserted
     * @return  whether the insertion could be completed successfully
     */

    //TODO
    public boolean insertAtPosition(int position, Wagon wagon) {
        // Check if the wagon can be attached
        if (!canAttach(wagon)) {
            return false;
        }

        // If there are no wagons in the train and position is not 0, return false
        if (!hasWagons() && position != 0) {
            return false;
        }

        // If the train has no wagons or position is 0, set the inserted wagon as the first wagon
        if (!hasWagons() || position == 0) {
            setFirstWagon(wagon);
            return true;
        }

        //-1 to set the length equal to the position
        int trainLength = firstWagon.getSequenceLength();


        if (trainLength == position){
            firstWagon.getLastWagonAttached().attachTail(wagon);
            return true;
        }

        Wagon toBeReattachedWagon = findWagonAtPosition(position);

        if (toBeReattachedWagon == null) {
            return false;
        }

        wagon.detachFront();

        Wagon toBeAttachedToWagon = toBeReattachedWagon.detachFront();

        toBeAttachedToWagon.attachTail(wagon);

        return true;
    }




    /**
     * Tries to remove one Wagon with the given wagonId from this train
     * and attach it at the rear of the given toTrain
     * No change is made if the removal or attachment cannot be made
     * (when the wagon cannot be found, or the trains are not compatible
     * or the engine of toTrain has insufficient capacity)
     * @param wagonId   the id of the wagon to be removed
     * @param toTrain   the train to which the wagon shall be attached
     *                  toTrain shall be different from this train
     * @return  whether the move could be completed successfully
     */
    public boolean moveOneWagon(int wagonId, Train toTrain) {

        Wagon toBeMovedWagon = findWagonById(wagonId);

        if (toBeMovedWagon == null) return false;


        //TODO een beetje questionable misschien nog anders oplossen.
        if (toBeMovedWagon == firstWagon){
            setFirstWagon(firstWagon.getNextWagon());
        }

        toBeMovedWagon.removeFromSequence();

        //check if you can attach to train
        if (!toTrain.canAttach(toBeMovedWagon)) return false;

        if (!toTrain.hasWagons()){
            toTrain.setFirstWagon(toBeMovedWagon);
        }

        toTrain.attachToRear(toBeMovedWagon);
        return true;
    }

    /**
     * Tries to split this train before the wagon at given position and move the complete sequence
     * of wagons from the given position to the rear of toTrain.
     * No change is made if the split or re-attachment cannot be made
     * (when the position is not valid for this train, or the trains are not compatible
     * or the engine of toTrain has insufficient capacity)
     * @param position  0 <= position < numWagons
     * @param toTrain   the train to which the split sequence shall be attached
     *                  toTrain shall be different from this train
     * @return  whether the move could be completed successfully
     */
    public boolean splitAtPosition(int position, Train toTrain) {
        if (!hasWagons()) return false;



        //is wagon in bounds?
        if (findWagonAtPosition(position) == null) return false;


        Wagon toBeMovedWagon = findWagonAtPosition(position);


        if (!toTrain.canAttach(toBeMovedWagon)) return false;

        toBeMovedWagon.detachFront();

        if (firstWagon == toBeMovedWagon){
            setFirstWagon(null);
        }

        toTrain.attachToRear(toBeMovedWagon);

        return true;

        // TODO
    }

    /**
     * Reverses the sequence of wagons in this train (if any)
     * i.e. the last wagon becomes the first wagon
     *      the previous wagon of the last wagon becomes the second wagon
     *      etc.
     * (No change if the train has no wagons or only one wagon)
     */
    public void reverse() {
        if (hasWagons()) {
            Wagon newFirstWagon = firstWagon.reverseSequence();

            setFirstWagon(newFirstWagon);
        }
    }

    // TODO string representation of a train
    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        String locomotive = "[Loc-" + engine.getlocNumber() + "]";

        stringBuilder.append(locomotive);


        if (!hasWagons()) return stringBuilder.toString();

        int trainLength = getNumberOfWagons();

        Wagon wagon = this.firstWagon;

        for (int i = 0; i < trainLength; i++) {
            stringBuilder.append("[Wagon-").append(wagon.id).append("]");
            wagon = wagon.getNextWagon();
        }

        stringBuilder.append(" with ").append(trainLength)
                .append(" wagons from ").append(origin).append(" to ").append(destination).append("\n");

        if (isPassengerTrain()){
            stringBuilder.append("Total number of seats: ").append(getTotalNumberOfSeats());
        }

        if (isFreightTrain()){
            stringBuilder.append("Total weight of the wagons: ").append(getTotalMaxWeight());
        }

        return stringBuilder.toString();
    }
}
