import java.util.TreeSet;

public class MainClass {
    static TreeSet<Request> currentJobs = new TreeSet<>();
    static TreeSet<Request> upPendingJobs = new TreeSet<>();
    static TreeSet<Request> downPendingJobs = new TreeSet<>();
    static Elevator elevator = new Elevator();

    public static void main(String [] args) {
        elevator.state = State.IDLE;
        ExternalRequest er1 = new ExternalRequest(Direction.UP, 0);
        InternalRequest ir1 = new InternalRequest(5);
        ExternalRequest er2 = new ExternalRequest(Direction.UP, 2);
        InternalRequest ir2 = new InternalRequest(4);
        ExternalRequest er4 = new ExternalRequest(Direction.UP, 4);
        InternalRequest ir4 = new InternalRequest(9);
        ExternalRequest er3 = new ExternalRequest(Direction.DOWN, 3);
        InternalRequest ir3 = new InternalRequest(0);
        ExternalRequest er5 = new ExternalRequest(Direction.DOWN, 3);
        InternalRequest ir5 = new InternalRequest(1);
        Request request1 = new Request(er1, ir1);
        Request request2 = new Request(er2, ir2);
        Request request3 = new Request(er3, ir3);
        Request request4 = new Request(er4, ir4);
        Request request5 = new Request(er5, ir5);
        addNewJob(request1);
        addNewJob(request2);
        addNewJob(request3);
        addNewJob(request4);
        addNewJob(request5);
        startElevator();
    }
    public static void startElevator() {
        System.out.println("Elevator Started");
        while(true) {
            if(checkIfJobPresent()) {
                if(elevator.direction == Direction.UP) {
                    serveUpJob(currentJobs.pollFirst());
                    if(currentJobs.isEmpty()) {
                        addPendingDownJobsToCurrentJobs();
                    }
                } else {
                    serveDownJob(currentJobs.pollLast());
                    if(currentJobs.isEmpty()) {
                        addPendingUpJobsToCurrentJobs();
                    }
                }
            }
        }
    }

    private static void addPendingDownJobsToCurrentJobs() {
        if(!downPendingJobs.isEmpty()) {
            currentJobs = downPendingJobs;
            elevator.direction = Direction.DOWN;
        }
    }

    private static void addPendingUpJobsToCurrentJobs() {
        if(!upPendingJobs.isEmpty()) {
            currentJobs = upPendingJobs;
            elevator.direction = Direction.UP;
        }
    }

    private static boolean checkIfJobPresent() {
        if(currentJobs.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public static void addNewJob(Request newRequest) {
        if(elevator.state == State.IDLE) {
            elevator.state = State.MOVING;
            elevator.direction = newRequest.externalRequest.directionToGo;
            currentJobs.add(newRequest);
        } else {
            if(elevator.direction != newRequest.externalRequest.directionToGo) {
                addToPendingJobs(newRequest);
            } else  {
                if(elevator.direction == Direction.UP
                        && elevator.currentFloor > newRequest.internalRequest.destinationFloor) {
                    addToPendingJobs(newRequest);
                } else if(elevator.direction == Direction.DOWN
                        && elevator.currentFloor < newRequest.internalRequest.destinationFloor) {
                    addToPendingJobs(newRequest);
                } else {
                    currentJobs.add(newRequest);
                }
            }
        }
    }
    private static void addToPendingJobs(Request newRequest) {
        if(newRequest.externalRequest.directionToGo == Direction.UP) {
            System.out.println("Added to Pending up Job");
            upPendingJobs.add(newRequest);
        } else {
            System.out.println("Added to Pending down Job");
            downPendingJobs.add(newRequest);
        }
    }

    static void  moveElevatorUpToRequestedFloor(Request request) {
        for(int i = elevator.currentFloor + 1; i <= request.externalRequest.sourceFloor; i++) {
            System.out.println("Elevator reached Floor - " +  i);
            elevator.currentFloor = i;
        }
    }

    static void serveUpJob(Request request) {
         moveElevatorUpToRequestedFloor(request);
        System.out.println("Opening Doors Floor - " + elevator.currentFloor);
        for(int i = elevator.currentFloor + 1; i <= request.internalRequest.destinationFloor; i++) {
           elevator.currentFloor = i ;
           System.out.println("Elevator reached Floor - " +  i);
           if(checkIfNewRequestCanbeServed(request)) {
                break;
           }
        }
    }

    static void moveElevatorDownToRequestedFloor(Request request) {
        for(int i = elevator.currentFloor - 1; i >= request.externalRequest.sourceFloor; i--) {
            System.out.println("Elevator reached Floor - " +  i);
            elevator.currentFloor = i;
        }
    }

    static void serveDownJob(Request request) {
        moveElevatorDownToRequestedFloor(request);
        System.out.println("Opening Doors Floor - " + elevator.currentFloor);
        for(int i = elevator.currentFloor - 1; i >= request.internalRequest.destinationFloor; i--) {
            elevator.currentFloor = i ;
            System.out.println("Elevator reached Floor - " +  i);
            if(checkIfNewRequestCanbeServed(request)) {
                break;
            }
        }
    }

     private static boolean checkIfNewRequestCanbeServed(Request currRequest) {
        if(!currentJobs.isEmpty()) {
            if (elevator.direction == Direction.UP) {
                Request newRequest = currentJobs.pollFirst();
                if (currRequest.internalRequest.destinationFloor > newRequest.internalRequest.destinationFloor) {
                    currentJobs.add(newRequest);
                    currentJobs.add(currRequest);
                    return true;
                } else {
                    currentJobs.add(newRequest);
                }
            } else {
                Request newRequest = currentJobs.pollLast();
                if (currRequest.internalRequest.destinationFloor < newRequest.internalRequest.destinationFloor) {
                    currentJobs.add(newRequest);
                    currentJobs.add(currRequest);
                    return true;
                } else {
                    currentJobs.add(newRequest);
                }
            }
        }
       return false;
    }
}
