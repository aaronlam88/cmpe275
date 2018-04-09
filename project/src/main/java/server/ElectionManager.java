package server;

import javax.xml.soap.Node;

public class ElectionManager {
}

abstract class State {
    public abstract boolean isLeader();

    public abstract void sendHeartBeat();

    public abstract void requestVote();

    public abstract void voteFor();

    public abstract void moveToNextState();
}

class NodeState {
    private static NodeState nodeState = null;

    State leaderState;
    State followerState;
    State cadidateState;

    State currentState;
    boolean voted;

    private NodeState() {
    }

    public static NodeState getNodeState() {
        if (nodeState == null) {
            nodeState = new NodeState();
            nodeState.init();
        }
        return nodeState;
    }

    private void init() {
        leaderState = new LeaderState(this);
        followerState = new FollowerState(this);
        cadidateState = new CadidateState(this);

        this.currentState = followerState;
        this.voted = false;
    }
}

class LeaderState extends State {
    NodeState nodeState;

    public LeaderState(NodeState nodeState) {
        this.nodeState = nodeState;
    }

    public boolean isLeader() {
        return true;
    }

    public void sendHeartBeat() {

    }

    public void requestVote() {

    }

    public void voteFor() {

    }

    public void moveToNextState() {

    }
}

class FollowerState extends State {
    NodeState nodeState;

    public FollowerState(NodeState nodeState) {
        this.nodeState = nodeState;
    }

    public boolean isLeader() {
        return false;
    }

    public void sendHeartBeat() {

    }

    public void requestVote() {

    }

    public void voteFor() {

    }

    public void moveToNextState() {

    }
}

class CadidateState extends State {
    NodeState nodeState;

    public CadidateState(NodeState nodeState) {
        this.nodeState = nodeState;
    }

    public boolean isLeader() {
        return  false;
    }

    public void sendHeartBeat() {

    }

    public void requestVote() {

    }

    public void voteFor() {

    }

    public void moveToNextState() {

    }
}