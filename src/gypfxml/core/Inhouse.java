package gypfxml.core;

public class Inhouse extends Part {
	
    private int machineID;
    
    public Inhouse(int machineID) {
        this.machineID = machineID;
    }

    public void setMachineID(int machineID) {
            this.machineID = machineID;
    }

    public int getMachineId() {
            return machineID;
    }
}
