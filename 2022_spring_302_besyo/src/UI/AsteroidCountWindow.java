package UI;

import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.bson.types.ObjectId;

import javax.swing.JLabel;

public class AsteroidCountWindow extends JFrame{
    
    private AsteroidCountPanel acp;
    private LoginPanel lp;
    private ObjectId userId;
    
    public AsteroidCountWindow() {

    	lp = new LoginPanel();
    	lp.setSize(800,800);
    	lp.requestFocus();
		lp.setFocusable(true);
		lp.setFocusTraversalKeysEnabled(false);
		add(lp);

    
    }

    public JPanel getLoginPanel() {
    	return lp;
    }
 
    public void setLoginPanel(LoginPanel e) {
    	this.lp = e;
    }
    
    public JPanel getAsteroidCountPanel() {
        return acp;
    }

    public void setAsteroidCountPanel(AsteroidCountPanel a) {
        this.acp = a;
    }
    
    public void setUserId(ObjectId e) {
    	this.userId = e;
    }

}
