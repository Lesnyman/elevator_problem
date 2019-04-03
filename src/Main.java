public class Main {

    public final static int MAX_ELEVATOR_NUMBER = 3;
    public final static int MAX_FLOOR_NUMBER = 20;

    public static void main(String[] args) {
        Simulation simulation = new Simulation(MAX_FLOOR_NUMBER, MAX_ELEVATOR_NUMBER);
        Elevators elevators = new Elevators(MAX_ELEVATOR_NUMBER);
        elevators.checkElevatorStatus();
        simulation.startSimulation(elevators);
    }
}