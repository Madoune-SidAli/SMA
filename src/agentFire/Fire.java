package agentFire;

import jade.core.*;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;

public class Fire extends Agent {
    private int nbr_onfire =0;
    protected void setup() {
        addBehaviour(new OrganBehaviour());
    }

    private class OrganBehaviour extends Behaviour {

        private int MYETAT = 0;
        private boolean fin = false;
        
        @Override
        public void action() {
            while (MYETAT < 3) {
                myAgent.doWait(500);
                ACLMessage msg = receive();
                if (msg != null) {
                    MYETAT = 2;
                    Envirenement.actionchangeOrganismeEtat(Envirenement.percept_getOrganposition(myAgent.getLocalName()), MYETAT);
                    myAgent.doDelete();
                }
                MYETAT = Envirenement.perceptOrganismeEtat(Envirenement.percept_getOrganposition(myAgent.getLocalName()));
                if (MYETAT == 1) {
                    myAgent.doWait(500);
                    
                    if (nbr_onfire<=10){
                     infectOrgane();   
                    }
                    else if (10<nbr_onfire|| nbr_onfire>=20){
                     infectOrgane();
                     infectOrgane();
                    }else if(nbr_onfire>20){
                     infectOrgane();
                     infectOrgane();
                     infectOrgane();
                    }
                    
                    //infectOrgane();
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
                    fin = true;
                    myAgent.doDelete();
                    nbr_onfire--;
                }
            }
        }

        @Override
        public boolean done() {
            return fin;
        }

        public void infectOrgane() {
            Random randomno = new Random();
            //choix 2 position aleatoir to infect 
            int nomberinfected = 0;
            while (nomberinfected < 2) {
                int x = randomno.nextInt(7);
                Position position = Envirenement.percept_getOrganposition(myAgent.getLocalName());
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
                   nbr_onfire++;
                }
            }
        }
    }

}
