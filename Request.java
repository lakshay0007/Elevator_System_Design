public class Request implements Comparable<Request> {
    public ExternalRequest externalRequest;
    public InternalRequest internalRequest;

    public Request(ExternalRequest externalRequest, InternalRequest internalRequest) {
        this.externalRequest = externalRequest;
        this.internalRequest = internalRequest;
    }

    @Override
    public int compareTo(Request o) {
        if(this.internalRequest.destinationFloor < o.internalRequest.destinationFloor) {
            return -1;
        } else {
            return 1;
        }
    }
}
