import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Window {

    String results;
    String query;
    String queryEdit;
    String cityState;
    Window() {



        webScrape view=new webScrape();
        JFrame f = new JFrame("Distance Calculator");
        try {
            f.setContentPane(new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("resources/images/logo.png")))));
        }
        catch(IOException e)
        {
            System.out.println("Could not find image.");
        }

        ImageIcon img=new ImageIcon("resources/images/logo.png");
        f.setIconImage(img.getImage());
        f.setResizable(false);
        f.setAlwaysOnTop(true);
        //submit button
        JButton button = new JButton("Calculate");
        button.setBounds(150, 265, 100, 40);
        //enter name label
        JLabel labelZip = new JLabel();
        labelZip.setText("Enter Zipcode:");
        labelZip.setBounds(10, -15, 100, 100);

        JLabel labelCity = new JLabel();
        labelCity.setText("Enter (City,State):");
        labelCity.setBounds(200, -15, 100, 100);

        //empty label which will show event after button clicked
        JLabel result = new JLabel();
        result.setFont(new Font("Courier", Font.BOLD,19));
        result.setBounds(80, 195, 300, 100);
        //textfield to enter name
        JTextField textfieldZip = new JTextField();
        textfieldZip.setBounds(100, 25, 80, 30);


        JTextField textfieldCity = new JTextField();
        textfieldCity.setBounds(300, 25, 110, 30);
        //add to frame
        f.add(result);
        f.add(textfieldZip);
        f.add(labelZip);
        f.add(textfieldCity);
        f.add(labelCity);
        f.add(button);
        f.setSize(450,350);
        f.setLayout(null);
        f.setVisible(true);
        f.getRootPane().setDefaultButton(button);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //action listener
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(!textfieldZip.getText().isEmpty()) {
                    query = textfieldZip.getText();
                    cityState=view.getCity(query);
                    results=view.getDistance(cityState);
                    result.setText(cityState+" "+results);
                    textfieldZip.setText("");
                }
                else if(!textfieldCity.getText().isEmpty()) {
                    query = textfieldCity.getText();
                    queryEdit=query.replaceAll("\\s","");
                    results=view.getDistance(queryEdit);
                    result.setText(query+" "+results);
                    textfieldCity.setText("");
                }
                else if (textfieldCity.getText().isEmpty()&& textfieldZip.getText().isEmpty()) {
                    result.setText("Please enter a location!");
                }
            }
        });
    }

    public String getAnswer() {
        return this.results;
    }
    public void setResult(String answer){
        this.results=answer;
    }
}
