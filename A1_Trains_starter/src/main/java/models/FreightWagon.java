package models;
public class FreightWagon extends Wagon{

    public int maxWeight;

    public FreightWagon(int wagonId, int maxWeight) {
        // TODO

        super(wagonId);

        this.maxWeight = maxWeight;
    }

    public int getMaxWeight() {
        return this.maxWeight;
    }
}
