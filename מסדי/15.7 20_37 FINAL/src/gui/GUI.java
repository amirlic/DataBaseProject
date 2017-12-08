package gui;
/*
 * Tal Sharon 302826797
 * Lea Tordjman 327321188
 * Amir Lichter 316129881
 * Orly paknahad 315444646
 */


import javafx.util.Pair;
import logic.DbQueryController;
import logic.FileToQueries;
import logic.QueryListener;
import logic.Table;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;

public class GUI extends JFrame{
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private static String dmlInput;
    private static String ddlInput;
    private static QueryListener queryListener;
    private static String dmlOutput;
    private static String ddlOutput;
    private static boolean dbWasSelected = false;

    private static GUI instance;

    private GUI(){

    }

    public static GUI getInstance(){
        if(instance == null){
            instance = new GUI();
        }
        return instance;
    }

    public static void connectingDialog(DbQueryController dbQueryController, Map<String, String> input){
        JPanel contentPane = new JPanel();
        contentPane.setOpaque(true);
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(new BorderLayout());

        JFrame frame = new JFrame("Please wait");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        JLabel title = new JLabel("Connecting . . .\n", JLabel.CENTER);
        title.setSize(200, 30);
        title.setLocation(10, 15);

        contentPane.add(title);

        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 100);
        frame.setLocation(800,400);
        frame.setVisible(true);

        dbQueryController.init(input);

