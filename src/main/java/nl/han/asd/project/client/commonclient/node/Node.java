package nl.han.asd.project.client.commonclient.node;

/**
 * Created by Julius on 15/04/16.
 */
public class Node {

    @Override
    public boolean equals(Object anotherObj){
        if (!(anotherObj instanceof Node)) return false;
        return true;
    }
}
