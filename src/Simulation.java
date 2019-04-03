import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Simulation {
    public final int MAX_FLOORS_NUMBER;
    public final int MAX_ELEVATORS_NUMBER;

    public Simulation(int maxFloorsNumber, int maxElevatorsNumber) {
        MAX_FLOORS_NUMBER = maxFloorsNumber;
        MAX_ELEVATORS_NUMBER = maxElevatorsNumber;
    }

    public void startSimulation(Elevators elevators) {
        while (true) {

            Scanner floorCalling = new Scanner(System.in);
            System.out.println();
            System.out.println("Calling floor");


            String callingFloor = floorCalling.nextLine();
            if (callingFloor.matches("id=\\d+ f=\\d+")) {
                Pattern p = Pattern.compile("\\d+");
                Matcher m = p.matcher(callingFloor);
                m.find();
                int elevatorID = Integer.parseInt(m.group());

                m.find();
                int calledFloor = Integer.parseInt(m.group());

                if (checkNumberIsInBoundInclusive(0, MAX_FLOORS_NUMBER-1, calledFloor) &&
                        checkNumberIsInBoundInclusive(0, MAX_ELEVATORS_NUMBER-1, elevatorID)) {
                    System.out.println("Elevator id " + elevatorID + " called floor " + calledFloor);
                    System.out.println();
                    System.out.println("________________________CALLING________");

                    elevators.checkElevatorStatus();
                    elevators.requestElevator(calledFloor, elevatorID);
                    elevators.checkElevatorStatus();

                    System.out.println("___________MOVING___________");
                    elevators.moveElevators(MAX_FLOORS_NUMBER);
                    elevators.checkElevatorStatus();
                    System.out.println("______________________");
                } else {
                    System.out.println("Floor number or elevator id out of bounds");
                }


            } else if (callingFloor.matches("\\d+")) {
                if (checkNumberIsInBoundInclusive(0, MAX_FLOORS_NUMBER, Integer.parseInt(callingFloor))) {
                    int floorsCalled = Integer.parseInt(callingFloor);
                    System.out.println();
                    System.out.println("________________________CALLING________");

                    elevators.checkElevatorStatus();
                    elevators.callElevator(floorsCalled);
                    elevators.checkElevatorStatus();

                    System.out.println("___________MOVING___________");
                    elevators.moveElevators(MAX_FLOORS_NUMBER);
                    elevators.checkElevatorStatus();
                    System.out.println("______________________");
                } else {
                    System.out.println("Floor number out of bounds");
                }


            } else {
                System.out.println("No call and no request. Moving time.");
                elevators.checkElevatorStatus();
                System.out.println("___________MOVING___________");
                elevators.moveElevators(MAX_FLOORS_NUMBER);
                elevators.checkElevatorStatus();
                System.out.println("______________________");
            }
        }
    }

    private boolean checkNumberIsInBoundInclusive(int min, int max, int number) {
        return number <= max && number >= min;
    }
}