package agentFire;

import jade.core.*;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;

public class Fire extends Agent {

    protected void setup() {
        
        addBehaviour(new OrganBehaviour());
    }

    private class OrganBehaviour extends Behaviour {

        private int MYETAT = 0;
        private boolean fin = false;

        @Override
        public void action() {
            while (MYETAT < 3) {
                //myAgent.doWait(500);
                ACLMessage msg = receive();
                if (msg != null) {
                    //System.out.println(msg.getContent());
                    MYETAT = 2;
                    Envirenement.actionchangeOrganismeEtat(Envirenement.percept_getOrganposition(myAgent.getLocalName()), MYETAT);
                    myAgent.doDelete();
                }
                MYETAT = Envirenement.perceptOrganismeEtat(Envirenement.percept_getOrganposition(myAgent.getLocalName()));
                 
                //System.out.println("my state"+myAgent.getLocalName()+" "+MYETAT);
                if (MYETAT == 1) {
                    //System.out.println("je suis malade"+myAgent.getLocalName());
                    myAgent.doWait(500);
                    infectOrgane();
                    Envirenement.actionchangeOrganismeEtat(Envirenement.percept_getOrganposition(myAgent.getLocalName()), 2);
                    try {
                        ACLMessage aclmsg = new ACLMessage(ACLMessage.INFORM);
                        aclmsg.addReceiver(new AID("secouriste", AID.ISLOCALNAME));
                        aclmsg.setContentObject(Envirenement.percept_getOrganposition(myAgent.getLocalName()));
                        myAgent.send(aclmsg);

                    } catch (IOException ex) {
                        Logger.getLogger(Fire.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                if (MYETAT == 2) {
                    ACLMessage aclmsg = new ACLMessage(ACLMessage.INFORM);
                    aclmsg.addReceiver(new AID("secouriste", AID.ISLOCALNAME));
                    aclmsg.setContent("etat2");
                    myAgent.send(aclmsg);
                   // System.out.println("state 2");
                    fin = true;
                    myAgent.doDelete();
                }
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }

        @Override
        public boolean done() {
            return fin;
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public void infectOrgane() {
            Random randomno = new Random();
            //choix 2 position aleatoir to infect 
            int nomberinfected = 0;
            while (nomberinfected < 2) {
                int x = randomno.nextInt(7);
                Position position = Envirenement.percept_getOrganposition(myAgent.getLocalName());
                //System.out.println(" x was " + x);
                switch (x) {
                    case 0:
                        if (Envirenement.percept_caninfectsoutheast(position)) {
                            position.setX(position.getX() + 1);
                            position.setY(position.getY() + 1);
                        }
                        break;
                    case 1:
                        if (Envirenement.percept_caninfectnortheast(position)) {
                            position.setX(position.getX() + 1);
                            position.setY(position.getY() - 1);
                        }
                        break;
                    case 6:
                        if (Envirenement.percept_caninfectnourthwest(position)) {
                            position.setX(position.getX() - 1);
                            position.setY(position.getY() - 1);
                        }
                        break;
                    case 7:
                        if (Envirenement.percept_caninfectsouthwest(position)) {
                            position.setX(position.getX() - 1);
                            position.setY(position.getY() + 1);
                        }
                        break;
                    case 2:
                        if (Envirenement.percept_caninfecteast(position)) {
                            position.setX(position.getX() + 1);
                        }
                        break;
                    case 3:
                        if (Envirenement.percept_caninfectwest(position)) {
                            position.setX(position.getX() - 1);
                        }
                        break;
                    case 4:
                        if (Envirenement.percept_caninfectsouth(position)) {
                            position.setY(position.getY() + 1);
                        }
                        break;
                    case 5:
                        if (Envirenement.percept_caninfectnorth(position)) {
                            position.setY(position.getY() - 1);
                        }
                        break;
                }
                if (Envirenement.percept_getOrganposition(myAgent.getLocalName()).getX() != position.getX()
                        || Envirenement.percept_getOrganposition(myAgent.getLocalName()).getY() != position.getY()) {
                    
                    Envirenement.actionchangeOrganismeEtat(position, 1);
                    nomberinfected++;
                }
            }
        }
    }

}
