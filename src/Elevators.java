import java.util.*;
import java.util.stream.Collectors;

public class Elevators {
    private List<Elevator> elevators = new ArrayList<>();

    public Elevators(int maxElevators) {
        for (int i = 0; i < maxElevators; i++) {
            elevators.add(new Elevator(i));
        }
    }

    public void callElevator(int floorCalling) {
        //checkElevator is not on this level already
        System.out.println("FLOOR " + floorCalling + " called!  ");
        boolean alreadyOnThisLevel = elevators.stream().anyMatch(e -> e.getFloorNumber() == floorCalling && e.getStatus().equals(Elevator.Status.STOP));
        if (alreadyOnThisLevel) {
            System.out.println("Elevator already on this floor!");
            return;
        }

        //check if there is happy path best elevator
        List<Elevator> bestPossibleElevators = elevators.stream().filter(e ->
                e.getFloorNumber() < floorCalling && e.getDirection().equals(Elevator.Direction.UP) ||
                        e.getFloorNumber() > floorCalling && e.getDirection().equals(Elevator.Direction.DOWN) ||
                        e.getDirection().equals(Elevator.Direction.NO_DIR)
        ).collect(Collectors.toList());

        if (bestPossibleElevators.size() > 0) {
            int distance = Math.abs(bestPossibleElevators.get(0).getFloorNumber() - floorCalling);
            int closestElevatorId = bestPossibleElevators.get(0).getElevatorId();
            for (Elevator e : bestPossibleElevators) {
                int tmpDst = Math.abs(e.getFloorNumber() - floorCalling);
                if (distance > tmpDst) {
                    distance = tmpDst;
                    closestElevatorId = e.getElevatorId();
                }
            }
            Elevator.Direction direction;
            Elevator elevator = elevators.get(closestElevatorId);
            Set<Integer> setStopOnFloors = elevator.getStopOnFloors();
            if (elevator.getFloorNumber() - floorCalling > 0) {
                direction = Elevator.Direction.DOWN;
                elevator.setDestinationFloorNumber(
                        floorCalling < elevator.getDestinationFloorNumber() ? floorCalling : elevator.getDestinationFloorNumber()
                );
                setStopOnFloors.add(floorCalling);
            } else {
                direction = Elevator.Direction.UP;
                elevator.setDestinationFloorNumber(
                        floorCalling > elevator.getDestinationFloorNumber() ? floorCalling : elevator.getDestinationFloorNumber()
                );
                setStopOnFloors.add(floorCalling);
                elevator.setStopOnFloors(setStopOnFloors);
            }
            elevator.setDirection(direction);
            elevator.setStatus(Elevator.Status.MOVE);
            System.out.println("Elevator ID: " + elevator.getElevatorId() + " was called for serving floor " + floorCalling);
        } else {
            //if there is no happy path best elevator, use elevator with closest destination
            int distance = Math.abs(elevators.get(0).getFloorNumber() - floorCalling);
            int closestElevatorId = elevators.get(0).getElevatorId();
            for (Elevator e : elevators) {
                int tmpDst = Math.abs(e.getFloorNumber() - floorCalling);
                if (distance > tmpDst) {
                    distance = tmpDst;
                    closestElevatorId = e.getElevatorId();
                }
            }
            elevators.get(closestElevatorId).getStopOnFloors().add(floorCalling);
            System.out.println("Elevator ID: " + elevators.get(closestElevatorId).getElevatorId() + " was called for serving floor " + floorCalling);
        }
    }

    public void requestElevator(int floorCalling, int elevatorId) {
        elevators.get(elevatorId).getStopOnFloors().add(floorCalling);
        System.out.println("Elevator id " + elevatorId + " called floor " + floorCalling);
    }