        contentPane.removeAll();
        contentPane.setVisible(false);
        frame.setVisible(false);
    }

    public static void run(QueryListener ql){
        queryListener = ql;
        final GUI gui = GUI.getInstance();

        JPanel contentPane = new JPanel();
        contentPane.setOpaque(true);
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(null);

        //Creating frame
        JFrame frame = new JFrame("Welcome to DB manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //title
        JLabel title = new JLabel("Please enter any command", JLabel.CENTER);
        title.setSize(WIDTH/2, 30);
        title.setFont(new Font("MV Boli", Font.PLAIN, 26));
        title.setLocation(title.getWidth()/2, 15);

        //DML explanation
        JLabel dmlLabel = new JLabel("DML command:", JLabel.LEFT);
        dmlLabel.setSize(WIDTH/2, 30);
        dmlLabel.setFont(new Font("AR ESSENCE", Font.PLAIN, 20));
        dmlLabel.setLocation(dmlLabel.getWidth()/12+100, 50);

        //DML input
        final JTextArea dmlTextField = new JTextArea();
        dmlTextField.setSize(WIDTH/2-50, 280);
        dmlTextField.setLocation(dmlTextField.getWidth()/12, 90);
        dmlTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        //DML scrolling pane
        JScrollPane dmlScroll = new JScrollPane(dmlTextField);
        dmlScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        dmlScroll.setBounds((int)dmlTextField.getLocation().getX(),
                (int)dmlTextField.getLocation().getY(), dmlTextField.getWidth(), dmlTextField.getHeight());

        //DDL txt explanation
        JLabel ddlLabel = new JLabel("DDL command:", JLabel.LEFT);
        ddlLabel.setSize(WIDTH/2, 30);
        ddlLabel.setFont(new Font("AR ESSENCE", Font.PLAIN, 20));
        ddlLabel.setLocation(ddlLabel.getWidth()/12+475, 50);

        //DDL txt input
        final JTextArea ddlTextField = new JTextArea();
        ddlTextField.setSize(WIDTH/2-50, 280);
        ddlTextField.setLocation(ddlTextField.getWidth()/12+370, 90);
        ddlTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        //DDL scrolling pane
        JScrollPane ddlScroll = new JScrollPane(ddlTextField);
        ddlScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        ddlScroll.setBounds((int)ddlTextField.getLocation().getX(),
                (int)ddlTextField.getLocation().getY(), ddlTextField.getWidth(), ddlTextField.getHeight());

        //Sending dml query button
        JButton dmlButton = new JButton("Send");
        dmlButton.setSize(100, 30);
        dmlButton.setAlignmentX(JButton.CENTER);
        dmlButton.setLocation((int)dmlTextField.getLocation().getX()+50, (int)dmlTextField.getLocation().getY() + dmlTextField.getHeight()+10);
        dmlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!dbWasSelected){
                    noBSelected();
                    return;
                }
                dmlInput = dmlTextField.getText();
                if(!queryListener.isDMLInputCorrect(dmlInput)){
                    wrongQueryStructureError();
                    dmlTextField.setText("");
                    return;
                }
                dmlOutput = queryListener.sendDML(dmlInput);
                dmlTextField.setText("");
                if(!queryListener.isDMLResponseCorrectStructure(dmlOutput)){
                    wrongQueryStructureError();
                }
                else if(!queryListener.isDMLResponseCorrectLogic(dmlOutput)){
                    wrongQueryLogicError();
                }
                dmlTextField.setText(dmlOutput);
            }
        });

        //clear ddl query button
        JButton dmlClearButton = new JButton("Clear");
        dmlClearButton.setSize(100, 30);
        dmlClearButton.setAlignmentX(JButton.CENTER);
        dmlClearButton.setLocation((int)dmlButton.getLocation().getX()+dmlButton.getWidth()+50,
                (int)ddlTextField.getLocation().getY() + ddlTextField.getHeight()+10);
        dmlClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dmlTextField.setText("");
            }
        });

        //Output label
        JLabel outputLabel = new JLabel("Script File Output:", JLabel.LEFT);
        outputLabel.setSize(WIDTH/2, 30);
        outputLabel.setFont(new Font("AR ESSENCE", Font.PLAIN, 20));
        outputLabel.setLocation(WIDTH/2-75, dmlClearButton.getY() + dmlClearButton.getHeight()+110);

        //Output text area
        final TextArea queryResponse = new TextArea();
        queryResponse.setSize(WIDTH-50, 200);
        queryResponse.setLocation(20, outputLabel.getY()+40);
        queryResponse.setEditable(false);

        //Scrolling pane for output
        JScrollPane queryScroll = new JScrollPane(queryResponse);
        queryScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        queryScroll.setBounds((int)queryResponse.getLocation().getX(),
                (int)queryResponse.getLocation().getY(), queryResponse.getWidth(), queryResponse.getHeight());

        //Send ddl query button
        JButton ddlButton = new JButton("Send");
        ddlButton.setSize(100, 30);
        ddlButton.setAlignmentX(JButton.CENTER);
        ddlButton.setLocation((int)ddlTextField.getLocation().getX()+50, (int)ddlTextField.getLocation().getY() + ddlTextField.getHeight()+10);
        ddlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ddlInput = ddlTextField.getText();
                if(!queryListener.isDDLInputCorrect(ddlInput)){
                    wrongInputError();
                    ddlTextField.setText("");
                    return;
                }
                if(ddlInput.contains("USE")){
                    queryListener.setDBName(ddlInput);
                    dbWasSelected = true;
                }
                ddlOutput = queryListener.sendDDL(ddlInput);
                ddlTextField.setText(ddlOutput);
            }
        });

        //clear ddl query button
        JButton ddlClearButton = new JButton("Clear");
        ddlClearButton.setSize(100, 30);
        ddlClearButton.setAlignmentX(JButton.CENTER);
        ddlClearButton.setLocation((int)ddlButton.getLocation().getX()+ddlButton.getWidth()+50,
                (int)ddlTextField.getLocation().getY() + ddlTextField.getHeight()+10);
        ddlClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               ddlTextField.setText("");
            }
        });


        //Choosing from script file button
        final JButton chooseFileButton = new JButton("Choose script file...");
        chooseFileButton.setSize(200, 30);
        chooseFileButton.setLocation((int)outputLabel.getLocation().getX()-35 , outputLabel.getY() - 30);
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileLocation = chooseFile(gui);
                String queries = FileToQueries.readFile(fileLocation);
                String res = queryListener.sendDML(queries);
                if(!queryListener.isDMLResponseCorrectStructure(res)){
                    wrongQueryStructureError();
                }
                else if(!queryListener.isDMLResponseCorrectLogic(res)){
                    wrongQueryLogicError();
                }
                queryResponse.setText(res);
                dbWasSelected = true;
            }
        });

        //Or label - to choose betwen dml txt and input file
        JLabel orLabel = new JLabel("OR:", JLabel.LEFT);
        orLabel.setSize(100, 30);
        orLabel.setFont(new Font("AR ESSENCE", Font.PLAIN, 16));
        orLabel.setLocation((int)chooseFileButton.getLocation().getX()-40, (int)chooseFileButton.getLocation().getY());


        //Simple command button - where we send simple queries
        JButton simpleCommandButton = new JButton("Simple DML Commands");
        simpleCommandButton.setSize(180,30);
        simpleCommandButton.setLocation((int)dmlButton.getLocation().getX() + 40,
                (int)dmlButton.getLocation().getY()+dmlButton.getHeight()+10);
        simpleCommandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!dbWasSelected){
                    noBSelected();
                    return;
                }
                final Pair<String[], Map<String, Table>> pair = queryListener.getDatabaseMetaData();
                String [] data = pair.getKey();
                final JFrame f = new JFrame("Choose Table To Show");
                final JList jlistScroll = new JList(data);
                Container c = f.getContentPane();
                JScrollPane sp = new JScrollPane(jlistScroll);
                jlistScroll.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        final JFrame submenu = new JFrame("Pick columns from table : " +jlistScroll.getSelectedValue());
                        submenu.setSize(500,400);
                        submenu.setLocation(650,350);
                        JPanel cntrlPanel = new JPanel();
                        cntrlPanel.setLayout(new BoxLayout(cntrlPanel, BoxLayout.Y_AXIS));

                        //adding checkboxes by amount of columns
                        String [] clmns = pair.getValue().get(jlistScroll.getSelectedValue()).getColumns();
                        final Vector<JCheckBox> allboxes = new Vector<>();
                        for(String s:clmns) {
                            JCheckBox one = new JCheckBox(s);
                            allboxes.add(one);
                            cntrlPanel.add(one);
                        }
                        //text area for WHERE command
                        final JTextArea whereCommand = new JTextArea("WHERE ");
                        cntrlPanel.add(whereCommand);
                        //final button
                        JButton donebtn = new JButton("done");
                        donebtn.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String chosensimpleinput = "SELECT ";
                                for(int i=0; i<allboxes.size(); i++){
                                    if(allboxes.elementAt(i).isSelected()){
                                        chosensimpleinput = chosensimpleinput+allboxes.elementAt(i).getText()+",";

                                    }
                                }
                                chosensimpleinput = chosensimpleinput.substring(0, chosensimpleinput.length() - 1);
                                chosensimpleinput = chosensimpleinput + " FROM " +jlistScroll.getSelectedValue()
                                        +" "+whereCommand.getText()+";";
                                submenu.setVisible(false);
                                submenu.dispose();
                                f.setVisible(false);
                                f.dispose();

                                if(!queryListener.isDMLInputCorrect(chosensimpleinput)){
                                    wrongQueryStructureError();
                                    dmlTextField.setText("");
                                    return;
                                }
                                dmlOutput = queryListener.sendDML(chosensimpleinput);
                                dmlTextField.setText("");
                                if(!queryListener.isDMLResponseCorrectStructure(chosensimpleinput)){
                                    wrongQueryStructureError();
                                }
                                else if(!queryListener.isDMLResponseCorrectLogic(chosensimpleinput)){
                                    wrongQueryLogicError();
                                }
                                dmlTextField.setText(dmlOutput);
                            }
                            });
                        cntrlPanel.add(donebtn);

                        //Scrolling pane for output
                        JScrollPane subScroll = new JScrollPane(cntrlPanel);
                        subScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

                        submenu.add(subScroll);
                        submenu.setVisible(true);

                    }
                });
                c.add(sp);
                f.setSize(600, 400);
                f.setLocation(600,300);
                f.setVisible(true);
            }
        });

        //adding elements
        contentPane.add(title);
        contentPane.add(ddlLabel);
        contentPane.add(dmlLabel);
        contentPane.add(dmlButton);
        contentPane.add(dmlClearButton);
        contentPane.add(ddlButton);
        contentPane.add(ddlClearButton);
        contentPane.add(dmlScroll);
        contentPane.add(ddlScroll);
        contentPane.add(queryScroll);
        contentPane.add(outputLabel);
        contentPane.add(orLabel);
        contentPane.add(chooseFileButton);
        contentPane.add(simpleCommandButton);


        //setting the frame
        frame.setContentPane(contentPane);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocation(550,100);
        frame.setVisible(true);

    }

    public static void wrongQueryStructureError(){
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame,
                "WRONG QUERY STRUCTURE","Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void wrongQueryLogicError(){
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame,
                "LOGICAL ERROR","Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void wrongInputError(){
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame,
                "WRONG INPUT","Error", JOptionPane.ERROR_MESSAGE);
    }

    public static String chooseFile(GUI gui) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(gui);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    public static void noBSelected(){
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame,
                "No Data Base Selected,\n Enter DB then try again","Error", JOptionPane.ERROR_MESSAGE);
    }
}
