package server;

import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ElectionManager {
  //private static final Logger logger = Logger.getLogger(ElectionManager.class.getName());
    //TODO implement Election algo here

    // Do i create a channel here and send message
    // All electionManager has a set of nodes information?

    public NodeState nodeState;
    private Timer timer;

    //Candidate state
    private int term;
    private int voteCount;

    public ElectionManager() {
        this.nodeState = NodeState.getNodeState();
        timer = new Timer();
        term = 0;
        startCountDown();
    }

    public void startCountDown() {
      timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                changeToCandidate();
            }
          },
            nodeState.getTimeout());
    }

    public void receiveHeartBeat() {
      nodeState.setTimeout();
      startCountDown();
    }

    public void changeToCandidate() {
      nodeState.getCurrentState().moveToNextState();
      term++;
      voteCount = 1;
      //TODO: send request to other nodes in network

      if (voteCount >= 2) {
        changeToLeader();
      } else {
        //TODO:
      }
    }

    public void changeToLeader() {
      nodeState.getCurrentState().moveToNextState();
      //TODO: send heartbeat and check response

    }
}

abstract class State {
    public abstract boolean isLeader();

    public abstract boolean shouldSendHeartbeat();

    public abstract boolean shouldRequestVote();

    public abstract boolean canVote();

    public abstract void moveToNextState();
}

class NodeState {
    private static NodeState nodeState = null;
    State leaderState;
    State followerState;
    State cadidateState;

    State currentState;
    boolean voted;
    int timeout;

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

        currentState = followerState;
        voted = false;
        timeout = (int) (Math.random() * 300) + 200; // random 200 - 500 ms timeout
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState (State state) {
        currentState = state;
    }

    public State getLeaderState() {
        return leaderState;
    }

    public State getFollowerState() {
        return followerState;
    }

    public State getCadidateState() {
        return cadidateState;
    }

    public int getTimeout() {
      return timeout;
    }

    public void setTimeout() {
      timeout = (int) (Math.random() * 300) + 200;
    }
}

class LeaderState extends State {
    NodeState nodeState;

    public LeaderState(NodeState nodeState) {
        this.nodeState = nodeState;
    }

    @Override
    public boolean isLeader() {
        return true;
    }

    @Override
    public boolean shouldSendHeartbeat() {
        return true;
    }

    @Override
    public boolean shouldRequestVote() {
        return false;
    }

    @Override
    public boolean canVote() {
        return false;
    }

    @Override
    public void moveToNextState() {
        // can move
        return;
    }


}

class FollowerState extends State {
  private static final Logger logger = Logger.getLogger(FollowerState.class.getName());

    NodeState nodeState;

    public FollowerState(NodeState nodeState) {
        this.nodeState = nodeState;
    }

    @Override
    public boolean isLeader() {
        return false;
    }

    @Override
    public boolean shouldSendHeartbeat() {
        return false;
    }

    @Override
    public boolean shouldRequestVote() {
        return false;
    }

    @Override
    public boolean canVote() {
        if (!nodeState.voted) {
            return true;
        }
        return false;
    }


    public void moveToNextState() {
        //TODO after timeout, no heartbeat from leader
        logger.info("Follower change to Candidate");
        nodeState.setCurrentState(nodeState.getCadidateState());

    }

}

class CadidateState extends State {
    NodeState nodeState;

    public CadidateState(NodeState nodeState) {
        this.nodeState = nodeState;
    }


    @Override
    public boolean isLeader() {
        return false;
    }

    @Override
    public boolean shouldSendHeartbeat() {
        return false;
    }

    @Override
    public boolean shouldRequestVote() {
        return true;
    }

    @Override
    public boolean canVote() {
        return false;
    }

    public void moveToNextState() {
        //TODO if enough vote, become leader
        nodeState.setCurrentState(nodeState.getLeaderState());
        // TODO if not enough vote, wait for timeout to see if there is a new Leader
        // TODO if after timeout, and no Leader, call for election again
    }
}