    //show all elevators status
    public void checkElevatorStatus() {
        for (Elevator e : elevators) {
            System.out.print("ID: " + e.getElevatorId());
            System.out.print(" | Floor: " + e.getFloorNumber());
            System.out.print(" | DST: " + e.getDestinationFloorNumber());
            System.out.print(" | DIR: " + e.getDirection());
            System.out.print(" | STATUS: " + e.getStatus());
            System.out.println(" | STOP ON: " + e.getStopOnFloors());
        }
    }

    private void setElevatorStopWithNoDirection(Elevator e) {
        e.setDirection(Elevator.Direction.NO_DIR);
        e.setStatus(Elevator.Status.STOP);
    }

    //move all elevators one step
    public void moveElevators(int maxFloorNumber) {

        //if elevator is stopped after reaching destination but contains floors in set, wakes them up
        elevators.stream().filter(e ->
                e.getStatus().equals(Elevator.Status.STOP) &&
                        e.getDirection().equals(Elevator.Direction.NO_DIR) &&
                        e.getStopOnFloors().size() > 0
        ).forEach(e -> setElevatorMoveAndDestination(e, wakeUpElevatorAfterReachingDestination(e)));

        List<Elevator> goingUp = elevators.stream().filter(e -> e.getDirection().equals(Elevator.Direction.UP)).collect(Collectors.toList());
        List<Elevator> goingDown = elevators.stream().filter(e -> e.getDirection().equals(Elevator.Direction.DOWN)).collect(Collectors.toList());

        //move elevators up
        for (Elevator e : goingUp) {
            if (maxFloorNumber != e.getFloorNumber()) {
                e.setFloorNumber(e.getFloorNumber() + 1);
                //if elevator should stop on floor, it STOPs, and remove floor from set
                if (e.getStopOnFloors().contains(e.getFloorNumber())) {
                    e.setStatus(Elevator.Status.STOP);
                    e.getStopOnFloors().remove(e.getFloorNumber());
                    //if elevator reached destination after moving, it stops
                    if (e.getDestinationFloorNumber() == e.getFloorNumber()) {
                        e.setDirection(Elevator.Direction.NO_DIR);
                    }
                }
                //if elevator that as moving down reaching floor max floor
                if (e.getFloorNumber() == maxFloorNumber) {
                    setElevatorStopWithNoDirection(e);
                }
            }
        }
        //move elevators down
        for (Elevator e : goingDown) {
            if (maxFloorNumber != 0) {
                e.setFloorNumber(e.getFloorNumber() - 1);
                //if elevator should stop on floor, it STOPs, and remove floor from set
                if (e.getStopOnFloors().contains(e.getFloorNumber())) {
                    e.setStatus(Elevator.Status.STOP);
                    e.getStopOnFloors().remove(e.getFloorNumber());
                    //if elevator reached destination after moving, it stops
                    if (e.getDestinationFloorNumber() == e.getFloorNumber()) {
                        e.setDirection(Elevator.Direction.NO_DIR);
                    }
                }
                //if elevator that as moving down reaching floor 0
                if (e.getFloorNumber() == 0) {
                    setElevatorStopWithNoDirection(e);
                }
            }
        }
    }

    private int wakeUpElevatorAfterReachingDestination(Elevator e) {
        Set<Integer> floors = e.getStopOnFloors();
        Integer min = Collections.min(floors);
        Integer max = Collections.max(floors);

        if (max.compareTo(min) == 0) {
            return Integer.parseInt(max.toString());
        } else if (max.compareTo(min) == 1) {
            return Integer.parseInt(min.toString());
        } else {
            return Integer.parseInt(max.toString());
        }
    }

    private void setElevatorMoveAndDestination(Elevator e, int dstFloor) {
        e.setStatus(Elevator.Status.MOVE);
        e.setDestinationFloorNumber(dstFloor);
        if (e.getFloorNumber() > dstFloor) {
            e.setDirection(Elevator.Direction.DOWN);
        } else {
            e.setDirection(Elevator.Direction.UP);
        }
    }

}