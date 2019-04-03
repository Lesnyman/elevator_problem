import java.util.HashSet;
import java.util.Set;

public class Elevator {
    private int elevatorId;
    private int floorNumber;
    private int destinationFloorNumber;
    private Direction direction;
    private Status status;
    private Set<Integer> stopOnFloors;

    public Elevator(int elevatorId) {
        this.elevatorId = elevatorId;
        this.floorNumber = 0;
        this.destinationFloorNumber = 0;
        this.direction = Direction.NO_DIR;
        this.status = Status.STOP;
        this.stopOnFloors = new HashSet();
    }

    public Set<Integer> getStopOnFloors() {
        return stopOnFloors;
    }

    public void setStopOnFloors(Set<Integer> stopOnFloors) {
        this.stopOnFloors = stopOnFloors;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getElevatorId() {
        return elevatorId;
    }

    public void setElevatorId(int elevatorId) {
        this.elevatorId = elevatorId;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public int getDestinationFloorNumber() {
        return destinationFloorNumber;
    }

    public void setDestinationFloorNumber(int destinationFloorNumber) {
        this.destinationFloorNumber = destinationFloorNumber;
    }

    public enum Direction {
        UP, DOWN, NO_DIR
    }

    public enum Status {
        MOVE, STOP
    }
}
