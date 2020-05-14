import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

public class Window {

    String[] results= new String[4];
    String query;
    String queryEdit;
    String cityState;
    Double businessRange=25.00;
    Window() {

        webScrape view=new webScrape();
        JFrame f = new JFrame("Distance Calculator");
        try {
            JLabel image= new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("resources/images/logo.png")).getScaledInstance(100,60, Image.SCALE_DEFAULT)));

            image.setBounds(0,0,100,100);
            f.add(image);
        }
        catch(IOException e)
        {
            System.out.println("Could not find image.");
        }

        ImageIcon img=new ImageIcon("resources/images/logo.png");
        f.setIconImage(img.getImage());
        f.setResizable(false);
        //f.setAlwaysOnTop(true);

        //submit button
        JButton button = new JButton("Calculate");
        button.setFocusable(false);
        button.setBounds(125, 265, 100, 40);
        //enter location labels
        JLabel labelLocation = new JLabel();
        labelLocation.setText("Enter Location:  (City,State or Zip)");
        labelLocation.setBounds(75, -35, 200, 100);
        

        //empty label which will show event after button clicked
        JLabel result1 = new JLabel();
        result1.setFont(new Font("SansSerif", Font.BOLD,19));
        result1.setBounds(20, 65, 400, 100);

        JLabel result2 = new JLabel();
        result2.setFont(new Font("SansSerif", Font.BOLD,19));
        result2.setBounds(20, 100, 400, 100);

        JLabel result3 = new JLabel();
        result3.setFont(new Font("SansSerif", Font.BOLD,19));
        result3.setBounds(20, 135, 400, 100);

        JLabel result4 = new JLabel();
        result4.setFont(new Font("SansSerif", Font.BOLD,19));
        result4.setBounds(20, 170, 400, 100);

        JLabel cityState=new JLabel();
        cityState.setFont(new Font("SansSerif", Font.BOLD, 23));
        cityState.setBounds(110, 20, 350, 100);

        //textfield to enter name
        JTextField txtFieldLocal = new JTextField();
        txtFieldLocal.setBounds(110, 25, 120, 30);

        JButton mapButton=new JButton("Map It!");
        mapButton.setBounds(250,265,75,30);
        mapButton.setEnabled(false);
        
        //add to frame
        f.add(result1);
        f.add(result2);
        f.add(result3);
        f.add(result4);
        f.add(cityState);
        f.add(txtFieldLocal);
        f.add(labelLocation);
        f.add(button);
        f.add(mapButton);
        f.setSize(350,350);
        f.setLayout(null);
        f.setVisible(true);
        f.getRootPane().setDefaultButton(button);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //action listener
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(!txtFieldLocal.getText().isEmpty()) {
                    query = txtFieldLocal.getText();
                    if (query.matches("[0-9]+")) {
                        results = view.getByZip(query);
                        query=view.getCity(query);
                        queryEdit=query.replaceAll("\\s","");
                    }
                    else {
                        queryEdit=query.replaceAll("\\s","");
                        results =view.getByCity(queryEdit);
                    }
                    result1.setForeground(Color.black);
                    result1.setText(results[0]);
                    if(Double.parseDouble(results[0].substring(0,results[0].indexOf(" ")))>businessRange){
                        result1.setForeground(Color.red);
                        result1.setText(view.isOverrange(results[0]));
                    }

                    result2.setForeground(Color.black);
                    result2.setText(results[1]);
                    if(Double.parseDouble(results[1].substring(0,results[1].indexOf(" ")))>businessRange) {
                        result2.setText(view.isOverrange(results[1]));
                        result2.setForeground(Color.red);
                    }

                    result3.setForeground(Color.black);
                    result3.setText(results[2]);
                    if(Double.parseDouble(results[2].substring(0,results[2].indexOf(" ")))>businessRange) {
                        result3.setText(view.isOverrange(results[2]));
                        result3.setForeground(Color.red);
                    }
                    result4.setForeground(Color.black);
                    result4.setText(results[3]);
                    if(Double.parseDouble(results[3].substring(0,results[3].indexOf(" ")))>businessRange) {
                        result4.setText(view.isOverrange(results[3]));
                        result4.setForeground(Color.red);
                    }
                    txtFieldLocal.setText("");
                    query=query.substring(0,1).toUpperCase()+query.substring(1);
                    query=query.substring(0,query.length()-2)+query.substring(query.length()-2).toUpperCase();
                    cityState.setText("<html><u>"+query+"</u></html>");
                    mapButton.setEnabled(true);
                }
                else if (txtFieldLocal.getText().isEmpty()) {
                    result1.setText("Please enter a location!");
                }
            }
        });

        mapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    Desktop.getDesktop().browse(new URL("https://maps.googleapis.com/maps/api/staticmap?size=1000x1000&Center=42.1830225,-71.0475334&zoom=9&maptype=roadmap"+
                            "&markers=color:blue%7Clabel:H%7C4516+Bedford+St+East+Bridgewater,+MA+02333"+
                            "&markers=color:blue%7Clabel:%7C4Providence,RI"+
                            "&markers=color:blue%7Clabel:D%7C4Dedham,MA"+
                            "&markers=color:blue%7Clabel:P%7C4plymouth,ma"+
                            "&markers=color:red%7Clabel:H%7C4"+queryEdit+
                            "&key=AIzaSyApSazA37My_KthkgkqLcwRujUoRvV0J7Q").toURI());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
