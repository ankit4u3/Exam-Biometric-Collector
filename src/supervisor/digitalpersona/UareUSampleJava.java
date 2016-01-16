package supervisor.digitalpersona;

import tracker.digitalpersona.*;
import com.digitalpersona.uareu.Fmd;
import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.ReaderCollection;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.UareUGlobal;
import csbc.attendence.RoomSelect;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import tracker.digitalpersona.StepOne;

public class UareUSampleJava
        extends JPanel
        implements ActionListener {

    private static final long serialVersionUID = 1;

    private static final String ACT_SELECTION = "selection";
    private static final String ACT_CAPTURE = "capture";
    private static final String ACT_STREAMING = "streaming";
    private static final String ACT_VERIFICATION = "verification";
    private static final String ACT_IDENTIFICATION = "identification";
    private static final String ACT_ENROLLMENT = "enrollment";
    private static final String ACT_EXIT = "exit";

    private JDialog m_dlgParent;
    private JTextArea m_textReader;

    private ReaderCollection m_collection;
    private Reader m_reader;
    private Fmd enrollmentFMD;

    public UareUSampleJava(String barcode) {
        final int vgap = 5;
        final int width = 380;

        BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        setLayout(layout);

        JLabel lblReader = new JLabel(" ");

        Dimension dm = lblReader.getPreferredSize();
        dm.width = width;
        lblReader.setPreferredSize(dm);
        add(lblReader);

        JButton btnEnrollment = new JButton("Run enrollment");
        btnEnrollment.setActionCommand(ACT_ENROLLMENT);
        btnEnrollment.addActionListener(this);
        add(btnEnrollment);
        add(Box.createVerticalStrut(vgap));

        JButton btnVerification = new JButton("Run verification");
        btnVerification.setActionCommand(ACT_VERIFICATION);
        btnVerification.addActionListener(this);
        add(btnVerification);
        add(Box.createVerticalStrut(vgap));

        add(Box.createVerticalStrut(vgap));
        JButton btnExit = new JButton("Exit");
        btnExit.setActionCommand(ACT_EXIT);
        btnExit.addActionListener(this);
        add(btnExit);
        add(Box.createVerticalStrut(vgap));

        setOpaque(true);
        gotoEnrollment(barcode);

    }

    public UareUSampleJava(String barcode, RoomSelect So) {

        final int vgap = 5;
        final int width = 380;

        BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        setLayout(layout);

        JLabel lblReader = new JLabel(" ");

        Dimension dm = lblReader.getPreferredSize();
        dm.width = width;
        lblReader.setPreferredSize(dm);
        add(lblReader);

        JButton btnEnrollment = new JButton("Run enrollment");
        btnEnrollment.setActionCommand(ACT_ENROLLMENT);
        btnEnrollment.addActionListener(this);
        add(btnEnrollment);
        add(Box.createVerticalStrut(vgap));

        JButton btnVerification = new JButton("Run verification");
        btnVerification.setActionCommand(ACT_VERIFICATION);
        btnVerification.addActionListener(this);
        add(btnVerification);
        add(Box.createVerticalStrut(vgap));

        add(Box.createVerticalStrut(vgap));
        JButton btnExit = new JButton("Exit");
        btnExit.setActionCommand(ACT_EXIT);
        btnExit.addActionListener(this);
        add(btnExit);
        add(Box.createVerticalStrut(vgap));

        setOpaque(true);
        gotoEnrollment(barcode, So);

    }

    public void gotoverification() {

        try {
            this.m_collection = UareUGlobal.GetReaderCollection();
            m_collection.GetReaders();
        } catch (UareUException e1) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(null, "Error getting collection");
            return;
        }

        if (m_collection.size() == 0) {
            MessageBox.Warning("Reader is not selected");
            return;
        }

        m_reader = m_collection.get(0);

        if (null == m_reader) {
            MessageBox.Warning("Reader is not selected");
        } else {
            Verification.Run(m_reader, this.enrollmentFMD);
        }
    }

    public void gotoEnrollment(String barcode) {
        try {
            m_collection = UareUGlobal.GetReaderCollection();
            m_collection.GetReaders();
        } catch (UareUException e1) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(null, "Error getting collection");
            return;
        }

        if (m_collection.size() == 0) {
            MessageBox.Warning("Reader is not selected");
            return;
        }

        m_reader = m_collection.get(0);

        if (null == m_reader) {
            MessageBox.Warning("Reader is not selected");
        } else {

            this.enrollmentFMD = Enrollment.Run(m_reader, barcode);
        }
    }

    public void gotoEnrollment(String barcode, RoomSelect So) {
        try {
            m_collection = UareUGlobal.GetReaderCollection();
            m_collection.GetReaders();
        } catch (UareUException e1) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(null, "Error getting collection");

         
            System.exit(0);
            return;
        }

        if (m_collection.size() == 0) {
            MessageBox.Warning("Reader is not selected");
         
            System.exit(0);
            return;
        }

        m_reader = m_collection.get(0);

        if (null == m_reader) {
            MessageBox.Warning("Reader is not selected");
      
            System.exit(0);
        } else {

            this.enrollmentFMD = Enrollment.Run(m_reader, barcode, So);
        }
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals(ACT_VERIFICATION)) {

            try {
                this.m_collection = UareUGlobal.GetReaderCollection();
                m_collection.GetReaders();
            } catch (UareUException e1) {
                // TODO Auto-generated catch block
                JOptionPane.showMessageDialog(null, "Error getting collection");

                return;
            }

            if (m_collection.size() == 0) {
                MessageBox.Warning("Reader is not selected");
                return;
            }

            m_reader = m_collection.get(0);

            if (null == m_reader) {
                MessageBox.Warning("Reader is not selected");
            } else {
                Verification.Run(m_reader, this.enrollmentFMD);
            }
        } else if (e.getActionCommand().equals(ACT_ENROLLMENT)) {

            try {
                this.m_collection = UareUGlobal.GetReaderCollection();
                m_collection.GetReaders();
            } catch (UareUException e1) {
                // TODO Auto-generated catch block
                JOptionPane.showMessageDialog(null, "Error getting collection");
                return;
            }

            if (m_collection.size() == 0) {
                MessageBox.Warning("Reader is not selected");
                return;
            }

            m_reader = m_collection.get(0);

            if (null == m_reader) {
                MessageBox.Warning("Reader is not selected");
            } else {

                this.enrollmentFMD = Enrollment.Run(m_reader, "1234");
            }
        } else if (e.getActionCommand().equals(ACT_EXIT)) {
            m_dlgParent.setVisible(false);
        }
    }

    private void doModal(JDialog dlgParent) {
        m_dlgParent = dlgParent;
        m_dlgParent.setContentPane(this);
        m_dlgParent.pack();
        m_dlgParent.setLocationRelativeTo(null);
        m_dlgParent.setVisible(true);
        m_dlgParent.dispose();
    }

    static public void createAndShowGUI(String id) {

        try {
            UareUGlobal.DestroyReaderCollection();
        } catch (UareUException e) {
            MessageBox.DpError("UareUGlobal.destroyReaderCollection()", e);
        }
    }

//	public static void main(String[] args) {
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                createAndShowGUI("007");
//            }
//        });
}
