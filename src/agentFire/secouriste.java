package agentFire;

import jade.core.*;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class secouriste extends Agent {

    Position Myposition;

    protected void setup() {
        doWait(500);
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                
                //nbr des case infecté =0 oui /,non
                if (Envirenement.isGameOver()) {
                    myAgent.doDelete();
                }
                
                String Responce;
                ACLMessage msg = receive(); //recoit msg de la part case infecté
                Myposition = Envirenement.percept_getposition(); // ? 

                if (msg != null) {
                    try {
                        Position positionINFire = (Position) msg.getContentObject();//optenir la pos en feu
                        while (Myposition.getX() != positionINFire.getX() || Myposition.getY() != positionINFire.getY()) {
                            //x
                            if (Myposition.getX() >= 0 && Myposition.getX() <= 10) {
                                if (Myposition.getX() < positionINFire.getX()) {
                                    Myposition.setX(Myposition.getX() + 1);
                                } else if (Myposition.getX() > positionINFire.getX()) {
                                    Myposition.setX(Myposition.getX() - 1);
                                }
                            }
                            
                            if (Myposition.getY() >= 0 && Myposition.getY() <= 10) {
                                if (Myposition.getY() < positionINFire.getY()) {
                                    Myposition.setY(Myposition.getY() + 1);
                                } else if (Myposition.getY() > positionINFire.getY()) {
                                    Myposition.setY(Myposition.getY() - 1);
                                }
                            }
                            Envirenement.secouriste_deplace(Myposition);
                            doWait(250);
                        }
                        //send done
                        ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                        reply.setContent("done");
                        reply.addReceiver(msg.getSender());
                        send(reply);
                    } catch (UnreadableException ex) {
                        Logger.getLogger(secouriste.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (Envirenement.isGameOver()) {
                        doDelete();
                    }
                    block();
                }
            }
        });

    }

}
