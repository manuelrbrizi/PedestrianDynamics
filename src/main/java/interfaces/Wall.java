package interfaces;

import implementations.Vector;

public interface Wall {

    Vector getMinPoint(Avoider avoider);
    Vector getP1();
    Vector getP2();

}
